package robot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;

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
import util.template.Iterator;

public class RobotConfig {
    /**
     * Arraylist for parts
     */
    private final ArrayList<RobotPart> parts;

    /**
     * Config Constructor
     * @param parts
     */
    public RobotConfig(RobotPart... parts){
       this.parts = new ArrayList<>(Arrays.asList(parts));
    }

    /**
     * Method to to set to current config
     */
    private void instantiate(){
        Iterator.forAll(parts, RobotPart::instantiate);
    }

    /**
     * Method to setConfig, must be called once
     * @param config
     */
    public static void setConfig(RobotConfig config){
        config.instantiate();
    }

}
