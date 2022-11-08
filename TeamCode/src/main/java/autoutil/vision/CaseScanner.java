package autoutil.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import elements.Case;
import util.template.Iterator;

import static global.General.log;
import static java.lang.Math.abs;


public class CaseScanner extends Scanner{
    private volatile Case caseDetected = Case.FIRST;
    protected final Case[] cases = new Case[]{Case.FIRST, Case.SECOND, Case.THIRD};

    public int getCase(Mat input){
//        double cyanHue = 100; double magentaHue = 170; double yellowHue = 20;
//        Rect cyan = getContourColor(input, cyanHue,3,  CYAN);
//        Rect magenta = getContourColor(input,  170,3,  MAGENTA);
//        Rect yellow = getContourColor(input, 20,3,  YELLOW);
//        double averageCyan = getAverage(HSV, cyan).val[0];
//        double averageMagenta = getAverage(HSV, magenta).val[0];
//        double averageYellow = getAverage(HSV, yellow).val[0];
//        double scaledCyan = cyan.area()/abs(averageCyan-cyanHue);
//        double scaledMagenta = magenta.area()/abs(averageMagenta-magentaHue);
//        double scaledYellow = yellow.area()/abs(averageYellow-yellowHue);

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
//        caseDetected = getCase();
//        log.show("Case Detected: ", caseDetected);
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
