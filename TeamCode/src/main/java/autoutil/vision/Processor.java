package autoutil.vision;

import org.firstinspires.ftc.teamcode.R;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

import util.codeseg.ParameterCodeSeg;

public class Processor {
    public static final Scalar BLUE = new Scalar(0, 0, 255);
    public static final Scalar GREEN = new Scalar(0, 255, 0);
    public static final Scalar RED = new Scalar(255, 0, 0);

    private final Mat Input = new Mat();
    private final Mat Original = new Mat();

    private final ArrayList<ParameterCodeSeg<Mat>> definers = new ArrayList<>();


    public void defineAll(Mat input){
        Core.rotate(input, Input, Core.ROTATE_90_COUNTERCLOCKWISE);
        for(ParameterCodeSeg<Mat> definer: definers){
            definer.run(Input);
        }
    }

    public void addDefiner(ParameterCodeSeg<Mat> definer){
        definers.add(definer);
    }

    public void toYCrCb(Mat input, Mat output) {
        Imgproc.cvtColor(input, output, Imgproc.COLOR_RGB2YCrCb);
    }

    public void toCb(Mat YCrCb, Mat output){
        Core.extractChannel(YCrCb, output, 2);
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
