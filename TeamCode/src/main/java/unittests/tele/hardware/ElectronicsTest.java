package unittests.tele.hardware;

import robotparts.electronics.continuous.CMotor;
import robotparts.electronics.continuous.CServo;
import robotparts.electronics.positional.PMotor;
import robotparts.electronics.positional.PServo;
import teleutil.button.Button;
import teleutil.button.OnPressEventHandler;
import unittests.tele.TeleUnitTest;

import static global.General.gph1;
import static global.General.log;

public class ElectronicsTest extends TeleUnitTest {
    /**
     * Class to test all of the electronics
     * NOTE: This does not test the actual electronic class since theres very few functions in that
     * This class will not work if certain parts of the robot don't exist (like the lift and outtake lock)
     */
    CMotor in;
    CServo cr;
    PMotor li;
    PServo lo;


    @Override
    protected void start() {
        /**
         * Get the electronics
         */
//        in = bot.tankIntake.getElectronicsOfType(CMotor.class).get("in");
//        cr = bot.tankCarousel.getElectronicsOfType(CServo.class).get("cr");
//        li = bot.tankLift.getElectronicsOfType(PMotor.class).get("li");
//        lo = bot.tankOuttake.getElectronicsOfType(PServo.class).get("lo");
        /**
         * Link the gamepad handler
         */
        gph1.link(Button.A, OnPressEventHandler.class, () -> in.setPower(0.2));
        gph1.link(Button.B, OnPressEventHandler.class, () -> cr.setPower(0.2));
        gph1.link(Button.Y, OnPressEventHandler.class, () -> li.move(0.2));
        gph1.link(Button.RIGHT_BUMPER, OnPressEventHandler.class, () -> lo.moveToPosition("start"));
    }

    @Override
    protected void loop() {
        /**
         * Controls
         */
        log.show("Press A to move intake");
        log.show("Press B to move carousel servo");
        log.show("Press Y to move lift");
        log.show("Press right bumper to move outtake lock");
        /**
         * All directions should be forward
         */
        log.show("Intake direction",in.getDirection());
        log.show("Carousel Servo Direction",cr.getDirection());
        log.show("Lift direction", li.getDirection());
        log.show("Outtake lock direction",lo.getDirection());
        /**
         * Should change when lift moves
         */
        log.show("Lift position",li.getPosition());
        /**
         * Should be 0
         */
        log.show("Lift target",li.getTarget());
        /**
         * Should change when outtake lock moves
         */
        log.show("Outtake lock position",lo.getPosition());
    }

    @Override
    public void stop() {
        /**
         * Halt the part
         */
        in.halt();
        cr.halt();
        li.halt();
    }
}
