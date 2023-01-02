package autoutil.vision;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.Math.min;


public class CaseScannerBar extends CaseScanner {

    @Override
    public int getCase(Mat input) {
        cropAndFill(input, getZoomedRect(input, 2));

        getGray(input);

        Imgproc.blur(Gray, Gray, new Size(2, 2));

        Imgproc.Canny(Gray, Edges, 100, 250);

        Imgproc.blur(Edges, Edges, new Size(2, 2));

        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(Edges, contours, Hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        Gray.release();
        Edges.release();

        ArrayList<Rect> rects = new ArrayList<>();
        double minAspect = 4;
        double minArea = 700;
        for (int i = 0; i < contours.size(); i++) {
            Rect rect = Imgproc.boundingRect(contours.get(i));
            if(getAspectRatio(rect) > minAspect && rect.area() > minArea){
                rects.add(rect);
//                drawRectangle(input, scaleRectAroundCenter(rect, 1.4), BLUE);
            }
        }
        int num = rects.size();
        return num == 0 ? 1 : num > 2 ? 2 : num-1;
    }

}