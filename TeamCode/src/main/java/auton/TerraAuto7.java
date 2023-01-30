package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import automodules.AutoModule;
import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import autoutil.AutoFramework;
import autoutil.reactors.MecanumJunctionReactor2;
import autoutil.reactors.Reactor;
import elements.Case;
import elements.Field;
import elements.FieldPlacement;
import elements.FieldSide;
import elements.GameItems;
import elements.Robot;
import geometry.framework.Point;
import geometry.position.Pose;
import robotparts.RobotPart;
import util.ExceptionCatcher;
import util.template.Mode;

import static global.General.bot;
import static global.General.fieldPlacement;
import static global.General.fieldSide;
import static global.General.log;
import static global.Modes.Height.HIGH;
import static global.Modes.Height.LOW;
import static global.Modes.Height.MIDDLE;


//    preselectTeleOp = "TerraOp"
@Autonomous(name = "TerraAuto7", group = "auto")
public class TerraAuto7 extends AutoFramework {

    {
        fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90);
    }

    private double x, s, y;

    @Override
    public void initialize() {
        setConfig(mecanumNonstopConfig);

        outtake.changeArmPosition("start", 0.03);

        lift.maintain();
        outtake.closeClaw();
        ExceptionCatcher.catchInterrupted(() -> Thread.sleep(500));
        outtake.readyStart();
        scan(false);
        x = 0; s = 0; y = 119;
    }

    AutoModule BackwardFirst = new AutoModule(
            RobotPart.pause(0.2),
            outtake.stageReadyEnd(0.0),
            lift.stageLift(1.0, heightMode.getValue(HIGH)+3.0)
    );

    static AutoModule Backward(double height){return new AutoModule(
            outtake.stage(0.67, 0.1),
            lift.stageLift(1.0, heightMode.getValue(HIGH)+height).attach(outtake.stageReadyEndAfter(0.1)),
            lift.moveTime(0.2, 0.4)
    );}

    AutoModule Forward(int i){return new AutoModule(
            outtake.stageEnd(0.1),
            outtake.stageOpen(0.0),
            lift.moveTime(-0.8, 0.2),
            outtake.stageAfter(0.025*(5-i) + 0.03, 0.0),
            lift.stageLift(1.0, i < 2 ? 5 : 0)
    );}

    AutoModule Grab = new AutoModule(
            outtake.stageClose(0.15),
            outtake.stage(0.67, 0.0),
            lift.moveTime(1,0.3)
    );

    @Override
    public void define() {



        // Pre-loaded cone move
        addWaypoint(1.0, 0, 100, 0);
//        addConcurrentAutoModule(BackwardFirst);
        // Pre-loaded cone place
        addTimedSetpoint(1.0, 0.65, 1.2, -6.5, 138, 40);
        addConcurrentAutoModule(Forward(0));
////
////        // Start 5 cycle
////        customNumber(5, i -> {
////            switch (i){
////                case 0: x = -0.5; s = -0.2;  break;
////                case 1: x = 0.6;  s = 1.0;  break;
////                case 2: x = 0.6;  s = 1.5;  break;
////                case 3: x = 1.0;  s = 2.0;  break;
////                case 4: x = 1.0;  s = 3.0;   break;
////            }
////            // Move to pick
////            addSegment(0.8, mecanumDefaultWayPoint, 18-x, 128 + s, 80);
////            addSegment(0.8, mecanumDefaultWayPoint, 52-x, 125 + s, 87);
////            addTimedWaypoint( 0.2, 0.2, 66-x, 126 + s, 87);
////            // Pick
////            addConcurrentAutoModuleWithCancel(Grab);
////            addTimedWaypoint( 0.3, 0.3, 60-x, 126 + s, 89);
////            // Move to place
////            addConcurrentAutoModuleWithCancel(Backward(4));
////            addSegment(0.8, mecanumDefaultWayPoint, 30-x, 124 + s, 75);
////            addSegment(0.8, mecanumDefaultWayPoint, 15-x, 137 + s, 47);
////            // Place
////            addTimedSetpoint(1.0, 0.8, 0.6, -9 - x, 143 + s, 53);
////            addConcurrentAutoModuleWithCancel(Forward(i+1 == 5 ? 0 : i+1), 0.1);
////        });
//        addPause(0.05);
//        addTimedSetpoint(2.0, 0.6, 0.6, -8.5, 129, -93);

        // Move to other side

        addTimedSetpoint(2.0, 0.6, 0.6, -8.5, 129, -93);
//        addTimedSetpoint(1.0, 0.4, 1.0, 0,0.01, -90);
        addCancelAutoModules();
        addCustomCode(() -> {
            Double[] distanceRight = new Double[]{100.0};
            whileNotExit(() -> odometry.getY() > 240 && distanceRight[0] < 30, () -> {
                log.show("Pose", odometry.getPose());
                drive.move(1.0,0.1*(150-odometry.getX()), 0.015*odometry.getHeading());
                distanceSensors.ready();
                distanceRight[0] = distanceSensors.getRightDistance();
            });
            Point point = new Point(135+distanceRight[0], Field.width - 46 - Robot.halfLength);
            odometry.setPoseUsingOffset(new Pose(point, 0));
        });
//        addConcurrentAutoModuleWithCancel(new AutoModule(new Stage(
//                drive.usePart(),
//                // TODO Move without voltage scale here
//                new Main(() -> {
////                    log.show("Pose", odometry.getPose());
//                    drive.move(1.0,0.1*(150-odometry.getX()), 0.015*odometry.getHeading());
////                    drive.move(1.0, 0.03*(odometry.getY()-135), -0.01*(odometry.getHeading()-(-93)));
//                    distanceSensors.ready();
//                    //&& distanceSensors.getRightDistance() < 30
//                }),
//                new Exit(() -> (odometry.getY() < 240)),
//                drive.returnPart())
//        ));

//        addCustomCode(() -> {
//            distanceSensors.ready();
//            Point point = new Point(odometry.getX(), Field.width - distanceSensors.getFrontDistance() - Robot.halfLength);
//            odometry.setPoseUsingOffset(new Pose(point, 0));
////            odometry.setCurrentPoint(point); odometry.setCurrentPoint(point);
//        });
//
//        addTimedSetpoint(1.0, 0.8, 2.0, -200, y+6, -93);

//        addCustomCode(() -> {
//            distanceSensors.ready();
//            double distanceFront = distanceSensors.getFrontDistance();
//            if (distanceFront < 40 ){
//                distanceFront+= GameItems.Cone.baseWidth;
//            }
//            Point point = new Point(135+distanceSensors.getRightDistance(), Field.width - distanceFront - Robot.halfLength);
//            odometry.setPoseUsingOffset(new Pose(point, 0));
////            odometry.setCurrentPoint(point); odometry.setCurrentPoint(point);
//        });
        addSegment(1.0, mecanumDefaultWayPoint, -225.0, y+11, -93);
        // First cone pick up
        addTimedWaypoint(0.2, 0.2, -240, y+15, -93);
        addConcurrentAutoModule(Grab);
        addTimedWaypoint(0.3, 0.3, -237, y+15, -93);
//        // First cone move to place
//        addConcurrentAutoModuleWithCancel(Backward(4));
//        addSegment(0.9, mecanumDefaultWayPoint, -200, y+12, -85);
//        addSegment(0.9, mecanumDefaultWayPoint, -180, y+17, -52);
//        // First cone place
//        addTimedSetpoint(1.0, 0.8, 0.4, -160, y+22, -52);
//        addConcurrentAutoModuleWithCancel(Forward(1));
//        // Start 2 cycle
//        customNumber(caseDetected.equals(Case.SECOND) ? 3 : 2, i -> {
//            switch (i){
//                case 0: x = 0.0; s = 1.0;  break;
//                case 1: x = 0.0; s = 1.5;  break;
//                case 2: x = 0.0; s = 2.0;  break;
//                case 3: x = 0.0; s = 2.5;  break;
//            }
//            // Move to pick
//            addSegment(0.8, mecanumDefaultWayPoint,  -186, y+16+s, -65);
//            addSegment(0.8, mecanumDefaultWayPoint, -223, y+10+s, -93);
//            // Pick
//            addTimedWaypoint(0.2, 0.2, -235, y+10+s, -93);
//            addConcurrentAutoModule(Grab);
//            addTimedWaypoint(0.3, 0.3, -231, y+10+s, -93);
//            // Move to place
//            addConcurrentAutoModuleWithCancel(Backward(5));
//            addSegment(0.9, mecanumDefaultWayPoint, -200, y+10+s, -85);
//            addSegment(0.9, mecanumDefaultWayPoint, -180, y+15+s, -52);
//            // Place
//            addTimedSetpoint(1.0, 0.8, 0.4, -160, y+21+s, -52);
//            addConcurrentAutoModuleWithCancel(Forward(i+1 == (caseDetected.equals(Case.SECOND) ? 3 : 2) ? 5 : i+2));
//        });
////        // Park other side
//        customCase(() -> {
//            addTimedWaypoint(1.0, 0.2, -170.0, y, -93);
//            addWaypoint(0.7, -215.0, y, -45);
//            addTimedSetpoint(1.0, 0.5,1.0, -227.0, 120, 0);
//        }, () -> {
//            addTimedSetpoint(1.0, 0.7, 1.0, -170.0, y, -93);
//        }, () -> {
//            addTimedWaypoint(1.0, 0.2, -170.0, y, -93);
//            addWaypoint(0.7, -120.0, y, -45);
//            addTimedSetpoint(1.0, 0.7, 1.0, -115.0, 120, 0);
//        });
//        addPause(0.1);
//        // End
    }


    @Override
    public void postProcess() {
        autoPlane.reflectY(); autoPlane.reflectX();
    }
}
