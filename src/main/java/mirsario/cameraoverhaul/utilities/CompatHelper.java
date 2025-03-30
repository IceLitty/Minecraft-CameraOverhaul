package mirsario.cameraoverhaul.utilities;

import mirsario.cameraoverhaul.CameraOverhaul;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;

@EventBusSubscriber(modid = CameraOverhaul.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CompatHelper {

    public static final String DO_A_BARREL_ROLL = "do_a_barrel_roll";
    public static final String ROLLING_DOWN_IN_THE_DEEP = "rolling_down_in_the_deep";
    private static boolean doABarrelRollLoaded = false;
    private static boolean rollingDownInTheDeepLoaded = false;

    public static boolean isDoABarrelRollLoaded() {
        return doABarrelRollLoaded;
    }

    public static boolean isRollingDownInTheDeepLoaded() {
        return rollingDownInTheDeepLoaded;
    }

    @SubscribeEvent
    public static void onEnqueue(final InterModEnqueueEvent event) {
        event.enqueueWork(() -> checkModLoad(DO_A_BARREL_ROLL, () -> doABarrelRollLoaded = true));
        event.enqueueWork(() -> checkModLoad(ROLLING_DOWN_IN_THE_DEEP, () -> rollingDownInTheDeepLoaded = true));
    }

    public static void checkModLoad(String modId, Runnable runnable) {
        if (ModList.get().isLoaded(modId)) {
            runnable.run();
        }
    }

}
