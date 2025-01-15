package robot;

//import robotparts.hardware.old.Drone;
//import robotparts.hardware.old.Hanging;
import robotparts.hardware.Bucket;
import robotparts.hardware.old.Flip;
import robotparts.hardware.Intake;
import robotparts.hardware.old.Intaketest;
import robotparts.hardware.LiftIntake;
import robotparts.hardware.LiftOuttake;
import robotparts.hardware.old.LiftVertical;
import robotparts.hardware.Claw;
import robotparts.sensors.odometry.NewOdometry;
import robotparts.sensors.odometry.OneOdometry;
//import robotparts.sensors.odometry.SecondOdometry;
import robotparts.sensors.odometry.RealOdometry;
import unittests.tele.framework.movement.AutoModuleTest;
import robotparts.hardware.Drive;
import robotparts.hardware.old.Lift;
//import robotparts.hardware.old.Outtake;
import robotparts.sensors.Cameras;
//import robotparts.sensors.ColorSensors;
import robotparts.sensors.GyroSensors;
import robotparts.unused.DistanceSensors;
import robotparts.unused.CustomTestPart;
import robotparts.unused.TouchSensors;

public interface RobotUser {
    /**
     * Implement this in any class that needs to use the robot
      */



    /**
     * USED
     */
    Drive drive = new Drive();
    Lift lift = new Lift();
    LiftIntake liftIntake = new LiftIntake();
    LiftOuttake liftOuttake = new LiftOuttake();
//    Outtake outtake = new Outtake();
//    Leds leds = new Leds();
    DistanceSensors distanceSensors = new DistanceSensors();
    GyroSensors gyro = new GyroSensors();
    Cameras camera = new Cameras();
//    Drone drone = new Drone();
//    Hanging hang = new Hanging();

//    Odometry odometry = new TwoOdometry();
//    ThreeOdometry odometry = new ThreeOdometry(); // TOD 5 EXTEND THIS CONCEPT TO ALL ROBOT PARTS
//    NewOdometry odometry = new NewOdometry();
//    OneOdometry oneOdometry = new OneOdometry();
    RealOdometry odometry = new RealOdometry();
//    SecondOdometry secondOdometry = new SecondOdometry();
    /**
     * UNUSED
     */

    //robotparts.unused.Intake intake = new robotparts.unused.Intake();
    Intake intake = new Intake();
    LiftVertical liftVertical = new LiftVertical();
    Claw claw = new Claw();

    Bucket bucket = new Bucket();
    Flip flip = new Flip();
    TouchSensors touchSensors = new TouchSensors();
    Intaketest intaketest = new Intaketest();
//    ColorSensors colorSensors = new ColorSensors();

    /**
     * Test Part
     */

    CustomTestPart customTestPart = new CustomTestPart();
    AutoModuleTest.TestPart2 testPart2 = new AutoModuleTest.TestPart2();



}
