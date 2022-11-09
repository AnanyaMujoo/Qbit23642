package autoutil.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import elements.Case;
import util.template.Iterator;

import static global.General.cameraMonitorViewId;
import static global.General.log;
import static java.lang.Math.abs;
import static java.lang.Math.pow;


public class CaseScanner extends Scanner{
    private volatile Case caseDetected = Case.FIRST;
    protected final Case[] cases = new Case[]{Case.FIRST, Case.SECOND, Case.THIRD};
    protected Scalar caseScalar = new Scalar(0,0,0);

    public int getCase(Mat input){
        getHSV(input);
        double cyanHue = 96; double magentaHue = 168; double yellowHue = 8;
        Rect cyan = getContourColor(input, cyanHue,3,  CYAN);
        Rect magenta = getContourColor(input,  magentaHue,5,  MAGENTA);
        Rect yellow = getContourColor(input, yellowHue,3,  YELLOW);
//
//        Point center = new Point(magenta.x + magenta.width/2, magenta.y + magenta.height/2);
//
//        drawSquareFromCenter(input, center, 50, GREEN);
//        Rect box = getSquareFromCenter(center, 50);
//        if(0 <= box.x
//                && 0 <= box.width
//                && box.x + box.width <= HSV.width()
//                && 0 <= box.y
//                && 0 <= box.height
//                && box.y + box.height <= HSV.height()){
//            caseScalar = new Scalar(getAverageSquareFromCenter(HSV, center, 50).val[0] - magentaHue, 0, 0, 0);
//        }
//
//        Rect cropMagenta = new Rect(magenta.x + magenta.width/4, magenta.y + magenta.y/4, magenta.width/2, magenta.height/2);
//        drawRectangle(input, cropMagenta, RED);

//        caseScalar = getAverage(HSV, cropMagenta);

//
//        caseScalar = new Scalar(getAverage(HSV, cyan).val[0], getAverage(HSV, magenta).val[0], getAverage(HSV, yellow).val[0]);

//        log.show("cyanStdev", getUniformity(cyan, cyanHue));
//        log.show("magentaStdev", getUniformity(magenta, magentaHue));
//        log.show("yellowStdev", getUniformity(yellow, yellowHue));

//        double averageCyan = getAverage(HSV, cyan).val[0];
//        double averageMagenta = getAverage(HSV, magenta).val[0];
//        double averageYellow = getAverage(HSV, yellow).val[0];
//        double scaledCyan = cyan.area()/pow(abs(averageCyan-cyanHue),3);
//        double scaledMagenta = magenta.area()/pow(abs(averageMagenta-magentaHue),3);
//        double scaledYellow = yellow.area()/pow(abs(averageYellow-yellowHue),3);

        debug(input);

        // TODO TEST
//
//        double areaCyan = getContourColor(input, 100, CYAN).area(); //WORKS
//        double areaMagenta = getContourColor(input, 170, MAGENTA).area(); //WORKS
//        double areaYellow = getContourColor(input, 20, YELLOW).area();
//        return Iterator.maxIndex(areaCyan, areaMagenta, areaYellow);
//        return Iterator.maxIndex(scaledCyan, scaledMagenta, scaledYellow);
        return 0;
    }

    public void message(){
//        log.show(caseScalar);
        caseDetected = getCase();
        log.show("Case Detected: ", caseDetected);
        logDebug();
    }


    @Override
    public final void preProcess(Mat input) { Core.rotate(input, input, Core.ROTATE_90_COUNTERCLOCKWISE); }

    @Override
    public final void postProcess(Mat input) { Core.rotate(input, input, Core.ROTATE_90_CLOCKWISE); }

    @Override
    public final void start() {}

    @Override
    public final void run(Mat input) { caseDetected = cases[getCase(input)]; }

    public final Case getCase(){ return caseDetected; }
}
