package robot.configs;

import robot.RobotFramework;
import robotparts.RobotPart;
import robotparts.hardware.mecanum.MecanumCarousel;
import robotparts.hardware.mecanum.MecanumDrive;
import robotparts.hardware.mecanum.MecanumIntake;
import robotparts.hardware.mecanum.MecanumLift;
import robotparts.hardware.mecanum.MecanumOuttake;
import robotparts.sensors.TwoOdometry;
import robotparts.unused.tank.TankCarousel;
import robotparts.unused.tank.TankIntake;
import robotparts.unused.tank.TankLift;
import robotparts.unused.tank.TankOuttake;
import robotparts.unused.tank.TankDrive;
import robotparts.unused.tank.TankTurret;
import robotparts.sensors.Cameras;
import robotparts.sensors.ColorSensors;
import robotparts.unused.DistanceSensors;
import robotparts.sensors.GyroSensors;
import robotparts.unused.Leds;
import robotparts.unused.TankOdometry;
import robotparts.unused.TouchSensors;

public abstract class RobotConfig extends RobotFramework {

    /**
     * USED
     */
    public final MecanumDrive drive = new MecanumDrive();
    public final MecanumCarousel carousel = new MecanumCarousel();
    public final MecanumIntake intake = new MecanumIntake();
    public final MecanumLift lift = new MecanumLift();
    public final MecanumOuttake outtake = new MecanumOuttake();
    public final ColorSensors color = new ColorSensors();
    public final GyroSensors gyro = new GyroSensors();
    public final Cameras camera = new Cameras();
    public final TwoOdometry odometry = new TwoOdometry();






    /**
     * UNUSED
     */

    /**
     * All of the tank-specific robot parts
     */
    public final TankDrive tankDrive = new TankDrive();
    public final TankIntake tankIntake = new TankIntake();
    public final TankTurret tankTurret = new TankTurret();
    public final TankLift tankLift = new TankLift();
    public final TankOuttake tankOuttake = new TankOuttake();
    public final TankCarousel tankCarousel = new TankCarousel();
    public final TankOdometry tankOdometry = new TankOdometry();

    /**
     * All of the robot parts in both tank and mecanum
     */
    public final TouchSensors touchSensors = new TouchSensors();
    public final DistanceSensors distanceSensors = new DistanceSensors();
    public final Leds leds = new Leds();


    protected final void define(RobotPart... parts){
        for(RobotPart part:parts){ part.instantiate(); }
    }
}
