package global;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import automodules.AutoModules;
import automodules.TankAutoModules;
import debugging.Synchroniser;
import elements.FieldSide;
import robot.TerraBot;
import teleutil.independent.Independents;
import teleutil.GamepadHandler;
import debugging.Fault;
import debugging.Logger;
import util.User;
import util.store.Storage;

public class General {
    /**
     *  Terrabot object, used for moving the robot, and other features related to the robot
     */
    public static TerraBot bot;
    /**
     * Hardware map object, has all of the hardware in the robot like the DcMotors and Servos
     */
    public static HardwareMap hardwareMap;
    /**
     * Telemetry object, used to display output to the phone (like system.out.print())
     */
    public static Telemetry telemetry;
    /**
     * Gamepad objects (controllers), there are two, one is conected using start+A and the other with start+B
     */
    public static Gamepad gamepad1;
    public static Gamepad gamepad2;
    /**
     * Elapsed timer (a timer) object that stores the gametimer, is reset on init
     */
    public static ElapsedTime gameTime;
    /**
     * Fault object (used to throw exceptions and raise warnings
     */
    public static Fault fault;
    /**
     * Gamepad handlers, used to make using the gamepads easier
     */
    public static GamepadHandler gph1;
    // TOD4
    // Make defualt onpress handler
    public static GamepadHandler gph2;
    /**
     * Logger object to store logs
     * NOTE: In most instances use logger instead of telemetry raw
     */
    public static Logger log;
    /**
     * Synchoniser which is used to record the refresh rate to check if the robot is lagging
     */
    public static Synchroniser sync;
    /**
     * Storage object to store data using addItem
     */
    public static Storage storage;
    /**
     * FieldSide object to represent which side of the field we are on (red or blue)
     */
    public static FieldSide fieldSide;
    /**
     * AutoModules (executes series of steps to make driving easier)
     */
    public static TankAutoModules tankAutoModules;
    public static AutoModules automodules;
    public static Independents independents;
    /**
     * The main user
     * NOTE: This should either be TELE or AUTO
     */
    public static User mainUser;
    /**
     * Camera Monitor View ID, used for camera viewing
     */
    public static int cameraMonitorViewId;
}
