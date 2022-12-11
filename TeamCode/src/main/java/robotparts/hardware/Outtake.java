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

public class Outtake extends RobotPart {

    private PServo armr, arml, turn, claw;

    @Override
    public void init() {
        armr = create("armr", ElectronicType.PSERVO_REVERSE);
        arml = create("arml", ElectronicType.PSERVO_FORWARD);

        arml.changePosition("start", 0.02);
        armr.changePosition("start", 0.02);

        arml.addPosition("startHalf", 0.25);
        armr.addPosition("startHalf", 0.25);

        armr.addPosition("middle", 0.4);
        arml.addPosition("middle", 0.4);

        arml.addPosition("endHalf", 0.56);
        armr.addPosition("endHalf", 0.56);

        arml.changePosition("end", 0.73);
        armr.changePosition("end", 0.73);

        turn = create("turn", ElectronicType.PSERVO_REVERSE);
        claw = create("claw", ElectronicType.PSERVO_REVERSE);

        turn.changePosition("start", 0.05);
        turn.addPosition("flipped", 0.77);

        claw.addPosition("open", 0.0);
        claw.addPosition("close", 0.35);

        openClaw();
        unFlip();
    }

    public void moveStart(){ armr.setPosition("start"); arml.setPosition("start"); unFlip(); }
    public void moveEnd(){ armr.setPosition("end"); arml.setPosition("end"); flip(); }
    public void openClaw(){ claw.setPosition("open"); }
    public void closeClaw(){ claw.setPosition("close"); }

    public void flip(){ turn.setPosition("flipped"); }
    public void unFlip(){ turn.setPosition("start"); }

    public void readyStart(){ armr.setPosition("startHalf"); arml.setPosition("startHalf"); }
    public void readyEnd(){ armr.setPosition("endHalf"); arml.setPosition("endHalf"); }


    public Stage stageReadyStart(double t){return super.customTime(this::readyStart, t);}

    public Stage stageStart(double t){ return super.customTime(this::moveStart, t); }
    public Stage stageEnd(double t){ return super.customTime(this::moveEnd, t); }
    public Stage stageOpen(double t){ return super.customTime(this::openClaw, t); }
    public Stage stageClose(double t){ return super.customTime(this::closeClaw, t); }
    public Stage stageFlip(double t){ return super.customTime(this::flip, t); }

    public Stage stageEndAfter(double t){ return new Stage(usePart(), new Main(()-> {}), exitTime(t), new Stop(this::moveEnd), returnPart()); }

    public Stage stageMiddle(double t){ return super.customTime(() -> {armr.setPosition("middle"); arml.setPosition("middle");}, t);}

    public Stage stageEndContinuous(double t){ return new Stage(usePart(), new Initial(() -> {arml.setContinuousTarget("end"); armr.setContinuousTarget("end");}), new Main(() -> {arml.moveContinuous(t); armr.moveContinuous(t);}), RobotPart.exitTime(t), returnPart());}




//    public void setArmTarget(String name){ armr.setContinuousTarget(name); arml.setContinuousTarget(name); }
//    public void moveArmContinuous(double time){ armr.moveContinuous(time); arml.moveContinuous(time); }


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
