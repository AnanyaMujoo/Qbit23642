package autoutil.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import elements.Case;
import util.codeseg.ParameterCodeSeg;

import static autoutil.vision.Processor.BLUE;
import static autoutil.vision.Processor.GREEN;

public abstract class CaseScanner extends Scanner{
    private volatile Case caseDetected;

    protected final Mat YCrCb = new Mat();
    protected final Mat Cb = new Mat();

    protected final Case[] cases = new Case[]{Case.LEFT, Case.CENTER, Case.RIGHT};
    protected Rect[] rects;

    protected abstract Rect[] defineRegions();
    protected abstract Case detectCase();

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

            caseDetected = detectCase();

            processor.drawRectangle(input, rects[0], BLUE);
            processor.drawRectangle(input, rects[1], BLUE);
            processor.drawRectangle(input, rects[2], BLUE);

            processor.drawFilledRectangle(input, rects[Arrays.asList(cases).indexOf(caseDetected)], GREEN);
        });
    }

    public Case getCase(){
        return caseDetected;
    }
}
