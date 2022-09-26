package autoutil.executors;

import static global.General.bot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import autoutil.generators.PoseGenerator;
import autoutil.reactors.mecanum.MecanumPIDReactor;
import geometry.position.Pose;
import util.codeseg.ReturnCodeSeg;

public class MecanumExecutorArcsPID extends Executor {

    LinearOpMode opMode;
    ReturnCodeSeg<Boolean> active;

    public MecanumExecutorArcsPID(LinearOpMode opMode) {
        super();
        this.opMode = opMode;
    }

    public MecanumExecutorArcsPID(ReturnCodeSeg<Boolean> active) {
        super();
        this.active = active;
    }

    @Override
    public void move(double f, double t) {
        bot.mecanumDrive.move(f, 0, t);
    }

    @Override
    public void moveSetpoint(Pose nextPose) {
        PoseGenerator generator = new PoseGenerator();
        MecanumExecutor executor;
        if (opMode == null) {
            executor = new MecanumExecutor(active);
        } else {
            executor = new MecanumExecutor(opMode);
        }

        double ang = -nextPose.ang; // Make CW Positive
        ang -= Math.PI/2; // Make +y 0
        ang *= 180/Math.PI; // Make it degrees

        generator.add(nextPose.p.x, nextPose.p.y, ang);

        executor.setPath(generator.getPath());
        executor.setReactor(new MecanumPIDReactor());

        executor.followPath();
    }
}
