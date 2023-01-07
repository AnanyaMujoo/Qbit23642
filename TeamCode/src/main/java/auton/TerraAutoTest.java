package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.AutoFramework;
import elements.Case;
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

    @Override
    public void preProcess() {
        caseDetected = Case.FIRST;
    }

    @Override
    public void define() {
//        addConcurrentAutoModule(BackwardAuto2);
//        addCustomSegment(mecanumJunctionSetpoint2, 0, 0, 0);
//        addConcurrentAutoModule(ForwardAuto2);
//        addPause(2.0);
//        addScaledSetpoint(1.0, 0,128, 90);
//        addScaledSetpoint(1.0, 0,128, -90);

        // TODO FIX ADDING SCALE
        // TODO FIX RANDOM MESSUPS

        addConcurrentAutoModule(BackwardAutoReadyFirst);
        addScaledWaypoint(1.0, 6, 130, 0);
        addCancelAutoModules();
        addConcurrentAutoModule(BackwardAuto2First);
        addAccuracyTimedScaledSetpoint(1.0, 0.3, 0.9, 6, 142, 60);
        addConcurrentAutoModule(ForwardAuto2(0));
        addPause(0.4);
        customNumber(5, i -> {
            addScale(0.5); addCustomSegment(mecanumDefaultWayPoint, 20, 126, 87);
            addScale(0.7); addCustomSegment(mecanumDefaultWayPoint, 50, 120, 87);
            addScale(0.4); addCustomSegment(mecanumDefaultWayPoint, 76, 120, 87);
            addConcurrentAutoModule(GrabAuto2);
            addPause(0.35);
            addConcurrentAutoModule(BackwardAutoReady);
            addScale(0.6); addCustomSegment(mecanumDefaultWayPoint, 35+(1.5*i), 128, 60);
            addScale(0.6); addCustomSegment(mecanumDefaultWayPoint, 20+(1.5*i), 142, 60);
            addConcurrentAutoModule(BackwardAuto2);
            addAccuracyTimedScaledSetpoint(1.5, 0.35, 0.8, 6+(1.5*i), 142+(2.0*i), 60);
            addConcurrentAutoModule(ForwardAuto2(i));
            addPause(0.4);
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
