package autoutil.controllers;

import autoutil.paths.PathSegment2;
import geometry.position.Pose;
import geometry.position.Vector2;

public class Default2D extends Controller2D{
    public Default2D(Controller1D xController, Controller1D yController) {
        super(xController, yController);
    }

    @Override
    public void update(Pose pose, PathSegment2 pathSegment) {
        xController.update(pose, pathSegment);
        yController.update(pose, pathSegment);
        Vector2 powerVector = new Vector2(xController.getOutput(), yController.getOutput());
        powerVector.rotate(pose.ang);
        setOutputX(powerVector.getX());
        setOutputY(powerVector.getY());
        isAtTarget =  xController.isAtTarget() && yController.isAtTarget();
    }

}
