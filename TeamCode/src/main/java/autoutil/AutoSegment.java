package autoutil;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import automodules.AutoModule;
import autoutil.generators.Generator;
import autoutil.reactors.Reactor;
import geometry.position.Pose;
import util.codeseg.ParameterCodeSeg;
import util.codeseg.ReturnCodeSeg;
import util.condition.Expectation;
import util.condition.Magnitude;

import static global.General.fault;

public class AutoSegment<R extends Reactor, G extends Generator> {

    private final ReturnCodeSeg<R> getReactor;
    private final ReturnCodeSeg<G> getGenerator;
    private ParameterCodeSeg<Generator> generatorFunction = gen -> {};

    public AutoSegment(ReturnCodeSeg<R> r, ReturnCodeSeg<G> g){getReactor = r; getGenerator = g; }

    public ReturnCodeSeg<R> getReactorReference(){ return getReactor; }
    public ReturnCodeSeg<G> getGeneratorReference(){ return getGenerator; }

    public void setGeneratorFunction(ParameterCodeSeg<Generator> generatorFunction){ this.generatorFunction = generatorFunction; }

    public void run(LinearOpMode opMode){
        Generator generator = getGenerator.run();
        generatorFunction.run(generator);
        Executor executor = new Executor(opMode, generator, getReactor.run());
//        if(isIndependent){ executor.makeIndependent(); } // TOD 5
        executor.followPath();
    }

    public enum Type {
        PAUSE,
        SETPOINT,
        WAYPOINT,
        AUTOMODULE,
        CONCURRENT_AUTOMODULE,
        CANCEL_AUTOMODULE;

        private double time;
        private AutoModule autoModule;

        public Type set(double time){ this.time = time; return this; }
        public Type set(AutoModule autoModule){ this.autoModule = autoModule; return this; }

        public double getTime(){ return time; }
        public AutoModule getAutoModule(){ return autoModule; }
    }

}
