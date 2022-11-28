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

import java.util.ArrayList;
import java.util.Map.*;
import java.util.Objects;
import java.util.TreeMap;

import automodules.AutoModule;
import automodules.StageBuilder;
import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
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
import util.codeseg.CodeSeg;
import util.codeseg.ParameterCodeSeg;
import util.codeseg.ReturnCodeSeg;
import util.condition.Expectation;
import util.condition.Magnitude;
import util.template.Iterator;

public abstract class RobotPart extends StageBuilder implements RobotUser {
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
    public abstract void init();
    public void reset(){}

    /**
     * Creates electronic given name and type
     * Ex: To create a CMotor named fr that has a default direction of forward
     *
     * fr = create(fr, ElectronicType.CMOTOR_FORWARD);
     *
     * @param name
     * @param type
     * @param <T>
     * @return electronic
     */
    @SuppressWarnings("unchecked")
    protected <T extends Electronic> T create(String name, ElectronicType type){
        Electronic newElectronic = createFromType(name, type);
        addElectronic(name, Objects.requireNonNull(newElectronic));
        return (T) newElectronic;
    }

    /**
     * Get electronic object from type
     * @param name
     * @param type
     * @return
     */
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
            case CSERVO_FORWARD:
                return new CServo(hardwareMap.get(CRServo.class, name), DcMotorSimple.Direction.FORWARD);
            case CSERVO_REVERSE:
                return new CServo(hardwareMap.get(CRServo.class, name), DcMotorSimple.Direction.REVERSE);
            case PMOTOR_FORWARD:
                return new PMotor(hardwareMap.get(DcMotor.class, name), DcMotorSimple.Direction.FORWARD, DcMotor.ZeroPowerBehavior.BRAKE, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            case PMOTOR_REVERSE:
                return new PMotor(hardwareMap.get(DcMotor.class, name), DcMotorSimple.Direction.REVERSE, DcMotor.ZeroPowerBehavior.BRAKE, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            case PMOTOR_FORWARD_FLOAT:
                return new PMotor(hardwareMap.get(DcMotor.class, name), DcMotorSimple.Direction.FORWARD, DcMotor.ZeroPowerBehavior.FLOAT, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            case PMOTOR_REVERSE_FLOAT:
                return new PMotor(hardwareMap.get(DcMotor.class, name), DcMotorSimple.Direction.REVERSE, DcMotor.ZeroPowerBehavior.FLOAT, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            case PSERVO_FORWARD:
                return new PServo(hardwareMap.get(Servo.class, name), Servo.Direction.FORWARD);
            case PSERVO_REVERSE:
                return new PServo(hardwareMap.get(Servo.class, name), Servo.Direction.REVERSE);
            case ICAMERA_EXTERNAL:
                return new ICamera((OpenCvInternalCamera) OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, name)), ICamera.CameraType.EXTERNAL, OpenCvCameraRotation.SIDEWAYS_RIGHT); // TODO CHECK
            case ICAMERA_EXTERNAL_DISPLAY:
                return new ICamera((OpenCvInternalCamera) OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, name), cameraMonitorViewId), ICamera.CameraType.EXTERNAL, OpenCvCameraRotation.SIDEWAYS_RIGHT);
            case ICAMERA_INTERNAL:
                return new ICamera(OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK), ICamera.CameraType.INTERNAL, OpenCvCameraRotation.UPRIGHT);
            case ICAMERA_INTERNAL_DISPLAY:
                return new ICamera(OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId), ICamera.CameraType.INTERNAL, OpenCvCameraRotation.UPRIGHT);
            case ICOLOR:
                return new IColor(hardwareMap.get(ColorRangeSensor.class, name));
            case IDISTANCE:
                return new IDistance(hardwareMap.get(DistanceSensor.class, name));
            case IENCODER_NORMAL:
                return new IEncoder(hardwareMap.get(DcMotor.class, IEncoder.getMotorName(name)), IEncoder.EncoderType.NORMAL);
            case IENCODER_PMOTOR:
                return new IEncoder(hardwareMap.get(DcMotor.class, IEncoder.getMotorName(name)), IEncoder.EncoderType.PMOTOR);
            case IENCODER_CMOTOR:
                return new IEncoder(hardwareMap.get(DcMotor.class, IEncoder.getMotorName(name)), IEncoder.EncoderType.CMOTOR);
            case IGYRO:
                return new IGyro(hardwareMap.get(BNO055IMU.class, name));
            case ITOUCH:
                return new ITouch(hardwareMap.get(TouchSensor.class, name));
            case OLED:
                return new OLed(hardwareMap.get(DigitalChannel.class,  "g" + name), hardwareMap.get(DigitalChannel.class,  "r" + name));
            default:
                fault.check("Electronic creation does not match any known type", Expectation.INCONCEIVABLE, Magnitude.CATASTROPHIC);
                return null;
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
    public <T> TreeMap<String, T> getElectronicsOfType(Class<T> electronicType) {
        TreeMap<String, T> ret = new TreeMap<>();
        for (Entry<String, Electronic> o : electronics.entrySet()) {
            if (o.getValue().getClass().equals(electronicType)) {
                ret.put(o.getKey(), (T) o.getValue());
            }
        }
        return ret;
    }

    /**
     * Halt the cmotors and cservos (i.e. set the power to 0)
     * NOTE: This should only be called in a thread that has access to use the robot
     */
    public void halt(){ Iterator.forAll(electronics, Electronic::halt); }

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
     * Stop the part
     * @return stop
     */
    @Override
    public final Stop stop(){ return new Stop(this::halt); }

    /**
     * Use this robot part
     * NOTE: This must be called before the robot part can be used in a stage
     * @return initial
     */
    @Override
    public final Initial usePart(){return new Initial(() -> switchUser(User.ROFU));}

    /**
     * Return the robot part to the main user
     * NOTE: This must be called after the robot part is use in a stage
     * @return stop
     */
    @Override
    public final Stop returnPart(){return new Stop(() -> switchUser(mainUser));}

    /**
     * Make part used for backgroud task
     * @return
     */
    public Initial usePartForBackgroundTask(){return new Initial(() -> switchUser(User.BACK));}
}
