package robotparts.hardware;

import automodules.StageBuilder;
import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import automodules.stage.Stop;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PServo;

import static global.Modes.OuttakeStatus.DRIVING;
import static global.Modes.OuttakeStatus.PLACING;
import static global.Modes.outtakeStatus;

public class Outtake extends RobotPart {

    public PServo armr, arml, turn, claw;

//    public boolean cycleMachine = false;
//    public boolean pauseMachine = false;
//    public boolean skipMachine = false;

    @Override
    public void init() {
        armr = create("armr", ElectronicType.PSERVO_REVERSE);
        arml = create("arml", ElectronicType.PSERVO_FORWARD);

        arml.changePosition("start", 0.03);
        armr.changePosition("start", 0.03);

        arml.addPosition("startHalf", 0.38);
        armr.addPosition("startHalf", 0.38);

        armr.addPosition("middle", 0.38);
        arml.addPosition("middle", 0.38);

        arml.addPosition("endHalf", 0.71);
        armr.addPosition("endHalf", 0.71);

        arml.changePosition("end", 0.84);
        armr.changePosition("end", 0.84);

        turn = create("turn", ElectronicType.PSERVO_REVERSE);
        claw = create("claw", ElectronicType.PSERVO_REVERSE);

        turn.changePosition("start", 0.05);
        turn.addPosition("flipped", 0.84);

        claw.addPosition("open", 0.15);
        claw.addPosition("close", 0.35);

        unFlip();
        outtakeStatus.set(PLACING);
    }

    public void changeArmPosition(String name, double pos){
        armr.changePosition(name, pos);
        arml.changePosition(name, pos);
    }

    public void arm(double pos){ armr.setPosition(pos); arml.setPosition(pos); }

    public void moveStart(){ armr.setPosition("start"); arml.setPosition("start"); unFlip(); }
    public void moveEnd(){ armr.setPosition("end"); arml.setPosition("end"); flip(); }
    public void openClaw(){ claw.setPosition("open"); }
    public void closeClaw(){ claw.setPosition("close"); }

    public void flip(){ turn.setPosition("flipped"); }
    public void unFlip(){ turn.setPosition("start"); }

    public void readyStart(){ armr.setPosition("startHalf"); arml.setPosition("startHalf"); }
    public void readyEnd(){ armr.setPosition("endHalf"); arml.setPosition("endHalf"); flip(); }

    public Stage stageReadyStart(double t){return super.customTime(this::readyStart, t);}
    public Stage stageStart(double t){ return super.customTime(this::moveStart, t); }
    public Stage stageEnd(double t){ return super.customTime(this::moveEnd, t); }
    public Stage stageOpen(double t){ return super.customTime(this::openClaw, t); }
    public Stage stageClose(double t){ return super.customTime(this::closeClaw, t); }
    public Stage stageFlip(double t){ return super.customTime(this::flip, t); }

    public Stage stageFlipAfter(double t){ return super.customTimeAfter(this::flip, t); }
    public Stage stageCloseAfter(double t){ return super.customTimeAfter(this::closeClaw, t);}
    public Stage stageUnFlip(double t){ return super.customTime(this::unFlip, t); }
    public Stage stageEndAfter(double t){ return super.customTimeAfter(this::moveEnd, t); }
    public Stage stageOpenAfter(double t){ return super.customTimeAfter(this::openClaw, t); }

    public Stage stageMiddle(double t){ return super.customTime(() -> {armr.setPosition("middle"); arml.setPosition("middle"); flip();}, t);}
    public Stage stageReadyEnd(double t){ return super.customTime(this::readyEnd, t); }
    public Stage stageReadyEndAfter(double t){ return super.customTimeAfter(this::readyEnd, t); }
    public Stage stageStartAfter(double t){ return super.customTimeAfter(this::moveStart, t); }
    public Stage stageReadyStartAfter(double t){ return super.customTimeAfter(this::readyStart, t); }

    public Stage stage(double pos, double t){ return super.customTime(() -> {unFlip(); arm(pos);},  t); }

    public Stage stageWithFlip(double pos, double t){ return super.customTime(() -> {flip(); arm(pos);},  t); }
    public Stage stageAfter(double pos, double t){ return super.customTimeAfter(() -> {unFlip(); arm(pos);},  t); }
    public Stage stageWithFlipAfter(double pos, double t){ return super.customTimeAfter(() -> {flip(); arm(pos);},  t); }

    public Stage stageEndContinuous(double t){ return super.customContinuousTime(() -> armr, () -> arml, "end", t); }


    public Stage stageMiddleWithoutFlip(double t){ return super.customTime(() -> {armr.setPosition("middle"); arml.setPosition("middle");}, t);}
    public Stage stageReadyEndWithoutFlip(double t){ return super.customTime(() -> {armr.setPosition("endHalf"); arml.setPosition("endHalf");}, t); }




//
//    public Stage stageEnd(){
//        return super.customTime(new StageBuilderTime(this)
//                .addSubStage(0.1, () -> {closeClaw(); readyStart();})
//                .addSubStage(0.3, () -> {flip(); })
//                        //setArmTarget("end");
//                        .addSubStage(0.5, () -> moveEnd())
////                .addSubStage(0.5, () -> moveArmContinuous(0.5))
//        );
//    }
//
//    public Stage stageStart() {
//        return super.customTime(new StageBuilderTime(this)
//                .addSubStage(0.1, () -> {openClaw();readyEnd();})
//                .addSubStage(0.3, () -> {closeClaw(); unFlip();  })
//                //setArmTarget("start");
////                .addSubStage(0.5, () -> moveArmContinuous(0.5))
//                .addSubStage(0.5, () -> moveStart())
//        );
//    }

}
