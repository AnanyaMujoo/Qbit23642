package robotparts.hardware;

import static global.General.fieldSide;

import com.qualcomm.ftccommon.SoundPlayer;

import java.sql.Time;
import java.util.ArrayList;

import automodules.AutoModule;
import automodules.StageBuilder;
import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import automodules.stage.Stop;
import elements.FieldSide;
import elements.SampleColor;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CServo;
import robotparts.electronics.positional.PServo;
import robotparts.sensors.ColorSensors;
import util.Timer;
import util.codeseg.ReturnCodeSeg;
import util.template.Iterator;

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
    public boolean sampleLoaded = false;

    public ArrayList<SampleColor> lastColors = new ArrayList<>();
    public int code = 0;
    public SampleColor color = SampleColor.NONE;
    public final int lastNum = 3;

    public boolean isSampleVertical = false;

//    public ArrayList<Double> lastCounts = new ArrayList<>();

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

        flipl.setPosition("auto", 0.45);
        flipr.setPosition("auto", 0.45);



        ld.setPosition("open", 0);
        rd.setPosition("open", 0);
        ld.setPosition("close", 0.7);
        rd.setPosition("close", 0.8);

        stopSpin = false;
        shimmy = false;
        timer.reset();
        intakeMode = false;
        sampleLoaded = false;
        lastColors = new ArrayList<>();
        code = 0;
        color = SampleColor.NONE;
        isSampleVertical = false;
//        lastCounts = new ArrayList<>();
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
    public void flipAuto(){moveFlip("auto");}



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
    public Stage stageFlipAuto(double t){ return super.customTime(this::flipAuto, t);}
    public Stage stageDisable(double t){ return super.customTime(this::disable, t); }
    public Stage moveTime(double p, double t) { return super.moveTime(p, t); }
    public Stage moveUntilStop(double p) { return customExit(p, () -> stopSpin).combine(new Initial(() -> stopSpin = false)); }

    public ArrayList<SampleColor> getLastValues(ArrayList<SampleColor> values,int n){
        ArrayList<SampleColor> out = new ArrayList<>();
        int s = values.size();
        if(values.size() > n){
            for (int i = s - n; i < s; i++) {
                out.add(values.get(i));
            }
            return out;
        }else{
            return values;
        }
    }

    public SampleColor getColor(){
        ArrayList<SampleColor> last = getLastValues(lastColors, lastNum);
        SampleColor color = last.get(0);
        for(int i = 1; i < last.size(); i++){
            if(!last.get(i).equals(color)){
                return SampleColor.NONE;
            }
        }
        return color;
    }

    public Stage moveUntilColor(){
        return new Stage(
                usePart(),
                exitTime(1),
                colorSensors.usePart(),
                new Initial(() -> {
                    stopSpin = false;
                    color = SampleColor.NONE;
                    lastColors = new ArrayList<>();
                    code = 0;

                }),
                new Main(() -> {
                    move(1);
                    lastColors.add(colorSensors.getSampleColor());
//                    lastCounts.add(colorSensors.isSampleLoaded() ? 1.0 : 0.0);

                }),
                new Exit(() -> {
//                    color = colorSensors.getSampleColor();
                    color = getColor();
                    code = colorSensors.determineCodeFromColor(color);
                    if (code != 0) {
                        stopSpin = true;
                        sampleLoaded = true;
                        return true;
                    }
                    else {
                        return stopSpin;
                    }
//                    }else {
//                        return stopSpin;
//                    }
                }),
                stop(),
                returnPart(),
                colorSensors.returnPart()
        );
    }

    public Stage moveUntilColorOut(){
        return new Stage(
                usePart(),
                colorSensors.usePart(),
                new Initial(() -> stopSpin = false),
                new Main(() -> {
                    move(-1);
                }),
                new Exit(() -> {
                    if(!colorSensors.isSampleLoaded()){
                        stopSpin = true;
                        sampleLoaded = false;
                        return true;
                    }else{
                        return stopSpin;
                    }
                }),
                stop(),
                returnPart(),
                colorSensors.returnPart()
        );
    }


    public Stage shimmyUntilStopDir(double pow, double freq){
        return new Stage(
                usePart(),
                drive.usePart(),
                new Initial(() -> {
                    stopSpin = false;
                    lastColors = new ArrayList<>();
                    code = 0;
                    color = SampleColor.NONE;
                }),
                new Initial(timer::reset),
                new Main(() -> {
                    move(1);
                    drive.move(0.0, 0.0, pow*Math.sin(timer.seconds()*2*Math.PI*freq));
                    lastColors.add(colorSensors.getSampleColor());
                }),
                new Exit(() -> {
                    color = getColor();
                    code = colorSensors.determineCodeFromColor(color);
                    if (code != 0) {
                        stopSpin = true;
                        sampleLoaded = true;
                        return true;
                    } else {
                        return stopSpin;
                    }
                }),
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


    public Stage isSampleStillThere(){
        return new Stage(
                usePart(),
                colorSensors.usePart(),
                new Main(() -> {
                    if(colorSensors.getDistance() < 5){
                        isSampleVertical = true;
                    }
                }),
                RobotPart.exitAlways(),
                stop(),
                returnPart(),
                colorSensors.returnPart()
        );
    }


    public Stage showIfVertical(){
        return new Stage(
                usePart(),
                new Initial(timer::reset),
                new Main(() -> {
                    double freq = 2;
                    if(isSampleVertical){
                        move(Math.sin(timer.seconds()*2*Math.PI*freq));
                    }
                }),
                new Exit(() -> timer.seconds() > 2 || !isSampleVertical),
                new Stop(() -> {
                    isSampleVertical = false;
                }),
                stop(),
                returnPart()
        );
    }


}
