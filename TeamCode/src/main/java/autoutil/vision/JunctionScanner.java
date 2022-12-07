package autoutil.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;

import autoutil.Profiler;
import geometry.position.Pose;
import geometry.position.Vector;
import util.template.Iterator;

import static global.General.log;
import static global.General.sync;

public class JunctionScanner extends Scanner {

    private RotatedRect maxRect = new RotatedRect();
    public double distanceToJunction, angleToJunction = 0;
    private final double cameraFov = 47;
    private final double realJunctionWidth = 2.672; // cm
    private final Profiler distanceProfiler = new Profiler(() -> distanceToJunction);
    private final Profiler angleProfiler = new Profiler(() -> angleToJunction);
    private static boolean pausing = true;

    public static void pause(){ pausing = true; }
    public static void resume(){ pausing = false; }

    @Override
    public void start() {
        super.start();
        distanceProfiler.reset();
        angleProfiler.reset();
    }

    @Override
    public void run(Mat input) {
        if(!pausing) {
//        cropAndFill(input, getZoomedRect(input, 1.5));
            getHSV(input);

            Scalar lowHSV = new Scalar(16, 150, 50);
            Scalar highHSV = new Scalar(35, 255, 255);

            Core.inRange(HSV, lowHSV, highHSV, Thresh);

            Core.bitwise_and(HSV, HSV, Mask, Thresh);

            Imgproc.blur(Mask, Mask, new Size(3, 3));

//        Mask.copyTo(input);

            Imgproc.Canny(Mask, Edges, 200, 350);

//        Edges.copyTo(input);


//
//
//        Scalar average = Core.mean(Mask, Thresh);
//
//        Mask.convertTo(ScaledMask, -1, 150/average.val[1], 0);
//
//        Scalar strictLowHSV = new Scalar(0, 0, 0);
////        Scalar strictLowHSV = new Scalar(0, 80, 80);
//        Scalar strictHighHSV = new Scalar(255, 255, 255);
//
//        Core.inRange(ScaledMask, strictLowHSV, strictHighHSV, ScaledThresh);
//
//        Core.bitwise_and(HSV, HSV, Output, ScaledThresh);
//
//        Imgproc.blur(Output, Output, new Size(2, 2));
//
//        Output.copyTo(input);
//
//        Imgproc.Canny(Output, Edges, 100, 200);

//        Edges.copyTo(input);


            ArrayList<MatOfPoint> contours = new ArrayList<>();
            Imgproc.findContours(Edges, contours, Hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

            MatOfPoint2f[] contoursPoly = new MatOfPoint2f[contours.size()];
            RotatedRect[] rects = new RotatedRect[contours.size()];
            ArrayList<RotatedRect> rectsList = new ArrayList<>();
            double minAspect = 1.3;
            for (int i = 0; i < contours.size(); i++) {
                contoursPoly[i] = new MatOfPoint2f();
                Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 5, true);
                rects[i] = Imgproc.minAreaRect(contoursPoly[i]);
                double width = Math.min(rects[i].size.height, rects[i].size.width);
                double height = Math.max(rects[i].size.height, rects[i].size.width);
                double aspect = height / width;
                if (aspect > minAspect) {
//                drawRotatedRect(input, rects[i], ORANGE);
                    rectsList.add(rects[i]);
                }
            }

            if (rectsList.size() > 0) {
                maxRect = Iterator.forAllCompareMax(rectsList, rect -> rect.boundingRect().area());
//            drawRectangle(input, scaleRectAroundCenter(maxRect.boundingRect(), 1.4), BLUE);
//            drawRotatedRect(input, maxRect, RED);
            }


            double junctionCenterX = maxRect.center.x;
            double screenCenterX = input.width() / 2.0;
            double junctionCenterOffset = junctionCenterX - screenCenterX;
            double screenWidth = input.width();


            double screenJunctionWidth = Math.min(maxRect.size.height, maxRect.size.width);
            distanceToJunction = realJunctionWidth / distanceRatio(screenJunctionWidth, screenWidth);

            double realJunctionOffset = distanceToJunction * distanceRatio(junctionCenterOffset, screenWidth);
            angleToJunction = Math.toDegrees(Math.atan2(realJunctionOffset, distanceToJunction));

            distanceProfiler.update();
            angleProfiler.update();


            // 2 tan (angular size / 2) = w / d


            HSV.release();
            Mask.release();
            Thresh.release();
            ScaledMask.release();
            ScaledThresh.release();
            Output.release();
            Edges.release();
            Hierarchy.release();
        }
    }

    public Pose getPose(){
        int n = 3;
        double distance = distanceToJunction;
        if(distanceProfiler.areLastValuesNearby(n, 2)){
            distance = distanceProfiler.getRunningAverage(n);
        }
        double angle = angleToJunction;
        if(angleProfiler.areLastValuesNearby(n, 5)){
            angle = angleProfiler.getRunningAverage(n);
        }
        Vector distanceVector = new Vector(0, distance);
        distanceVector.rotate(angle);
        return new Pose(distanceVector.getX(), distanceVector.getY(), angle);
    }

    private double distanceRatio(double sizeOnScreen, double screenWidth){ return 2*Math.tan(Math.toRadians(cameraFov*(sizeOnScreen/screenWidth))/2.0); }


    @Override
    public void message() {
        log.show("Pose", getPose());
//        log.show("Angle: ", maxRect.angle);
//        log.show("Width:", maxRect.size.width);
//        log.show("Height:", maxRect.size.height);
//        log.show("Camera FOV: ", cameraFov);
//        log.show("Junction Width: ", realJunctionWidth);
//        log.show("DistanceToJunction: ", distanceToJunction);
//        log.show("AngleToJunction: ", angleToJunction);
    }

    @Override
    public final void preProcess(Mat input) { Core.rotate(input, input, Core.ROTATE_90_COUNTERCLOCKWISE); }

    @Override
    public final void postProcess(Mat input) { Core.rotate(input, input, Core.ROTATE_90_CLOCKWISE); }

}
