package autoutil.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import util.codeseg.ParameterCodeSeg;
import util.template.Iterator;

public abstract class Scanner extends OpenCvPipeline {


    public static final int width = 320;
    public static final int height = 240;

    public static final Point center = new Point(width/2.0, height/2.0);

    public static final Scalar BLUE = new Scalar(0, 0, 255);
    public static final Scalar GREEN = new Scalar(0, 255, 0);
    public static final Scalar RED = new Scalar(255, 0, 0);
    public static final Scalar YELLOW = new Scalar(255, 255, 0);
    public static final Scalar CYAN = new Scalar(0,255,255);
    public static final Scalar MAGENTA = new Scalar(255,0,255);

    private final Mat Hierarchy = new Mat();
    private final Mat Mask = new Mat();
    protected final Mat HSV = new Mat();

    public abstract void start();
    public abstract void run(Mat input);
    public abstract void preProcess(Mat input);
    public abstract void postProcess(Mat input);

    @Override
    public final void init(Mat firstFrame){ start(); }

    @Override
    public final Mat processFrame(Mat input){
        preProcess(input); run(input); postProcess(input);
        return input;
    }

    private void toYCrCb(Mat input, Mat output) { Imgproc.cvtColor(input, output, Imgproc.COLOR_RGB2YCrCb); }
    private void toCb(Mat YCrCb, Mat output){ Core.extractChannel(YCrCb, output, 2); }
    private void getHSV(Mat input){ Imgproc.cvtColor(input, HSV, Imgproc.COLOR_RGB2HSV); }

    private Rect getLargestContour(List<MatOfPoint> contours){
        ArrayList<Rect> rects = new ArrayList<>();
        Iterator.forAll(contours, c -> {rects.add(Imgproc.boundingRect(c)); c.release();});
        return rects.stream().max(Comparator.comparing(Rect::area)).get();
    }

    public Rect getContourColor(Mat input, double hue, double hueOffset, Scalar color){
        getHSV(input);
        Core.inRange(HSV, new Scalar(hue-hueOffset,10,10), new Scalar(hue+hueOffset,255,255), Mask);
        Imgproc.blur(Mask, Mask, new Size(8, 8));
        List<MatOfPoint> out = new ArrayList<>();
        Imgproc.findContours(Mask, out, Hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Rect maxRect = getLargestContour(out);
        drawRectangle(input, maxRect, color);
        return maxRect;
    }

    protected Rect[] defaultSquareCaseGenerator(Point center, int size, int offset){
        Rect rectLeft = getSquareFromCenter(new Point(center.x-offset, center.y), size);
        Rect rectCenter = getSquareFromCenter(center, size);
        Rect rectRight = getSquareFromCenter(new Point(center.x+offset, center.y), size);
        return new Rect[]{rectLeft, rectCenter, rectRight};
    }

    public Mat getSubmat(Mat input, Rect region){ return input.submat(region); }
    public Scalar getAverage(Mat mat){ return Core.mean(mat); }
    public Scalar getAverage(Mat input, Rect region){ return getAverage(getSubmat(input, region));}

    public void drawRectangle(Mat input, Rect rect, Scalar color){ Imgproc.rectangle(input, rect, color, 2); }
    public void drawFilledRectangle(Mat input, Rect rect, Scalar color){ Imgproc.rectangle(input, rect, color, -1); }
    public Rect getSquareFromCenter(Point center, int size){ int halfSize = size/2; return new Rect((int) center.x-halfSize, (int) center.y-halfSize, size, size); }


//    protected Rect[] defineRegions() {
//        return defaultRegionGenerator(new Point(width/2.0,(height/2.0)+0.0), 60, 130);
//    }
//
//    @Override
//    protected CaseOld detectCase() {
//        List<Integer> averages = Arrays.asList(
//                processor.getAverage(processor.getSubmat(Cb, rects[0])),
//                processor.getAverage(processor.getSubmat(Cb, rects[1])),
//                processor.getAverage(processor.getSubmat(Cb, rects[2]))
//        );
//        int minIndex = averages.indexOf(Collections.min(averages));
//        return cases[minIndex];
//    }

}