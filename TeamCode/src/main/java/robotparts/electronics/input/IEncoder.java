package robotparts.electronics.input;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import java.util.List;

import global.Constants;
import robot.BackgroundTask;
import robotparts.Electronic;
import util.condition.Expectation;
import util.condition.Magnitude;
import util.template.Iterator;

import static global.General.bot;
import static global.General.fault;
import static global.General.hardwareMap;

public class IEncoder extends Electronic {
    /**
     * Encoder class, note that the following naming convention should be used
     * [motor name]Enc
     *
     * Ex: If the motor was named bl, the encoder name would be blEnc
     */


    /**
     * Motor that the encoder refrences
     */
    private final DcMotorEx motor;
    /**
     * Type of encoder
     * @link Type
     */
    private final EncoderType encoderType;


    private volatile double position = 0; // ticks
    private volatile double angularVelocity = 0; // radians
    private volatile double current = 0; // amps

    /**
     * Constructor to create the encoder
     * @param m
     * @param t
     */
    public IEncoder(DcMotor m, EncoderType t) {
        motor = (DcMotorEx) m;
        encoderType = t;
        resetPrecisionTimers();
        if(encoderType.equals(EncoderType.PMOTOR)) {
            bot.addBackgroundTask(new BackgroundTask(this::updatePMotor));
        }else if(encoderType.equals(EncoderType.CMOTOR)){
            bot.addBackgroundTask(new BackgroundTask(this::updateCMotor));
        }
    }


    private void updateCMotor(){
        current = motor.getCurrent(CurrentUnit.AMPS);
    }

    private void updatePMotor(){
        position = motor.getCurrentPosition();
        angularVelocity = motor.getVelocity(AngleUnit.RADIANS);
        current = motor.getCurrent(CurrentUnit.AMPS);
    }

    public double getPos() { return position; }

    public double getAngularVelocity(){ return angularVelocity; }

    public double getCurrent(){ return current; }


    /**
     * Get the type of encoder
     * @return type
     */
    public EncoderType getType(){
        return encoderType;
    }

    /**
     * Reset the encoder
     */
    public void reset(){
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * Normal a separate encoder module and motor is a motor encoder
     */
    public enum EncoderType {
        NORMAL,
        PMOTOR,
        CMOTOR
    }

    /**
     * Gets the motor named from the encoderName
     * @param encoderName
     * @return
     */
    public static String getMotorName(String encoderName){
        fault.check("Encoder named incorrectly", Expectation.EXPECTED, Magnitude.MODERATE, encoderName.endsWith("Enc"), true);
        return encoderName.substring(0, encoderName.length() - 3);
    }

    public static void setEncoderReadingAuto(){
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);
        Iterator.forAll(allHubs, module -> module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO));
    }
}
