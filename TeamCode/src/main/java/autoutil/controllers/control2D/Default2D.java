package autoutil.controllers.control2D;

import autoutil.controllers.control1D.Controller1D;
import autoutil.paths.PathSegment;
import geometry.position.Pose;
import geometry.position.Vector2;

public class Default2D extends Controller2D{
    public Default2D(Controller1D xController, Controller1D yController) {
        super(xController, yController);
    }

    @Override
    protected void updateController(Pose pose, PathSegment pathSegment) {
        xController.update(pose, pathSegment);
        yController.update(pose, pathSegment);
        Vector2 powerVector = new Vector2(xController.getOutput(), yController.getOutput());
        powerVector.rotate(pose.getAngle());
        setOutputX(powerVector.getX());
        setOutputY(powerVector.getY());
        isAtTarget =  xController.isAtTarget() && yController.isAtTarget();
    }

}
