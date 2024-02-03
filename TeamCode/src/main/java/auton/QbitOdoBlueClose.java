package auton;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.AutoFramework;
import autoutil.vision.CaseScannerRectBl;
import autoutil.vision.CaseScannerRectBottom;
import elements.Field;
import elements.FieldPlacement;
import elements.FieldSide;
import elements.TeamProp;
import geometry.position.Pose;

import static global.General.fieldPlacement;
import static global.General.fieldSide;


@Autonomous(name = "QbitoOdoBlueClose", group = "Autonomous")
public class QbitOdoBlueClose extends AutoFramework {
    protected TeamProp propCaseDetected = TeamProp.RIGHT;

    {
        fieldSide = FieldSide.BLUE;
        fieldPlacement = FieldPlacement.UPPER;
        startPose = new Pose(20.5, Field.width / 2.0 - 1.5*Field.tileWidth, 90);
    }


    @Override
    public void initialize() {
        setConfig(mecanumNonstopConfig);
        lift.maintain();
        wait(0.5);
        scan(new CaseScannerRectBl(), true, "blue", "left");
    }


    @Override
    public void define() {
        if (caseDetected==TeamProp.RIGHT) {
            addSegment(0.4, mecanumDefaultWayPoint, 0, -70, -50);
            addConcurrentAutoModule(closeClaw());
            closeClaw();

            addTimedSetpoint(1.0, 0.4, 1.0, -13, -70, -60);
            addSegment(0.4, mecanumDefaultWayPoint, 0, -30, 0);

            addTimedSetpoint(1.0, 0.4, 1.0, 0, -10, 0);
            addSegment(0.8, mecanumDefaultWayPoint, 86, 0, 0);
            addTimedSetpoint(1.0, 0.4, 1.0, 86, 0, 0);
            addSegment(0.4, mecanumDefaultWayPoint, 86, 0, 80);
            addTimedSetpoint(1.0, 0.3, 1.0, 117, -75, 90);

            addConcurrentAutoModule(AutoYellow());
            AutoYellow();
            addPause(0.5);
            AutoYellow();
            addConcurrentAutoModule(openClaw());
            addPause(1);
            openClaw();
            addPause(3);
            addSegment(1.0, 0.3, mecanumDefaultWayPoint, 86, -85, 0);
            addConcurrentAutoModule(WhyWontLiftWork());
            WhyWontLiftWork();
            addSegment(1.0, 0.4, mecanumDefaultWayPoint, 117, -85, 0);
            addTimedSetpoint(1.0, 0.4, 1.0, 117, -115, 0);
            //park on other side

//            addTimedSetpoint(1.0, 0.4, 1.0, 117, -10, 0);






//            addTimedSetpoint(1.0, 0.4, 1.0, 0, -15, 0);
        }
        if (caseDetected==TeamProp.CENTER) {
            addConcurrentAutoModule(closeClaw());
            closeClaw();
            addSegment(0.4, mecanumDefaultWayPoint, 0, -70, 0);

            addTimedSetpoint(1.0, 0.4, 1.0, 0, -76, 0);
            addSegment(0.4, mecanumDefaultWayPoint, 0, -30, 0);

            addTimedSetpoint(1.0, 0.4, 1.0, 0, -10, 0);
            addSegment(0.8, mecanumDefaultWayPoint, 86, 0, 0);
            addTimedSetpoint(1.0, 0.4, 1.0, 86, 0, 0);
            addSegment(0.4, mecanumDefaultWayPoint, 86, 0, 80);
            addTimedSetpoint(1.0, 0.3, 1.0, 117, -65, 90);

            addConcurrentAutoModule(AutoYellow());
            AutoYellow();
            addPause(0.5);
            AutoYellow();
            addConcurrentAutoModule(openClaw());
            addPause(1);
            openClaw();
            addPause(3);
            addSegment(1.0, 0.3, mecanumDefaultWayPoint, 86, -75, 0);
            addConcurrentAutoModule(WhyWontLiftWork());
            WhyWontLiftWork();
            addSegment(1.0, 0.4, mecanumDefaultWayPoint, 117, -75, 0);
            addTimedSetpoint(1.0, 0.4, 1.0, 117, -115, 0);
            //park on other side
//            addTimedSetpoint(1.0, 0.4, 1.0, 117, -10, 0);






        }
        if (caseDetected==TeamProp.LEFT) {
            addConcurrentAutoModule(closeClaw());
            closeClaw();
            addSegment(0.4, mecanumDefaultWayPoint, 0, -55, -20);

            addTimedSetpoint(1.0, 0.4, 1.0, 0, -60, -20);
            addSegment(0.4, mecanumDefaultWayPoint, 0, -30, 0);

            addTimedSetpoint(1.0, 0.4, 1.0, 0, -10, 0);
            addSegment(0.8, mecanumDefaultWayPoint, -86, 0, 0);
            addTimedSetpoint(1.0, 0.4, 1.0, -86, 0, 0);
            addSegment(0.4, mecanumDefaultWayPoint, -86, 0, -80);
            addTimedSetpoint(1.0, 0.3, 1.0, -117, -58, -90);

            addConcurrentAutoModule(AutoYellow());
            AutoYellow();
            addPause(1);
            addConcurrentAutoModule(openClaw());
            addPause(1);
            openClaw();
            addPause(1.5);
            closeClaw();
            openClaw();
            closeClaw();
            lift.stageLift(0.3,5);
            addPause(3);
            addSegment(1.0, 0.3, mecanumDefaultWayPoint, -86, -58, -90);
            addConcurrentAutoModule(WhyWontLiftWork());
            WhyWontLiftWork();
            addSegment(1.0, 0.3, mecanumDefaultWayPoint, -117, -65, 0);
            addSegment(1.0, 0.4, mecanumDefaultWayPoint, -117, -65, 0);
            addTimedSetpoint(1.0, 0.4, 1.0, -235, -117, 0);

            //park on other side

//            addTimedSetpoint(1.0, 0.4, 1.0, 117, -10, 0);







        }
//        }
//        addSegment(0.5, mecanumDefaultWayPoint, 0, 90, 0);
//
//        customFlipped(() -> {
//            addTimedSetpoint(1.0,0.5,1.5, -68,81,56);
//        }, () -> {
//            addTimedSetpoint(1.0,0.5,1.5, -68,81,45);
//        });
//        addConcurrentAutoModuleWithCancel(TerraAutoRam.BackwardFirst, 2.0);
//        addConcurrentAutoModuleWithCancel(TerraAutoRam.Forward, 1.2);

//        addTimedSetpoint(1.0, 0.5,0.6, -57, 70, 90);
//        addConcurrentAutoModule(new AutoModule(outtake.stage(0.2, 0.1), outtake.stageOpenComp(0.0),  lift.stageLift(1.0,  -0.5)));
//        customCase(() -> {
//            addTimedSetpoint(1.0, 0.5,1.0, -57, 70, 0);
//        }, () -> {
//            addSegment(0.5, noStopNewSetPoint, 0, 70, 90);
//            addTimedSetpoint(1.0, 0.5,1.0, 0, 70, 0);
//        }, () -> {
//            addSegment(0.5, noStopNewSetPoint, 58, 70, 90);
//            addTimedSetpoint(1.0, 0.5,1.0, 58, 70, 0);
//        });
//        addPause(0.1);

    }


    @Override
    public void postProcess() {
//        autoPlane.reflectY(); autoPlane.reflectX();
    }
}
