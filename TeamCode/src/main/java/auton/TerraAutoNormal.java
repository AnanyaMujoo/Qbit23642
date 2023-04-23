package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import automodules.AutoModule;
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
import geometry.position.Pose;
import robotparts.RobotPart;
import util.ExceptionCatcher;
import util.User;
import util.template.Mode;

import static global.General.bot;
import static global.General.fieldPlacement;
import static global.General.fieldSide;
import static global.General.log;
import static global.Modes.Height.HIGH;
import static global.Modes.Height.LOW;
import static global.Modes.Height.MIDDLE;

public class TerraAutoNormal extends AutoFramework {

    private double x, s;

    public static void normalInit(AutoFramework auto){
        auto.setConfig(mecanumNonstopConfig);
        bot.saveLocationOnField();
        lift.maintain();
        outtake.closeClaw();
        ExceptionCatcher.catchInterrupted(() -> Thread.sleep(500));
        outtake.readyStart();
        auto.scan(false);
    }

    @Override
    public void initialize() {
        normalInit(this);
        x = 0; s = 0;
    }

    AutoModule BackwardFirst = new AutoModule(
            lift.stageLift(1.0, heightMode.getValue(HIGH)+2.5).attach(outtake.stageReadyEndContinuous(0.6))
    ).setStartCode(outtake::moveMiddle);

    AutoModule Backward = new AutoModule(
            lift.stageLift(1.0, heightMode.getValue(HIGH)+3.5).attach(outtake.stageReadyEndContinuous(0.9))
    ).setStartCode(outtake::flip);

    AutoModule ForwardFirst = new AutoModule(
            outtake.stageEnd(0.15),
            outtake.stageOpen(0.0),
            lift.moveTime(-1.0, 0.15),
            lift.stageLift(1.0,  13).attach(outtake.stageStartAfter(0.2))
    );

    AutoModule Forward(int i){return new AutoModule(
            lift.moveTime(-0.8, 0.2),
            lift.stageLift(1.0,  Math.max(13 - (i*13/4.6), -0.5)).attach(outtake.stageBack(i == 5 ? 0.3 : 0.2))
    );}

    AutoModule GrabBack = new AutoModule(
            drive.moveTime(-0.02, 0, 0,0.1),
            RobotPart.pause(0.1),
            lift.moveTimeBackOverride(-0.1, 1.0, 0.4)
    ).setStartCode(outtake::closeClaw);


    AutoModule GrabBackLast = new AutoModule(
            drive.moveTime(-0.2, 0, 0,0.2),
            lift.moveTimeBackOverride(-0.15, 1.0, 0.3)
    );

    @Override
    public void define() {

        // Pre-loaded cone move
        addConcurrentAutoModuleWithCancel(new AutoModule(outtake.stageMiddle(0.0), lift.stageLift(1.0, heightMode.getValue(LOW))));
        customFlipped(() -> {
            addSegment(1.0, mecanumDefaultWayPoint, 2, 110, 0);
            addSegment(0.6, mecanumDefaultWayPoint, 3, 126, 0);
            addSegment(0.36, mecanumDefaultWayPoint, 3, 130, 0);
            addSegment(0.65, mecanumDefaultWayPoint, 4, 123, 0);
        }, () -> {
            addSegment(1.0, mecanumDefaultWayPoint, 0, 110, 0);
            addSegment(0.6, mecanumDefaultWayPoint, -1, 126, 0);
            addSegment(0.36, mecanumDefaultWayPoint, -1, 130, 0);
            addSegment(0.65, mecanumDefaultWayPoint, -2, 120, 0);
        });


        addConcurrentAutoModuleWithCancel(BackwardFirst);

        // Pre-loaded cone place
        customFlipped(() -> {
            addTimedSetpoint(1.0, 0.6, 0.5, 5, 125, 40);
            addTimedSetpoint(1.0, 0.6, 0.9, -9.5, 137, 45);
        }, () -> {
            addTimedSetpoint(1.0, 0.6, 0.5, 5, 123, 40);
            addTimedSetpoint(1.0, 0.6, 0.9, -10.0, 140, 45);
        });
        addConcurrentAutoModuleWithCancel(ForwardFirst, 0.2);

        // Start 5 cycle
        customNumber(5, i -> {
            customFlipped(() -> {
                x = 0.0;
                s = -1.5-0.0*i;
            }, () -> {
                x = 2.0-0.2*i;
                s = -0.3*i;
            });
            // Move to pick
            addSegment(0.7, mecanumDefaultWayPoint, 18-x, 130 + s, 80);
            // Pick
            customFlipped(() -> {
                addSegment(0.55, mecanumDefaultWayPoint, 46-x, 129 + s, 88);
                addSegment(0.4, mecanumDefaultWayPoint, 64-x, 128 + s, 88);
                addTimedSetpoint(1.0, 0.1, 0.3, 68-x, 128 + s , 88);
            }, () -> {
                addSegment(0.55, mecanumDefaultWayPoint, 47.5-x, 127 + s, 88);
                addSegment(0.4, mecanumDefaultWayPoint, 65.5-x, 127 + s, 88);
                addTimedSetpoint(1.0, 0.1, 0.3, 73.5-x, 127 + s, 88);
            });
            addCustomCode(() -> {
                outtake.checkAccess(User.AUTO);
                outtake.closeClaw();
                bot.addAutoModuleWithCancel(i+1 != 5 ? GrabBack : GrabBackLast);
                pause(0.5);
                outtake.flip();
                outtake.readyStart();
            });
            // Move to place
            addConcurrentAutoModuleWithCancel(Backward);
            // Place
            customFlipped(() -> {
                addSegment(0.55, mecanumDefaultWayPoint, 30-x, 124 + s, 80);
                addSegment(0.5, mecanumDefaultWayPoint, 11-x, 132 + s, 50);
                addTimedSetpoint(1.0, 0.5, 0.8, -10.5 - x, 140 + s, 51.5);
            }, () -> {
                addSegment(0.55, mecanumDefaultWayPoint, 30-x, 124 + s, 80);
                addSegment(0.5, mecanumDefaultWayPoint, 11-x, 134 + s, 55);
                addTimedSetpoint(1.0, 0.5, 0.8, -6 - x, 140.5 + s, 52);
            });
            addCustomCode(() -> {
                drive.move(0,0,0);
                outtake.moveEnd();
                pause(0.05);
                outtake.moveEnd();
                bot.cancelAutoModules();
                outtake.openClaw();
                pause(0.05);
                outtake.openClaw();
            });
            addConcurrentAutoModuleWithCancel(Forward(i+1), 0.1);
        });
        pause(0.1);
        addTimedWaypoint(0.6, 0.5, 2.4, 125, 53);
//        // Park
        customCase(() -> {
            addSegment(0.7, mecanumDefaultWayPoint, -7, 128, 90);
            addSegment(0.7, mecanumDefaultWayPoint, -10, 128, 90);
            addSegment(0.7, mecanumDefaultWayPoint, -45, 125, 55);
            addSegment(0.7, mecanumDefaultWayPoint, -54, 126, 5);
            addTimedSetpoint(1.0, 0.7, 1.2, -58, 83, 0);
        }, () -> {
            addTimedWaypoint(0.7, 0.5, 3.0, 122, 0);
            customFlipped(() -> {
                addTimedSetpoint(1.0, 0.7, 1.2, 1.0, 83, 0);
            }, () -> {
                addTimedSetpoint(1.0, 0.7, 1.2, -3.5, 83, 0);
            });
        }, () -> {
            addSegment(0.7, mecanumDefaultWayPoint, 7, 130, 90);
            addSegment(0.7, mecanumDefaultWayPoint, 39, 130, 90);
            addSegment(0.7, mecanumDefaultWayPoint, 51, 114, 32);
            addSegment(0.7, mecanumDefaultWayPoint,  56, 98, 0);
            addTimedSetpoint(1.0, 0.7, 1.2, 65, 83, 0);
        });
        addPause(0.1);
        // End
    }


    @Override
    public void postProcess() { autoPlane.reflectY(); autoPlane.reflectX(); }

//
//    /**
//     *  6.0 start
//     */
//
//    @Autonomous(name = "R. RIGHT - 4.0s + 6.0", group = "auto", preselectTeleOp = "TerraOp")
//    public static class TA_RIGHT_18 extends TA_RIGHT_1 {{minusTime = 1.0; startTime = 3.0;}}
//
//    @Autonomous(name = "Q. RIGHT - 4.2s + 6.0", group = "auto", preselectTeleOp = "TerraOp")
//    public static class TA_RIGHT_17 extends TA_RIGHT_1 {{minusTime = 0.8; startTime = 3.0;}}
//
//    @Autonomous(name = "P. RIGHT - 4.4s + 6.0", group = "auto", preselectTeleOp = "TerraOp")
//    public static class TA_RIGHT_16 extends TA_RIGHT_1 {{minusTime = 0.6; startTime = 3.0;}}
//
//    /**
//     *  5.0 start
//     */
//
//    @Autonomous(name = "O. RIGHT - 4.0s + 5.0", group = "auto", preselectTeleOp = "TerraOp")
//    public static class TA_RIGHT_15 extends TA_RIGHT_1 {{minusTime = 1.0; startTime = 2.0;}}
//
//    @Autonomous(name = "N. RIGHT - 4.2s + 5.0", group = "auto", preselectTeleOp = "TerraOp")
//    public static class TA_RIGHT_14 extends TA_RIGHT_1 {{minusTime = 0.8; startTime = 2.0;}}
//
//    @Autonomous(name = "M. RIGHT - 4.4s + 5.0", group = "auto", preselectTeleOp = "TerraOp")
//    public static class TA_RIGHT_13 extends TA_RIGHT_1 {{minusTime = 0.6; startTime = 2.0;}}
//
//    @Autonomous(name = "L. RIGHT - 4.6s + 5.0", group = "auto", preselectTeleOp = "TerraOp")
//    public static class TA_RIGHT_12 extends TA_RIGHT_1 {{minusTime = 0.4; startTime = 2.0;}}
//
//    /**
//     *  4.0 start
//     */
//
//    @Autonomous(name = "K. RIGHT - 4.0s + 4.0", group = "auto", preselectTeleOp = "TerraOp")
//    public static class TA_RIGHT_11 extends TA_RIGHT_1 {{minusTime = 1.0; startTime = 1.0;}}
//
//    @Autonomous(name = "J. RIGHT - 4.2s + 4.0", group = "auto", preselectTeleOp = "TerraOp")
//    public static class TA_RIGHT_10 extends TA_RIGHT_1 {{minusTime = 0.8; startTime = 1.0;}}
//
//    @Autonomous(name = "I. RIGHT - 4.4s + 4.0", group = "auto", preselectTeleOp = "TerraOp")
//    public static class TA_RIGHT_9 extends TA_RIGHT_1 {{minusTime = 0.6; startTime = 1.0;}}
//
//    @Autonomous(name = "H. RIGHT - 4.6s + 4.0", group = "auto", preselectTeleOp = "TerraOp")
//    public static class TA_RIGHT_8 extends TA_RIGHT_1 {{minusTime = 0.4; startTime = 1.0;}}
//
//    @Autonomous(name = "G. RIGHT - 4.8s + 4.0", group = "auto", preselectTeleOp = "TerraOp")
//    public static class TA_RIGHT_7 extends TA_RIGHT_1 {{minusTime = 0.2; startTime = 1.0; }}
//
//    /**
//     *  3.0 start
//     */
//
//    @Autonomous(name = "F. RIGHT - 4.0s + 3.0", group = "auto", preselectTeleOp = "TerraOp")
//    public static class TA_RIGHT_6 extends TA_RIGHT_1 {{minusTime = 1.0;}}
//
//    @Autonomous(name = "E. RIGHT - 4.2s + 3.0", group = "auto", preselectTeleOp = "TerraOp")
//    public static class TA_RIGHT_5 extends TA_RIGHT_1 {{minusTime = 0.8;}}
//
//    @Autonomous(name = "D. RIGHT - 4.4s + 3.0", group = "auto", preselectTeleOp = "TerraOp")
//    public static class TA_RIGHT_4 extends TA_RIGHT_1 {{minusTime = 0.6;}}
//
//    @Autonomous(name = "C. RIGHT - 4.6s + 3.0", group = "auto", preselectTeleOp = "TerraOp")
//    public static class TA_RIGHT_3 extends TA_RIGHT_1 {{minusTime = 0.4;}}

//    @Autonomous(name = "A. RIGHT - 4.8s + 3.0", group = "auto", preselectTeleOp = "TerraOp")
//    public static class TA_RIGHT_2 extends TA_RIGHT_1 {{minusTime = 0.2;}}


    /**
     * Right
     */

    // - 4.8s + 3.0
    @Autonomous(name = "A. RIGHT NORMAL", group = "auto", preselectTeleOp = "TerraOp")
    public static class RIGHT extends TerraAutoNormal {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90); }}

    @Autonomous(name = "B. LEFT NORMAL", group = "auto", preselectTeleOp = "TerraOp")
    public static class LEFT extends TerraAutoNormal {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; startPose = new Pose(20.5, Field.width/2.0 + Field.tileWidth + GameItems.Cone.height + 16,90); }}



//    @Autonomous(name = "B. RIGHT RED - 4.8s + 3.0", group = "auto", preselectTeleOp = "TerraOp")
//    public static class TA_RIGHT_2 extends TerraAutoNormal {{ fieldSide = FieldSide.RED; fieldPlacement = FieldPlacement.UPPER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90); autoMode = AutoMode.NORMAL;}}


    /**
     * Left
     */

    // 4.8s + 3.0

//    @Autonomous(name = "D. LEFT RED - 4.8s + 3.0", group = "auto", preselectTeleOp = "TerraOp")
//    public static class TA_LEFT_2 extends TerraAutoNormal {{ fieldSide = FieldSide.RED; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 + Field.tileWidth + GameItems.Cone.height + 16,90); autoMode = AutoMode.NORMAL;}}



//    @Autonomous(name = "Z. BLUE", group = "auto")
//    public static class TA_BLUE extends Auto {
//        @Override
//        public void initAuto() { fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; }
//        @Override
//        public void runAuto() { bot.saveLocationOnField(); }
//    }
//
//    @Autonomous(name = "Z. RED", group = "auto")
//    public static class TA_RED extends Auto {
//        @Override
//        public void initAuto() { fieldSide = FieldSide.RED; fieldPlacement = FieldPlacement.LOWER; }
//        @Override
//        public void runAuto() { bot.saveLocationOnField(); }
//    }

    // TOD 4 Convert to enter program that saves constants

























//
//
//
//
//
//
//
//
//
//    private double x, s;
//    protected double minusTime = 0.0;
//    protected double startTime = 0.0;
//
//    public static void normalInit(AutoFramework auto){
//        auto.setConfig(mecanumNonstopConfig);
//        bot.saveLocationOnField();
//        lift.maintain();
//        outtake.closeClaw();
//        ExceptionCatcher.catchInterrupted(() -> Thread.sleep(500));
//        outtake.readyStart();
//        auto.scan(false);
//    }
//
//    @Override
//    public void initialize() {
//        normalInit(this);
//        x = 0; s = 0;
//    }
//
//    AutoModule BackwardFirst = new AutoModule(
//            outtake.stageMiddle(0.0),
//            lift.stageLift(1.0, heightMode.getValue(HIGH)+3).attach(outtake.stageReadyEndAfter(0.3))
//    );
//
//    AutoModule Backward = new AutoModule(
//            RobotPart.pause(0.05),
//            outtake.stageFlip(0.0),
//            lift.stageLift(1.0, heightMode.getValue(HIGH)+4).attach(outtake.stageReadyEndAfter(0.25))
//    );
//
//    AutoModule Forward(int i){return new AutoModule(
//            outtake.stageEnd(0.15),
//            outtake.stageOpen(0.0),
//            lift.moveTime(-0.7, 0.15),
//            lift.stageLift(1.0,  i == 0 ? 15.0 : Math.max(15.0 - (i*15.0/4.5), 0)).attach(outtake.stageStartAfter(0.2))
//    );}
//
//
//
//    AutoModule GrabBack = new AutoModule(
//            outtake.stageClose(0.2),
//            lift.moveTimeBack(-0.7, 0.5, () -> 0.3).attach(outtake.stageReadyStart(0.0))
//    );
//
//    @Override
//    public void define() {
//
//        // Pre-loaded cone move
//        customFlipped(() -> {
//            addWaypoint(1.0, 2, 126, 0);
//            addWaypoint(0.7, 9, 110, 0);
//        }, () -> {
//            addWaypoint(1.0, 0, 126, 0);
//            addWaypoint(0.7, -2, 110, 0);
//        });
//        // Pre-loaded cone place
//        addConcurrentAutoModuleWithCancel(BackwardFirst);
//        customFlipped(() -> {
//            addTimedSetpoint(1.0, 0.6, 0.5, 5, 125, 40);
//            addTimedSetpoint(1.0, 0.6, 0.9+startTime, -9.5, 140, 45);
//        }, () -> {
//            addTimedSetpoint(1.0, 0.6, 0.5, 5, 125, 40);
//            addTimedSetpoint(1.0, 0.6, 0.9, -10.0, 137.5, 47);
//        });
//        addConcurrentAutoModuleWithCancel(Forward(0), 0.2);
//
//        // Start 5 cycle
//        customNumber(5, i -> {
//            customFlipped(() -> {
//                switch (i){
//                    case 0: x = -0.5; s = -1.2;  break;
//                    case 1: x = 0.6;  s = -0.5;  break;
//                    case 2: x = 0.6;  s = 0.0;  break;
//                    case 3: x = 1.0;  s = 0.5;  break;
//                    case 4: x = 1.0;  s = 1.0;   break;
//                }
//            }, () -> {
//                switch (i){
//                    case 0: x = 2.0; s = -0.2;  break;
//                    case 1: x = 3.0;  s = -0.4;  break;
//                    case 2: x = 3.0;  s = -0.8;  break;
//                    case 3: x = 4.0;  s = -1.2;  break;
//                    case 4: x = 4.0;  s = -1.6;   break;
//                }
//            });
//            // Move to pick
//            addSegment(0.7, mecanumDefaultWayPoint, 18-x, 128 + s, 80);
//            // Pick
//            customFlipped(() -> {
//                addSegment(0.6, mecanumDefaultWayPoint, 60-x, 125 + s, 87);
//            }, () -> {
//                addSegment(0.6, mecanumDefaultWayPoint, 61-x, 125 + s, 87);
//            });
//            addTimedWaypoint( 0.1, 0.25, 68-x, 126 + s, 87);
//            addCustomCode(() -> {
//                bot.cancelAutoModules(); bot.addAutoModule(GrabBack);
//                pause(0.5);
//            });
////            addTimedWaypoint( 0.1, 0.35, 63-x, 126 + s, 89);
//            // Move to place
//            addConcurrentAutoModuleWithCancel(Backward);
//            addSegment(0.65, mecanumDefaultWayPoint, 30-x, 124 + s, 80);
//            // Place
//            customFlipped(() -> {
//                addSegment(0.65, mecanumDefaultWayPoint, 11-x, 132 + s, 50);
//                addTimedSetpoint(1.0, 0.6, 1.5 - minusTime, -9 - x, 143 + s, 51.5);
//            }, () -> {
//                addSegment(0.65, mecanumDefaultWayPoint, 11-x, 132 + s, 60);
//                addTimedSetpoint(1.0, 0.6, 1.5, -9 - x, 138 + s, 50);
//            });
//            addConcurrentAutoModuleWithCancel(Forward(i+1));
//            addPause(0.2);
//        });
//        addTimedWaypoint(0.8, 0.5, 2.4, 125, 53);
////        // Park
//        customCase(() -> {
//            addSegment(0.7, mecanumDefaultWayPoint, -7, 128, 90);
//            addSegment(0.7, mecanumDefaultWayPoint, -10, 128, 90);
//            addSegment(0.7, mecanumDefaultWayPoint, -45, 125, 60);
//            addSegment(0.7, mecanumDefaultWayPoint, -50, 126, 25);
//            addTimedSetpoint(1.0, 0.8, 1.2, -58, 78, 0);
//        }, () -> {
//            addTimedWaypoint(0.7, 0.5, 3.0, 122, 0);
//            addTimedSetpoint(1.0, 0.8, 1.2, 3.0, 75, 0);
//        }, () -> {
//            addSegment(0.7, mecanumDefaultWayPoint, 7, 130, 90);
//            addSegment(0.7, mecanumDefaultWayPoint, 39, 130, 58);
//            addSegment(0.7, mecanumDefaultWayPoint, 51, 114, 32);
//            addSegment(0.7, mecanumDefaultWayPoint,  56, 98, 0);
//            addTimedSetpoint(1.0, 0.8, 1.2, 65, 78, 0);
//        });
//        addPause(0.1);
//        // End
//    }

//
//    @Override
//    public void postProcess() { autoPlane.reflectY(); autoPlane.reflectX(); }
}
