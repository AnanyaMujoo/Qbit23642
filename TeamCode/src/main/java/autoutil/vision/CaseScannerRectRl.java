package autoutil.vision;
import static global.General.log;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import java.util.Arrays;

import elements.TeamProp;

public class CaseScannerRectRl extends Scanner {

    private volatile TeamProp caseDetected = TeamProp.LEFT;
    protected final TeamProp[] cases = new TeamProp[]{TeamProp.LEFT, TeamProp.CENTER, TeamProp.RIGHT};
    protected final TeamProp[] pastCases = new TeamProp[5];

    {
        Arrays.fill(pastCases, caseDetected);
    }

    String color;
    String side;
    public double centerAverage;
    public double rightAverage;

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
    public TeamProp getCase (Mat input) {
        // Input Size: 640 x 480 pixels


        // Define case that is detected
        TeamProp caseDetected;

        // Define regions to crop
        Rect centerRect = new Rect(95, 180, 130, 130);
        Rect rightRect = new Rect(480, 150, 130, 130);


        // Convert to different color space
        Mat YCrCb = new Mat();
        convertToYCrCb(input, YCrCb);

        // Get the correct color
        int colorIndex = 0;
        if(color.equalsIgnoreCase("red")){
            colorIndex = 1;
        }else if(color.equalsIgnoreCase("blue")){
            colorIndex = 2;
        }


        // Get the Color Channel,  Channel Index   Y: 0   Cr: 1   Cb: 2
        Mat Color = new Mat();
        getChannel(YCrCb,Color, colorIndex);

        // Alpha is contrast, beta is brightness adjust (alpha: 1.3, beta: -30)
        increaseContrast(Color, 1.3, -30);

        // Copy to input so we can see the red color space
        Color.copyTo(input);

        // Crop the red mat
        Mat rightCrop = getSubmat(Color, rightRect);
        Mat centerCrop = getSubmat(Color, centerRect);

        // Calculate the averages
        centerAverage = getAverageValue(centerCrop);
        rightAverage = getAverageValue(rightCrop);



        // Define the threshold (160) based on color
        int threshold = 0;
        if(color.equalsIgnoreCase("red")){
            // Red case threshold
            threshold = 160;
        }else if(color.equalsIgnoreCase("blue")){
            // Blue case threshold
            threshold = 160;
        }


        // Decide the case based on the averages
        if(centerAverage > threshold){
            caseDetected = TeamProp.CENTER;
            //LEFT IS RIGHT TODO CHANGE
        }else if(rightAverage > threshold){
            caseDetected = TeamProp.RIGHT;
        }else{
            caseDetected = TeamProp.LEFT;
        }

        // Release the memory
        YCrCb.release();
        Color.release();


        // Draw to screen
        drawRectangle(input, rightRect, GREEN);
        drawRectangle(input, centerRect, GREEN);

        return caseDetected;




//            Mat centerCrop = YCbCr.submat(centerRect);
//            Mat rightCrop = YCbCr.submat(rightRect);
//            Core.extractChannel(centerCrop, centerCrop, 1);
//            Core.extractChannel(centerCrop, centerCrop, 1);


//            return 0;


    }

    @Override
    public void message () {
        log.show("Case Detected", caseDetected);
        log.show("Center Average", centerAverage);
    }





    //            log.show("Color", color);
    //            log.show("left", leftAvgFin);
    //            log.show("center", centerAvgFin);
    //            log.show("right", rightAvgFin);
//        }

//
//            int index = 0;
//
//            if (color.equalsIgnoreCase("red")) {
//                index = 1;
//            } else if (color.equalsIgnoreCase("blue")) {
//                index = 2;
//            } else {
//                log.show("Invalid color");
//            }
//
//            log.show("STARTING");
//            Imgproc.cvtColor(input, YCbCr, Imgproc.COLOR_RGB2YCrCb);
//
//            Rect rightRect = new Rect(30, 90, 70, 70);
//            Rect centerRect = new Rect(250, 80, 100, 100);
//            Rect leftRect = new Rect(0, 0, 20, 20);
//
//
//            if (side.equalsIgnoreCase("left")) {
//                leftRect = new Rect(200, 419, 300, 300);
//                centerRect = new Rect(650, 400, 400, 319);
//                rightRect = new Rect(1050, 500, 200, 200);
//                notSeen = rightRect;
//            } else if (side.equalsIgnoreCase("right")) {
//                notSeen = leftRect;
//            }
//
//            //        input.copyTo(output);
//
//            //  Imgproc.rectangle(input, leftRect, WHITE, 5);
//            // Imgproc.rectangle(input, centerRect, WHITE, 5);
//            //Imgproc.rectangle(input, rightRect, WHITE, 5);
//
//            leftCrop = YCbCr.submat(leftRect);
//            centerCrop = YCbCr.submat(centerRect);
//            rightCrop = YCbCr.submat(rightRect);
//
//            Core.extractChannel(leftCrop, leftCrop, index);
//            Core.extractChannel(centerCrop, centerCrop, index);
//            Core.extractChannel(rightCrop, rightCrop, index);
//
//            Scalar leftAvg = Core.mean(leftCrop);
//            Scalar centerAvg = Core.mean(centerCrop);
//            Scalar rightAvg = Core.mean(rightCrop);
//
//            leftAvgFin = leftAvg.val[0];
//            centerAvgFin = centerAvg.val[0];
//            rightAvgFin = rightAvg.val[0];
//
////            double maxFin = Math.max(leftAvgFin, centerAvgFin);
////            if (maxFin < rightAvgFin) {
////                maxFin = rightAvgFin;
////            }
//
//            if (notSeen.equals(rightRect)) {
//                if (leftAvgFin < threshhold && centerAvgFin < threshhold) {
//                    return 2;
//                } else {
//                    if (leftAvgFin > centerAvgFin) {
//                        return 0;
//                    } else if (centerAvgFin > leftAvgFin) {
//                        return 1;
//                    }
//                }
//            } else if (notSeen.equals(leftRect)) {
//                if (rightAvgFin < threshhold && centerAvgFin < threshhold) {
//                    return 0;
//                } else {
//                    if (rightAvgFin > centerAvgFin) {
//                        return 2;
//                    } else if (centerAvgFin > rightAvgFin) {
//                        return 1;
//                    }
//                }
//            }
//            //TODO FIX LESS MEMEORY
//            output.release();
//            YCbCr.release();
//            leftCrop.release();
//            centerCrop.release();
//            rightCrop.release();
//            // return -1;
//            if (leftAvgFin > centerAvgFin && leftAvgFin > rightAvgFin) {
//                Imgproc.rectangle(input, leftRect, GREEN, 5);
//                //            telemetry.addLine("Left");
//                return 0;
//            } else if (centerAvgFin > leftAvgFin && centerAvgFin > rightAvgFin) {
//                Imgproc.rectangle(input, centerRect, GREEN, 5);
//
//                //            telemetry.addLine("Center");
//                return 1;
//            } else if (rightAvgFin > leftAvgFin && rightAvgFin > centerAvgFin) {
//                Imgproc.rectangle(input, rightRect, GREEN, 5);
//
//                //            telemetry.addLine("Right");
//                return 2;
//            }
//            return -1;
//            //  return 0;



    private TeamProp makeCaseStable (TeamProp currentCase){
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
    public void run (Mat input) {
        caseDetected = makeCaseStable(getCase(input));
    }


//        caseDetected = cases[getCase(input, "blue")];

//        caseDetected = cases[getCase(input)];
//            Rect leftRect = new Rect(60, 180, 140, 140);
//            Rect centerRect = new Rect(500, 160, 200, 200);
//        Rect rightRect = new Rect(899, 300, 379, 419);

//            Imgproc.rectangle(input, leftRect, RED, 5);
//            Imgproc.rectangle(input, centerRect, RED, 5);
//        Imgproc.rectangle(input, rightRect, RED, 5);
//


    @Override
    public void preProcess (Mat input){

    }

    @Override
    public void postProcess (Mat input){

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