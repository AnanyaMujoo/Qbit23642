package unittests.unused.tank;

import automodules.AutoModule;
import teleutil.button.Button;
import teleutil.button.OnPressEventHandler;
import unittests.tele.TeleUnitTest;
import static global.General.*;

public class TankLiftTest extends TeleUnitTest {
    /**
     * Tests the lift
     */
    public AutoModule test = new AutoModule(
            bot.tankLift.liftEncoder(0.5, 30)
    );
    public AutoModule test2 = new AutoModule(
            bot.tankLift.liftTime(0.6, 0.5)
    );

    // TOD4 NEW
    // Make something that detects when encoders are disconnected


    @Override
    protected void start() {
        /**
         * Link the gamepad handlers
         */
        gph1.link(Button.A, OnPressEventHandler.class, () -> bot.addAutoModule(test));
        gph1.link(Button.B, OnPressEventHandler.class, () -> bot.addAutoModule(test2));
    }

    @Override
    public void loop() {
        showConfig(bot.tankLift);
        /**
         * Lift should move
         */
        log.show("Use right stick y");
        bot.tankLift.move(-gamepad1.right_stick_y);
        /**
         * Should change when lift moves
         */
        log.show("Lift pos", bot.tankLift.getPos()[0]);
        /**
         * Should not change when lift moves
         */
        log.show("Lift target pos", bot.tankLift.getTarget());
    }
}
