package robot;

import unused.auto.MecanumCarousel;
import robotparts.sensors.odometry.Odometry;
import robotparts.sensors.odometry.ThreeOdometry;
import unittests.tele.framework.movement.AutoModuleTest;
import robotparts.hardware.Drive;
import robotparts.hardware.Intake;
import robotparts.hardware.Lift;
import robotparts.hardware.Outtake;
import robotparts.sensors.Cameras;
import robotparts.sensors.ColorSensors;
import robotparts.sensors.GyroSensors;
import robotparts.unused.DistanceSensors;
import robotparts.unused.Leds;
import unused.mecanumold.auto.TwoOdometryOldMecanum;
import unused.tankold.TankOdometry;
import robotparts.unused.CustomTestPart;
import robotparts.unused.TouchSensors;
import unused.mecanumold.MecanumDrive;
import unused.mecanumold.MecanumIntake;
import unused.mecanumold.MecanumLift;
import unused.mecanumold.MecanumOuttake;
import unused.tankold.TankCarousel;
import unused.tankold.TankDrive;
import unused.tankold.TankIntake;
import unused.tankold.TankLift;
import unused.tankold.TankOuttake;
import unused.tankold.TankTurret;

public interface RobotUser {
    /**
     * Implement this in any class that needs to use the robot
      */



    /**
     * USED
     */
    Drive drive = new Drive();
    Intake intake = new Intake();
    Lift lift = new Lift();
    Outtake outtake = new Outtake();


    ColorSensors color = new ColorSensors();
    GyroSensors gyro = new GyroSensors();
    Cameras camera = new Cameras();

//    Odometry odometry = new TwoOdometry();
    Odometry odometry = new ThreeOdometry(); // TOD 5 EXTEND THIS CONCEPT TO ALL ROBOT PARTS
    /**
     * UNUSED
     */
    TwoOdometryOldMecanum mecanumOdometry = new TwoOdometryOldMecanum();
    MecanumOuttake mecanumOuttake = new MecanumOuttake();
    MecanumDrive mecanumDrive = new MecanumDrive();
    MecanumIntake mecanumIntake = new MecanumIntake();
    MecanumLift mecanumLift = new MecanumLift();
    unused.mecanumold.MecanumCarousel mecanumCarousel = new unused.mecanumold.MecanumCarousel();

    TankDrive tankDrive = new TankDrive();
    TankIntake tankIntake = new TankIntake();
    TankTurret tankTurret = new TankTurret();
    TankLift tankLift = new TankLift();
    TankOuttake tankOuttake = new TankOuttake();
    TankCarousel tankCarousel = new TankCarousel();
    TankOdometry tankOdometry = new TankOdometry();

    TouchSensors touchSensors = new TouchSensors();
    DistanceSensors distanceSensors = new DistanceSensors();
    Leds leds = new Leds();

    /**
     * Test Part
     */

    CustomTestPart customTestPart = new CustomTestPart();
    AutoModuleTest.TestPart2 testPart2 = new AutoModuleTest.TestPart2();

}
