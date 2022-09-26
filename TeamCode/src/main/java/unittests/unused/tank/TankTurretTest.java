package unittests.unused.tank;

import unittests.tele.TeleUnitTest;
import static global.General.*;

public class TankTurretTest extends TeleUnitTest {
    /**
     * Tests the turret
     */

    @Override
    public void loop() {
        showConfig(bot.tankTurret);
        /**
         * Should move the turret
         */
        log.show("Use left stick x");
        bot.tankTurret.move(gamepad1.left_stick_x);
        /**
         * Should change when the turret moves
         */
        log.show("Turret pos", bot.tankTurret.getTurretPos());
        /**
         * Should not change when the turret moves
         */
        log.show("Turret target pos", bot.tankTurret.getTargetPos());
    }
}
