package unittests.tele.hardware.Tank;

import teleutil.button.Button;
import teleutil.button.OnTurnOffEventHandler;
import teleutil.button.OnTurnOnEventHandler;
import teleutil.button.WhenOnEventHandler;
import unittests.tele.TeleUnitTest;

import static global.General.*;

public class TankDriveTest extends TeleUnitTest {
    /**
     * Tests the tank drive
     */


    /**
     * Is the movement smooth?
     */
    boolean smooth = false;

    @Override
    public void init() {
        /**
         * Link the gamepad handlers
         */
        gph1.link(Button.Y, OnTurnOnEventHandler.class, () -> smooth = true);
        gph1.link(Button.Y, OnTurnOffEventHandler.class, () -> smooth = false);
    }

    @Override
    public void loop() {
        showConfig(bot.tankDrive);
        /**
         * Should move the tank drive
         */
        log.show("Use right stick y (forward) and left stick x (turn)");
        log.show("Press Y to switch between smooth and normal mode");
        if(!smooth){
            bot.tankDrive.move(-gamepad1.right_stick_y, gamepad1.left_stick_x);
            log.show("Current mode","Smooth");
        }else{
            bot.tankDrive.moveSmooth(-gamepad1.right_stick_y, gamepad1.left_stick_x);
            log.show("Current mode","Default");
        }
    }
}
