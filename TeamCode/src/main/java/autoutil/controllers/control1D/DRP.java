package autoutil.controllers.control1D;

import com.sun.tools.javac.jvm.Gen;

import autoutil.generators.Generator;
import geometry.position.Pose;
import util.template.Precision;

public class DRP extends Controller1D{
    // Dynamic Rest Power

    private final double kp;
    private double restPower;
    private final Precision precision = new Precision();

    public DRP(double kp, double rp){ this.kp = kp; this.restPower = rp; }

    @Override
    protected double setDefaultAccuracy() { return 0.5; }

    @Override
    protected double setDefaultMinimumTimeReachedTarget() { return 0.3; }

    @Override
    protected double setDefaultRestOutput() { return 0.0; }

    @Override
    protected void updateController(Pose pose, Generator generator) {
        precision.throttle(() -> restPower *= isWithinAccuracyRange() ? 0.98 : 1.02, 25);
        setRestOutput(restPower);
    }

    @Override
    protected double setOutput() { return kp*getError(); }

    @Override
    protected boolean hasReachedTarget() { return isWithinAccuracyRange(); }
}
