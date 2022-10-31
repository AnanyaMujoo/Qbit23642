package robot;

import robotparts.hardware.Carousel;
import robotparts.sensors.odometry.ThreeOdometry;
import robotparts.sensors.odometry.TwoOdometry;
import robotparts.sensors.odometry.TwoOdometryOnly;
import unittests.tele.framework.movement.AutoModuleTest;
import unused.mecanumold.MecanumCarousel;
import robotparts.hardware.Drive;
import robotparts.hardware.Intake;
import robotparts.hardware.Lift;
import robotparts.hardware.Outtake;
import robotparts.sensors.Cameras;
import robotparts.sensors.ColorSensors;
import robotparts.sensors.GyroSensors;
import unused.mecanumold.auto.TwoOdometryOld;
import robotparts.unused.DistanceSensors;
import robotparts.unused.Leds;
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
    Carousel carousel = new Carousel();


    ColorSensors color = new ColorSensors();
    GyroSensors gyro = new GyroSensors();
    Cameras camera = new Cameras();

    TwoOdometry twoOdometry = new TwoOdometry();
    ThreeOdometry threeOdometry = new ThreeOdometry();
    TwoOdometryOnly twoOdometryOnly = new TwoOdometryOnly();
    /**
     * UNUSED
     */
    TwoOdometryOld mecanumOdometry = new TwoOdometryOld();
    MecanumOuttake mecanumOuttake = new MecanumOuttake();
    MecanumDrive mecanumDrive = new MecanumDrive();
    MecanumIntake mecanumIntake = new MecanumIntake();
    MecanumLift mecanumLift = new MecanumLift();
    MecanumCarousel mecanumCarousel = new MecanumCarousel();

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
