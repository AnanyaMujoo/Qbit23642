package autoutil.vision;

import android.os.Build;

import static global.General.bot;
import static global.General.gamepad1;
import static global.General.log;
import static org.opencv.core.Core.inRange;
import static org.opencv.core.Core.min;


import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import geometry.position.Pose;
import geometry.position.Vector;


public class JunctionScanner2 extends Scanner {

    public double CONTOUR_AREA = 250.0;
    private final Scalar CONTOUR_COLOR = new Scalar(255,0,255);
    private final Scalar HORIZON_COLOR = new Scalar(0,255,0);
    private final Scalar TEXT_COLOR = new Scalar(0, 0, 0);

    private final Pose target = new Pose(0,18.5, -5);

    enum DETECT_COLOR {
        RED,
        BLUE,
        BOTH
    }

    public DETECT_COLOR coneColor = DETECT_COLOR.BOTH;

    public int horizon = 100;
    public int horizonCone = 20;

    private Rect redRect = new Rect();
    private Rect blueRect = new Rect();
    private Rect maxRect = new Rect();

    private final List<MatOfPoint> redContours = new ArrayList<>();
    private final List<MatOfPoint> blueContours = new ArrayList<>();

    private final ArrayList<MatOfPoint> contours = new ArrayList<>();
    private final ArrayList<Rect> possibleSignals = new ArrayList<>();
    // Cone mask scalars
    private final Scalar redLow = new Scalar(0, 161, 60);
    private final Scalar redHigh = new Scalar(200, 255, 255);
    private final Scalar blueLow = new Scalar(0, 80, 138);
    private final Scalar blueHigh = new Scalar(150, 255, 255);
    // Pole mask scalars
    public Scalar poleLower = new Scalar(59, 134, 40);
    public Scalar poleHigher = new Scalar(180, 180, 105);

//    public Scalar poleLower = new Scalar(59, 134, 20);
//    public Scalar poleHigher = new Scalar(180, 180, 105);
    // Signal sleeve mask scalars
    private final Scalar greenLower = new Scalar(32, 10, 75);
    private final Scalar greenUpper = new Scalar(86, 255,255);
    private final Scalar blueLower = new Scalar(80, 140, 75);
    private final Scalar blueUpper = new Scalar(133, 255, 190);
    private final Scalar yellowLower = new Scalar(17, 100, 100);
    private final Scalar yellowUpper = new Scalar(40, 255, 255);
    // Mat objects
    private final Mat maskRed = new Mat();
    private final Mat maskBlue = new Mat();
    private final Mat yCrCb = new Mat();
    private final Mat hsvMat = new Mat();
    private final Mat binaryMat = new Mat();

    private final Size kSize = new Size(5, 5);
    private final Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, kSize);

    private static final double cameraFov = 47;
    private static final double realJunctionWidth = 2.672; // cm
    private static final double oneConeWidth = 7.5; // cm
    private static final double twoConeWidth = 9.7; // cm
    private static final double manyConeWidth = 11.0; // cm
    public double distanceToJunction = 0, angleToJunction = 0;

    public Pose error = new Pose();

    public int coneMode = 1;


    @Override
    public void run(Mat input) {}

    @Override
    public void preProcess(Mat input) {}

    @Override
    public void postProcess(Mat input) {}

    Scalar mean = new Scalar(0,0,0);
    @Override
    public void message() {
//        log.show("Cone Mode", coneMode);
//        log.show("Pose", getPose());
//        log.showAndRecord("Mean", mean);
    }

    @Override
    public Mat processFrame(Mat input) {
        crop(input, new Rect(0, horizon, input.width(), input.height()-horizon));
        detectPole(Crop);
        Pose errorInternal = getErrorInternal();
        if(cuttoff(errorInternal)){
            error = errorInternal.getCopy();
            return Crop;
        }
        crop(input, new Rect(0, horizonCone, input.width(), input.height()-horizonCone));
        detectCone(Crop);
        errorInternal = getErrorInternal();
        if(cuttoff(errorInternal)){
            error = errorInternal.getCopy();
            return Crop;
        }
        error = new Pose();
        return input;
//        return detectPole(input);
//        detectPole(input).copyTo(input);
//        return input;
    }

    public boolean cuttoff(Pose error){
        return Math.abs(error.getY()) < 15 && Math.abs(error.getAngle()) < 20;
    }

    public Pose getError(){
        return error;
    }
    private Pose getErrorInternal(){
        Pose error = target.getSubtracted(getPose());
        error.invertOrientation();
        return error;
    }

    public Pose getPose(){
        double distance = distanceToJunction;
        double angle = angleToJunction;
        Vector distanceVector = new Vector(0, distance);
        distanceVector.rotate(angle);
        return new Pose(distanceVector.getX(), distanceVector.getY(), angle);
    }


    private Mat detectPole(Mat input) {
        boolean exitEarly = true;

        Imgproc.cvtColor(input, yCrCb, Imgproc.COLOR_RGB2YCrCb);

        Imgproc.erode(yCrCb, yCrCb, kernel);



        inRange(yCrCb, poleLower, poleHigher, binaryMat);

//        binaryMat.copyTo(input);

        Imgproc.findContours(binaryMat, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

//        Imgproc.drawContours(input, contours, -1, CONTOUR_COLOR);

        if(!contours.isEmpty()) {
            MatOfPoint biggestPole = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                biggestPole = Collections.max(contours, Comparator.comparingDouble(t0 -> Imgproc.boundingRect(t0).height));
            }
            if(biggestPole != null) {
                if (Imgproc.contourArea(biggestPole) > CONTOUR_AREA) {
                    maxRect = Imgproc.boundingRect(biggestPole);
                    exitEarly = false;

//                    Imgproc.rectangle(input, maxRect, CONTOUR_COLOR, 2);
//                    Imgproc.putText(input, "Pole", new Point(maxRect.x, maxRect.y < 10 ? (maxRect.y + maxRect.height + 20) : (maxRect.y - 8)), Imgproc.FONT_HERSHEY_SIMPLEX, 0.8, TEXT_COLOR, 2);
                }
            }
        }
//        Rect rect = getRectFromCenter(getCenter(input), 80, 80);
//        drawRectangle(input, rect, GREEN);
//        mean = getAverage(yCrCb, rect);

        contours.clear();
        yCrCb.release();
        binaryMat.release();

        if(exitEarly){
            distanceToJunction = 1000; angleToJunction = 1000;
            return input;
        }



        double junctionCenterX = getCenter(maxRect).x;
        double screenCenterX = input.width() / 2.0;
        double junctionCenterOffset = junctionCenterX - screenCenterX;
        double screenWidth = input.width();


        double screenJunctionWidth = maxRect.width;
        distanceToJunction = realJunctionWidth / distanceRatio(screenJunctionWidth, screenWidth);

        double realJunctionOffset = distanceToJunction * distanceRatio(junctionCenterOffset, screenWidth);
        angleToJunction = Math.toDegrees(Math.atan2(realJunctionOffset, distanceToJunction));

        return input;
    }

    private double distanceRatio(double sizeOnScreen, double screenWidth){ return 2*Math.tan(Math.toRadians(cameraFov*(sizeOnScreen/screenWidth))/2.0); }

//    //ToD Convert to using only a single contour list
    private Mat detectCone(Mat input) {

        boolean exitEarly = true;

        Imgproc.cvtColor(input, yCrCb, Imgproc.COLOR_RGB2YCrCb);
        Imgproc.erode(yCrCb, yCrCb, kernel);

//        if (coneColor.equals(DETECT_COLOR.RED) || coneColor.equals(DETECT_COLOR.BOTH)) {
//            inRange(yCrCb, redLow, redHigh, maskRed);
//
//            redContours.clear();
//
//            Imgproc.findContours(maskRed, redContours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
//            redContours.removeIf(c -> Imgproc.boundingRect(c).y + (Imgproc.boundingRect(c).height / 2.0) < horizon);
//            Imgproc.drawContours(input, redContours, -1, CONTOUR_COLOR);
//
//            if(!redContours.isEmpty()) {
//                MatOfPoint biggestRedContour = Collections.max(redContours, Comparator.comparingDouble(t0 -> Imgproc.boundingRect(t0).width));
//                if(Imgproc.contourArea(biggestRedContour) > CONTOUR_AREA) {
//                    redRect = Imgproc.boundingRect(biggestRedContour);
//
//                    Imgproc.rectangle(input, redRect, CONTOUR_COLOR, 2);
//                    Imgproc.putText(input, "Red Cone", new Point(redRect.x, redRect.y < 10 ? (redRect.y+redRect.height+20) : (redRect.y - 8)), Imgproc.FONT_HERSHEY_SIMPLEX, 0.8, TEXT_COLOR, 1);
//                }
//            }
//
//            maskRed.release();
//        }
//
//        if (coneColor.equals(DETECT_COLOR.BLUE) || coneColor.equals(DETECT_COLOR.BOTH)) {
//            inRange(yCrCb, blueLow, blueHigh, maskBlue);
//
//            blueContours.clear();
//
//            Imgproc.findContours(maskBlue, blueContours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
//            blueContours.removeIf(c -> Imgproc.boundingRect(c).y + (Imgproc.boundingRect(c).height / 2.0) < horizon);
//            Imgproc.drawContours(input, blueContours, -1, CONTOUR_COLOR);
//
//            if(!blueContours.isEmpty()) {
//                MatOfPoint biggestBlueContour = Collections.max(blueContours, Comparator.comparingDouble(t0 -> Imgproc.boundingRect(t0).width));
//                if(Imgproc.contourArea(biggestBlueContour) > CONTOUR_AREA) {
//                    blueRect = Imgproc.boundingRect(biggestBlueContour);
//
//                    Imgproc.rectangle(input, blueRect, CONTOUR_COLOR, 2);
//                    Imgproc.putText(input, "Blue Cone", new Point(blueRect.x, blueRect.y < 10 ? (blueRect.y+blueRect.height+20) : (blueRect.y - 8)), Imgproc.FONT_HERSHEY_SIMPLEX, 0.8, TEXT_COLOR, 1);
//                }
//            }
//            maskBlue.release();
//        }

        inRange(yCrCb, blueLow, blueHigh, maskBlue);

        Imgproc.findContours(maskBlue, blueContours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

//        Imgproc.drawContours(input, blueContours, -1, CONTOUR_COLOR);

        MatOfPoint2f bluePoly = new MatOfPoint2f();
        MatOfPoint approxBluePoly = new MatOfPoint();

        double blueArea;

        if(!blueContours.isEmpty()) {
            MatOfPoint biggestBlueContour = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                biggestBlueContour = Collections.max(blueContours, Comparator.comparingDouble(t0 -> Imgproc.boundingRect(t0).width));
            }
            if(biggestBlueContour != null) {

                blueArea = Imgproc.contourArea(biggestBlueContour);
                if (blueArea > CONTOUR_AREA) {
                    blueRect = Imgproc.boundingRect(biggestBlueContour);
                    Imgproc.approxPolyDP(new MatOfPoint2f(biggestBlueContour.toArray()), bluePoly, 15, true);
                    bluePoly.convertTo(approxBluePoly, CvType.CV_32S);
//                    ArrayList<MatOfPoint> contours = new ArrayList<>();
//                    contours.add(approxBluePoly);
//                    Imgproc.drawContours(input, contours, -1, GREEN);
                    exitEarly = false;
//
//                    Imgproc.rectangle(input, blueRect, CONTOUR_COLOR, 2);
//                    Imgproc.putText(input, "Blue Cone", new Point(blueRect.x, blueRect.y < 10 ? (blueRect.y + blueRect.height + 20) : (blueRect.y - 8)), Imgproc.FONT_HERSHEY_SIMPLEX, 0.8, TEXT_COLOR, 1);
                }
            }
        }

        blueContours.clear();
        maskBlue.release();
        yCrCb.release();

        if(exitEarly){
            distanceToJunction = 1000; angleToJunction = 1000;
            return input;
        }


        double junctionCenterX = getCenter(blueRect).x;
        double screenCenterX = input.width() / 2.0;
        double junctionCenterOffset = junctionCenterX - screenCenterX;
        double screenWidth = input.width();
        double screenJunctionWidth = blueRect.width;
//        double areaRatio = blueArea/screenJunctionWidth;


        double offset = blueRect.y;
        int numPoints = approxBluePoly.toList().size();

        if(numPoints <= 6){
            coneMode = (offset > 5 ? 1 : 2);
        }else if(numPoints <= 11){ coneMode = 3; } else if(numPoints <= 15){ coneMode = 4; }else{ coneMode = 5; }

//        log.record("Height", input.height());
//        log.record("Y", blueRect.y);
//        log.record("HeightR", blueRect.height);
//        log.record("Aspect", getAspectRatio(blueRect));
//        log.record("Area Ratio", areaRatio);

        distanceToJunction = (coneMode == 1 ? oneConeWidth : (coneMode == 2 ? twoConeWidth : manyConeWidth))/distanceRatio(screenJunctionWidth, screenWidth);

        double realJunctionOffset = distanceToJunction * distanceRatio(junctionCenterOffset, screenWidth);
        angleToJunction = Math.toDegrees(Math.atan2(realJunctionOffset, distanceToJunction));



        return input;
    }


//    private Mat detectSleeve(Mat input) {
//        Imgproc.cvtColor(input, hsvMat, Imgproc.COLOR_RGB2HSV);
//        Imgproc.erode(hsvMat, hsvMat, kernel);
//
//        inRange(hsvMat, greenLower, greenUpper, binaryMat);
//        Imgproc.findContours(binaryMat, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
//        drawContour(contours);
//        contours.clear();
//        binaryMat.release();
//
//        inRange(hsvMat, yellowLower, yellowUpper, binaryMat);
//        Imgproc.findContours(binaryMat, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
//        drawContour(contours);
//        contours.clear();
//        binaryMat.release();
//
//        inRange(hsvMat, blueLower, blueUpper, binaryMat);
//        Imgproc.findContours(binaryMat, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
//        drawContour(contours);
//        contours.clear();
//        binaryMat.release();
//        hsvMat.release();
//
//        // Check what contour has the largest area.
//        if(possibleSignals.get(0).area() > possibleSignals.get(1).area() && possibleSignals.get(0).area() > possibleSignals.get(2).area()) {
//            signalColor = Signal.GREEN;
//            Imgproc.rectangle(input, possibleSignals.get(0), CONTOUR_COLOR);
//            Imgproc.putText(input, "Green", new Point(possibleSignals.get(0).x, wrapText(0)), Imgproc.FONT_HERSHEY_SIMPLEX, 0.8, TEXT_COLOR, 2);
//        } else if(possibleSignals.get(1).area() > possibleSignals.get(2).area()) {
//            signalColor = Signal.YELLOW;
//            Imgproc.rectangle(input, possibleSignals.get(1), CONTOUR_COLOR);
//            Imgproc.putText(input, "Yellow", new Point(possibleSignals.get(1).x, wrapText(1)), Imgproc.FONT_HERSHEY_SIMPLEX, 0.8, TEXT_COLOR, 2);
//        } else if(possibleSignals.get(2).area() > 1) {
//            signalColor = Signal.BLUE;
//            Imgproc.rectangle(input, possibleSignals.get(2), CONTOUR_COLOR);
//            Imgproc.putText(input, "Blue", new Point(possibleSignals.get(2).x, wrapText(2)), Imgproc.FONT_HERSHEY_SIMPLEX, 0.8, TEXT_COLOR, 2);
//        } else
//            signalColor = Signal.NONE;
//
//        possibleSignals.clear();
//
//        Imgproc.line(input, new Point(0,horizon), new Point(640, horizon), HORIZON_COLOR);
//
//        return input;
//    }

//    private void drawContour(ArrayList<MatOfPoint> contours) {
//        // TOd Remove sorting of contours and just loop through them with a largestRect variable
//        // Order contours in descending order by width
//        contours.sort(Collections.reverseOrder(Comparator.comparingDouble(t0 -> Imgproc.boundingRect(t0).width)));
//        Rect r = new Rect(0,0,0,0);
//        for (MatOfPoint c : contours) {
//            r = Imgproc.boundingRect(c.clone());
//            c.release();
//            if (r.y + (r.height/2.0) > horizon && r.area() > 50.0)
//                break;
//            r = new Rect(0,0,0,0);
//        }
//        possibleSignals.add(r);
//    }

//    private double wrapText(int i) {
//        return possibleSignals.get(0).y < 10 ? (possibleSignals.get(i).y+possibleSignals.get(i).height+20) : (possibleSignals.get(i).y - 8);
//    }
//
//    public void setTrackType(TrackType trackType) {
//        this.trackType = trackType;
//    }
//
//
//    public Signal getSignalColor() {
//        return signalColor;
//    }
//
//    public Rect getBiggestCone() {
//        return coneColor.equals(DETECT_COLOR.RED) ? redRect : blueRect;
//    }
}