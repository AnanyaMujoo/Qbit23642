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
    public final Direction direction;
    private double lower = 0;
    private double upper = 1;

    /**
     * Map of all of the preset positions and values
     */
    private final TreeMap<String, Double> positions = new TreeMap<>();

    /**
     * Constructor to create the the positional servo
     * @param s
     * @param dir
     */
    public PServo(Servo s, Direction dir){
        servo = s;
        direction = dir;
        servo.setDirection(direction);
    }

    /**
     * Scale the range of the servo to low and high
     * @param l
     * @param u
     */
    public void scaleRange(double l, double u){
        lower = l;
        upper = u;
        servo.scaleRange(lower, upper);
    }

    /**
     * Add a position to the pservo
     * @param name
     * @param p
     */
    public void addPosition(String name, double p){
        positions.put(name, p);
    }


    /**
     * Change position to another value
     * @param name
     * @param p
     */
    public void changePosition(String name, double p){
        positions.remove(name);
        addPosition(name, p);
    }

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
