package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.AutoFramework;
import elements.Field;
import elements.FieldPlacement;
import elements.FieldSide;
import elements.GameItems;
import geometry.position.Pose;
import static global.General.bot;
import static global.General.fieldPlacement;
import static global.General.fieldSide;



@Autonomous(name = "TerraAutoTest", group = "auto")
public class TerraAutoTest extends AutoFramework {
    {
        fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER;
        startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90);
    }

    @Override
    public void initialize() {
        setConfig(mecanumNonstopConfig);
        lift.maintain();
        outtake.readyStart(); outtake.closeClaw();
//        scan(false);
//        setScannerAfterInit(MecanumJunctionReactor2.junctionScanner);
    }

    public void place(){
        addAccuracyScaledSetpoint(1.0, 1.0,4.0, 136.5, 50);
    }

    public void pick(){
        addAccuracyScaledSetpoint(1.0, 0.7, 61, 126, 87);
    }

    @Override
    public void define() {
//        addConcurrentAutoModule(BackwardAuto2);
//        addCustomSegment(mecanumJunctionSetpoint2, 0, 0, 0);
//        addConcurrentAutoModule(ForwardAuto2);
//        addPause(2.0);
//        addScaledSetpoint(1.0, 0,128, 0);

        addScaledWaypoint(1.0, 0, 128, 0);
        addConcurrentAutoModule(BackwardAutoReady);
        addScaledWaypoint(1.0, 0, 124, 0);
        addAccuracyScaledSetpoint(2.0, 0.8,6, 142, 60);
        addAutoModule(BackwardAuto2);
        addPause(1.0);
        addAutoModule(ForwardAuto2);
        addPause(0.3);
        customNumber(1, i -> {
            addScaledWaypoint(0.6, 30, 126, 87);
            pick();
//            addScaledWaypoint(0.6, 30, 128, 75);
//            addScaledWaypoint(0.35, 3, 128.5, 50);
//            place();
        });
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
