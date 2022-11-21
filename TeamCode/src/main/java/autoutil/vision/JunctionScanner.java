package autoutil.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class JunctionScanner extends Scanner {

    @Override
    public void run(Mat input) {
        cropAndFill(input, getZoomedRect(input, 1.5));
        getHSV(input);


    }


    @Override
    public void start() {}

    @Override
    public final void preProcess(Mat input) { Core.rotate(input, input, Core.ROTATE_90_COUNTERCLOCKWISE); }

    @Override
    public final void postProcess(Mat input) { Core.rotate(input, input, Core.ROTATE_90_CLOCKWISE); }
}
