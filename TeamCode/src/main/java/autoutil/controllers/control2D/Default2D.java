package autoutil.controllers.control2D;

import autoutil.controllers.control1D.Controller1D;
import geometry.position.Pose;
import geometry.position.Vector;

public class Default2D extends Controller2D{
    public Default2D(Controller1D xController, Controller1D yController) {
        super(xController, yController);
    }

    @Override
    protected void updateController(Pose pose, PathSegment pathSegment) {
        xController.update(pose, pathSegment);
        yController.update(pose, pathSegment);
        Vector powerVector = new Vector(xController.getOutput(), yController.getOutput());
        powerVector.rotate(pose.getAngle());
        setOutputX(powerVector.getX());
        setOutputY(powerVector.getY());
    }

    @Override
    protected boolean hasReachedTarget() {
        return xController.isAtTarget() && yController.isAtTarget();
    }

}
