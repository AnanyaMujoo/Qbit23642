package robot;

import static robot.RobotConfig.setConfig;

public class Configs implements RobotUser{

    /**
     * Used to test only the test part
     */
    RobotConfig TestConfig = new RobotConfig(customTestPart);
    RobotConfig TestConfig2 = new RobotConfig(testPart2);
    RobotConfig TestConfig3 = new RobotConfig(drive, camera);

//    RobotConfig PowerPlay = new RobotConfig(drive, lift, outtake, gyro, camera, odometry, distanceSensors);
//    RobotConfig PowerPlay = new RobotConfig(drive, lift, outtake, gyro, camera, odometry);
//    RobotConfig PowerPlay = new RobotConfig(drive, lift, outtake, gyro, camera, odometry);

    /**
     * Used for CenterStage robot
     */

  //  RobotConfig CenterStageTestConfig = new RobotConfig(outtake, drone);


//    RobotConfig CenterStageBasicConfig = new RobotConfig(outtake, drone, lift, hang, drive, intake, colorSensors, camera, gyro, odometry);

//RobotConfig CenterStageBasicConfig = new RobotConfig(outtake, drone, lift, hang, drive, intake, camera, gyro, odometry);

//    RobotConfig TestConfig4 = new RobotConfig(intake);
//    RobotConfig TestConfig5 = new RobotConfig(drive);
//
//    RobotConfig TestConfig6 = new RobotConfig(bucket,claw);


    RobotConfig IntoTheDeepBasicConfig = new RobotConfig(drive, liftIntake, liftOuttake, bucket, claw, intake, gyro, odometry);

//    RobotConfig IntoTheDeepConfig = new RobotConfig(drive, lift, liftVertical, claw, bucket, flip, intake, odometry);
    /**
     * Current Config
     */
    public void setCurrentConfig(){
        setConfig(IntoTheDeepBasicConfig);
    }

}
