package autoutil.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

import autoutil.AutoFramework;
import elements.FieldSide;
import util.codeseg.ParameterCodeSeg;

import static global.General.fieldSide;
import static global.General.gamepad1;
import static global.General.log;
import static java.lang.Math.abs;

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
    public static final Scalar ORANGE = new Scalar(255,165,0);

    protected final Mat Hierarchy = new Mat();
    protected final Mat Mask = new Mat();
    protected final Mat HSV = new Mat();
    protected final Mat Crop = new Mat();
    protected final Mat Thresh = new Mat();
    protected final Mat ScaledMask = new Mat();
    protected final Mat ScaledThresh = new Mat();
    protected final Mat Output = new Mat();
    protected final Mat Edges = new Mat();
    protected final Mat Gray = new Mat();

    protected boolean isStarted = false;

    public void start() { isStarted = true; }
    public abstract void run(Mat input);
    public abstract void preProcess(Mat input);
    public abstract void postProcess(Mat input);
    public abstract void message();

    public final void log(){ if(isStarted) { message(); }else{ log.show("Vision Starting..."); } }

    private final Point debugCenter = center;
    private double debugSize = 10;
    private Scalar debugColor = new Scalar(0,0,0);

    private final ArrayList<Rect> rects = new ArrayList<>();
    private final ArrayList<Double> stdevs = new ArrayList<>();
    private final ArrayList<Scalar> values = new ArrayList<>();

    @Override
    public final void init(Mat firstFrame){ start(); }

    @Override
    public Mat processFrame(Mat input){
        preProcess(input); run(input); postProcess(input);
        return input;
    }

    private void toYCrCb(Mat input, Mat output) { Imgproc.cvtColor(input, output, Imgproc.COLOR_RGB2YCrCb); }
    private void toCb(Mat YCrCb, Mat output){ Core.extractChannel(YCrCb, output, 2); }
    protected void getHSV(Mat input){ Imgproc.cvtColor(input, HSV, Imgproc.COLOR_RGB2HSV); }
    protected void getGray(Mat input){ Imgproc.cvtColor(input, Gray, Imgproc.COLOR_RGB2GRAY);}

    private Rect getLargestContour(List<MatOfPoint> contours){
        Rect maxRect = new Rect();
        for (MatOfPoint c: contours) { Rect newRect = Imgproc.boundingRect(c); if(newRect.area() > maxRect.area()){ maxRect = newRect; } c.release(); }
        return maxRect;
    }

    private Rect getLargestUniformContour(Mat input, List<MatOfPoint> contours, double hue, double uniformityCutoff){
        Rect maxRect = new Rect();
        for (MatOfPoint c: contours) { Rect newRect = Imgproc.boundingRect(c); if(newRect.area() > maxRect.area() && getStDev(input, newRect) < uniformityCutoff){ maxRect = newRect; } c.release(); }
        return maxRect;
    }

    protected double getUniformity(Mat input, Rect rect, double hue){
        return 1 - (abs(getAverage(input, rect).val[0] - hue)/180);  // On a scale of 0 - 1
    }


    protected void loopThroughRects(Mat input, int width, int height, ParameterCodeSeg<Rect> code){
        for (int i = 0; i < input.width() - width; i += 30) {
            for (int j = 0; j < input.height() - height; j += 30) {
                Rect newRect = new Rect(i, j, width, height);
                code.run(newRect);
            }
        }
    }

    protected void computeRects(int width, int height){
        rects.clear();
        stdevs.clear();
        values.clear();
        loopThroughRects(HSV, width, height, rect -> {
            rects.add(rect);
            stdevs.add(getStDev(HSV, rect));
            values.add(getAverage(HSV, scaleRectAroundCenter(rect, 0.4)));
        });
    }

    // TOD 5 make more efficient / clean
    protected double getBestRectStDev(Mat input, double hueLow, double hueHigh, Scalar rectColor){
        Rect bestRect = null; double bestST = 10000;
        for (int i = 0; i < rects.size(); i++) {
            double newSt = stdevs.get(i);
            double hue = values.get(i).val[0];
            double sat = values.get(i).val[1];
            if(newSt < bestST && hue > hueLow && hue < hueHigh && sat > 60){
                bestST = newSt; bestRect = rects.get(i);
            }
        }
        if(bestRect != null){
            Point center = getCenter(input);
            Point pictureCenter = new Point(center.x + (AutoFramework.isFlipped() ? -40 : 10), center.y+30);
            if(getCenter(bestRect).inside(getRectFromCenter(pictureCenter, 100, 150))) {
                drawRectangle(input, bestRect, rectColor);
            }else{ bestST = 10000; }
        }
        return bestST;
    }

    protected double getStDev(Mat input, Rect rect){
        Mat submat = getSubmat(input, rect); double mean = getAverage(submat).val[0]; double sum = 0; int num = 0;
        for (int i = 0; i < submat.width(); i+=5) {
            for (int j = 0; j < submat.height(); j+=5) {
                sum += Math.pow(submat.get(j,i)[0] - mean, 2); num++;
            }
        }
        return Math.sqrt(sum/num);
    }

    public Rect getContourColor(Mat input, double hue, double hueOffset, Scalar color){
        Core.inRange(HSV, new Scalar(hue-hueOffset, 60, 10), new Scalar(hue+hueOffset, 255, 255), Mask);
        Imgproc.blur(Mask, Mask, new Size(8, 8));
        List<MatOfPoint> out = new ArrayList<>();
        Imgproc.findContours(Mask, out, Hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
        Mask.copyTo(input);
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
    public double getAverageHue(Mat mat, Rect region){ return getAverage(mat, region).val[0]; }
    public Scalar getAverage(Mat input, Rect region){ return getAverage(getSubmat(input, region));}
    public Scalar getAverageSquareFromCenter(Mat input, Point center, int size){ return getAverage(input, getSquareFromCenter(center, size)); }

    public void drawRotatedRect(Mat input, RotatedRect rect, Scalar color){ Point[] vertices = new Point[4]; rect.points(vertices); for (int j = 0; j < 4; j++){ Imgproc.line(input, vertices[j], vertices[(j+1)%4], color, 2); } }
    public void drawRectangle(Mat input, Rect rect, Scalar color){ Imgproc.rectangle(input, rect, color, 2); }
    public void drawSquareFromCenter(Mat input, Point center, int size, Scalar color){ drawRectangle(input, getSquareFromCenter(center, size), color);}
    public void drawFilledRectangle(Mat input, Rect rect, Scalar color){ Imgproc.rectangle(input, rect, color, -1); }
    public Rect getSquareFromCenter(Point center, int size){ int halfSize = size/2; return new Rect((int) center.x-halfSize, (int) center.y-halfSize, size, size); }
    public Rect getRectFromCenter(Point center, int width, int height){ return new Rect((int) (center.x-width/2), (int) (center.y - height/2), width, height); }

    public void debug(Mat input){
        int shift = 1; int scale = 2;
        debugCenter.x += (gamepad1.dpad_left ? -shift : 0) + (gamepad1.dpad_right ? shift : 0);
        debugCenter.y += (gamepad1.dpad_down ? -shift : 0) + (gamepad1.dpad_up ? shift : 0);
        debugSize += (gamepad1.left_bumper ? -scale : 0) + (gamepad1.right_bumper ? scale : 0);
        drawSquareFromCenter(input, debugCenter, (int) Math.abs(debugSize), GREEN);
        debugColor = getAverageSquareFromCenter(HSV, debugCenter, (int) Math.abs(debugSize));
    }

    public void logDebug() {
        log.show("Debug Center", debugCenter);
        log.show("Debug Size", (int) Math.abs(debugSize));
        log.show("Debug Color", debugColor);
    }


    public Point getCenter(Rect rect){
        return new Point(rect.x + rect.width/2.0, rect.y + rect.height/2.0);
    }

    public Point getCenter(Mat mat){
        return new Point(mat.width()/2.0, mat.height()/2.0);
    }


    public boolean checkIfInside(Mat input, Rect region){
        return 0 <= region.x && 0 <= region.width && region.x + region.width <= input.width()  && 0 <= region.y  && 0 <= region.height && region.y + region.height <= input.height();
    }


    public Rect scaleRectAroundCenter(Rect rect, double scale){
        Point center = getCenter(rect); int width = ((int)(rect.width*scale)); int height = ((int)(rect.height*scale));
        return new Rect((int) (center.x - width/2), (int) (center.y - height/2), width, height);
    }

    public Rect getZoomedRect(Mat input, double zoom, double offset){ return getRectFromCenter(new Point(input.width()/2.0, (input.height()/2.0) + offset), (int) (input.width()/zoom), (int) (input.height()/zoom)); }


    public void cropAndFill(Mat input, Rect rect){
        Imgproc.resize(getSubmat(input, rect), Crop,  new Size(input.width(), input.height()), Imgproc.INTER_CUBIC);
        Crop.copyTo(input);
    }

    public void crop(Mat input, Rect rect){
        Imgproc.resize(getSubmat(input, rect), Crop,  new Size(input.width(), input.height()), Imgproc.INTER_CUBIC);
    }

    public double getAspectRatio(Rect rect){
        if(rect.area() != 0) {
            return (double) (rect.height) / rect.width;
        }else{
            return 0;
        }
    }

}