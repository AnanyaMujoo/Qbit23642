package util;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import util.condition.Expectation;
import util.condition.Magnitude;

import static global.General.fault;

public enum User {
    /**
     * A user enum which represents what is using something, either teleop, auton, robotfunctions, or none
     * This is used in order to control which threads have access to move parts of the robot
     * @link Automodules
     */
    TELE,
    AUTO,
    ROFU,
    BACK,
    NONE;

    /**
     * Get the user from the type of opmode, if the opmode is a linear opmode then set it to auton
     * Used to automatically set the main user to AUTO or TELE
     * NOTE: If null is passed in this method returns NONE
     * @param opMode
     * @return user
     */
    public static User getUserFromTypeOfOpMode(OpMode opMode){
        if(opMode instanceof LinearOpMode){
            return User.AUTO;
        }else if(opMode != null){
            return User.TELE;
        }else{
            fault.warn("The main user is set to NONE", Expectation.INCONCEIVABLE, Magnitude.CRITICAL);
            return User.NONE;
        }
    }
}
