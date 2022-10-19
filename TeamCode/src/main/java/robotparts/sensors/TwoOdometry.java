package robotparts.sensors;

import java.util.ArrayList;

import autoutil.profilers.Profiler;
import geometry.position.Pose;
import geometry.position.Vector;
import global.Constants;
import robot.BackgroundTask;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IEncoder;
import util.codeseg.ExceptionCodeSeg;
import util.template.Iterator;

import static global.General.bot;

public class TwoOdometry extends RobotPart {
    private IEncoder enc1, enc2;
    private final ExceptionCodeSeg<RuntimeException> odometryUpdateCode = this::update;
    private final Pose currentPose = new Pose();
    private final double encoderWheelDiameter = 3.5; //cm

    @Override
    public void init() {
        enc1 = create("enc1", ElectronicType.IENCODER_NORMAL);
        enc2 = create("enc2", ElectronicType.IENCODER_NORMAL);
        bot.addBackgroundTask(new BackgroundTask(this::updateBackground));
    }

    protected void updateBackground(){
        enc1.updateNormal(); enc2.updateNormal();  gyro.updateHeading();
    }

    protected void reset(){
        enc1.reset(); enc2.reset();
    }




    protected void update(){
        ArrayList<Double> newDeltaPositionsEnc1 = enc1.getNewDeltaPositions();
        ArrayList<Double> newDeltaPositionsEnc2 = enc2.getNewDeltaPositions();
        ArrayList<Double> newHeadingPositions = gyro.getNewDeltaHeadings();
        for (int i = 0; i < newDeltaPositionsEnc1.size(); i++) {
            update(new Pose(newDeltaPositionsEnc1.get(i), newDeltaPositionsEnc2.get(i), newHeadingPositions.get(i)));
        }
    }
    private void update(Pose delta){
        Vector deltaPos = new Vector(delta.getPoint()).getScaled(encoderWheelDiameter*Math.PI/Constants.ENCODER_TICKS_PER_REV);
        // TODO 4 MAKE

    }



}
