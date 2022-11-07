package autoutil.vision;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import elements.CaseOld;

import static autoutil.vision.Processor.BLUE;
import static autoutil.vision.Processor.GREEN;
import static autoutil.vision.Processor.RED;

public abstract class CaseScanner extends Scanner{
    private volatile CaseOld caseOldDetected;

    protected final Mat YCrCb = new Mat();
    protected final Mat Cb = new Mat();

    protected final CaseOld[] cases = new CaseOld[]{CaseOld.LEFT, CaseOld.CENTER, CaseOld.RIGHT};
    protected Rect[] rects;

    protected abstract Rect[] defineRegions();
    protected abstract CaseOld detectCase();

    public static int n = 0;

    protected Rect[] defaultRegionGenerator(Point center, int size, int offset){
        Rect rectLeft = processor.getRectFromCenter(new Point(center.x-offset, center.y), size);
        Rect rectCenter = processor.getRectFromCenter(center, size);
        Rect rectRight = processor.getRectFromCenter(new Point(center.x+offset, center.y), size);
        return new Rect[]{rectLeft, rectCenter, rectRight};
    }

    @Override
    protected void start() {
//        rects = defineRegions();

        processor.setDefiner(input -> {
            double areaCyan = processor.getContourColor(input, 100, BLUE).area(); //WORKS
            double areaYellow = processor.getContourColor(input, 20, GREEN).area();
            double areaMagenta = processor.getContourColor(input, 170, RED).area(); //WORKS
            n = getIndexMax(areaCyan, areaMagenta, areaYellow);



//            List<MatOfPoint> contours = processor.getContours(input);
//            Rect maxRect = processor.getLargestContour(input);

//            processor.toYCrCb(input, YCrCb);
//            processor.toCb(YCrCb, Cb);
//
//            caseOldDetected = detectCase();
//
//            processor.drawRectangle(input, rects[0], BLUE);
//            processor.drawRectangle(input, rects[1], BLUE);
//            processor.drawRectangle(input, rects[2], BLUE);
//
//            processor.drawFilledRectangle(input, rects[Arrays.asList(cases).indexOf(caseOldDetected)], GREEN);
        });
    }


    public int getIndexMax(double... arr){
        int ind = 0;
        double max = arr[0];
        for (int i = 0; i < arr.length ; i++) {
            if(arr[i] > max){max = arr[i]; ind = i;}
        }
        return ind;
    }

    public CaseOld getCase(){
        return caseOldDetected;
    }
}
