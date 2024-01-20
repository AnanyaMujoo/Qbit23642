package robotparts.hardware;

import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PServo;

public class Outtake extends RobotPart {

    public PServo flipRight, flipLeft, clawRight, clawLeft;

    @Override
    public void init() {
        flipRight = create("flipr", ElectronicType.PSERVO_REVERSE);
        flipLeft = create("flipl", ElectronicType.PSERVO_FORWARD);

        flipLeft.setPosition("start", 0.2);
        flipRight.setPosition("start", 0.2);

        flipRight.setPosition("middle", 0.5);
        flipLeft.setPosition("middle", 0.5);

        flipLeft.setPosition("end", 0.8);
        flipRight.setPosition("end", 0.8);



        clawRight = create("clawr", ElectronicType.PSERVO_REVERSE);
        clawLeft = create("clawl", ElectronicType.PSERVO_FORWARD);

        // 0.4, 0.42
        clawRight.setPosition("open", 0.45);
        clawLeft.setPosition("open", 0.48);

        clawRight.setPosition("close", 0.55);
        //TODO - tweak so the claw is closer
        clawLeft.setPosition("close", 0.59);


        moveClawOpen();

    }

    private void moveFlip(String positionName){ flipRight.moveToPosition(positionName); flipLeft.moveToPosition(positionName); }
    public void moveFlipStart(){ moveFlip("start"); }
    public void moveFlipMiddle(){ moveFlip("middle"); }
    public void moveFlipEnd(){ moveFlip("end"); }

    private void moveClaw(String positionName){ clawRight.moveToPosition(positionName); clawLeft.moveToPosition(positionName); }
    public void moveClawOpen(){ moveClaw("open"); }
    public void moveClawClose(){ moveClaw("close"); }

    public void moveOuttakeToStart(){ moveClawOpen(); moveFlipStart(); }
    public void moveOuttakeToEnd(){ moveClawClose(); moveFlipEnd(); }












    public Stage stageFlipStart(double t){ return super.customTime(this::moveFlipStart, t); }
    public Stage stageFlipMiddle(double t){ return super.customTime(this::moveFlipMiddle, t); }
    public Stage stageFlipEnd(double t){ return super.customTime(this::moveFlipEnd, t); }
    public Stage stageClawOpen(double t){ return super.customTime(this::moveClawOpen, t); }
    public Stage stageClawClose(double t){ return super.customTime(this::moveClawClose, t); }

    public Stage stageFlipStartAfter(double t){ return super.customTimeAfter(this::moveFlipStart, t); }
    public Stage stageFlipEndAfter(double t){ return super.customTimeAfter(this::moveFlipEnd, t);}
    public Stage stageClawOpenAfter(double t){ return super.customTimeAfter(this::moveClawOpen, t); }
    public Stage stageClawCloseAfter(double t){ return super.customTimeAfter(this::moveClawClose, t); }


    public Stage stageFlipEndContinuous(double t){ return super.customContinuousTime(() -> flipRight, () -> flipLeft, "end", t); }
    public Stage stageFlipStartContinuous(double t){ return super.customContinuousTime(() -> flipRight, () -> flipLeft, "start", t); }


    public Stage stageFlipBack(double start){
        return super.customTime(new StageBuilderTime(this)
                .addSubStage(start, () -> {})
                .addSubStage(0.1, this::moveFlipEnd)
                .addSubStage(0.1, this::moveClawClose)
        );
    }




}
