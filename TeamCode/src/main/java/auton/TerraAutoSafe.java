package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import automodules.AutoModule;
import autoutil.AutoFramework;
import autoutil.reactors.Reactor;
import elements.Case;
import elements.Field;
import elements.FieldPlacement;
import elements.FieldSide;
import elements.GameItems;
import geometry.position.Pose;
import robotparts.RobotPart;
import util.ExceptionCatcher;
import util.template.Mode;

import static global.General.bot;
import static global.General.fieldPlacement;
import static global.General.fieldSide;
import static global.Modes.Height.HIGH;

public class TerraAutoSafe extends AutoFramework {

    private double x, s;

    @Override
    public void initialize() {
        TerraAutoNormal.normalInit(this);
        x = 0;
        s = 0;
    }

    // TODO FIX SAFE AUTO

    // TODO UPDATE CONTROL HUB/ CODE VERSION

    // TODO MAKE UPRIGHT CONE

    // TODO MAKE ADJUST WHILE PAUSED

    // TODO MAKE Y MACHINE

    // TODO MAKE CYCLE STACK MACHINE

    AutoModule BackwardFirst = new AutoModule(
            RobotPart.pause(0.1),
            lift.changeCutoff(1.0),
            outtake.stageMiddle(0.0),
            lift.stageLift(1.0, heightMode.getValue(HIGH) + 3.5).attach(outtake.stageReadyEndAfter(0.3))
    );

    AutoModule Backward = new AutoModule(
            RobotPart.pause(0.1),
            outtake.stageFlip(0.0),
            lift.stageLift(1.0, heightMode.getValue(HIGH) + 4.5).attach(outtake.stageReadyEndAfter(0.25))
    );

    AutoModule Forward(int i) { return new AutoModule(
            outtake.stageEnd(0.1),
            outtake.stageOpen(0.0),
            lift.moveTime(-0.7, 0.15),
            lift.stageLift(1.0, i == 0 ? 14.5 : Math.max(14.5 - (i * 14.5 / 4.6), 0)).attach(outtake.stageStartAfter(0.1))
    );}


    static AutoModule CancelAfter(double t){ return new AutoModule(
            RobotPart.pause(t),
            Reactor.forceExit()
    );}

    AutoModule GrabBack = new AutoModule(
            outtake.stageClose(0.25),
            lift.moveTimeBack(-0.75, 0.5, () -> 0.15).attach(outtake.stageReadyStart(0.0))
    );
//
//    @Override
//    public void preProcess() {
//        caseDetected = Case.FIRST;
//    }

    @Override
    public void define() {

        // Pre-loaded cone move
        customFlipped(() -> {
            addSegment(1.0, mecanumDefaultWayPoint, -2.0, 80, 0.0);
            addSegment(0.5, mecanumDefaultWayPoint, -6.0, 105, 80.0);
            addConcurrentAutoModuleWithCancel(BackwardFirst);
            addSegment(0.8, mecanumDefaultWayPoint, -29.5, 119, 89.0);
            addSegment(0.5, mecanumDefaultWayPoint, -42.0, 107, 110.0);
        }, () -> {
            addSegment(1.0, mecanumDefaultWayPoint, -4.0, 80, 0.0);
            addSegment(0.5, mecanumDefaultWayPoint, -8.0, 105, 110.0);
            addConcurrentAutoModuleWithCancel(BackwardFirst);
            addSegment(0.6, mecanumDefaultWayPoint, -31.5, 114, 110.0);
            addSegment(0.5, mecanumDefaultWayPoint, -44.0, 107, 110.0);
        });
        // Pre-loaded cone place
        customFlipped(() -> {
            addTimedSetpoint(1.0, 0.5, 0.9, -68.0, 107, 110.0);
        }, () -> {
            addTimedSetpoint(1.0, 0.5, 0.9, -68.0, 104.5, 110.0);
        });
        addConcurrentAutoModuleWithCancel(Forward(0), 0.1);
        // Start 5 cycle
        customNumber(5, i -> {
            customFlipped(() -> {
                switch (i){
                    case 0: x = 0.0; s = 1.0;  break;
                    case 1: x = 1.0; s = 2.8;  break;
                    case 2: x = 2.0; s = 4.6;  break;
                    case 3: x = 3.0; s = 7.4;  break;
                    case 4: x = 4.0; s = 8.2;   break;
                }
            }, () -> {
                switch (i){
                    case 0: x = 0.0; s = -0.9;  break;
                    case 1: x = 1.0; s = -1.3;  break;
                    case 2: x = 1.0; s = -1.7;  break;
                    case 3: x = 2.0; s = -2.1;  break;
                    case 4: x = 2.0; s = -2.5;   break;
                }
            });
            // Move to pick
            customFlipped(() -> {
                addSegment(0.6, mecanumDefaultWayPoint, -37 - x, 119 + s, 110);
                addSegment(1.0, mecanumDefaultWayPoint, 20 - x, 123 + s, 91);
                addSegment(0.55, mecanumDefaultWayPoint, 60 - x, 123 + s, 93);
                addConcurrentAutoModuleWithCancel(CancelAfter(0.3));
                addSegment(0.1, mecanumDefaultWayPoint, 80 - x, 125 + s, 93);
            }, () -> {
                addSegment(0.6, mecanumDefaultWayPoint, -37 - x, 119 + s, 110);
                addSegment(1.0, mecanumDefaultWayPoint, 20 - x, 124 + s, 91);
                addSegment(0.55, mecanumDefaultWayPoint, 61 - x, 124 + s, 93);
                addConcurrentAutoModuleWithCancel(CancelAfter(0.3));
                addSegment(0.1, mecanumDefaultWayPoint, 80 - x, 126 + s, 93);
            });
            // Pick
            addCustomCode(() -> {
                bot.cancelAutoModules();
                bot.addAutoModule(GrabBack);
                pause(0.4);
            });
            customFlipped(() -> {
                // Move to place
                addSegment(1.0, mecanumDefaultWayPoint, 0 - x, 121 + s, 88);
                addConcurrentAutoModuleWithCancel(Backward);
                addSegment(0.5, mecanumDefaultWayPoint, -43 - x, 121 + s, 88);
                addTimedSetpoint(1.0, 0.5, 1.1, -71.0 - x, 107 + s, 114.0);
            }, () -> {
                addSegment(1.0, mecanumDefaultWayPoint, 0 - x, 120 + s, 90);
                addConcurrentAutoModuleWithCancel(Backward);
                addSegment(0.5, mecanumDefaultWayPoint, -43 - x, 120 + s, 92);
                addTimedSetpoint(1.0, 0.5, 1.1, -71.0 - x, 104.5 + s, 114.0);
            });
            // Place
            addConcurrentAutoModuleWithCancel(Forward(i + 1), 0.1);
        });
//        // Park
        customCase(() -> {
            addTimedWaypoint(0.6,0.3,  -58.5, 126.0, 129.0);
            addTimedWaypoint(0.3, 0.8, -61.5, 133.0, 0);
            addTimedSetpoint(1.0, 0.5, 1.2, -65, 78, 0);
        }, () -> {
            addSegment(0.6, mecanumDefaultWayPoint, -37 - x, 126 + s, 114);
            addSegment(0.6, mecanumDefaultWayPoint, -11.0, 114.5, 35.0);
            addTimedSetpoint(1.0, 0.5, 1.2, -4.0, 78, 0);
        }, () -> {
            addSegment(0.6, mecanumDefaultWayPoint, -37 - x, 123 + s, 110);
            addSegment(1.0, mecanumDefaultWayPoint, 15 - x, 123 + s, 91);
            addSegment(0.6, mecanumDefaultWayPoint, 45.0, 114.5, 35.0);
            addTimedSetpoint(1.0, 0.5, 1.3, 55, 78, 0);
        });
        addPause(0.1);
        // End
    }


    @Override
    public void postProcess() {
        autoPlane.reflectY();
        autoPlane.reflectX();
    }

//    @Autonomous(name = "C. RIGHT SAFE", group = "auto", preselectTeleOp = "TerraOp")
//    public static class RIGHT extends TerraAutoSafe {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90); }}
//
//    @Autonomous(name = "D. LEFT SAFE", group = "auto", preselectTeleOp = "TerraOp")
//    public static class LEFT extends TerraAutoSafe {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; startPose = new Pose(20.5, Field.width/2.0 + Field.tileWidth + GameItems.Cone.height + 16,90); }}



    @Autonomous(name = "C. RIGHT SAFE", group = "auto")
    public static class RIGHT extends TerraAutoSafe {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90); }}

    @Autonomous(name = "D. LEFT SAFE", group = "auto")
    public static class LEFT extends TerraAutoSafe {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; startPose = new Pose(20.5, Field.width/2.0 + Field.tileWidth + GameItems.Cone.height + 16,90); }}


}