package robot;

import static robot.RobotConfig.setConfig;

public class Configs implements RobotUser{

    /**
     * Used to test only the test part
     */
    RobotConfig TestConfig = new RobotConfig(customTestPart);
    RobotConfig TestConfig2 = new RobotConfig(testPart2);
    RobotConfig TestConfig3 = new RobotConfig(drive, gyro, twoOdometry);
    RobotConfig TestConfig4 = new RobotConfig(drive, gyro, threeOdometry);
    RobotConfig TestConfig5 = new RobotConfig(drive, gyro, twoOdometryOnly);

    /**
     * Used for tank robots
     */
    RobotConfig TankConfig = new RobotConfig(tankDrive, tankIntake, tankTurret, tankLift, tankOuttake, tankCarousel, color, gyro, mecanumOdometry, camera);

    /**
     * Used for mecanum robots
     */
    RobotConfig MecanumConfig = new RobotConfig(mecanumDrive, gyro, mecanumOdometry, camera, mecanumCarousel, mecanumIntake, mecanumLift, mecanumOuttake, color);

    /**
     * Used for PowerPlay robot
     */
    RobotConfig PowerPlay = new RobotConfig(drive, intake, lift, outtake, carousel, color);

    /**
     * Current Config
     */
    public void setCurrentConfig(){
//        setConfig(TestConfig3);
        setConfig(TestConfig5);
//        setConfig(TestConfig4);
//        setConfig(PowerPlay);
//        setConfig(MecanumConfig);
    }

}
