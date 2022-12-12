package autoutil;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import automodules.AutoModule;
import autoutil.generators.BreakpointGenerator;
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
    private ParameterCodeSeg<Reactor> reactorFunction = rea -> {};
    private ParameterCodeSeg<Generator> generatorFunction = gen -> {};
    private boolean skip = false;

    public AutoSegment(ReturnCodeSeg<R> r, ReturnCodeSeg<G> g){getReactor = r; getGenerator = g; }

    public ReturnCodeSeg<R> getReactorReference(){ return getReactor; }
    public ReturnCodeSeg<G> getGeneratorReference(){ return getGenerator; }

    public void setGeneratorFunction(ParameterCodeSeg<Generator> generatorFunction){ this.generatorFunction = generatorFunction; }
    public void setReactorFunction(ParameterCodeSeg<Reactor> reactorFunction){ this.reactorFunction = reactorFunction; }

    public void skip(){ if(!(getGenerator.run() instanceof BreakpointGenerator)) {skip = true; }}
    public void reset(){ skip = false; }

    public void run(AutoFramework auto){
        if(!skip) {
            Generator generator = getGenerator.run();
            generatorFunction.run(generator);
            Reactor reactor = getReactor.run();
            reactorFunction.run(reactor);
            Executor executor = new Executor(auto, generator, reactor);
            executor.followPath();
        }
    }

    public enum Type {
        PAUSE,
        SETPOINT,
        WAYPOINT,
        AUTOMODULE,
        CONCURRENT_AUTOMODULE,
        CANCEL_AUTOMODULE,
        CUSTOM,
        BREAKPOINT
    }

}
