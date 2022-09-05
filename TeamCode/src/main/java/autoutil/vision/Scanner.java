package autoutil.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;

import org.opencv.core.Point;

import java.util.Arrays;
import java.util.Collections;

import elements.Case;

import static autoutil.vision.Processor.BLUE;
import static autoutil.vision.Processor.GREEN;

public abstract class Scanner extends OpenCvPipeline {

    protected final Processor processor = new Processor();

    protected final int width = 320;
    protected final int height = 240;

    protected abstract void start();

    @Override
    public void init(Mat firstFrame){
        start();
        processor.defineAll(firstFrame);
    }

    @Override
    public Mat processFrame(Mat input){
        processor.defineAll(input);
        return processor.getOriginal();
    }
}