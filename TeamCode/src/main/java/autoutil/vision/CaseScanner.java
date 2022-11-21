package autoutil.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.Arrays;

import elements.Case;
import util.Timer;
import util.template.Iterator;

import static global.General.log;
import static java.lang.Math.abs;


public class CaseScanner extends Scanner{
    private volatile Case caseDetected = Case.THIRD;
    protected final Case[] cases = new Case[]{Case.FIRST, Case.SECOND, Case.THIRD};
    protected final Case[] pastCases = new Case[10];
    protected boolean isStarted = false;
    { Arrays.fill(pastCases, caseDetected); }

    public int getCase(Mat input){
        cropAndFill(input, getZoomedRect(input, 2));
        getHSV(input);

        computeRects(80, 150);

//        debug(input);

        double cyanValue = getBestRectStDev(input, 90, 110, CYAN);
        double magentaValue = getBestRectStDev(input, 130, 170, MAGENTA);
        double orangeValue = getBestRectStDev(input, 1, 30, ORANGE);
        return Iterator.minIndex(cyanValue, magentaValue, orangeValue);
        // TODO 4 NEW Program more vision for pole
    }

    public void message(){
        if(isStarted) {
            caseDetected = getCaseStable(getCase());
            log.show("Case Detected: ", caseDetected);
//            logDebug();
        }else{
            log.show("Vision Starting...");
        }
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
    public final void start() { isStarted = true; } // timer.reset(); }

    @Override
    public final void run(Mat input) { caseDetected = cases[getCase(input)]; }

    public final Case getCase(){ return caseDetected; }
}
