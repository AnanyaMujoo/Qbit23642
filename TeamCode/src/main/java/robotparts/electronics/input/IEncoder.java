package robotparts.electronics.input;

import com.qualcomm.robotcore.hardware.DcMotor;

import robotparts.Electronic;

public class IEncoder extends Electronic {
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
}
