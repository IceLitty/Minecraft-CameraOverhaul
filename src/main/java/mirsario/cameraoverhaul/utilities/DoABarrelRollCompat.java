package mirsario.cameraoverhaul.utilities;

import net.minecraft.world.entity.Entity;
import nl.enjarai.doabarrelroll.api.RollEntity;

public class DoABarrelRollCompat {

    public static boolean checkIfPlayerIsRolling(Entity entity) {
        if (!CompatHelper.isDoABarrelRollLoaded()) {
            return false;
        }
        if (entity instanceof RollEntity rE) {
            return rE.doABarrelRoll$isRolling();
        }
        return false;
    }

}
