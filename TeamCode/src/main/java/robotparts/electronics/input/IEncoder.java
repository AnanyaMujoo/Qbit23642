package robotparts.electronics.input;

import com.qualcomm.robotcore.hardware.DcMotor;

import robotparts.Electronic;
import util.condition.Expectation;
import util.condition.Magnitude;

import static global.General.fault;

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
    private DcMotor motor;
    /**
     * Type of encoder
     * @link Type
     */
    private EncoderType encoderType;

    /**
     * Constructor to create the encoder
     * @param m
     * @param t
     */
    public IEncoder(DcMotor m, EncoderType t) {
        motor = m;
        encoderType = t;
    }

    /**
     * Get the current position in ticks
     * @return
     */
    public double getPos() {
        return motor.getCurrentPosition();
    }

    /**
     * Get the type of encoder
     * @return
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
        MOTOR
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
}
