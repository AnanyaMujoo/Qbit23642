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

        Imgproc.adaptiveThreshold(Gray, Mask, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 131, 50);

        Mask.copyTo(input);

        Imgproc.Canny(Mask, Edges, 10, 20);

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
            if(getAspectRatio(rect) > minAspect && rect.area() > minArea && getCenter(rect).y > 120){
                rects.add(rect);
//                drawRectangle(input, scaleRectAroundCenter(rect, 1.4), BLUE);
            }
        }

        int num = rects.size();
        return num == 0 ? 1 : num > 2 ? 2 : num-1;


        /**  (0,0)
         *  ^
         *  |
         *  y
         *  |
         *  v            (320, 120)
         *    < - x - >
         */
    }

}