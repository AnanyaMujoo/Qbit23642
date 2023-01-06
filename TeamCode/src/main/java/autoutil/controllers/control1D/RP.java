package autoutil.controllers.control1D;

import autoutil.generators.Generator;
import geometry.position.Pose;

public class RP extends Controller1D {

    private final double kp;

    public RP(double kp, double restPower){ setRestOutput(restPower); this.kp = kp; }

    @Override
    protected double setDefaultAccuracy() { return 0.25; }

    @Override
    protected double setDefaultMinimumTimeReachedTarget() { return 0.01; }

    @Override
    protected double setDefaultRestOutput() { return 0; }

    @Override
    protected void updateController(Pose pose, Generator generator) {}

    @Override
    protected double setOutput() { return kp * getError(); }

    @Override
    protected boolean hasReachedTarget() { return isWithinAccuracyRange(); }
}
