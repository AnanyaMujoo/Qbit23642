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

        flipLeft.setPosition("start", 0.0);
        flipRight.setPosition("start", 0.0);

        flipRight.setPosition("middle", 0.75);
        flipLeft.setPosition("middle", 0.75);

        flipLeft.setPosition("end", 1.0);
        flipRight.setPosition("end", 1.0);



        clawRight = create("clawr", ElectronicType.PSERVO_REVERSE);
        clawLeft = create("clawl", ElectronicType.PSERVO_REVERSE);


        clawRight.setPosition("open", 0.0);
        clawLeft.setPosition("open", 0.0);

        clawRight.setPosition("close", 0.2);
        clawLeft.setPosition("close", 0.2);


        moveOuttakeToStart();

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
