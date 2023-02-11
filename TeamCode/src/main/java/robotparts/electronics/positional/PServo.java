package robotparts.electronics.positional;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Servo.*;

import java.util.Objects;
import java.util.TreeMap;

import robotparts.Electronic;
import util.Timer;

public class PServo extends Electronic {
    /**
     * Servo and related paramters
     */
    private final Servo servo;
    public final Direction direction;
    private double lower = 0;
    private double upper = 1;
    private final Timer timer = new Timer();
    private double contTarget = 0;
    private double contStart = 0;

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
        addPosition("start", 0.0);
        addPosition("end", 1.0);
        timer.reset();
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
    public void setPosition(String name){
        if(access != null) {
            if (access.isAllowed()) {
                servo.setPosition(positions.get(name));
            }
        }
//        else{
//            servo.setPosition(positions.get(name));
//        }
    }

    public void setPosition(double pos){
        if(access != null){
            if (access.isAllowed()) {
                servo.setPosition(pos);
            }
        }
//        else{
//            servo.setPosition(pos);
//        }
    }

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


    public void setContinuousTarget(String name){ contTarget = positions.get(name); contStart = servo.getPosition(); timer.reset(); }

    public void moveContinuous(double time){ servo.setPosition(contStart + ((contTarget-contStart) * timer.seconds()/time)); }

}
