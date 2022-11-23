package autoutil.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;

import util.template.Iterator;

import static global.General.log;

public class JunctionScanner extends Scanner {

    private RotatedRect maxRect = new RotatedRect();
    public double distanceToJunction, angleToJunction = 0;
    private final double cameraFov = 47;
    private final double realJunctionWidth = 2.672; // cm

    @Override
    public void run(Mat input) {
//        cropAndFill(input, getZoomedRect(input, 1.5));
        getHSV(input);

        Scalar lowHSV = new Scalar(18, 50, 50);
        Scalar highHSV = new Scalar(32, 255, 255);

        Core.inRange(HSV, lowHSV, highHSV, Thresh);
        Core.bitwise_and(HSV, HSV, Mask, Thresh);

        Scalar average = Core.mean(Mask, Thresh);

        Mask.convertTo(ScaledMask, -1, 150/average.val[1], 0);

        Scalar strictLowHSV = new Scalar(0, 80, 80);
        Scalar strictHighHSV = new Scalar(255, 255, 255);

        Core.inRange(ScaledMask, strictLowHSV, strictHighHSV, ScaledThresh);

//        Core.bitwise_and(HSV, HSV, Output, ScaledThresh);
//
        Imgproc.blur(ScaledThresh, ScaledThresh, new Size(2, 2));

//
//        ScaledThresh.copyTo(input);
//
//        Imgproc.Canny(Output, Edges, 100, 200);

        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(ScaledThresh, contours, Hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        MatOfPoint2f[] contoursPoly = new MatOfPoint2f[contours.size()];
        RotatedRect[] rects = new RotatedRect[contours.size()];
        for (int i = 0; i < contours.size(); i++) {
            contoursPoly[i] = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 5, true);
            rects[i] = Imgproc.minAreaRect(contoursPoly[i]);
            drawRotatedRect(input, rects[i], ORANGE);
        }

        ArrayList<RotatedRect> rectsList = new ArrayList<>(Arrays.asList(rects));
        if(rectsList.size() > 0) {
            maxRect = Iterator.forAllCompareMax(rectsList, rect -> rect.boundingRect().area());
            drawRotatedRect(input, maxRect, RED);
        }

        double junctionCenterX = maxRect.center.x;
        double screenCenterX = input.width()/2.0;
        double junctionCenterOffset = junctionCenterX-screenCenterX;
        double screenWidth = input.width();


        double screenJunctionWidth = Math.min(maxRect.size.height, maxRect.size.width);
        distanceToJunction = realJunctionWidth/distanceRatio(screenJunctionWidth, screenWidth);

        double realJunctionOffset = distanceToJunction*distanceRatio(junctionCenterOffset, screenWidth);
        angleToJunction = Math.toDegrees(Math.atan2(realJunctionOffset, distanceToJunction));




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

    private double distanceRatio(double sizeOnScreen, double screenWidth){ return 2*Math.tan(Math.toRadians(cameraFov*(sizeOnScreen/screenWidth))/2.0); }


    @Override
    public void message() {
        log.show("Angle: ", maxRect.angle);
        log.show("Width:", maxRect.size.width);
        log.show("Height:", maxRect.size.height);
        log.show("Camera FOV: ", cameraFov);
        log.show("Junction Width: ", realJunctionWidth);
        log.show("DistanceToJunction: ", distanceToJunction);
        log.show("AngleToJunction: ", angleToJunction);
    }

    @Override
    public final void preProcess(Mat input) { Core.rotate(input, input, Core.ROTATE_90_COUNTERCLOCKWISE); }

    @Override
    public final void postProcess(Mat input) { Core.rotate(input, input, Core.ROTATE_90_CLOCKWISE); }

}
