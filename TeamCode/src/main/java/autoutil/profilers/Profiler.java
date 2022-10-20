package autoutil.profilers;

import java.util.ArrayList;

import geometry.framework.Point;
import math.calculus.Differentiator;
import math.calculus.Integrator;
import util.Timer;
import util.codeseg.ReturnCodeSeg;
import util.condition.Expectation;
import util.condition.Magnitude;
import util.template.Iterator;

import static global.General.fault;
public class Profiler {

    // TODO 4 MAKE get new delta values method


    private final ReturnCodeSeg<Double> processVariable;
    private final Integrator integrator;
    private final Differentiator differentiator;
    private final Timer timer = new Timer();
    private volatile ArrayList<Double> values = new ArrayList<>();
    private volatile ArrayList<Double> times = new ArrayList<>();
    private volatile int lastAccessedIndex = 0;

    public Profiler(ReturnCodeSeg<Double> processVariable){
        this.processVariable = processVariable;
        integrator = new Integrator();
        integrator.defineFunction(processVariable);
        differentiator = new Differentiator();
        differentiator.defineFunction(processVariable);
        values.add(0.0);
        times.add(0.0);
        timer.reset();
    }

    public void update(){
        values.add(processVariable.run());
        times.add(timer.seconds());
        updateIntegrator();
        updateDifferentiator();
    }

    private void updateIntegrator(){
        if(getUpdateNumber() == 1){
            integrator.integrate(getPreviousProcess(1), getCurrentProcess());
        }else{
            integrator.integrate(getPreviousProcess(2), getPreviousProcess(1), getCurrentProcess());
        }
    }
    private void updateDifferentiator(){
        if(getUpdateNumber() == 1){
            differentiator.differentiate(getPreviousProcess(1), getCurrentProcess());
        }else{
            differentiator.differentiate(getPreviousProcess(2), getPreviousProcess(1), getCurrentProcess());
        }
    }

    public void reset(){
        integrator.reset();
        values = new ArrayList<>();
        times = new ArrayList<>();
        values.add(0.0);
        times.add(0.0);
        timer.reset();
    }

    public void resetIntegral(){
        integrator.reset();
    }

    private int getUpdateNumber(){
        return values.size()-1;
    }

    public double getCurrentValue(){
        return getPreviousValue(0);
    }

    public double getCurrentTime(){
        return getPreviousTime(0);
    }

    private Point getCurrentProcess(){
        return getPreviousProcess(0);
    }

    private double getPreviousValue(int n){
        fault.check("You tried to get a value that doesn't exist", Expectation.EXPECTED, Magnitude.CRITICAL, (values.size()-n) < 0, false);
        return values.get(values.size()-n-1);
    }

    private double getPreviousTime(int n){
        fault.check("You tried to get a time that doesn't exist", Expectation.EXPECTED, Magnitude.CRITICAL, (times.size()-n) < 0, false);
        return times.get(times.size()-n-1);
    }

    private Point getPreviousProcess(int n){
        fault.check("You tried to get a time that doesn't exist", Expectation.EXPECTED, Magnitude.CRITICAL, (times.size()-n) < 0, false);
        return new Point(getPreviousTime(n), getPreviousValue(n));
    }

    public double getDerivative(){
        return differentiator.getDerivative();
    }

    public double getIntegral(){
        return integrator.getIntegral();
    }


    public ArrayList<Double> getNewValues(){
        ArrayList<Double> out = getLastValues(getUpdateNumber() - lastAccessedIndex);
        lastAccessedIndex = getUpdateNumber();
        return out;
    }


    private ArrayList<Double> getLastValues(int n){
        ArrayList<Double> out = new ArrayList<>();
        int s = values.size();
        for (int i = s-n; i < s; i++) {
            out.add(values.get(i));
        }
        return out;
    }

    public double getRunningAverage(int num){
        if(getUpdateNumber() > num){
            return Iterator.forAllAverage(getLastValues(num));
        }else{
            return Iterator.forAllAverage(values);
        }
    }

}
