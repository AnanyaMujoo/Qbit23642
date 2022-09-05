package robot.configs;

public class TankConfig extends RobotConfig{
    /**
     *  Define RobotPart objects here. If you are not using a specific robot part
     *  define the object but don't instantiate it. All other methods related to the robot
     *  should go in robot framework.
     */
    {
        define(
                tankDrive,
                tankIntake,
                tankTurret,
                tankLift,
                tankOuttake,
                tankCarousel,
                color,
                gyro,
                odometry,
                camera
        );
    }
}
