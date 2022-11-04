package autoutil;

import autoutil.generators.AutoModuleGenerator;
import autoutil.generators.LineGenerator;
import autoutil.generators.PoseGenerator;
import autoutil.reactors.MecanumPIDReactor;
import autoutil.reactors.MecanumPurePursuitReactor;
import autoutil.vision.TeamElementScanner;
import geometry.position.Pose;

public interface AutoUser {

    LineGenerator lineGenerator = new LineGenerator();
    PoseGenerator poseGenerator = new PoseGenerator();

    MecanumPIDReactor mecanumPIDReactor = new MecanumPIDReactor();
    MecanumPurePursuitReactor mecanumPurePursuitReactor = new MecanumPurePursuitReactor();

    TeamElementScanner teamElementScanner = new TeamElementScanner();

    AutoSegment<MecanumPIDReactor, PoseGenerator> mecanumDefaultSetpoint = new AutoSegment<>(mecanumPIDReactor, poseGenerator);
    AutoSegment<MecanumPurePursuitReactor, LineGenerator> mecanumDefaultWayPoint = new AutoSegment<>(mecanumPurePursuitReactor, lineGenerator);

    AutoConfig mecanumDefaultConfig = new AutoConfig(mecanumDefaultSetpoint, mecanumDefaultWayPoint, teamElementScanner);

}
