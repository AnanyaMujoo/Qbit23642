package robotparts.hardware;

import automodules.AutoModule;
import automodules.stage.Exit;
import automodules.stage.Main;
import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CServo;
import robotparts.electronics.positional.PServo;
import util.codeseg.ReturnCodeSeg;

public class Intake extends RobotPart {
    public CServo rturn;
    public CServo lturn;
    public PServo rd;
    public PServo ld;
    public PServo flipl;
    public PServo flipr;
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


        ld.setPosition("open", 0);
        rd.setPosition("open", 0);
        ld.setPosition("close", 0.7);
        rd.setPosition("close", 0.8);


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


    public Stage stageOpen(double t){ return super.customTime(this::openDoor, t); }
    public Stage stageClose(double t){ return super.customTime(this::closeDoor, t); }
    public Stage stageFlipOut(double t){return super.customTime(this::flipOut, t);}
    public Stage stageFlipIn(double t){return super.customTime(this::flipIn, t);}
    public Stage stageFlipHalf(double t){return super.customTime(this::flipHalf, t);}
    public Stage stageFlipAlmost(double t){return super.customTime(this::flipAlmost, t);}


    public Stage moveTime(double p, double t) { return super.moveTime(p, t); }

}
