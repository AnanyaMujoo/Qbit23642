package autoutil.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class JunctionScanner extends Scanner {

    @Override
    public void run(Mat input) {
        cropAndFill(input, getZoomedRect(input, 1.5));
        getHSV(input);

        Scalar lowHSV = new Scalar(20, 70, 80);
        Scalar highHSV = new Scalar(32, 255, 255);

        Core.inRange(HSV, lowHSV, highHSV, Thresh);
        Core.bitwise_and(HSV, HSV, Mask, Thresh);

        Scalar average = Core.mean(Mask, Thresh);

        Mask.convertTo(ScaledMask, -1, 150/average.val[1], 0);

        Scalar strictLowHSV = new Scalar(0, 150, 100);
        Scalar strictHighHSV = new Scalar(255, 255, 255);

        Core.inRange(ScaledMask, strictLowHSV, strictHighHSV, ScaledThresh);

        Core.bitwise_and(input, input, Output, ScaledThresh);

        Imgproc.Canny(Output, Edges, 100, 200);

        ArrayList<MatOfPoint> contours = new ArrayList<>();

        Imgproc.findContours(Edges, contours, Hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f[] contoursPoly = new MatOfPoint2f[contours.size()];

        RotatedRect[] rects = new RotatedRect[contours.size()];

        for (int i = 0; i < contours.size(); i++) {
            contoursPoly[i] = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 5, true);
        }




    }


    @Override
    public void start() {}

    @Override
    public final void preProcess(Mat input) { Core.rotate(input, input, Core.ROTATE_90_COUNTERCLOCKWISE); }

    @Override
    public final void postProcess(Mat input) { Core.rotate(input, input, Core.ROTATE_90_CLOCKWISE); }
}
