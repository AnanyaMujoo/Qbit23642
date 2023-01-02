package autoutil.vision;

import android.annotation.SuppressLint;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;



public class CaseScannerBar extends CaseScanner {

    @SuppressLint("NewApi")
    @Override
    public int getCase(Mat input) {
        cropAndFill(input, getZoomedRect(input, 2));

        getGray(input);

        Imgproc.blur(Gray, Gray, new Size(2, 2));

        Core.inRange(Gray, new Scalar(0,0,0), new Scalar(50,50,50), Mask);

//        Mask.copyTo(input);

        Imgproc.Canny(Mask, Edges, 100, 250);

        Imgproc.blur(Edges, Edges, new Size(2, 2));

//        Edges.copyTo(input);

        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(Edges, contours, Hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

        Gray.release();
        Edges.release();
        Mask.release();

        ArrayList<Rect> rects = new ArrayList<>();
        double minAspect = 4;
        double minArea = 700;
        for (int i = 0; i < contours.size(); i++) {
            Rect rect = Imgproc.boundingRect(contours.get(i));
            if(getAspectRatio(rect) > minAspect && rect.area() > minArea && getCenter(rect).y < 100){
                rects.add(rect);
                drawRectangle(input, scaleRectAroundCenter(rect, 1.4), BLUE);
            }
        }
        drawRectangle(input, new Rect(100,60,10,10), GREEN);

        int num = rects.size();
        return num == 0 ? 1 : num > 2 ? 2 : num-1;
    }

}