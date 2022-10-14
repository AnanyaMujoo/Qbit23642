package autoutil.controllers.control1D;

import autoutil.paths.PathSegment;
import geometry.position.Pose;

public class BangBang extends Controller1D {
    private final double power;

    public BangBang(double power){ this.power = power; }

    @Override
    protected double setDefaultAccuracy() { return 1; }

    @Override
    protected double setDefaultMinimumTimeReachedTarget() { return 0.5; }

    @Override
    protected double setDefaultRestOutput() { return power; }

    @Override
    protected void updateController(Pose pose, PathSegment pathSegment) {}

    @Override
    protected double setOutput() { return 0; }

    @Override
    protected boolean hasReachedTarget() { return true; }
}
