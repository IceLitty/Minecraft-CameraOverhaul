package mirsario.cameraoverhaul.mixins;

import mirsario.cameraoverhaul.CameraContext;
import mirsario.cameraoverhaul.CameraOverhaul;
import mirsario.cameraoverhaul.CameraSystem;
import mirsario.cameraoverhaul.TimeSystem;
import mirsario.cameraoverhaul.CameraContext.Perspective;
import mirsario.cameraoverhaul.utilities.*;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Camera.class})
public abstract class CameraMixin {
    public CameraMixin() {
    }

    @Shadow
    public abstract float getXRot();

    @Shadow
    public abstract float getYRot();

    @Shadow
    public abstract Vec3 getPosition();

    @Shadow
    protected abstract void setRotation(float var1, float var2);

    @Inject(
            method = {"setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V"},
            at = {@At("RETURN")}
    )
    private void onCameraUpdate(BlockGetter area, Entity entity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        boolean renderHandle = true;
        if (CompatHelper.isDoABarrelRollLoaded()) {
            renderHandle = !DoABarrelRollCompat.checkIfPlayerIsRolling(entity);
        }
        if (renderHandle && CompatHelper.isRollingDownInTheDeepLoaded()) {
            renderHandle = !RollingDownInTheDeepCompat.checkIfPlayerIsRolling();
        }
        if (renderHandle) {
            CameraSystem system = CameraOverhaul.camera;
            Entity vehicle = entity.getVehicle();
            Entity controlledEntity = vehicle != null ? vehicle : entity;
            CameraContext context = new CameraContext();
            context.isRiding = vehicle != null;
            context.isRidingMount = vehicle instanceof Animal;
            context.isRidingVehicle = vehicle instanceof VehicleEntity;
            context.velocity = VectorUtils.toJoml(controlledEntity.getDeltaMovement());
            context.transform = new Transform(VectorUtils.toJoml(this.getPosition()), new Vector3d((double)this.getXRot(), (double)this.getYRot(), 0.0));
            context.perspective = thirdPerson ? (inverseView ? Perspective.THIRD_PERSON_REVERSE : Perspective.THIRD_PERSON) : Perspective.FIRST_PERSON;
            if (entity instanceof LivingEntity) {
                context.isFlying = ((LivingEntity)entity).isFallFlying();
                context.isSwimming = entity.isSwimming();
                context.isSprinting = entity.isSprinting();
            }

            TimeSystem.update();
            system.onCameraUpdate(context, TimeSystem.getDeltaTime());
            system.modifyCameraTransform(context.transform);
            this.setRotation((float)context.transform.eulerRot.y, (float)context.transform.eulerRot.x);
        }
    }
}
