package robot;

import static robot.RobotConfig.setConfig;

public class Configs implements RobotUser{

    /**
     * Used to test only the test part
     */
    RobotConfig TestConfig = new RobotConfig(customTestPart);

    /**
     * Used for tank robots
     */
    RobotConfig TankConfig = new RobotConfig(tankDrive, tankIntake, tankTurret, tankLift, tankOuttake, tankCarousel, color, gyro, odometry, camera);

    /**
     * Used for mecanum robots
     */
    RobotConfig MecanumConfig = new RobotConfig(drive, gyro, odometry, camera, carousel, intake, lift, outtake, color);

    /**
     * Current Config
     */
    public void setCurrentConfig(){
        setConfig(MecanumConfig);
    }

}
