package autoutil.vision;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.codeseg.ParameterCodeSeg;

import static global.General.log;

public class Processor {
    public static final Scalar BLUE = new Scalar(0, 0, 255);
    public static final Scalar GREEN = new Scalar(0, 255, 0);
    public static final Scalar RED = new Scalar(255, 0, 0);

    private final Mat Input = new Mat();
    private final Mat Original = new Mat();
    private final Mat Gray = new Mat();
    private final Mat Hierarchy = new Mat();
    private final Mat Mask = new Mat();
    protected final Mat HSV = new Mat();

    public static String val = "";

    private ParameterCodeSeg<Mat> definer = mat -> {};


    public void define(Mat input){
        Core.rotate(input, Input, Core.ROTATE_90_COUNTERCLOCKWISE);
        definer.run(Input);
    }

    public void setDefiner(ParameterCodeSeg<Mat> definer){ this.definer = definer; }

    public void toYCrCb(Mat input, Mat output) { Imgproc.cvtColor(input, output, Imgproc.COLOR_RGB2YCrCb); }

    public void toCb(Mat YCrCb, Mat output){
        Core.extractChannel(YCrCb, output, 2);
    }

    public void toHSV(Mat input, Mat output){
        Imgproc.cvtColor(input, output, Imgproc.COLOR_RGB2HSV);
    }

//    public List<MatOfPoint> getContours(Mat input){
//        List<MatOfPoint> out = new ArrayList<>();
//        Imgproc.cvtColor(input, Gray, Imgproc.COLOR_RGB2GRAY);
//        Imgproc.blur(Gray, Gray, new Size(3, 3));
//        double thresh = 80;
//        Imgproc.Canny(Gray, Gray, thresh, thresh*3);
//        Imgproc.findContours(Gray, out, Hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
////        Imgproc.drawContours(input, out, -1,  BLUE,2,   Imgproc.LINE_8, Hierarchy, 2) ;
//        return out;
//    }

    private Rect getLargestContour(List<MatOfPoint> contours){
        Rect maxRect = new Rect(); double maxArea = 0;
        for (MatOfPoint c : contours) {
            Rect rect = Imgproc.boundingRect(c); double newArea = rect.area();
            if(newArea > maxArea){ maxRect = rect; maxArea = newArea; }
            c.release();
        }
        return maxRect;
    }

    public Rect getContourColor(Mat input, double hue, Scalar color){
        toHSV(input, HSV);
        Core.inRange(HSV, new Scalar(hue-3,20,10), new Scalar(hue+3,255,255), Mask);
        Imgproc.blur(Mask, Mask, new Size(8, 8));
        List<MatOfPoint> out = new ArrayList<>();
        Imgproc.findContours(Mask, out, Hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Rect maxRect = getLargestContour(out);
        drawRectangle(input, maxRect, color);
        return maxRect;
    }

    public Mat getSubmat(Mat input, Rect region){
        return input.submat(region);
    }

    public int getAverage(Mat mat){
        return ((int) Core.mean(mat).val[0]);
    }

    public void drawRectangle(Mat input, Rect rect, Scalar color){
        Imgproc.rectangle(input, rect, color, 2);
    }

    public void drawFilledRectangle(Mat input, Rect rect, Scalar color){
        Imgproc.rectangle(input, rect, color, -1);
    }

    public Mat getOriginal(){
        Core.rotate(Input, Original, Core.ROTATE_90_CLOCKWISE);
        return Original;
    }

    public Rect getRectFromCenter(Point center, int size){
        int halfSize = size/2;
        return new Rect((int) center.x-halfSize, (int) center.y-halfSize, size, size);
    }
}
