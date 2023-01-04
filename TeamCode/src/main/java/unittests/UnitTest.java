package unittests;

import java.util.Map.*;

import automodules.AutoModuleUser;
import global.Modes;
import robot.RobotConfig;
import robot.RobotUser;
import robotparts.RobotPart;
import robotparts.electronics.continuous.CMotor;
import robotparts.electronics.continuous.CServo;
import robotparts.electronics.positional.PServo;
import util.Timer;
import util.condition.Status;
import util.template.Precision;

import static global.General.*;

public class UnitTest implements RobotUser, AutoModuleUser, Modes {


    protected final Precision precision = new Precision();

    /**
     * Status of the unit test, starts at idle
     */
    protected Status status = Status.IDLE;
    protected Timer timer = new Timer();

    /**
     * For init and stop see tele, run tests the UnitTest
     * @link Tele
     */
    public void init(){}
    public void test(){}
    public void stop(){}

    /**
     * Reset the timer
     */
    public void resetTimerIfIdle(){ if(status.equals(Status.IDLE)){ timer.reset();} }

    /**
     * Reset the status to idle
     */
    public void reset(){
        status = Status.IDLE;
    }

    /**
     * Method to show the configuration of the motors and servos in a robotpart
     * NOTE: This also displays separators
     * @param part
     */
    protected void showConfig(RobotPart part){
        log.header("config start");
        for (Entry<String, CMotor> entry: part.getElectronicsOfType(CMotor.class).entrySet()) { showConfig(entry.getKey(), entry.getValue()); }
        for (Entry<String, PServo> entry: part.getElectronicsOfType(PServo.class).entrySet()) { showConfig(entry.getKey(), entry.getValue()); }
        for (Entry<String, CServo> entry: part.getElectronicsOfType(CServo.class).entrySet()) { showConfig(entry.getKey(), entry.getValue()); }
        log.header("config end");
    }

    /**
     * Methods to show the config for a specific motor or servo
     * @param name
     * @param motor
     */
    protected void showConfig(String name, CMotor motor){
        log.showAndRecord("DcMotor: "+ name, "Dir: "+ motor.getDirection());
    }
    protected void showConfig(String name, PServo pservo){
        log.showAndRecord("Servo: "+ name, "Dir: "+ pservo.getDirection() + "Lower: "+ pservo.getLower() + "Upper: " + pservo.getUpper());
    }
    protected void showConfig(String name, CServo crservo){
        log.showAndRecord("CRServo: "+ name, "Dir: "+ crservo.getDirection());
    }
}
