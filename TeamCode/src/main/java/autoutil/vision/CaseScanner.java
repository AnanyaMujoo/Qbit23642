package autoutil.vision;

import android.graphics.Color;

import org.checkerframework.checker.units.qual.C;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import elements.Case;
import util.template.Iterator;

import static global.General.bot;
import static global.General.cameraMonitorViewId;
import static global.General.log;
import static java.lang.Math.abs;
import static java.lang.Math.pow;


public class CaseScanner extends Scanner{
    private volatile Case caseDetected = Case.FIRST;
    protected final Case[] cases = new Case[]{Case.FIRST, Case.SECOND, Case.THIRD};
    protected final Case[] pastCases = new Case[10];
    { Arrays.fill(pastCases, Case.FIRST); }
    protected final Rect view = getRectFromCenter(center, height/2,width/2);

    // TODO TEST

    public int getCase(Mat input){
        Imgproc.resize(getSubmat(input, view), input, new Size(input.width(), input.height()));
        getHSV(input);
        double cyanValue = getCaseValue(input, 96, 3, CYAN);
        double magentaValue = getCaseValue(input, 168, 3, MAGENTA);
        double orangeValue = getCaseValue(input, 8, 3, ORANGE);
        return Iterator.maxIndex(cyanValue, magentaValue, orangeValue);
    }

    public double getCaseValue(Mat input, double hue, double hueOffset, Scalar color){
        Rect rect = scaleRectAroundCenter(getContourColor(input, hue, hueOffset,  color), 0.4);
        drawRectangle(input, rect, color);
        if(checkIfInside(HSV, rect)){ return getUniformity(HSV, rect, hue); }else{ return 0; }
    }

    public void message(){
        caseDetected = getCaseStable(getCase());
        log.show("Case Detected: ", caseDetected);
    }


    private Case getCaseStable(Case currentCase){
        boolean casesAreSame = true;
        for (int i = 0; i < pastCases.length-1; i++) { pastCases[i] = pastCases[i+1]; if(!pastCases[i].equals(currentCase)){ casesAreSame = false; } } pastCases[0] = currentCase;
        return casesAreSame ? currentCase : caseDetected;
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
