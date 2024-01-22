package autoutil.vision;
import static global.General.log;
import static global.General.telemetry;

import android.annotation.SuppressLint;

import com.sun.tools.javac.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;

import elements.Case;
import elements.TeamProp;
import util.template.Iterator;

public class CaseScannerRect extends Scanner {

    private volatile TeamProp caseDetected = TeamProp.FIRST;
    protected final TeamProp[] cases = new TeamProp[]{TeamProp.FIRST, TeamProp.SECOND, TeamProp.THIRD};
    protected final TeamProp[] pastCases = new TeamProp[5];

    {
        Arrays.fill(pastCases, caseDetected);
    }

    String color;
    String side;
    int index;
    double leftAvgFin;
    double centerAvgFin;
    double rightAvgFin;
    final private int threshhold = 136; //TODO change threshold at event
    Rect notSeen;

    public int getCase(Mat input, String color) {
        cropAndFill(input, getZoomedRect(input, 1.6, 30));
        getHSV(input);

        computeRects(80, 150);


        debug(input);


        Point center = getCenter(input);
        Point pictureCenter = new Point(center.x + 10, center.y + 30);
        drawRectangle(input, getRectFromCenter(pictureCenter, 100, 150), GREEN);

        double redValue = getBestRectStDev(input, 350, 10, RED);
        double blueValue = getBestRectStDev(input, 230, 180, BLUE);

        if (color.equals("red")) {

        }
        return 0;
    }


        Mat output = new Mat();
        public int getCase (Mat input){
            Mat YCbCr = new Mat();
            Mat leftCrop;
            Mat centerCrop;
            Mat rightCrop;

            int index = 0;

            if (color.equalsIgnoreCase("red")) {
                index = 1;
            } else if (color.equalsIgnoreCase("blue")) {
                index = 2;
            } else {
                log.show("Invalid color");
            }

            log.show("STARTING");
            Imgproc.cvtColor(input, YCbCr, Imgproc.COLOR_RGB2YCrCb);

            Rect rightRect = new Rect(30, 90, 70, 70);
            Rect centerRect = new Rect(250, 80, 100, 100);
            Rect leftRect = new Rect(0, 0, 20, 20);


            if (side.equalsIgnoreCase("left")) {
                leftRect = new Rect(200, 419, 300, 300);
                centerRect = new Rect(650, 400, 400, 319);
                rightRect = new Rect(1050, 500, 200, 200);
                notSeen = rightRect;
            } else if (side.equalsIgnoreCase("right")) {
                notSeen = leftRect;
            }

            //        input.copyTo(output);

            //  Imgproc.rectangle(input, leftRect, WHITE, 5);
            // Imgproc.rectangle(input, centerRect, WHITE, 5);
            //Imgproc.rectangle(input, rightRect, WHITE, 5);

            leftCrop = YCbCr.submat(leftRect);
            centerCrop = YCbCr.submat(centerRect);
            rightCrop = YCbCr.submat(rightRect);

            Core.extractChannel(leftCrop, leftCrop, index);
            Core.extractChannel(centerCrop, centerCrop, index);
            Core.extractChannel(rightCrop, rightCrop, index);

            Scalar leftAvg = Core.mean(leftCrop);
            Scalar centerAvg = Core.mean(centerCrop);
            Scalar rightAvg = Core.mean(rightCrop);

            leftAvgFin = leftAvg.val[0];
            centerAvgFin = centerAvg.val[0];
            rightAvgFin = rightAvg.val[0];

//            double maxFin = Math.max(leftAvgFin, centerAvgFin);
//            if (maxFin < rightAvgFin) {
//                maxFin = rightAvgFin;
//            }

            if (notSeen.equals(rightRect)) {
                if (leftAvgFin < threshhold && centerAvgFin < threshhold) {
                    return 2;
                } else {
                    if (leftAvgFin > centerAvgFin) {
                        return 0;
                    } else if (centerAvgFin > leftAvgFin) {
                        return 1;
                    }
                }
            } else if (notSeen.equals(leftRect)) {
                if (rightAvgFin < threshhold && centerAvgFin < threshhold) {
                    return 0;
                } else {
                    if (rightAvgFin > centerAvgFin) {
                        return 2;
                    } else if (centerAvgFin > rightAvgFin) {
                        return 1;
                    }
                }
            }
            //TODO FIX LESS MEMEORY
            output.release();
            YCbCr.release();
            leftCrop.release();
            centerCrop.release();
            rightCrop.release();
            // return -1;
            if (leftAvgFin > centerAvgFin && leftAvgFin > rightAvgFin) {
                Imgproc.rectangle(input, leftRect, GREEN, 5);
                //            telemetry.addLine("Left");
                return 0;
            } else if (centerAvgFin > leftAvgFin && centerAvgFin > rightAvgFin) {
                Imgproc.rectangle(input, centerRect, GREEN, 5);

                //            telemetry.addLine("Center");
                return 1;
            } else if (rightAvgFin > leftAvgFin && rightAvgFin > centerAvgFin) {
                Imgproc.rectangle(input, rightRect, GREEN, 5);

                //            telemetry.addLine("Right");
                return 2;
            }
            return -1;
            //  return 0;

        }

        private TeamProp getCaseStable (TeamProp currentCase){
            boolean casesAreSame = true;
            for (int i = 0; i < pastCases.length - 1; i++) {
                pastCases[i] = pastCases[i + 1];
                if (!pastCases[i].equals(currentCase)) {
                    casesAreSame = false;
                }
            }
            pastCases[pastCases.length - 1] = currentCase;
            return casesAreSame ? currentCase : pastCases[0];
        }

        public void setColor (String color){
            this.color = color;
        }

        public void setSide (String side){
            this.side = side;
        }

        @Override
        public void run (Mat input){
//        caseDetected = cases[getCase(input, "blue")];
//        caseDetected = getCaseStable(cases[getCase(input)]);
//        caseDetected = cases[getCase(input)];
            Rect leftRect = new Rect(30, 90, 70, 70);
            Rect centerRect = new Rect(250, 80, 100, 100);
//        Rect rightRect = new Rect(899, 300, 379, 419);

            Imgproc.rectangle(input, leftRect, RED, 5);
            Imgproc.rectangle(input, centerRect, RED, 5);
//        Imgproc.rectangle(input, rightRect, RED, 5);
//
        }

        @Override
        public void preProcess (Mat input){

        }

        @Override
        public void postProcess (Mat input){

        }

        @Override
        public void message () {
            log.show("Case Detected", caseDetected);
            log.show("Color", color);
            log.show("left", leftAvgFin);
            log.show("center", centerAvgFin);
            log.show("right", rightAvgFin);
        }

        public final TeamProp getCase () {
            return caseDetected;
        }

//    @SuppressLint("NewApi")
//    public int getCase(Mat input) {return 0;};
    }


/*
package autoutil.vision;

import static global.General.log;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.Arrays;

import elements.Case;
import util.template.Iterator;


public class CaseScanner extends Scanner{
    private volatile Case caseDetected = Case.THIRD;
    protected final Case[] cases = new Case[]{Case.FIRST, Case.SECOND, Case.THIRD};
    protected final Case[] pastCases = new Case[5];
    { Arrays.fill(pastCases, caseDetected); }

    public int getCase(Mat input){
        cropAndFill(input, getZoomedRect(input, 1.6, 30));
        getHSV(input);

        computeRects(80, 150);


        debug(input);


        Point center = getCenter(input);
        Point pictureCenter = new Point(center.x+10, center.y+30);
        drawRectangle(input, getRectFromCenter(pictureCenter, 100, 150), GREEN);

        double cyanValue = getBestRectStDev(input, 90, 110, CYAN);
        double magentaValue = getBestRectStDev(input, 140, 180, MAGENTA);
        double orangeValue = getBestRectStDev(input, 1, 60, ORANGE);
        return Iterator.minIndex(cyanValue*1.5, magentaValue*1.2, orangeValue);
    }

    @Override
    public void message(){
//        logDebug();
//        caseDetected = getCaseStable(getCase());
        log.show("Case Detected", caseDetected);
    }


    private Case getCaseStable(Case currentCase){
        boolean casesAreSame = true;
        for (int i = 0; i < pastCases.length-1; i++) {
            pastCases[i] = pastCases[i+1];
            if(!pastCases[i].equals(currentCase)){ casesAreSame = false; }
        }
        pastCases[pastCases.length-1] = currentCase;
        return casesAreSame ? currentCase : pastCases[0];
    }


    @Override
    public final void preProcess(Mat input) {
//        Core.rotate(input, input, Core.ROTATE_90_COUNTERCLOCKWISE);
    }

    @Override
    public final void postProcess(Mat input) {
//        Core.rotate(input, input, Core.ROTATE_90_CLOCKWISE);
    }

    @Override
    public final void run(Mat input) {
        caseDetected = getCaseStable(cases[getCase(input)]);
    }

    public final Case getCase(){ return caseDetected; }
}
*/