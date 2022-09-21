package robotparts.electronics.positional;

import android.os.Build;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Servo.*;

import java.util.Objects;
import java.util.TreeMap;

import androidx.annotation.RequiresApi;
import robotparts.Electronic;

public class PServo extends Electronic {
    /**
     * Servo and related paramters
     */
    private final Servo servo;
    private final Direction direction;
    private final double lower;
    private final double upper;

    /**
     * Map of all of the preset positions and values
     */
    private final TreeMap<String, Double> positions = new TreeMap<>();

    /**
     * Constructor to create the the positional servo
     * @param s
     * @param dir
     * @param l
     * @param u
     */
    public PServo(Servo s, Direction dir, double l , double u){
        servo = s;
        lower = l;
        upper = u;
        direction = dir;
        servo.setDirection(direction);
        servo.scaleRange(lower, upper);
        addPosition("lower", lower);
        addPosition("upper", upper);
    }

    /**
     * Add a position to the pservo
     * @param name
     * @param p
     */
    public void addPosition(String name, double p){
        positions.put(name, p);
    }
//
//    public void changePosition(String name, double p){
//        positions.remove(name);
//        addPosition(name, p);
//    }

    /**
     * Set the position to the pservo based on the name
     * @param name
     */
    public void setPosition(String name){ if(access.isAllowed()){ servo.setPosition(positions.get(name)); } }

    /**
     * Get the position that the servo is trying to move to
     * @return position
     */
    public double getPosition(){
        return servo.getPosition();
    }

    /**
     * Get the lower bound of the pservo
     * @return lower
     */
    public double getLower(){
        return lower;
    }

    /**
     * Get the upper bound of the pservo
     * @return upper
     */
    public double getUpper(){
        return upper;
    }

    /**
     * Get the direction the servo is logically configured to move it
     * @return direction
     */
    public Direction getDirection(){
        return servo.getDirection();
    }

}
