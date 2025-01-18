package unittests.tele.framework.movement;
import automodules.AutoModule;
import automodules.stage.Stage;
import global.Constants;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PMotor;
import teleutil.button.Button;
import unittests.tele.TeleUnitTest;

import static global.General.bot;
import static global.General.gph1;
import static global.General.log;

public class AutoModuleTest extends TeleUnitTest {
    /**
     * Test automodules using intake as an example
     */

    public static class TestPart2 extends RobotPart {
        public PMotor car;

        @Override
        public void init() {
            car = create("car", ElectronicType.PMOTOR_FORWARD);
            car.setToRotational(Constants.ORBITAL_ENCODER_TICKS_PER_REVOLUTION, 1);
        }

        @Override
        protected void move(double p) { car.move(p); }

        @Override
        public AutoModule MoveTime(double p, double t) {
            return super.MoveTime(p, t);
        }

        public Stage stageRotate(double power, double deg){
            return moveTarget(() -> car, power, deg);
        }
    }

    private final AutoModule module = new AutoModule(
            testPart2.stageRotate(1, 360)
    );

    private final AutoModule module2 = new AutoModule(
            testPart2.stageRotate(1, 0)
    );

    @Override
    protected void start() {
        /**
         * Link gamepad handlers
         */
        gph1.link(Button.B, module);
        gph1.link(Button.Y, module2);
        gph1.link(Button.A, bot::cancelAutoModules);
        gph1.link(Button.RIGHT_BUMPER,  bot::pauseAutoModules);
        gph1.link(Button.LEFT_BUMPER, bot::resumeAutoModules);
    }

    @Override
    protected void loop() {
        /**
         * Should run automodules
         */
        log.show("Click b or y to start");
        log.show("Click a to cancel the AutoModules");
        log.show("Click right bumper to pause the AutoModules");
        log.show("Click left bumper to resume the AutoModules");
    }
}
