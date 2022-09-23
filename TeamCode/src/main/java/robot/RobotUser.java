package robot;

import robotparts.hardware.mecanum.MecanumCarousel;
import robotparts.hardware.mecanum.MecanumDrive;
import robotparts.hardware.mecanum.MecanumIntake;
import robotparts.hardware.mecanum.MecanumLift;
import robotparts.hardware.mecanum.MecanumOuttake;
import robotparts.sensors.Cameras;
import robotparts.sensors.ColorSensors;
import robotparts.sensors.GyroSensors;
import robotparts.sensors.TwoOdometry;
import robotparts.unused.DistanceSensors;
import robotparts.unused.Leds;
import robotparts.unused.TankOdometry;
import robotparts.unused.CustomTestPart;
import robotparts.unused.TouchSensors;
import robotparts.unused.tank.TankCarousel;
import robotparts.unused.tank.TankDrive;
import robotparts.unused.tank.TankIntake;
import robotparts.unused.tank.TankLift;
import robotparts.unused.tank.TankOuttake;
import robotparts.unused.tank.TankTurret;

public interface RobotUser {
    /**
     * Implement this in any class that needs to use the robot
      */



    /**
     * USED
     */

    MecanumDrive drive = new MecanumDrive();
    MecanumCarousel carousel = new MecanumCarousel();
    MecanumIntake intake = new MecanumIntake();
    MecanumLift lift = new MecanumLift();
    MecanumOuttake outtake = new MecanumOuttake();

    ColorSensors color = new ColorSensors();
    GyroSensors gyro = new GyroSensors();
    Cameras camera = new Cameras();
    TwoOdometry odometry = new TwoOdometry();

    /**
     * UNUSED
     */

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
