package autoutil;

import autoutil.generators.Generator;
import autoutil.generators.LineGenerator;
import autoutil.generators.PoseGenerator;
import autoutil.reactors.MecanumJunctionReactor;
import autoutil.reactors.MecanumJunctionReactor2;
import autoutil.reactors.MecanumNonstopReactor;
import autoutil.reactors.MecanumPIDReactor;
import autoutil.reactors.MecanumPurePursuitReactor;
import autoutil.reactors.Reactor;
import autoutil.vision.Scanner;
import util.codeseg.ReturnCodeSeg;
import util.template.ParameterConstructor;

public interface AutoUser {


    // TOD5 Fix naming conventions (drop mecanum, setpoint -> SP)

    ReturnCodeSeg<LineGenerator> lineGenerator = generator(LineGenerator.class);
    ReturnCodeSeg<PoseGenerator> poseGenerator = generator(PoseGenerator.class);

    ReturnCodeSeg<MecanumPIDReactor> mecanumPIDReactor = reactor(MecanumPIDReactor.class);
    ReturnCodeSeg<MecanumPurePursuitReactor> mecanumPurePursuitReactor = reactor(MecanumPurePursuitReactor.class);
    ReturnCodeSeg<MecanumJunctionReactor> mecanumJunctionReactor = reactor(MecanumJunctionReactor.class);
    ReturnCodeSeg<MecanumJunctionReactor2> mecanumJunctionReactor2 = reactor(MecanumJunctionReactor2.class);
    ReturnCodeSeg<MecanumNonstopReactor> mecanumNonstopReactor = reactor(MecanumNonstopReactor.class);
    ReturnCodeSeg<MecanumNonstopReactor.MecanumNonstopReactorSetpoint> mecanumNonstopReactorSetpoint = reactor(MecanumNonstopReactor.MecanumNonstopReactorSetpoint.class);


    AutoSegment<?, ?> mecanumDefaultSetpoint = new AutoSegment<>(mecanumPIDReactor, poseGenerator);
    AutoSegment<?, ?> mecanumJunctionSetpoint = new AutoSegment<>(mecanumJunctionReactor, poseGenerator);
    AutoSegment<?, ?> mecanumJunctionSetpoint2 = new AutoSegment<>(mecanumJunctionReactor2, poseGenerator);
    AutoSegment<?, ?> mecanumDefaultWayPoint = new AutoSegment<>(mecanumPurePursuitReactor, lineGenerator);
    AutoSegment<?, ?> mecanumNonstopWayPoint = new AutoSegment<>(mecanumNonstopReactor, lineGenerator);
    AutoSegment<?, ?> mecanumNonstopSetPoint = new AutoSegment<>(mecanumNonstopReactorSetpoint, lineGenerator);

    AutoConfig mecanumDefaultConfig = new AutoConfig(mecanumDefaultSetpoint, mecanumDefaultWayPoint);
    AutoConfig mecanumNonstopConfig = new AutoConfig(mecanumNonstopSetPoint, mecanumNonstopWayPoint);



    static <T extends Generator> ReturnCodeSeg<T> generator(Class<T> type){ return ParameterConstructor.getNewInstance(type); }
    static <T extends Reactor> ReturnCodeSeg<T> reactor(Class<T> type){ return ParameterConstructor.getNewInstance(type); }
}
