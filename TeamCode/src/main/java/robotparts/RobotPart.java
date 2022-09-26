package robotparts;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import java.util.Map.*;
import java.util.Objects;
import java.util.TreeMap;

import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Stage;
import automodules.stage.Stop;
import robot.RobotFramework;
import robot.RobotUser;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;
import robotparts.electronics.continuous.CServo;
import robotparts.electronics.input.ICamera;
import robotparts.electronics.input.IColor;
import robotparts.electronics.input.IDistance;
import robotparts.electronics.input.IEncoder;
import robotparts.electronics.input.IGyro;
import robotparts.electronics.output.OLed;
import robotparts.electronics.positional.PMotor;
import robotparts.electronics.positional.PServo;

import static global.General.*;

import robotparts.electronics.input.ITouch;
import util.User;
import util.codeseg.ParameterCodeSeg;
import util.condition.Expectation;
import util.condition.Magnitude;
import util.template.Iterator;
import util.template.ParameterConstructor;

public class RobotPart implements RobotUser {
    /**
     * Represents a part of the robot like the drivetrain or the intake
     * When making a new part of the robot part make sure to extend this class
     * then override the init method
     */


    /**
     * TreeMap to store the list of electronics
     */
    public TreeMap<String, Electronic> electronics = new TreeMap<>();
    /**
     * The currentUser of the robotPart, by default none
     */
    private volatile User currentUser = User.NONE;

    /**
     * Method to "instantiate" the robot part
     * This automatically adds itself to the robotparts list
     */
    public void instantiate(){
        RobotFramework.allRobotParts.add(this);
    }

    /**
     * Init method (to be overwritten)
     */
    public void init() {}

    // TODO 4 FIX Finish this and implement it everywhere it needs to be




    @SuppressWarnings("unchecked")
    protected <T extends Electronic> T create(String name, ElectronicType type){
        Electronic newElectronic = createFromType(name, type);
        addElectronic(name, Objects.requireNonNull(newElectronic));
        return (T) newElectronic;
    }

    private Electronic createFromType(String name, ElectronicType type){
        switch (type){
            case CMOTOR_FORWARD:
                return new CMotor(hardwareMap.get(DcMotor.class, name), DcMotorSimple.Direction.FORWARD, DcMotor.ZeroPowerBehavior.BRAKE, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            case CMOTOR_REVERSE:
                return new CMotor(hardwareMap.get(DcMotor.class, name), DcMotorSimple.Direction.REVERSE, DcMotor.ZeroPowerBehavior.BRAKE, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            case CMOTOR_FORWARD_FLOAT:
                return new CMotor(hardwareMap.get(DcMotor.class, name), DcMotorSimple.Direction.FORWARD, DcMotor.ZeroPowerBehavior.FLOAT, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            case CMOTOR_REVERSE_FLOAT:
                return new CMotor(hardwareMap.get(DcMotor.class, name), DcMotorSimple.Direction.REVERSE, DcMotor.ZeroPowerBehavior.FLOAT, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            case PSERVO_FORWARD:
                return new PServo(hardwareMap.get(Servo.class, name), Servo.Direction.FORWARD, 0, 1);
            case PMOTOR_REVERSE:
                return new PServo(hardwareMap.get(Servo.class, name), Servo.Direction.REVERSE, 0, 1);
            default:
                fault.check("Electronic creation does not match any known type", Expectation.INCONCEIVABLE, Magnitude.CATASTROPHIC);
                return null;
        }
    }

    /**
     * Create different robot parts from a set of parameters
     */
    protected CMotor createCMotor(String name, DcMotor.Direction dir){
        CMotor cmotor = new CMotor(hardwareMap.get(DcMotor.class, name), dir, DcMotor.ZeroPowerBehavior.BRAKE, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        addElectronic(name, cmotor);
        return cmotor;
    }

    protected CMotor createCMotor(String name, DcMotor.Direction dir, DcMotor.ZeroPowerBehavior zpb, DcMotor.RunMode mode){
        CMotor cmotor = new CMotor(hardwareMap.get(DcMotor.class, name), dir, zpb, mode);
        addElectronic(name, cmotor);
        return cmotor;
    }

    protected CServo createCServo(String name, CRServo.Direction dir){
        CServo cservo = new CServo(hardwareMap.get(CRServo.class, name), dir);
        addElectronic(name, cservo);
        return cservo;
    }

    protected PServo createPServo(String name, Servo.Direction dir, double startpos, double endpos){
        PServo pservo = new PServo(hardwareMap.get(Servo.class, name), dir, startpos, endpos);
        addElectronic(name, pservo);
        return pservo;
    }

    protected PMotor createPMotor(String name, DcMotor.Direction dir){
        PMotor pmotor = new PMotor(hardwareMap.get(DcMotor.class, name), dir, DcMotor.ZeroPowerBehavior.BRAKE, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        addElectronic(name, pmotor);
        return pmotor;
    }

    protected PMotor createPMotor(String name, DcMotor.Direction dir, DcMotor.ZeroPowerBehavior zph){
        PMotor pmotor = new PMotor(hardwareMap.get(DcMotor.class, name), dir, zph, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        addElectronic(name, pmotor);
        return pmotor;
    }

    protected IGyro createGyro(String name){
        IGyro gyro = new IGyro(hardwareMap.get(BNO055IMU.class, name));
        addElectronic(name, gyro);
        return gyro;
    }

    protected IDistance createDistanceSensor(String name){
        IDistance distanceSensor = new IDistance(hardwareMap.get(DistanceSensor.class, name));
        addElectronic(name, distanceSensor);
        return distanceSensor;
    }

    protected IColor createColorSensor(String name){
        IColor colorSensor = new IColor(hardwareMap.get(ColorRangeSensor.class, name));
        addElectronic(name, colorSensor);
        return colorSensor;
    }

    protected ITouch createTouchSensor(String name){
        ITouch touchSensor = new ITouch(hardwareMap.get(TouchSensor.class, name));
        addElectronic(name, touchSensor);
        return touchSensor;
    }

    protected IEncoder createEncoder(String motor, String name, IEncoder.EncoderType encoderType){
        IEncoder encoder = new IEncoder(hardwareMap.get(DcMotor.class, motor), encoderType);
        addElectronic(name, encoder);
        return encoder;
    }

    protected OLed createLED(String name){
        OLed led = new OLed(hardwareMap.get(DigitalChannel.class,  "g" + name), hardwareMap.get(DigitalChannel.class,  "r" + name));
        addElectronic(name, led);
        return led;
    }

    protected ICamera createExternalCamera(String name, OpenCvCameraRotation orientation, boolean turnOnDisplay){
        if(turnOnDisplay) {
            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            return new ICamera(OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, name), cameraMonitorViewId), ICamera.CameraType.EXTERNAL, orientation);
        }else{
            return new ICamera(OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, name)), ICamera.CameraType.EXTERNAL, orientation);
        }
    }

    protected ICamera createInternalCamera(OpenCvCameraRotation orientation, boolean turnOnDisplay){
        if(turnOnDisplay) {
            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            return new ICamera(OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId), ICamera.CameraType.INTERNAL, orientation);
        }else{
            return new ICamera(OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK), ICamera.CameraType.INTERNAL, orientation);
        }
    }

    /**
     * Adds an electronic to the electronics arraylist
     * and gives access to the electronic to the curret thread
     */
    private void addElectronic(String name, Electronic e){
        e.access.allow();
        electronics.put(name, e);
    }


    /**
     * Gets treemap of specific electronics
     * @return Electronic TreeMap
     */
    @SuppressWarnings("unchecked")
    public <T> TreeMap<String, T> getElectronicsOfType(Class<T> encoderType) {
        TreeMap<String, T> ret = new TreeMap<>();
        for (Entry<String, Electronic> o : electronics.entrySet()) {
            if (o.getValue().getClass().equals(encoderType)) {
                ret.put(o.getKey(), (T) o.getValue());
            }
        }
        return ret;
    }

    /**
     * Halt the cmotors and cservos (i.e. set the power to 0)
     * NOTE: This should only be called in a thread that has access to use the robot
     */
    public void halt(){
        Iterator.forAll(electronics, Electronic::halt);
    }

    /**
     * Get the currentUser
     * @return currentUser
     */
    public User getUser(){
        return currentUser;
    }

    /**
     * Switch the user to the new user,
     * @param newUser
     */
    public synchronized void switchUser(User newUser){
        currentUser = newUser;
    }

    /**
     * Check the access of the user if they match the current user
     * This means that only the currentUser has access and all other users will be denied
     * In order to use a robotpart you must call switch user and then checkAccess
     * Returns true if the user access was granted
     * @param potentialUser
     */
    public synchronized boolean checkAccess(User potentialUser){
        if(potentialUser.equals(currentUser)) {
            Iterator.forAll(electronics, e -> e.access.allow());
            return true;
        }else{
            Iterator.forAll(electronics, e -> e.access.deny());
            return false;
        }
    }

    /**
     * For all electronics of a certain encoderType run...
     * @param run
     */
    private <T extends Electronic> void forAllElectronicsOfType(Class<T> encoderType, ParameterCodeSeg<T> run){ for(Electronic e: getElectronicsOfType(encoderType).values()){ run.run((T) e); } }

    /**
     * Exit based on time
     * @param s
     * @return exit
     */
    public static Exit exitTime(double s){return new Exit(() -> bot.rfsHandler.timer.seconds() > s);}

    /**
     * Exit always
     * @return exit
     */
    public static Exit exitAlways(){return new Exit(() -> true);}

    /**
     * Exit never
     * @return exit
     */
    public static Exit exitNever(){return new Exit(() -> false);}

    /**
     * Use this robot part
     * NOTE: This must be called before the robot part can be used in a stage
     * @return initial
     */
    public Initial usePart(){return new Initial(() -> switchUser(User.ROFU));}

    /**
     * Return the robot part to the main user
     * NOTE: This must be called after the robot part is use in a stage
     * @return stop
     */
    public Stop returnPart(){return new Stop(() -> switchUser(mainUser));}

    /**
     * Make part used for backgroud task
     * @return
     */
    public Initial usePartForBackgroundTask(){return new Initial(() -> switchUser(User.BACK));}

    /**
     * Pause for some amount of time
     * @param time
     * @return
     */
    public static Stage pause(double time){
        return new Stage(exitTime(time));
    }
}
