package autoutil.vision;

import android.graphics.Color;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import java.util.Arrays;

import elements.Case;
import util.Timer;
import util.template.Iterator;

import static global.General.log;
import static java.lang.Math.abs;


public class CaseScanner extends Scanner{
    private volatile Case caseDetected = Case.THIRD;
    protected final Case[] cases = new Case[]{Case.FIRST, Case.SECOND, Case.THIRD};
    protected final Case[] pastCases = new Case[5];
    { Arrays.fill(pastCases, caseDetected); }

    public int getCase(Mat input){
        cropAndFill(input, getZoomedRect(input, 1.6, 30));
        getHSV(input);

        computeRects(80, 150);


        debug(input);


        Point center = getCenter(input);
        Point pictureCenter = new Point(center.x+10, center.y+30);
        drawRectangle(input, getRectFromCenter(pictureCenter, 100, 150), GREEN);

        double cyanValue = getBestRectStDev(input, 90, 110, CYAN);
        double magentaValue = getBestRectStDev(input, 140, 180, MAGENTA);
        double orangeValue = getBestRectStDev(input, 1, 60, ORANGE);
        return Iterator.minIndex(cyanValue*1.5, magentaValue*1.2, orangeValue);
    }

    @Override
    public void message(){
//        logDebug();
//        caseDetected = getCaseStable(getCase());
        log.show("Case Detected", caseDetected);
    }


    private Case getCaseStable(Case currentCase){
        boolean casesAreSame = true;
        for (int i = 0; i < pastCases.length-1; i++) {
            pastCases[i] = pastCases[i+1];
            if(!pastCases[i].equals(currentCase)){ casesAreSame = false; }
        }
        pastCases[pastCases.length-1] = currentCase;
        return casesAreSame ? currentCase : pastCases[0];
    }


    @Override
    public final void preProcess(Mat input) {
//        Core.rotate(input, input, Core.ROTATE_90_COUNTERCLOCKWISE);
    }

    @Override
    public final void postProcess(Mat input) {
//        Core.rotate(input, input, Core.ROTATE_90_CLOCKWISE);
    }

    @Override
    public final void run(Mat input) {
        caseDetected = getCaseStable(cases[getCase(input)]);
    }

    public final Case getCase(){ return caseDetected; }
}
