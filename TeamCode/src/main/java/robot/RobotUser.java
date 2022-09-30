package robot;

import org.checkerframework.checker.units.qual.C;

import robotparts.hardware.Carousel;
import robotparts.unused.mecanumold.MecanumCarousel;
import robotparts.hardware.Drive;
import robotparts.hardware.Intake;
import robotparts.hardware.Lift;
import robotparts.hardware.Outtake;
import robotparts.sensors.Cameras;
import robotparts.sensors.ColorSensors;
import robotparts.sensors.GyroSensors;
import robotparts.sensors.odometry.TwoOdometry;
import robotparts.unused.DistanceSensors;
import robotparts.unused.Leds;
import robotparts.sensors.odometry.TankOdometry;
import robotparts.unused.CustomTestPart;
import robotparts.unused.TouchSensors;
import robotparts.unused.mecanumold.MecanumDrive;
import robotparts.unused.mecanumold.MecanumIntake;
import robotparts.unused.mecanumold.MecanumLift;
import robotparts.unused.mecanumold.MecanumOuttake;
import robotparts.unused.tankold.TankCarousel;
import robotparts.unused.tankold.TankDrive;
import robotparts.unused.tankold.TankIntake;
import robotparts.unused.tankold.TankLift;
import robotparts.unused.tankold.TankOuttake;
import robotparts.unused.tankold.TankTurret;

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
    TwoOdometry odometry = new TwoOdometry();

    /**
     * UNUSED
     */
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

}
