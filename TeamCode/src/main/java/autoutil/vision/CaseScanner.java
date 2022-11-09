package autoutil.vision;

import android.graphics.Color;

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


    // TODO TEST
    public int getCase(Mat input){
        getHSV(input);
        double cyanValue = getCaseValue(input, 96, 3, CYAN);
        double magentaValue = getCaseValue(input, 168, 5, MAGENTA);
        double orangeValue = getCaseValue(input, 8, 3, ORANGE);
        caseScalar = new Scalar(cyanValue, magentaValue, orangeValue);
        return Iterator.maxIndex(cyanValue, magentaValue, orangeValue);
    }

    public double getCaseValue(Mat input, double hue, double hueOffset, Scalar color){
        Rect rect = scaleRectAroundCenter(getContourColor(input, hue,hueOffset,  CYAN), 0.5);
        drawRectangle(input, rect, color);
        if(checkIfInside(HSV, rect)){ return getUniformity(HSV, rect, hue); }else{ return 0; }
    }

    public void message(){
        log.show(caseScalar);
        caseDetected = getCase();
        log.show("Case Detected: ", caseDetected);
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
