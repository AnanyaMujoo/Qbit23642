package robot;

import robotparts.hardware.Drone;
import robotparts.hardware.Hanging;
import robotparts.hardware.Intake2;
import robotparts.sensors.odometry.NewOdometry;
import robotparts.sensors.odometry.OneOdometry;
//import robotparts.sensors.odometry.SecondOdometry;
import unittests.tele.framework.movement.AutoModuleTest;
import robotparts.hardware.Drive;
import robotparts.unused.Intake;
import robotparts.hardware.Lift;
import robotparts.hardware.Outtake;
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
    Outtake outtake = new Outtake();
//    Leds leds = new Leds();
    DistanceSensors distanceSensors = new DistanceSensors();
    GyroSensors gyro = new GyroSensors();
    Cameras camera = new Cameras();
    Drone drone = new Drone();
    Hanging hang = new Hanging();

//    Odometry odometry = new TwoOdometry();
//    ThreeOdometry odometry = new ThreeOdometry(); // TOD 5 EXTEND THIS CONCEPT TO ALL ROBOT PARTS
    NewOdometry odometry = new NewOdometry();
    OneOdometry oneOdometry = new OneOdometry();
//    SecondOdometry secondOdometry = new SecondOdometry();
    /**
     * UNUSED
     */

    Intake intake = new Intake();
    Intake2 intake2 = new Intake2();

    TouchSensors touchSensors = new TouchSensors();

//    ColorSensors colorSensors = new ColorSensors();

    /**
     * Test Part
     */

    CustomTestPart customTestPart = new CustomTestPart();
    AutoModuleTest.TestPart2 testPart2 = new AutoModuleTest.TestPart2();



}
