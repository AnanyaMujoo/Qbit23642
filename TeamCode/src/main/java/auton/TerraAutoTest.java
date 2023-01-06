package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import automodules.AutoModule;
import automodules.AutoModuleUser;
import automodules.stage.Main;
import automodules.stage.Stage;
import autoutil.AutoFramework;
import autoutil.reactors.MecanumJunctionReactor;
import autoutil.vision.JunctionScanner;
import elements.Case;
import elements.Field;
import elements.FieldPlacement;
import elements.FieldSide;
import elements.GameItems;
import geometry.position.Pose;
import global.Constants;
import util.Timer;
import util.condition.DecisionList;

import static display.Drawer.fieldSize;
import static global.General.bot;
import static global.General.fieldPlacement;
import static global.General.fieldSide;
import static global.General.log;


@Autonomous(name = "TerraAutoTest", group = "auto")
public class TerraAutoTest extends AutoFramework {
    {
        fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER;
        startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90);
    }

    @Override
    public void initialize() {
        setConfig(mecanumNonstopConfig);
    }

    public void place(){
        addAccuracyScaledSetpoint(1.0, 1.0,4.0, 130.5, 50);
    }

    public void pick(){
        addAccuracyScaledSetpoint(2.0, 0.7, 61, 125, 87);
    }

    @Override
    public void define() {
        addScaledWaypoint(1.0, 0, 128, 0);
        addAccuracyScaledSetpoint(1.0, 0.8,5.5, 129, 50);
//        customNumber(1, i -> {
//            addScaledWaypoint(0.4, 30, 125, 87);
//            pick();
//            addScaledWaypoint(0.6, 30, 128, 75);
//            addScaledWaypoint(0.35, 3, 128.5, 50);
//            place();
//        });
//        customCase(() -> {
//            addWaypoint(-7, 124, 90);
//            addScaledWaypoint(0.8, -10, 124, 90);
//            addScaledWaypoint(0.8, -45, 122, 60);
//            addScaledWaypoint(0.8, -50, 123, 25);
//            addScaledSetpoint(0.9, -62, 75, 0);
//        }, () -> {
//            addWaypoint(0, 130, 35);
//            addWaypoint(0, 105, 0);
//            addScaledSetpoint(0.9, 0, 75, 0);
//        }, () -> {
//            addWaypoint(7, 128, 90);
//            addScaledWaypoint(0.8, 16, 128, 90);
//            addScaledWaypoint(0.8, 39, 122, 58);
//            addScaledWaypoint(0.8, 51, 111, 32);
//            addScaledWaypoint(0.8, 56, 95, 0);
//            addScaledSetpoint(0.9, 58, 75, 0);
//        });
    }


    @Override
    public void postProcess() {
        autoPlane.reflectY(); autoPlane.reflectX();
    }

    @Override
    public void stopAuto() {
        bot.savePose(odometry.getPose());
        bot.saveLocationOnField();
        super.stopAuto();
    }
}
