package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.AutoFramework;
import elements.Case;
import elements.Field;
import elements.FieldPlacement;
import elements.FieldSide;
import elements.GameItems;
import geometry.position.Pose;

import static global.General.fieldPlacement;
import static global.General.fieldSide;



@Autonomous(name = "TerraAutoTest", group = "auto")
public class TerraAutoTest extends AutoFramework {
    {
        fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER;
        startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90);
    }

    double x, s, t;

    @Override
    public void initialize() {
        setConfig(mecanumNonstopConfig);
        lift.maintain();
        outtake.readyStart(); outtake.closeClaw();
//        scan(false);
//        setScannerAfterInit(MecanumJunctionReactor2.junctionScanner);
        x = 0; s = 0; t = 0;
    }

    @Override
    public void preProcess() {
        caseDetected = Case.FIRST;
    }

    @Override
    public void define() {


//        for (int i = 0; i < 5; i++) {
//            addScaledSetpoint( 0.5, 0, 50, 90);
//            addScaledSetpoint(0.5, 0, 0, -90);
//        }

//        Pose target = new Pose(0, 50, 90);
//        Pose adjusted = odometry.getAdjustedPose(target);
//        addScaledSetpoint(0.5, 0, 50, 90);
//        addScaledSetpoint(0.1, adjusted.getX(), adjusted.getY(), adjusted.getAngle());


//        for (int i = 0; i < 5; i++) {
//            addScaledSetpoint(0.5, 0, 50, 90);
//            addScaledSetpoint(0.5, 50, 50, 180);
//            addScaledSetpoint(0.5, 50, 0, 270);
//            addScaledSetpoint(0.5, 0, 0, 360);
//        }
//        addAccuracyScaledSetpoint(0.1, 0.5, 0, 0, 0);



//        addConcurrentAutoModule(BackwardAuto2);
//        addCustomSegment(mecanumJunctionSetpoint2, 0, 0, 0);
//        addConcurrentAutoModule(ForwardAuto2);
//        addPause(2.0);
//        addScaledSetpoint(1.0, 0,128, 90);
//        addScaledSetpoint(1.0, 0,128, -90);

        addConcurrentAutoModule(BackwardAutoReadyFirst);
        addWaypoint(1.0, 6, 122, 0);
        addCancelAutoModules();
        addConcurrentAutoModule(BackwardAuto2First);
        addTimedSetpoint(1.0, 0.7, 1.1, -6.5, 138, 45);
        addConcurrentAutoModule(ForwardAuto2(0));
        addPause(0.4);

        customNumber(5, i -> {
            switch (i){
                case 0: s = 0.5; x = 0.0; t = 0.0; break;
                case 1: s = 1.0; x = 0.6; t = 1.5; break;
                case 2: s = 1.5; x = 1.2; t = 3.0; break;
                case 3: s = 2.0; x = 1.8; t = 4.5; break;
                case 4: s = 2.5; x = 2.4; t = 6.0; break;
            }
            t = 0;
            addSegment(0.7, mecanumDefaultWayPoint, 18-x, 128 + s, 65-t);
            addSegment(0.7, mecanumDefaultWayPoint, 55-x, 122 + s, 87-t);
            addTimedWaypoint( 0.2, 0.3, 67-x, 123 + s, 87);
            addConcurrentAutoModule(GrabAuto2);
            addTimedWaypoint( 0.3, 0.3, 62-x, 123 + s, 87-t);
            addCancelAutoModules();
            addConcurrentAutoModule(BackwardAutoReady);
            addSegment(0.7, mecanumDefaultWayPoint, 30-x, 124 + s, 85-t);
            addSegment(0.6, mecanumDefaultWayPoint, 11-x, 135 + s, 52-t);
            addCancelAutoModules();
            addConcurrentAutoModule(BackwardAuto2);
            addTimedSetpoint(1.0, 0.6, 0.6, -6.5-x, 135 + s, 53-t);
            addCancelAutoModules();
            addConcurrentAutoModule(ForwardAuto2(i));
            addPause(0.25);
        });
        double y = -1.5;
        addConcurrentAutoModule(ForwardAuto2(-1));
        addTimedWaypoint(0.2, 0.5, -8.5, 136, -90);
        addSegment(1.0, mecanumDefaultWayPoint,  -50.0, 144, -93);
        addSegment(1.0, mecanumDefaultWayPoint, -190.0, 146, -93);
        addSegment(0.7, mecanumDefaultWayPoint,  -230.0, 146, -93);
        addTimedWaypoint(0.2, 0.3, -239, 146 + y, -93);
        addConcurrentAutoModule(GrabAuto2);
        addTimedWaypoint(0.2, 0.3, -235, 146 + y, -93);
//        addCancelAutoModules();
//        addConcurrentAutoModule(BackwardAutoReady);
//        addSegment(0.7, mecanumDefaultWayPoint, -200, 144, -85);
//        addSegment(0.6, mecanumDefaultWayPoint, -183, 161, -52);
//        addCancelAutoModules();
//        addConcurrentAutoModule(BackwardAuto2);
//        addTimedSetpoint(1.0, 0.6, 0.6, -161.5, 163+y, -56);
//        addCancelAutoModules();
//        addConcurrentAutoModule(ForwardAuto2(0));
//        addPause(0.25);
//        addSegment(0.7, mecanumDefaultWayPoint,  -186, 150, -65);
//        addSegment(0.7, mecanumDefaultWayPoint, -230, 146, -93);
//        addTimedWaypoint(0.2, 0.3, -239, 146+y, -93);
//        addConcurrentAutoModule(GrabAuto2);
//        addTimedWaypoint(0.2, 0.3, -235, 146+y, -93);
//        addCancelAutoModules();
//        addConcurrentAutoModule(BackwardAutoReady);
//        addSegment(0.7, mecanumDefaultWayPoint, -200, 144, -85);
//        addSegment(0.6, mecanumDefaultWayPoint, -183, 161, -52);
//        addCancelAutoModules();
//        addConcurrentAutoModule(BackwardAuto2);
//        addTimedSetpoint(1.0, 0.6, 0.6, -161.5, 163+y, -56);
//        addCancelAutoModules();
//        addConcurrentAutoModule(ForwardAuto2(5));
//        addPause(0.25);
//        addTimedSetpoint(1.0, 0.8, 0.5, -180.0, 148, 0);




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
//        bot.savePose(odometry.getPose());
//        bot.saveLocationOnField();
        super.stopAuto();
    }
}
