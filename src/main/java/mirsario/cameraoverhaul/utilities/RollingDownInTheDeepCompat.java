package mirsario.cameraoverhaul.utilities;

import dev.enjarai.rollingdowninthedeep.RollingDownInTheDeep;

public class RollingDownInTheDeepCompat {

    public static boolean checkIfPlayerIsRolling() {
        if (!CompatHelper.isRollingDownInTheDeepLoaded()) {
            return false;
        }
        return RollingDownInTheDeep.shouldRoll();
    }

}
