package autoutil.vision;

import static global.General.log;
import static global.Modes.Height.HIGH;
import static global.Modes.Height.LOW;
import static global.Modes.Height.MIDDLE;
import static global.Modes.heightMode;
import static org.opencv.core.Core.inRange;
import static org.opencv.core.Core.min;


import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import elements.FieldSide;
import elements.GameItems;
import elements.Robot;
import geometry.position.Pose;
import geometry.position.Vector;
import math.trigonmetry.Trig;
import util.template.Iterator;
import util.template.Precision;


public class JunctionScannerAll extends Scanner {

    public final double minContourArea = 250.0;

    private static final Pose defaultTarget = new Pose(0,17.0, -1);

    public final int horizonPole = 90;
    public final int horizonCone = 20;

    private Rect coneRect = new Rect();
    private Rect poleRect = new Rect();

    private final List<MatOfPoint> coneContours = new ArrayList<>();
    private final ArrayList<MatOfPoint> poleContours = new ArrayList<>();

    private final Scalar redLow = new Scalar(0, 161, 60);
    private final Scalar redHigh = new Scalar(255, 255, 255);

    private final Scalar blueLow = new Scalar(0, 80, 138);
    private final Scalar blueHigh = new Scalar(255, 255, 255);

    public Scalar poleLower = new Scalar(59, 134, 40);
    public Scalar poleHigher = new Scalar(180, 180, 105);

    private final Mat maskCone = new Mat();
    private final Mat yCrCb = new Mat();
    private final Mat Cb = new Mat();
    private final Mat Cr = new Mat();
    private final Mat binaryMat = new Mat();
    private final Mat Mask3 = new Mat();

    private final Size blurSize = new Size(2,2);

    private final Mat Mask2 = new Mat();
    private final Mat S = new Mat();

    private final Size kSize = new Size(8, 8);
    private final Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, kSize);

    private static final double cameraFov = 47;
    private static final double realPoleWidth = 2.672; // cm
    private static final double oneConeWidth = 7.0; // cm
    private static final double twoConeWidth = 7.8; // cm
    private static final double manyConeWidth = 10.5; // cm bigger means closer
    public static double distanceToJunction = 0, angleToJunction = 0, rollOfJunction = 0;

    // TOD4 SAVE ODOMETRY AND USE THAT

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
            crop(input, new Rect(0, horizonPole, input.width(), input.height() - horizonPole));
            if(detectPole(Crop)){ return Crop; }
//            crop(input, new Rect(0, horizonCone, input.width(), input.height() - horizonCone));
//            if(detectCone(Crop)){ return Crop; }
            return Crop;
        }
        return input;
    }

    private static boolean cutoff(){
        return !getPose().within(new Pose(), 2, 40);
//        return getPose().within(getTarget(), 20, 40) && !getPose().within(new Pose(), 2, 40);
    }

    public static Pose getTarget(){
        double robotCenterToDetection = defaultTarget.getY()-2;
        double robotAngle = -(rollOfJunction) * (heightMode.modeIs(HIGH) ? GameItems.Junction.highHeight : (heightMode.modeIs(MIDDLE) ? GameItems.Junction.middleHeight : GameItems.Junction.lowHeight)) / robotCenterToDetection;
        double yOffset = (Trig.cos(robotAngle) - 1) * (robotCenterToDetection + Robot.halfLength);
        return defaultTarget.getAdded(new Pose(0, targetCorrection()+yOffset,robotAngle)).getCopy();
    }

    public static Pose getPose(){ Vector distanceVector = new Vector(0, distanceToJunction); distanceVector.rotate(angleToJunction); return new Pose(distanceVector.getX(), distanceVector.getY(), angleToJunction); }

    private boolean detectPole(Mat input) {
        boolean exitEarly = true;

        Imgproc.cvtColor(input, yCrCb, Imgproc.COLOR_RGB2YCrCb);

        Core.inRange(yCrCb, new Scalar(20,130,0), new Scalar(255,255,120), Mask);

//        Mask.copyTo(binaryMat);

        Core.bitwise_and(input, input, Mask2, Mask);

        Imgproc.cvtColor(Mask2, HSV, Imgproc.COLOR_RGB2HSV);

        Core.inRange(HSV, new Scalar(20,150,0), new Scalar(30,255,255), binaryMat);

        Imgproc.blur(binaryMat, binaryMat, blurSize);

//        binaryMat.copyTo(input);

        Imgproc.findContours(binaryMat, poleContours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f polePoly = new MatOfPoint2f();

//        Imgproc.drawContours(input, contours, -1, CONTOUR_COLOR);

        if(!poleContours.isEmpty()){ poleContours.removeIf(contour -> getAspectRatio(Imgproc.boundingRect(contour)) < 1.5);}
//
        if(!poleContours.isEmpty()) {

            MatOfPoint biggestPole = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                biggestPole = Collections.max(poleContours, Comparator.comparingDouble(t0 -> Imgproc.boundingRect(t0).height));
            }
            if(biggestPole != null) {
                if (Imgproc.contourArea(biggestPole) > minContourArea) {
                    poleRect = Imgproc.boundingRect(biggestPole);
                    Imgproc.approxPolyDP(new MatOfPoint2f(biggestPole.toArray()), polePoly, 5, true);
                    ArrayList<Point> points = new ArrayList<>(polePoly.toList());

                    ArrayList<geometry.framework.Point> points1 = new ArrayList<>();

                    RotatedRect rot1 = Imgproc.minAreaRect(new MatOfPoint2f(points.toArray(new Point[]{})));

                    double bottomHorizon = Precision.clip(input.height()/2.0 + Math.min(rot1.size.width,rot1.size.height)*0.5, 0, input.height());

                    /**
                     * Bottom Horizon
                     */

//                    Imgproc.line(input, new Point(0, bottomHorizon), new Point(input.width(), bottomHorizon), RED);

                    if(!points.isEmpty()){ points.removeIf(p -> p.y > 20 && p.y < bottomHorizon); }

//
//                    if(points.size() >= 4){
//                        ArrayList<Point> points2 = new ArrayList<>(points);
//                        points2.removeIf(p -> p.y > 20);
//                        ArrayList<Point> points3 = new ArrayList<>(points);
//                        points3.removeIf(p -> p.y < bottomHorizon);
//
//                        if(points2.size() >= 2 && points3.size() >= 2) {
//                            Collections.sort(points2, Comparator.comparingDouble(p -> p.x));
//                            Collections.sort(points3, Comparator.comparingDouble(p -> p.x));
//                            Point point1 = points2.get(0);
//                            Point point2 = points3.get(0);
//                            Point point3 = points3.get(points3.size()-1);
//                            Point point4 = points2.get(points2.size() - 1);
//
//                            points = new ArrayList<>(Arrays.asList(point1, point2, point3, point4));
//                        }
//                    }

                    if(points.size() >= 4) {

                        /**
                         * Contour
                         */

//                        drawPoly(input, new MatOfPoint2f(points.toArray(new Point[]{})));

                        RotatedRect rot = Imgproc.minAreaRect(new MatOfPoint2f(points.toArray(new Point[]{})));
                        double height = Math.max(rot.size.width, rot.size.height);
                        double width = Math.min(rot.size.width, rot.size.height);


                        if(height > input.height()*0.4) {
                            /**
                             * Rotated Rect
                             */

//                            drawRotatedRect(input, rot, BLUE);

//                        for(Point p : points){
//                            geometry.framework.Point point = new geometry.framework.Point(p);
//                            points1.add(point);
//                        }
//                        Vector v1 = new Vector(points1.get(0), points1.get(1));
//                        Vector v3 = new Vector(points1.get(3), points1.get(2));

                            poleRect = new Rect(poleRect.x, poleRect.y, (int) width, (int) height);

                            /**
                             * Rect
                             */
//                        drawRectangle(input, poleRect, RED);


                            if (getCenter(poleRect).x > input.width() * 0.05 && getCenter(poleRect).x < input.width() * 0.95) {
                                rollOfJunction = rot.angle > 45 ? rot.angle - 90.0 : rot.angle;
//                            rollOfJunction = ((v1.getTheta() + v3.getTheta())/2.0)-90;
                                rollOfJunction = Precision.clip(rollOfJunction, 10);
                            }
                        }
                    }

                    exitEarly = false;
//                    Imgproc.rectangle(input, poleRect, RED, 2);
//                    Imgproc.putText(input, "Pole", new Point(poleRect.x, poleRect.y < 10 ? (poleRect.y + poleRect.height + 20) : (poleRect.y - 8)), Imgproc.FONT_HERSHEY_SIMPLEX, 0.8, RED, 2);
                }
            }
        }

        poleContours.clear();

        yCrCb.release();
        Mask.release();
        Mask2.release();
        HSV.release();
        binaryMat.release();

        if(exitEarly){ reset(); return false; }

        update(input, poleRect, realPoleWidth);

        return cutoff();
    }
    public void reset(){
        distanceToJunction = 1000; angleToJunction = 1000;
    }

    public static double targetCorrection(){
        if(heightMode != null) { return heightMode.modeIs(MIDDLE) ? -1 : (heightMode.modeIs(LOW) ? -2 : 0); }
        return 0;
    }


    private boolean detectCone(Mat input) {

        boolean exitEarly = true;

        Imgproc.cvtColor(input, yCrCb, Imgproc.COLOR_RGB2YCrCb);

        FieldSide.on(() -> inRange(yCrCb, new Scalar(20, 0, 134), new Scalar(255, 120, 255), Mask), () -> inRange(yCrCb, redLow, redHigh, Mask));

//        Mask.copyTo(binaryMat);

        Core.bitwise_and(input, input, Mask2, Mask);

        Imgproc.cvtColor(Mask2, HSV, Imgproc.COLOR_RGB2HSV);

        Core.inRange(HSV, new Scalar(110,100,0), new Scalar(120,255,255), binaryMat);

        Imgproc.blur(binaryMat, binaryMat, blurSize);

//        binaryMat.copyTo(input);

        Imgproc.findContours(binaryMat, coneContours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

//        Imgproc.drawContours(input, blueContours, -1, CONTOUR_COLOR);

        MatOfPoint2f conePoly = new MatOfPoint2f();

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

                    Imgproc.approxPolyDP(new MatOfPoint2f(biggestConeContour.toArray()), conePoly, coneRect.width/16.0, true);

                    ArrayList<Point> points = new ArrayList<>(conePoly.toList());

//                    drawPoly(input, new MatOfPoint2f(points.toArray(new Point[]{})));


                    /**
                     * Top Horizon
                     */
                    double topHorizon = Precision.clip((input.height() / 2.0) - coneRect.width * 0.5, 0, input.height());

//                    Imgproc.line(input, new Point(0, topHorizon), new Point(input.width(), topHorizon), GREEN);


                    /**
                     * Largest Y Delta
                     */
                    ArrayList<Double> yDeltas = new ArrayList<>();
                    Iterator.forAllPairs(points, (p1, p2) -> {yDeltas.add(Math.abs(p1.y - p2.y));});
                    double maxDeltaY = Iterator.forAllCompareMax(yDeltas, y -> y);

                    coneMode = coneRect.y > topHorizon ? 1 : ((coneRect.y < 5 && maxDeltaY > input.height()-5) ? 2 : 3);



//
//                    if(points.size() >= 4) {
//                        ArrayList<Point> points2 = new ArrayList<>(points);
////                        points2.removeIf(p -> p.y > coneRect.y + 10);
//                        double lowerBound =  coneRect.y + coneRect.height - 40;
//                        double upperBound = coneRect.y + coneRect.height - 5;
//                        points2.removeIf(p -> p.y < lowerBound-50 || p.y > lowerBound);
//                        ArrayList<Point> points3 = new ArrayList<>(points);
////                        points3.removeIf(p -> p.y < coneRect.y + coneRect.height - 20);
//                        points3.removeIf(p -> p.y < upperBound);
//
//                        if (points2.size() >= 2 && points3.size() >= 2) {
//                            Collections.sort(points2, Comparator.comparingDouble(p -> p.x));
//                            Collections.sort(points3, Comparator.comparingDouble(p -> p.x));
//                            Point point1 = points2.get(0);
//                            Point point2 = points3.get(0);
//                            Point point3 = points3.get(points3.size() - 1);
//                            Point point4 = points2.get(points2.size() - 1);
//
//                            points = new ArrayList<>(Arrays.asList(point1, point2, point3, point4));
//
//                            drawPoly(input, new MatOfPoint2f(points.toArray(new Point[]{})));
//
//
//                            double minWidth = Math.abs(point1.x - point4.x);
//                            double maxWidth = Math.abs(point2.x - point3.x);
//
//
//                            /**
//                             * Top Horizon
//                             */
//                            double topHorizon = Precision.clip((input.height() / 2.0) - coneRect.width * 0.5, 0, input.height());
//
////                            Imgproc.line(input, new Point(0, topHorizon), new Point(input.width(), topHorizon), GREEN);
//
//
//                            /**
//                             * Ratio
//                             */
//
//                            double ratio = maxWidth / minWidth;
//
////                            log.record("Ratio", ratio);
//
//                            coneMode = coneRect.y > topHorizon ? 1 : (ratio < 1.65 ? 2 : 3);
//                        }
//                    }




                    exitEarly = false;

//                    Imgproc.rectangle(input, coneRect, RED, 2);
//                    Imgproc.putText(input, "Blue Cone", new Point(coneRect.x, coneRect.y < 10 ? (coneRect.y + coneRect.height + 20) : (coneRect.y - 8)), Imgproc.FONT_HERSHEY_SIMPLEX, 0.8, RED, 1);
                }
            }
        }

        coneContours.clear();

        yCrCb.release();
        Mask.release();
        Mask2.release();
        HSV.release();
        binaryMat.release();

        if(exitEarly){ reset(); return false;  }


        update(input, coneRect, coneMode == 1 ? oneConeWidth : (coneMode == 2 ? twoConeWidth : manyConeWidth));

        return cutoff();
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


    private void drawPoly(Mat input, MatOfPoint2f poly){
        MatOfPoint approxPoly = new MatOfPoint();
        poly.convertTo(approxPoly, CvType.CV_32S);
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        contours.add(approxPoly);
        Imgproc.drawContours(input, contours, -1, GREEN);
    }
}