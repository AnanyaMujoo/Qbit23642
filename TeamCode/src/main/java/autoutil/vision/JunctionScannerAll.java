package autoutil.vision;

import static global.General.fieldSide;
import static global.General.log;
import static global.Modes.Height.LOW;
import static global.Modes.Height.MIDDLE;
import static global.Modes.heightMode;
import static org.opencv.core.Core.inRange;
import static org.opencv.core.Core.min;


import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import autoutil.Profiler;
import elements.FieldSide;
import elements.GameItems;
import elements.Robot;
import geometry.position.Pose;
import geometry.position.Vector;
import util.template.Precision;


public class JunctionScannerAll extends Scanner {

    public final double minContourArea = 250.0;

    private final Pose defaultTarget = new Pose(0,18.0, -1);
    private Pose target = defaultTarget.getCopy();

    public final int horizon = 100;
    public final int horizonCone = 20;

    private Rect coneRect = new Rect();
    private Rect poleRect = new Rect();

    private final List<MatOfPoint> coneContours = new ArrayList<>();
    private final ArrayList<MatOfPoint> poleContours = new ArrayList<>();

    private final Scalar redLow = new Scalar(0, 161, 60);
    private final Scalar redHigh = new Scalar(200, 255, 255);
    private final Scalar blueLow = new Scalar(0, 80, 138);
    private final Scalar blueHigh = new Scalar(150, 255, 255);

    public Scalar poleLower = new Scalar(59, 134, 40);
    public Scalar poleHigher = new Scalar(180, 180, 105);

    private final Mat maskCone = new Mat();
    private final Mat yCrCb = new Mat();
    private final Mat binaryMat = new Mat();

    private final Size kSize = new Size(5, 5);
    private final Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, kSize);

    private static final double cameraFov = 47;
    private static final double realPoleWidth = 2.672; // cm
    private static final double oneConeWidth = 7.5; // cm
    private static final double twoConeWidth = 9.7; // cm
    private static final double manyConeWidth = 11.0; // cm
    public static double distanceToJunction = 0, angleToJunction = 0, rollOfJunction = 0;

    public static Pose error = new Pose();

    public int coneMode = 1;

    private static boolean pausing = true;

    public static void pause(){ pausing = true; }
    public static void resume(){ pausing = false; }

    @Override public void run(Mat input) {}
    @Override public void preProcess(Mat input) {}
    @Override public void postProcess(Mat input) {}

    @Override
    public void message() {
//        log.show("Roll", rollOfJunction);

//        Scalar mean = new Scalar(0,0,0);
//        log.show("Cone Mode", coneMode);
//        log.show("Pose", getPose());
//        log.showAndRecord("Mean", mean);
//        Rect rect = getRectFromCenter(getCenter(input), 80, 80);
//        drawRectangle(input, rect, GREEN);
//        mean = getAverage(yCrCb, rect);

//        log.record("Height", input.height());
//        log.record("Y", blueRect.y);
//        log.record("HeightR", blueRect.height);
//        log.record("Aspect", getAspectRatio(blueRect));
//        log.record("Area Ratio", areaRatio);


//        double areaRatio = coneArea/coneRect.width;
    }

    @Override
    public Mat processFrame(Mat input) {
        if(!pausing) {
            crop(input, new Rect(0, horizon, input.width(), input.height() - horizon));
            detectPole(Crop);
            Pose errorInternal = getErrorInternal();
            if (cutoff(errorInternal)) {
                error = errorInternal.getCopy();
                return Crop;
            }
            crop(input, new Rect(0, horizonCone, input.width(), input.height() - horizonCone));
            detectCone(Crop);
            errorInternal = getErrorInternal();
            if (cutoff(errorInternal)) {
                error = errorInternal.getCopy();
                return Crop;
            }
            error = new Pose();
        }
        return input;
    }



    public boolean cutoff(Pose error){return Math.abs(error.getY()) < 20 && (Math.abs(error.getAngle()) < 35); }

    public static Pose getError(){
        return error;
    }
    private Pose getErrorInternal(){ Pose error = target.getSubtracted(getPose()); error.invertOrientation(); return error; }
    public Pose getPose(){ Vector distanceVector = new Vector(0, distanceToJunction); distanceVector.rotate(angleToJunction); return new Pose(distanceVector.getX(), distanceVector.getY(), angleToJunction); }

    private void detectPole(Mat input) {
        boolean exitEarly = true;

        Imgproc.cvtColor(input, yCrCb, Imgproc.COLOR_RGB2YCrCb);

        Imgproc.erode(yCrCb, yCrCb, kernel);

        inRange(yCrCb, poleLower, poleHigher, binaryMat);

//        binaryMat.copyTo(input);

        Imgproc.findContours(binaryMat, poleContours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f polePoly = new MatOfPoint2f();

//        Imgproc.drawContours(input, contours, -1, CONTOUR_COLOR);

        if(!poleContours.isEmpty()) {
            MatOfPoint biggestPole = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                biggestPole = Collections.max(poleContours, Comparator.comparingDouble(t0 -> Imgproc.boundingRect(t0).height));
            }
            if(biggestPole != null) {
                if (Imgproc.contourArea(biggestPole) > minContourArea) {
                    poleRect = Imgproc.boundingRect(biggestPole);
                    Imgproc.approxPolyDP(new MatOfPoint2f(biggestPole.toArray()), polePoly, 2, true);
                    rollOfJunction = Imgproc.minAreaRect(polePoly).angle; if(rollOfJunction > 45){ rollOfJunction -= 90; }

                    if(Math.abs(rollOfJunction) < 1){
                        rollOfJunction = 0;
                        target = defaultTarget.getCopy().getAdded(new Pose(0, targetCorrection(), 0));
                    }else {
                        rollOfJunction = Precision.clip(rollOfJunction, 8);
                        target = defaultTarget.getAdded(new Pose(0, -0.2*Math.abs(rollOfJunction) + targetCorrection(), -rollOfJunction * 1.2 * GameItems.Junction.highHeight / (defaultTarget.getY())));

                    }

                    exitEarly = false;

//                    Imgproc.rectangle(input, maxRect, CONTOUR_COLOR, 2);
//                    Imgproc.putText(input, "Pole", new Point(maxRect.x, maxRect.y < 10 ? (maxRect.y + maxRect.height + 20) : (maxRect.y - 8)), Imgproc.FONT_HERSHEY_SIMPLEX, 0.8, TEXT_COLOR, 2);
                }
            }
        }

        poleContours.clear();
        yCrCb.release();
        binaryMat.release();

        if(exitEarly){ reset(); return; }

        update(input, poleRect, realPoleWidth);

    }
    public void reset(){
        distanceToJunction = 1000; angleToJunction = 1000; rollOfJunction = 0; target = defaultTarget.getCopy();
    }

    public double targetCorrection(){
        if(heightMode != null) { return heightMode.modeIs(MIDDLE) ? -1 : (heightMode.modeIs(LOW) ? -2 : 0); }
        return 0;
    }

    private void detectCone(Mat input) {

        boolean exitEarly = true;

        Imgproc.cvtColor(input, yCrCb, Imgproc.COLOR_RGB2YCrCb);

        Imgproc.erode(yCrCb, yCrCb, kernel);

        FieldSide.on(() -> inRange(yCrCb, blueLow, blueHigh, maskCone), () -> inRange(yCrCb, redLow, redHigh, maskCone));

        Imgproc.findContours(maskCone, coneContours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

//        Imgproc.drawContours(input, blueContours, -1, CONTOUR_COLOR);

        MatOfPoint2f conePoly = new MatOfPoint2f();
        MatOfPoint approxConePoly = new MatOfPoint();

        double coneArea;

        if(!coneContours.isEmpty()) {
            MatOfPoint biggestConeContour = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                biggestConeContour = Collections.max(coneContours, Comparator.comparingDouble(t0 -> Imgproc.boundingRect(t0).width));
            }
            if(biggestConeContour != null) {

                coneArea = Imgproc.contourArea(biggestConeContour);
                if (coneArea > minContourArea) {
                    coneRect = Imgproc.boundingRect(biggestConeContour);
                    Imgproc.approxPolyDP(new MatOfPoint2f(biggestConeContour.toArray()), conePoly, 15, true);
                    conePoly.convertTo(approxConePoly, CvType.CV_32S);
//                    ArrayList<MatOfPoint> contours = new ArrayList<>();
//                    contours.add(approxBluePoly);
//                    Imgproc.drawContours(input, contours, -1, GREEN);
                    target = defaultTarget.getCopy().getAdded(new Pose(0, targetCorrection(), 0));
                    exitEarly = false;
//
//                    Imgproc.rectangle(input, blueRect, CONTOUR_COLOR, 2);
//                    Imgproc.putText(input, "Blue Cone", new Point(blueRect.x, blueRect.y < 10 ? (blueRect.y + blueRect.height + 20) : (blueRect.y - 8)), Imgproc.FONT_HERSHEY_SIMPLEX, 0.8, TEXT_COLOR, 1);
                }
            }
        }

        coneContours.clear();
        maskCone.release();
        yCrCb.release();

        if(exitEarly){ reset(); return;  }

        double offset = coneRect.y;
        int numPoints = approxConePoly.toList().size();

        if(numPoints <= 6){
            coneMode = (offset > 5 ? 1 : 2);
        }else if(numPoints <= 11){ coneMode = 3; } else if(numPoints <= 15){ coneMode = 4; }else{ coneMode = 5; }

        update(input, coneRect, coneMode == 1 ? oneConeWidth : (coneMode == 2 ? twoConeWidth : manyConeWidth));
    }


    private void update(Mat input, Rect rect, double realWidth){
        double junctionCenterX = getCenter(rect).x;
        double screenCenterX = input.width() / 2.0;
        double junctionCenterOffset = junctionCenterX - screenCenterX;
        double screenWidth = input.width();

        double screenJunctionWidth = rect.width;
        distanceToJunction = realWidth / distanceRatio(screenJunctionWidth, screenWidth);

        double realJunctionOffset = distanceToJunction * distanceRatio(junctionCenterOffset, screenWidth);
        angleToJunction = Math.toDegrees(Math.atan2(realJunctionOffset, distanceToJunction));
    }

    private double distanceRatio(double sizeOnScreen, double screenWidth){ return 2*Math.tan(Math.toRadians(cameraFov*(sizeOnScreen/screenWidth))/2.0); }

}