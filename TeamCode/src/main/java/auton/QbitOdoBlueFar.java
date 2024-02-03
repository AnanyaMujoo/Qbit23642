package auton;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.AutoFramework;
import autoutil.vision.CaseScannerRectBottom;
import elements.Field;
import elements.FieldPlacement;
import elements.FieldSide;
import elements.TeamProp;
import geometry.position.Pose;

import static global.General.fieldPlacement;
import static global.General.fieldSide;
import static global.General.log;


@Autonomous(name = "QbitOdoBlueFar", group = "Autonomous")
public class QbitOdoBlueFar extends AutoFramework {
    protected CaseScannerRectBottom CaseScanner = new CaseScannerRectBottom();

    {
        fieldSide = FieldSide.BLUE;
        fieldPlacement = FieldPlacement.LOWER;
        startPose = new Pose(20.5, Field.width / 2.0 - 1.5*Field.tileWidth, 90);
    }


    @Override
    public void initialize() {
        setConfig(mecanumNonstopConfig);

        lift.maintain();
        wait(0.5);
        scan(new CaseScannerRectBottom(), true, "blue", "right");


    }


    @Override
    public void define() {
        if (caseDetected==TeamProp.LEFT) {
            addSegment(0.4, mecanumDefaultWayPoint, 0, -70, 50);
            addConcurrentAutoModule(closeClaw());
            closeClaw();

            addTimedSetpoint(1.0, 0.4, 1.0, 13, -70, 60);
            addSegment(0.4, mecanumDefaultWayPoint, 0, -30, 0);

            addTimedSetpoint(1.0, 0.4, 1.0, 0, -10, 0);
            addSegment(0.8, mecanumDefaultWayPoint, 200, 0, 0);
            addTimedSetpoint(1.0, 0.4, 1.0, 208, 0, 0);
            addSegment(0.4, mecanumDefaultWayPoint, 200, 0, 80);
            addTimedSetpoint(1.0, 0.3, 1.0, 236, -58, 90);

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
            addSegment(1.0, 0.3, mecanumDefaultWayPoint, 180, -58, 90);
            addConcurrentAutoModule(WhyWontLiftWork());
            WhyWontLiftWork();
            addSegment(1.0, 0.3, mecanumDefaultWayPoint, 180, -65, 0);
            addSegment(1.0, 0.4, mecanumDefaultWayPoint, 235, -65, 0);
            addTimedSetpoint(1.0, 0.4, 1.0, 235, -115, 0);




//            addTimedSetpoint(1.0, 0.4, 1.0, 0, -15, 0);
        }
        if (caseDetected==TeamProp.CENTER) {
            addConcurrentAutoModule(closeClaw());
            closeClaw();
            addSegment(0.4, mecanumDefaultWayPoint, 0, -70, 0);

            addTimedSetpoint(1.0, 0.4, 1.0, 0, -76, 0);
            addSegment(0.4, mecanumDefaultWayPoint, 0, -30, 0);

            addTimedSetpoint(1.0, 0.4, 1.0, 0, -10, 0);
            addSegment(0.8, mecanumDefaultWayPoint, 200, 0, 0);
            addTimedSetpoint(1.0, 0.4, 1.0, 208, 0, 0);
            addSegment(0.4, mecanumDefaultWayPoint, 200, 0, 80);
            addTimedSetpoint(1.0, 0.3, 1.0, 236, -78, 90);

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
            addConcurrentAutoModule(pleaseDontLift());
            pleaseDontLift();
            addPause(3);
            addSegment(1.0, 0.3, mecanumDefaultWayPoint, 180, -78, 90);
            addConcurrentAutoModule(WhyWontLiftWork());
            WhyWontLiftWork();
            addSegment(1.0, 0.3, mecanumDefaultWayPoint, 180, -85, 0);
            addSegment(1.0, 0.4, mecanumDefaultWayPoint, 235, -85, 0);
            addTimedSetpoint(1.0, 0.4, 1.0, 235, -115, 0);



        }
        if (caseDetected==TeamProp.RIGHT) {
            addConcurrentAutoModule(closeClaw());
            closeClaw();
            addSegment(0.4, mecanumDefaultWayPoint, 0, -50, -30);

            addTimedSetpoint(1.0, 0.4, 1.0, 0, -60, -30);
            addSegment(0.4, mecanumDefaultWayPoint, 0, -30, 0);

            addTimedSetpoint(1.0, 0.4, 1.0, 0, -10, 0);
            addSegment(0.8, mecanumDefaultWayPoint, 200, 0, 0);
            addTimedSetpoint(1.0, 0.4, 1.0, 208, 0, 0);
            addSegment(0.4, mecanumDefaultWayPoint, 200, 0, 80);
            addTimedSetpoint(1.0, 0.3, 1.0, 236, -106, 90);

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
            addConcurrentAutoModule(pleaseDontLift());
            pleaseDontLift();
            addPause(3);
            addSegment(1.0, 0.3, mecanumDefaultWayPoint, 180, -106, 90);
            addConcurrentAutoModule(WhyWontLiftWork());
            WhyWontLiftWork();
            addSegment(1.0, 0.3, mecanumDefaultWayPoint, 235, -106, 0);
            addSegment(1.0, 0.4, mecanumDefaultWayPoint, 235, -106, 0);
            addTimedSetpoint(1.0, 0.4, 1.0, 235, -115, 0);


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
