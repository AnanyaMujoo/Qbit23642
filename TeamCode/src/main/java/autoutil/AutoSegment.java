package autoutil;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import autoutil.generators.Generator;
import autoutil.reactors.Reactor;

public class AutoSegment<R extends Reactor, G extends Generator> {
    private final R reactor;
    private final G generator;

    public AutoSegment(R r, G g){ reactor = r; generator = g; }

    public Reactor getReactor(){
        return reactor;
    }

    public Generator getGenerator(){
        return generator;
    }

    public void run(LinearOpMode opMode){
        Executor executor = new Executor(opMode, generator, reactor);
//        if(isIndependent){ executor.makeIndependent(); } // TOD 5
        executor.followPath();
    }

}
