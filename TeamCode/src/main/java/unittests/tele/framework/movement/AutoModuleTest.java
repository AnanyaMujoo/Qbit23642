package unittests.tele.framework.movement;
import automodules.AutoModule;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import global.Constants;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PMotor;
import teleutil.button.Button;
import unittests.tele.TeleUnitTest;
import util.Timer;
import util.template.Iterator;

import static global.General.bot;
import static global.General.gamepad1;
import static global.General.gph1;
import static global.General.log;

public class AutoModuleTest extends TeleUnitTest {
    /**
     * Test automodules using intake as an example
     */

    // TODO TEST

    public static class TestPart2 extends RobotPart {
        public PMotor car;

        @Override
        public void init() {
            car = create("car", ElectronicType.PMOTOR_FORWARD);
            car.setToRotational(Constants.ORBITAL_TICKS_PER_REV, 1);
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


    private final Timer timerB = new Timer();
    private final Timer timerY = new Timer();

    private final double timeC = 0.3;

    @Override
    protected void start() {
        /**
         * Link gamepad handlers
         */
//        gph1.link(Button.B, () -> {bot.cancelAutoModules(); bot.addAutoModule(module); });
//        gph1.link(Button.Y, () -> {bot.cancelAutoModules(); bot.addAutoModule(module2); });
        gph1.link(Button.A, () -> bot.cancelAutoModules());

//        gph1.link(Button.RIGHT_BUMPER, () -> { bot.addAutoModule(testPart2.MoveTime(1,0.5));});
//        gph1.link(Button.LEFT_BUMPER, testPart2.MoveTime(-1,0.5));

//        gph1.link(Button.A, OnPressEventHandler.class,() -> bot.addAutoModule(IntakeOut));
//        gph1.link(Button.B, OnPressEventHandler.class, bot::cancelAutoModules);
//        gph1.link(Button.RIGHT_BUMPER, OnPressEventHandler.class, bot::pauseAutoModules);
//        gph1.link(Button.LEFT_BUMPER, OnPressEventHandler.class, bot::resumeAutoModules);

        timerB.reset();
        timerY.reset();
    }

    @Override
    protected void loop() {
        if(gamepad1.b && timerB.seconds() > timeC){
           bot.cancelAutoModules();
           bot.addAutoModule(testPart2.MoveTime(1,0.5));
           timerB.reset();
        }

        if(gamepad1.y && timerY.seconds() > timeC){
            bot.cancelAutoModules();
            bot.addAutoModule(testPart2.MoveTime(-1,0.5));
            timerY.reset();
        }

        /**
         * Should run automodules
         */
//        log.show("Pos", testPart2.car.getPosition());
//        log.show("Click a to start intake");
//        log.show("Click b to cancel the AutoModules");
//        log.show("Click right bumper to pause the AutoModules");
//        log.show("Click left bumper to resume the AutoModules");
    }
}
