package robot;

import static robot.RobotConfig.setConfig;

import robotparts.RobotPart;

public class Configs implements RobotUser{

    /**
     * Used to test only the test part
     */
//    RobotConfig TestConfig = new RobotConfig(customTestPart);
//    RobotConfig TestConfig2 = new RobotConfig(testPart2);
//    RobotConfig TestConfig3 = new RobotConfig(drive, camera);

//    RobotConfig PowerPlay = new RobotConfig(drive, lift, outtake, gyro, camera, odometry, distanceSensors);
//    RobotConfig PowerPlay = new RobotConfig(drive, lift, outtake, gyro, camera, odometry);
//    RobotConfig PowerPlay = new RobotConfig(drive, lift, outtake, gyro, camera, odometry);

    /**
     * Used for CenterStage robot
     */

//    RobotConfig CenterStageTestConfig = new RobotConfig(outtake, drone);


//    RobotConfig CenterStageBasicConfig = new RobotConfig(outtake, drone, lift, hang, drive, intake, colorSensors, camera, gyro, odometry);

//RobotConfig CenterStageBasicConfig = new RobotConfig(outtake, drone, lift, hang, drive, intake, camera, gyro, odometry);
    RobotConfig TestConfig = new RobotConfig(intake2);
    RobotConfig MahiyaConfig = new RobotConfig(mahiya);




    /**
     * Current Config
     */
    public void setCurrentConfig(){
        setConfig(MahiyaConfig);
    }

}
