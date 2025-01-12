package automodules;

import global.Modes;
import robot.RobotUser;

import static automodules.StageBuilder.pause;
import static global.Modes.driveMode;


public interface AutoModuleUser extends RobotUser {

    AutoModule PrepareRam = new AutoModule(
            claw.stageReady(0.5),
            claw.stageDisable(0.1),
            driveMode.ChangeMode(Modes.Drive.SLOW)
    );

    AutoModule Ram = new AutoModule(
            claw.stageSqueeze(0.3),
            liftOuttake.stageLift(0.2, 10),
            drive.moveTime(0.3,0,0,0.3),
            claw.stageHold(0.3),
            driveMode.ChangeMode(Modes.Drive.FAST),
            liftOuttake.stageLift(0.2, 42)
    );
    AutoModule Specimen = new AutoModule(
            liftOuttake.stageDown(-0.2, 32),
            claw.stageReady(0.3),
            drive.moveTime(0.3,0,0,0.4),
            claw.stageRelease(0.3),
            liftOuttake.stageDown(-0.15, 0)
    );

    AutoModule Intake= new AutoModule(
      intake.stageFlipHalf(0.1),
            liftIntake.stageLift(0.5, 25),
            intake.stageOpen(0.5),
            driveMode.ChangeMode(Modes.Drive.SLOW)

    );

    AutoModule IntakeIn = new AutoModule(
            intake.stageClose(0.1),
            intake.stageFlipAlmost(0.1),
            liftIntake.stageDown(0.5, 0),
            intake.stageFlipIn(0.1),
            intake.stageOpen(0.1),
            intake.moveTime(0.2,0.5)
            //intake.stageClose(0.5)
            //remember to flip after

    );

    AutoModule MoveIntake = new AutoModule(
            intake.stageFlipOut(0.1),
            intake.moveTime(1,5)
//            drive.moveTime(-0.1,0,-0,0.5),
//
//            drive.moveTime(0,0,-0.2,0.3),
//            drive.moveTime(0,0,0.4,0.3),
//            drive.moveTime(0,0,-0.2,0.3)

            //intake.stageClose(0.5)
            //remember to flip after

    );
    AutoModule PrepareBucket= new AutoModule(
      intake.stageFlipOut(0.1),
      claw.stageHold(0.1),
      liftOuttake.stageLift(0.2,93.8),
      bucket.stageHold(0.1),
            claw.stageSqueeze(0.1)

    );
    AutoModule Deposit = new AutoModule(
            bucket.stageTop(0.5),
            bucket.stageBottom(0.5)

            );
    AutoModule LiftDown = new AutoModule(
            claw.stageHold(0.1),
      liftOuttake.stageDown(0.2, 0),
            bucket.stageBottom(0.1),
            intake.stageFlipIn(0.1)

    );
    AutoModule testIntakeLift = new AutoModule(

            liftIntake.stageLift(0.1,5),
            liftIntake.stageDown(0.1,0)
    );
    AutoModule RamBucket = new AutoModule(
            claw.stageSqueeze(0.3),
            drive.moveTime(0.3,0,0,0.5),
            driveMode.ChangeMode(Modes.Drive.FAST),
            liftOuttake.stageLift(0.2, 42)
    );

//    default AutoModule AutoYellow(){return new AutoModule(
//            lift.stageLift(0.2,5),
//            outtake.stageFlipStart(0.5),
//            outtake.stageFlipEnd(0.5)
//
//
//
//
//    );}
//    default AutoModule closeClaw(){return new AutoModule(
//        outtake.stageClawClose(0.5)
//    );}
//    default AutoModule openClaw(){return new AutoModule(
//            outtake.stageClawOpen(0.5)
//    );}
//    default AutoModule WhyWontLiftWork(){return new AutoModule(
//        lift.stageLift(0.3,0),
//            outtake.stageFlipStart(0.5)
//    );}

        default AutoModule DropPurpleR (double height2) {return new AutoModule(
                drive.moveTime(-0.3,-0.2,0,0.7),
                drive.moveTime(-0.3,0,0,0.5),
                pause(1),
                drive.moveTime(0.23,0,0,0.45),
                drive.moveTime(0.3,0.2,0,0.6)



//                lift.stageLift(0.2, height2),
//                outtake.stageClawClose(0),
//                outtake.stageFlipEnd(0),
//                pause(1),
//                outtake.stageLeftOpen(1)
        );    }

    default AutoModule DropPurpleL (double height2) {
        return new AutoModule(
                drive.moveTime(-0.6, 0.0, -0.28, 0.76),
                pause(1),
                drive.moveTime(0.6, 0.0, 0.28, 0.70)
        );
    }


    default AutoModule DropPurpleC (double height3) {
        return new AutoModule(
                drive.moveTime(-0.6, 0.1, 0, 0.86),
                pause(1),
                drive.moveTime(0.53, -0.1, 0, 0.8)




//            drive.moveTime(0,0,-0.3,0.7),
//            lift.stageLift(0.2, height2),
//            outtake.stageClawClose(0),
//            outtake.stageFlipEnd(0),
//            pause(1),
//            outtake.stageLeftOpen(1)
    );    }


//    AutoModule Intake = new AutoModule(
////            intake.stageMoveUntilPixelsAreLoaded(0.3),
//            pause(5),
//            outtake.stageClawClose(0.0),
//            intake.moveTime(-0.3,1.0)
//            //outtake.stageFlipMiddle(0.5)
//    );






























//    /**
//     * Forward
//     */


//    AutoModule ForwardTeleHigh = new AutoModule(
//            RobotPart.pause(0.05),
//            drive.moveTime(1.0, 0.0, 0.0, 0.1),
//            outtake.stageReadyStartAfter(0.1),
////            outtake.stageStart(0.0),
//            lift.stageLift(1.0,0).attach(outtake.stageStartContinuousWithFlip(0.9, 0.0))
//    ).setStartCode(() -> {
//            lift.cap = false;
//            outtakeStatus.set(DRIVING);
//            outtake.openClaw();
//            outtake.moveFlipEnd();
//    });
//
//    // 0.6
//    AutoModule ForwardTeleMiddle = new AutoModule(
//            RobotPart.pause(0.05),
//            drive.moveTime(1.0, 0.0, 0.0, 0.1),
//            outtake.stageReadyStartAfter(0.1),
////            outtake.stageStart(0.0),
//            lift.stageLift(1.0,0).attach(outtake.stageStartContinuousWithFlip(0.9, 0.0))
//    ).setStartCode(() -> {
//            lift.cap = false;
//            outtakeStatus.set(DRIVING);
//            outtake.openClaw();
//            outtake.moveFlipEnd();
//    });
//
//    AutoModule ForwardTeleLow = new AutoModule(
//            RobotPart.pause(0.05),
//            drive.moveTime(1.0, 0.0, 0.0, 0.15).combine(new Stop(() -> drive.using = false)),
//            outtake.stageReadyStartAfter(0.1),
////            outtake.stageStart(0.0),
//            lift.stageLift(1.0,0).attach(outtake.stageStartContinuousWithFlip(0.9, 0.0))
//    ).setStartCode(() -> {
//            lift.cap = false;
//            drive.using = true;
//            outtakeStatus.set(DRIVING);
//            outtake.moveFlipEnd();
//            outtake.openClaw();
//    });
//
//
//    AutoModule ForwardTeleGround = new AutoModule(
//            RobotPart.pause(0.6),
//            outtake.stageStart(0.0),
//            lift.moveTime(-0.4, 0.4)
//    ).setStartCode(() -> {
//            lift.cap = false;
//            outtakeStatus.set(DRIVING);
//            driveMode.set(MEDIUM);
//            outtake.openClaw();
//    });
//
//    /**
//     * Backward
//     */
//
//
//    AutoModule BackwardGrabHighTele = new AutoModule(
//            outtake.stageClose(0.2),
//            lift.checkAndLift(),
////            outtake.stageFlip(0.0),
////            outtake.stageReadyStart(0.0),
//            outtake.stageFlip(0.0),
//            outtake.stageReadyStartCond(0.0),
//            lift.stageLift(1.0, heightMode.getValue(HIGH)+2).attach(
//                    outtake.stageReadyEndAfter(0.48)
////                    outtake.stageFlipThing()
////                    outtake.stageReadyEndContinuousWithFlip(0.7, 0.1)
////                    outtake.stageReadyEnd(0.0)
//            )
//    ).setStartCode(() -> {
//            lift.setGround(false);
//            outtakeStatus.set(PLACING);
//            heightMode.set(HIGH);
//    });
//
//    AutoModule BackwardGrabMiddleTele = new AutoModule(
//            outtake.stageClose(0.22),
//            lift.checkAndLift(),
//            outtake.stageFlip(0.0),
//            outtake.stageReadyStartCond(0.0),
//            lift.stageLift(1.0, heightMode.getValue(MIDDLE)+2).attach(
//                    outtake.stageReadyEndAfter(0.48)
////                    outtake.stageReadyEndContinuousWithFlip(0.7, 0.1)
////                    outtake.stageReadyEndContinuousWithFlip(0.8, 0.08)
////                    outtake.stageReadyEnd(0.0)
//            )
//    ).setStartCode(() -> {
//            lift.setGround(false);
//            outtakeStatus.set(PLACING);
//            heightMode.set(MIDDLE);
//    });
//
//    AutoModule BackwardGrabLowTele = new AutoModule(
//            outtake.stageClose(0.22),
//            lift.checkAndLift(),
//            outtake.stageFlip(0.0),
//            outtake.stageReadyStartCond(0.0),
//            lift.stageLift(1.0, heightMode.getValue(LOW)+2).attach(
//                    outtake.stageReadyEndAfter(0.48)
////                    outtake.stageReadyEndContinuousWithFlip(0.7, 0.1)
////                    outtake.stageReadyEndContinuousWithFlip(0.7, 0.06)
////                    outtake.stageReadyEnd(0.0)
//            )
//    ).setStartCode(() -> {
//            lift.setGround(false);
//            outtakeStatus.set(PLACING);
//            heightMode.set(LOW);
//    });
//
//    AutoModule BackwardGrabGroundTele = new AutoModule(
//            outtake.stageClose(0.22),
//            outtake.stage(0.1, 0.0),
//            lift.checkAndLift(),
//            lift.stageLift(1.0, 13)
//    ).setStartCode(() -> {
//            outtakeStatus.set(PLACING);
//            heightMode.set(GROUND);
//            lift.setGround(true);
//    });
//
//    AutoModule BackwardGrabGroundTele2 = new AutoModule(
//            outtake.stageClose(0.0),
//            outtake.stageUnFlip(0.4),
//            outtake.stageStartContinuous(0.7),
//            outtake.stage(0.1, 0.0),
//            lift.checkAndLift(),
//            lift.stageLift(1.0, 13)
//    ).setStartCode(() -> {
//        outtakeStatus.set(PLACING);
//        heightMode.set(GROUND);
//        lift.setGround(true);
//    });
//
//    AutoModule BackwardPlaceGroundTele = new AutoModule(
//            lift.moveTime(-0.5, 0.7)
//    ).setStartCode(() -> {
//            lift.setGround(false);
//            outtakeStatus.set(PLACING);
//            heightMode.set(GROUND);
//            outtake.moveFlipStart();
//    });
//
//    AutoModule ForwardTeleBottom = new AutoModule(
//            lift.stageLift(1.0,0),
//            outtake.stageStart(0.1)
//    ).setStartCode(() -> {
//            outtakeStatus.set(DRIVING);
//            outtake.moveFlipStart();
//            outtake.openClaw();
//            lift.ground = false;
//    });
//
//    AutoModule CapGrab = new AutoModule(outtake.stageClose(0.25));
//
////    AutoModule CapGrab = new AutoModule(
//////            outtake.stageClose(0.25)
//////    );
//////            ,
//////            lift.stageLift(1.0, heightMode.getValue(LOW)+2).attach(
//////                    outtake.stageReadyEndContinuousWithFlip(1.5, 0.15)
//////            )
////    ).setStartCode(() -> {
////        lift.setGround(false);
//////        outtakeStatus.set(PLACING);
//////        heightMode.set(LOW);
////    });
//
////    AutoModule CapPlace = new AutoModule(
////            outtake.stageOpenCap(0.8),
////            outtake.stageClose(0.2),
////            outtake.stageReadyEnd(0.2)
//////            ,
//////            outtake.stageClose(0.2)
////    ).setStartCode(() -> {
////            outtake.openClawCap();
////            outtake.moveEnd();
////    });
//
//    AutoModule ResetLift = new AutoModule(lift.moveTime(-0.3, 0.5),  lift.resetLift()).setStartCode(() -> {
//        lift.ground = false;
//        outtakeStatus.set(DRIVING);
//    });
//    AutoModule UprightCone = new AutoModule(outtake.stage(0.1, 0.0), lift.stageLift(1.0, 5), driveMode.ChangeMode(SLOW));
//    AutoModule FixCone = new AutoModule(lift.moveTimeBack(0.1, -0.4, () -> 0.15), outtake.stageStart(0.0), lift.moveTimeBack(-0.35, -0.3, () -> 0.3), driveMode.ChangeMode(MEDIUM)).setStartCode(()->{lift.currentCutoffPosition = lift.defaultCutoffPosition;});
//    AutoModule TakeOffCone = new AutoModule(heightMode.ChangeMode(HIGH), outtake.stageClose(0.0), lift.stageLift(1.0, heightMode.getValue(HIGH)+3.5).attach(outtake.stageReadyStartAfter(0.5)),RobotPart.pause(0.1),outtake.stageFlip(0.0));
//
//    static AutoModule ForwardStackTele(int i){return new AutoModule(
//        lift.stageLift(1.0,  Math.max(13 - ((i+1)*13/4.6), -0.5))
//    ).setStartCode(() -> {
//        outtake.openClaw();
//        outtake.moveFlipStart();
//    });}
//
//
//
//    /**
//     * Cycle
//     */
//
//    AutoModule BackwardCycle = new AutoModule(
//            outtake.stageClose(0.2),
//            lift.stageLift(1.0, heightMode.getValue(HIGH)+1.5).attach(outtake.stageEndContinuousWithFlip(1.0, 0.1))
//    );
//
//    AutoModule ForwardCycle = new AutoModule(
//            lift.stageLift(1.0,0).attach(outtake.stageBack(0.3))
//    ).setStartCode(outtake::dropConeRaw);
//
//    static Independent Cycle(int i) {return new Independent() {
//        @Override
//        public void define() {
//            double x = 0.0;
//            if(i==0){ addSegment(0.3, 0.1, mecanumNonstopSetPoint, x, 5.0, 0.0);}
//            addConcurrentAutoModuleWithCancel(BackwardCycle, 0.25);
//            addWaypoint(0.4, x, -38, 0);
//            addSegment(0.3, 0.1, mecanumNonstopSetPoint, x+0.5, -44.0, 0.0);
//            addConcurrentAutoModuleWithCancel(ForwardCycle, 0.1);
//            addWaypoint(0.4, x+0.5, -8, 0);
//            addSegment(0.3, 0.1, mecanumNonstopSetPoint, x, 2.0, 0.0);
//        }
//
//        @Override
//        public void flip() {
//
//        }
//    };}
//
//
//    Machine MachineCycle = new Machine()
//    .addInstruction(odometry::reset,0.1)
//    .addIndependent(8, AutoModuleUser::Cycle)
//    .addIndependent(new Independent() {
//        @Override
//        public void define() {
//            addSegment(0.4, 0.5, mecanumNonstopSetPoint, 0, 0.0, 0);
//        }
//    });
//
//
//    AutoModule BackwardCycleMiddle = new AutoModule(
//            outtake.stageClose(0.2),
//            lift.stageLift(1.0, heightMode.getValue(MIDDLE)+3).attach(outtake.stageReadyEndContinuousWithFlip(0.7, 0.15))
//    );
//
//    AutoModule BackwardCycleLow = new AutoModule(
//            outtake.stageClose(0.2),
//            lift.stageLift(1.0, heightMode.getValue(LOW)+1.5).attach(outtake.stageReadyEndContinuousWithFlip(1.1, 0.2))
//    );
//
//
//    AutoModule ForwardCycleMiddle = new AutoModule(
//            RobotPart.pause(0.1),
//            lift.stageLift(1.0,0).attach(outtake.stageBack(0.2))
//    ).setStartCode(outtake::dropConeRaw);
//
//
//    AutoModule ForwardCycleLow = new AutoModule(
//            RobotPart.pause(0.4),
//            lift.stageLift(1.0,0).attach(outtake.stageBack(0.2))
//    ).setStartCode(outtake::dropConeRaw);
//
//
//    AutoModule BackwardCycleLow2 = new AutoModule(
//            outtake.stageClose(0.2),
//            lift.stageLift(1.0, heightMode.getValue(LOW)+1.5).attach(outtake.stageReadyEndContinuousWithFlip(0.8, 0.2))
//    );
//
//    Machine MachineCycleExtra = new Machine()
//            .addInstruction(odometry::reset, 0.1)
//            .addIndependentWithPause(new Independent() {
//                @Override
//                public void define() {
//                    addSegment(0.2, 0.2, mecanumNonstopSetPoint, 0.0, 6.0, 0.0);
//                    addConcurrentAutoModuleWithCancel(BackwardCycleMiddle, 0.15);
//                    addWaypoint(0.8,-2,-15,-5.0);
//                    addSegment(1.0, 0.4, mecanumNonstopSetPoint, -31, -36.0, -50.0);
//                    addSegment(0.4, 0.4, mecanumNonstopSetPoint, -37, -42.0, -50.0);
//                }
//            })
//            .addIndependentWithPause(new Independent() {
//                @Override
//                public void define() {
//                    addConcurrentAutoModuleWithCancel(ForwardCycleMiddle, 0.2);
//                    addWaypoint(0.6, -32.0, 0.0, -21.0);
//                    addSegment(0.5, 0.34, mecanumNonstopSetPoint, -27.0, 26.0, -25.0);
//                    addConcurrentAutoModuleWithCancel(BackwardCycleLow, 0.15);
//                    addWaypoint(1.0, -25.0, 15.0, 0.0);
//                    addWaypoint(1.0, -30.0, -5.0, -45);
//                    addSegment(1.1, 0.4, mecanumNonstopSetPoint, -100.0, -45.0, -50);
//                }
//            })
//            .addIndependentWithPause(new Independent() {
//                @Override
//                public void define() {
//                    addConcurrentAutoModuleWithCancel(ForwardCycleLow, 0.15);
//                    addWaypoint(1.0, -70.0, -30.0, -75);
//                    addWaypoint(1.0, -50.0, -20.0, -70);
//                    addWaypoint(0.4, -40.0, -10.0, 0.0);
//                    addWaypoint(0.4, -27.0, 6.0, 0.0);
//                    addSegment(0.6, 0.34, mecanumNonstopSetPoint, -27.0, 26.0, -25.0);
//                    addConcurrentAutoModuleWithCancel(BackwardCycleLow2, 0.15);
//                    addSegment(0.7, 0.3, mecanumNonstopSetPoint, -25.0, 26.0, -57.0);
//                    addSegment(0.4, 0.4, mecanumNonstopSetPoint, -35.0, 9.0, -57.0);
//                }
//            })
//            .addIndependent(new Independent() {
//                @Override
//                public void define() {
//                    addConcurrentAutoModuleWithCancel(ForwardCycleLow,0.15);
//                    addSegment(0.4, 0.4, mecanumNonstopSetPoint, -25.0, 26.0, -57.0);
//                    addSegment(0.8, 0.35, mecanumNonstopSetPoint, -27.0, 10.0, 0.0);
//                }
//            });
//        ;

}
