package autoutil.vision;

import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;

public abstract class Scanner extends OpenCvPipeline {

    protected final Processor processor = new Processor();

    public static final int width = 320;
    public static final int height = 240;

    protected abstract void start();

    @Override
    public void init(Mat firstFrame){
        start();
        processor.define(firstFrame);
    }

    @Override
    public Mat processFrame(Mat input){
        processor.define(input);
        return processor.getOriginal();
    }
}