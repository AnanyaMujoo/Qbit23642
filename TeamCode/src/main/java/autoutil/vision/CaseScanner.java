package autoutil.vision;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import java.util.Arrays;

import elements.CaseOld;

import static autoutil.vision.Processor.BLUE;
import static autoutil.vision.Processor.GREEN;

public abstract class CaseScanner extends Scanner{
    private volatile CaseOld caseOldDetected;

    protected final Mat YCrCb = new Mat();
    protected final Mat Cb = new Mat();

    protected final CaseOld[] cases = new CaseOld[]{CaseOld.LEFT, CaseOld.CENTER, CaseOld.RIGHT};
    protected Rect[] rects;

    protected abstract Rect[] defineRegions();
    protected abstract CaseOld detectCase();

    protected Rect[] defaultRegionGenerator(Point center, int size, int offset){
        Rect rectLeft = processor.getRectFromCenter(new Point(center.x-offset, center.y), size);
        Rect rectCenter = processor.getRectFromCenter(center, size);
        Rect rectRight = processor.getRectFromCenter(new Point(center.x+offset, center.y), size);
        return new Rect[]{rectLeft, rectCenter, rectRight};
    }

    @Override
    protected void start() {
        rects = defineRegions();

        processor.addDefiner(input -> {
            processor.toYCrCb(input, YCrCb);
            processor.toCb(YCrCb, Cb);

            caseOldDetected = detectCase();

            processor.drawRectangle(input, rects[0], BLUE);
            processor.drawRectangle(input, rects[1], BLUE);
            processor.drawRectangle(input, rects[2], BLUE);

            processor.drawFilledRectangle(input, rects[Arrays.asList(cases).indexOf(caseOldDetected)], GREEN);
        });
    }

    public CaseOld getCase(){
        return caseOldDetected;
    }
}
