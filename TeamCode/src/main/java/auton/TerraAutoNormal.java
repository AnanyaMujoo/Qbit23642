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

public class TerraAutoNormal extends AutoFramework {

    @Override
    public void initialize() {
        setConfig(mecanumDefaultConfig);
//        bot.savePose(new Pose());
//        lift.maintain();
//        outtake.readyStart();
//        outtake.closeClaw();
        scan(false);
//        MecanumJunctionReactor.setFlipped(isFlipped());
//        setScannerAfterInit(MecanumJunctionReactor.junctionScanner);
//        JunctionScanner.resume();
    }

    @Override
    public void preProcess() {
//        caseDetected = Case.THIRD;
    }

    public void place(int i){
        if(i == 0) {
            addConcurrentAutoModule(BackwardAutoFirst);
            customFlipped(() -> {
                double x = 5.5; double y = 129;
                addSetpoint(3.0,1.05,  x, y, 50);
                addSegment(mecanumJunctionSetpoint, x, y, 50);
                addSetpoint(1.0, 1.05,x, y, 50);
            }, () -> {
                double x = 5.5; double y = 129;
                addSetpoint(3.0,1.05,  x, y, 50);
                addSegment(mecanumJunctionSetpoint, x, y, 50);
                addSetpoint(1.0, 1.05,x, y, 50);
            });
            addAutoModule(DropAutoFirst);
        }else{
            addConcurrentAutoModule(BackwardAuto);
            customFlipped(() -> {
                addSetpoint(1.0, 1.2,4.0, 130.5 + (i/3.0), 50);
            }, () -> {
                addSetpoint(1.0, 1.2,3.5-(i/5.0), 131+(i/4.0), 50);
            });
            addCancelAutoModules();
            addAutoModule(DropAuto);
        }
        addConcurrentAutoModule(ForwardAuto(i));
    }

    public void pick(int i){
        addBreakpoint(() -> timer.seconds() > 24.5);
        customFlipped(() -> {
            addSetpoint(2.0, 0.7, 61, 125 + (i/3.0), 87);
        }, () -> {
            addSetpoint(2.0, 0.7, 61-(i/5.0), 125, 87);
        });
        addConcurrentAutoModule(GrabAuto);
        addPause(0.5);
    }
    @Override
    public void define() {
//        addWaypoint(0, 40, 0);
//        customFlipped(() -> {
//            addScaledWaypoint(1.0, 0, 128, 0);
//        }, () -> {
//            addScaledWaypoint(1.0, 0, 125, 0);
//        });
//        addScaledWaypoint(1.0, 0, 120, 20);
//        place(0);
//        customNumber(4, i -> {
//            customFlipped(() -> {
//                addScaledWaypoint(0.4, 30, 125, 87);
//                pick(i+1);
//                addScaledWaypoint(0.6, 30, 128, 75);
//                addScaledWaypoint(0.35, 3, 128.5, 50);
//                place(i+1);
//            }, () -> {
//                addScaledWaypoint(0.4, 30, 125, 87);
//                pick(i+1);
//                addScaledWaypoint(0.6, 30, 128, 75);
//                addScaledWaypoint(0.35, 3, 128.5, 50);
//                place(i+1);
//            });
//        });
//        addBreakpointReturn();
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

    @Autonomous(name = "TerraAutoNormalRight", group = "auto", preselectTeleOp = "TerraOp")
    public static class TerraAutoNormalRight extends TerraAutoNormal {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90);}}
    @Autonomous(name = "TerraAutoNormalLeft", group = "auto", preselectTeleOp = "TerraOp")
    public static class TerraAutoNormalLeft extends TerraAutoNormal {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; startPose = new Pose(20.5, Field.width/2.0 + Field.tileWidth + GameItems.Cone.height + 16,90);}}

}
