package autoutil;

import autoutil.generators.Generator;
import autoutil.generators.LineGenerator;
import autoutil.generators.PoseGenerator;
import autoutil.reactors.MecanumPIDReactor;
import autoutil.reactors.MecanumPurePursuitReactor;
import autoutil.reactors.Reactor;
import autoutil.vision.Scanner;
import util.codeseg.ReturnCodeSeg;
import util.template.ParameterConstructor;

public interface AutoUser {


    ReturnCodeSeg<LineGenerator> lineGenerator = generator(LineGenerator.class);
    ReturnCodeSeg<PoseGenerator> poseGenerator = generator(PoseGenerator.class);

    ReturnCodeSeg<MecanumPIDReactor> mecanumPIDReactor = reactor(MecanumPIDReactor.class);
    ReturnCodeSeg<MecanumPurePursuitReactor> mecanumPurePursuitReactor = reactor(MecanumPurePursuitReactor.class);

    AutoSegment<?, ?> mecanumDefaultSetpoint = new AutoSegment<>(mecanumPIDReactor, poseGenerator);
    AutoSegment<?, ?> mecanumDefaultWayPoint = new AutoSegment<>(mecanumPurePursuitReactor, lineGenerator);

    AutoConfig mecanumDefaultConfig = new AutoConfig(mecanumDefaultSetpoint, mecanumDefaultWayPoint);


    static <T extends Generator> ReturnCodeSeg<T> generator(Class<T> type){ return ParameterConstructor.getNewInstance(type); }
    static <T extends Reactor> ReturnCodeSeg<T> reactor(Class<T> type){ return ParameterConstructor.getNewInstance(type); }
}
