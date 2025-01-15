package robotparts.hardware;

import java.sql.Time;

import automodules.AutoModule;
import automodules.StageBuilder;
import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CServo;
import robotparts.electronics.positional.PServo;
import util.Timer;
import util.codeseg.ReturnCodeSeg;

public class Intake extends RobotPart {
    public CServo rturn;
    public CServo lturn;
    public PServo rd;
    public PServo ld;
    public PServo flipl;
    public PServo flipr;
    public boolean stopSpin = false;
    public Timer timer = new Timer();
    public boolean shimmy = false;
    public boolean intakeMode = false;

    @Override
    public void init() {
        rturn = create("rturn", ElectronicType.CSERVO_REVERSE);
        lturn = create("lturn", ElectronicType.CSERVO_FORWARD);
        rd = create("rd", ElectronicType.PSERVO_REVERSE);
        ld = create("ld", ElectronicType.PSERVO_FORWARD);
        flipl = create("flipl", ElectronicType.PSERVO_FORWARD);
        flipr = create("flipr", ElectronicType.PSERVO_REVERSE);

//        //not configured
//        intake3.setPosition("open", 0.2);
//        intake4.setPosition("open", 0.2);
//        intake3.setPosition("close", 0.2);
//        intake4.setPosition("close", 0.2);
        flipl.setPosition("out", 0);
        flipr.setPosition("out", 0);

        flipl.setPosition("half", 0.3);
        flipr.setPosition("half", 0.3);
        flipl.setPosition("in", 0.68);
        flipr.setPosition("in", 0.68);
        flipl.setPosition("almost", 0.5);
        flipr.setPosition("almost", 0.5);

        flipl.setPosition("almostOut", 0.1);
        flipr.setPosition("almostOut", 0.1);


        ld.setPosition("open", 0);
        rd.setPosition("open", 0);
        ld.setPosition("close", 0.7);
        rd.setPosition("close", 0.8);

        stopSpin = false;
        shimmy = false;
        timer.reset();
        intakeMode = false;
    }



    @Override
    public void move(double intakePower) {
        rturn.setPower(intakePower);
        lturn.setPower(intakePower);
    }
    private void moveDoor(String positionName){ ld.moveToPosition(positionName); rd.moveToPosition(positionName); }
    private void moveFlip(String positionName){ flipr.moveToPosition(positionName); flipl.moveToPosition(positionName); }

    public void openDoor(){moveDoor("open");}
    public void closeDoor(){moveDoor("close");}

    public void flipOut(){moveFlip("out");}

    public void flipIn(){moveFlip("in");}
    public void flipHalf(){moveFlip("half");}
    public void flipAlmost(){moveFlip("almost");}
    public void flipAlmostOut(){moveFlip("almostOut");}


    public void disable(){
        flipl.disable();
        flipr.disable();
    }


    public Stage stageOpen(double t){ return super.customTime(this::openDoor, t); }
    public Stage stageClose(double t){ return super.customTime(this::closeDoor, t); }
    public Stage stageFlipOut(double t){return super.customTime(this::flipOut, t);}
    public Stage stageFlipIn(double t){return super.customTime(this::flipIn, t);}
    public Stage stageFlipHalf(double t){return super.customTime(this::flipHalf, t);}
    public Stage stageFlipAlmost(double t){return super.customTime(this::flipAlmost, t);}
    public Stage stageFlipAlmostOut(double t){ return super.customTime(this::flipAlmostOut, t);}
    public Stage stageDisable(double t){ return super.customTime(this::disable, t); }
    public Stage moveTime(double p, double t) { return super.moveTime(p, t); }
    public Stage moveUntilStop(double p) { return customExit(p, () -> stopSpin).combine(new Initial(() -> stopSpin = false)); }


    public Stage shimmyUntilStopDir(double pow, double freq){
        return new Stage(
                usePart(),
                drive.usePart(),
                new Initial(() -> stopSpin = false),
                new Initial(timer::reset),
                new Main(() -> {
                    move(1);
                    drive.move(0.0, 0.0, pow*Math.sin(timer.seconds()*2*Math.PI*freq));
                }),
                new Exit(() -> stopSpin),
                stop(),
                drive.stop(),
                returnPart(),
                drive.returnPart()
        );
    }

    public Stage stagepPickUp(){
        return super.customTime(new StageBuilderTime(this)
                .addSubStage(0.05, () -> move(1))
                .addSubStage(0.05, this::flipAlmost)
                .addSubStage(0.05, this::closeDoor)
                .addSubStage(0.05, () -> move(0))
        );
    }



}
