package robot;

import static robot.RobotConfig.setConfig;

public class Configs implements RobotUser{

    /**
     * Used when there is nothing on the robot
     */
    public static RobotConfig EmptyConfig = new RobotConfig();

    /**
     * Used when there is only part to test
     */
    public static RobotConfig TestConfig = new RobotConfig(tankLift);

    /**
     * Used for tank robots
     */
    public static RobotConfig TankConfig = new RobotConfig(tankDrive, tankIntake, tankTurret, tankLift, tankOuttake, tankCarousel, color, gyro, odometry, camera);

    /**
     * Used for mecanum robots
     */
    public static RobotConfig MecanumConfig = new RobotConfig(drive, gyro, odometry, camera, carousel, intake, lift, outtake, color);


    /**
     * Current Config
     */
    public static void setCurrentConfig(){
        setConfig(MecanumConfig);
    }

}
