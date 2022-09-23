package robot;

import static robot.RobotConfig.setConfig;

public class Configs implements RobotUser{

    /**
     * Used to test only the test part
     */
    public static RobotConfig TestConfig = new RobotConfig(customTestPart);

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
        setConfig(TestConfig);
    }

}
