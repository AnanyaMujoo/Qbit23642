package auton;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.AutoFramework;
import autoutil.vision.CaseScannerRectBl;
import autoutil.vision.CaseScannerRectBottom;
import autoutil.vision.CaseScannerRectRl;
import autoutil.vision.CaseScannerRectRr;
import elements.Field;
import elements.FieldPlacement;
import elements.FieldSide;
import elements.TeamProp;
import geometry.position.Pose;

import static global.General.fieldPlacement;
import static global.General.fieldSide;
import static global.General.log;


@Autonomous(name = "QbitOdoRedFar", group = "Autonomous")
public class QbitOdoRedFar extends AutoFramework {
    protected CaseScannerRectRr CaseScanner = new CaseScannerRectRr();

    {
        fieldSide = FieldSide.RED;
        fieldPlacement = FieldPlacement.LOWER;
        startPose = new Pose(20.5, Field.width / 2.0 - 1.5*Field.tileWidth, 90);
    }


    @Override
    public void initialize() {
        setConfig(mecanumNonstopConfig);

        lift.maintain();
        wait(0.5);
        scan(new CaseScannerRectRr(), true, "red", "right");


    }


    @Override
    public void define() {
        // TODO SWITCH RIGHT AND LEFT IF NEEDED
        if (caseDetected==TeamProp.RIGHT) {
            addSegment(0.4, mecanumDefaultWayPoint, 0, -74, 50);
            addConcurrentAutoModule(closeClaw());
            closeClaw();

            addTimedSetpoint(1.0, 0.4, 1.0, 13, -76, 60);
            addSegment(0.4, mecanumDefaultWayPoint, 0, -30, 0);

            addTimedSetpoint(1.0, 0.4, 1.0, 0, -10, 0);
            addSegment(0.8, mecanumDefaultWayPoint, 200, 0, 0);
            addTimedSetpoint(1.0, 0.4, 1.0, 200, 0, 0);
            addSegment(0.4, mecanumDefaultWayPoint, 200, -50, 89);
            addTimedSetpoint(1.0, 0.3, 1.0, 236, -60, 90);
//            addSegment(0.4, mecanumDefaultWayPoint, 200, -90, 89);
//            addTimedSetpoint(1.0, 0.3, 1.0, 236, -90, 90);

            addConcurrentAutoModule(AutoYellow());
            AutoYellow();
            addPause(1);
            addConcurrentAutoModule(openClaw());
            addPause(1);
            openClaw();
            addPause(1.5);
            closeClaw();
            addPause(0.5);
            openClaw();
            addPause(0.5);
            closeClaw();
            addSegment(1.0, 0.3, mecanumDefaultWayPoint, 180, -120, 90);
            addConcurrentAutoModule(WhyWontLiftWork());
            WhyWontLiftWork();
            addSegment(1.0, 0.4, mecanumDefaultWayPoint, 235, -125, 90);
            addTimedSetpoint(1.0, 0.4, 1.0, 235, -130, 90);




//            addTimedSetpoint(1.0, 0.4, 1.0, 0, -15, 0);
        }
        if (caseDetected==TeamProp.CENTER) {
            addConcurrentAutoModule(closeClaw());
            closeClaw();
            addSegment(0.4, mecanumDefaultWayPoint, 0, -70, 0);

            addTimedSetpoint(1.0, 0.4, 1.0, 0, -76, 0);
            addSegment(0.4, mecanumDefaultWayPoint, 0, -30, 0);

            addTimedSetpoint(1.0, 0.4, 1.0, 0, -10, 0);
            addSegment(0.8, mecanumDefaultWayPoint, 200, -10, 0);
            addTimedSetpoint(1.0, 0.4, 1.0, 200, -10, 0);
            addSegment(0.4, mecanumDefaultWayPoint, 200, -80, 89);
            addTimedSetpoint(1.0, 0.3, 1.0, 236, -80, 90);

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
            addSegment(1.0, 0.3, mecanumDefaultWayPoint, 180, -100, 90);
            addConcurrentAutoModule(WhyWontLiftWork());
            WhyWontLiftWork();
            addSegment(1.0, 0.4, mecanumDefaultWayPoint, 200, -125, 90);
            addTimedSetpoint(1.0, 0.4, 1.0, 235, -130, 90);



        }
        if (caseDetected==TeamProp.LEFT) {
            addConcurrentAutoModule(closeClaw());
            closeClaw();
            addSegment(0.4, mecanumDefaultWayPoint, 0, -50, -30);

            addTimedSetpoint(1.0, 0.4, 1.0, 0, -60, -30);
            addSegment(0.4, mecanumDefaultWayPoint, 0, -25, 0);

            addTimedSetpoint(1.0, 0.4, 1.0, 0, -10, 0);
            addSegment(0.8, mecanumDefaultWayPoint, 200, -10, 0);
            addTimedSetpoint(1.0, 0.4, 1.0, 208, -10, 0);
//            addSegment(0.4, mecanumDefaultWayPoint, 200, -50, 89);
//            addTimedSetpoint(1.0, 0.3, 1.0, 236, -60, 90);
            addSegment(0.4, mecanumDefaultWayPoint, 200, -90, 89);
            addTimedSetpoint(1.0, 0.3, 1.0, 236, -90, 90);
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
            addSegment(1.0, 0.3, mecanumDefaultWayPoint, 180, -100, 90);
            addConcurrentAutoModule(WhyWontLiftWork());
            WhyWontLiftWork();
            addSegment(1.0, 0.4, mecanumDefaultWayPoint, 200, -125, 90);
            addTimedSetpoint(1.0, 0.4, 1.0, 235, -130, 90);


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
