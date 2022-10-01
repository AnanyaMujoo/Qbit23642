package autoutil.reactors;

import autoutil.controllers.control1D.Controller1D;
import autoutil.controllers.control2D.Controller2D;
import autoutil.paths.PathSegment;
import geometry.position.Pose;

public abstract class Reactor {

    protected Controller2D movementController;
    protected Controller1D headingController;
    protected PathSegment pathSegment;

    public abstract void init();
    public abstract Pose getPose();
    public abstract void setTarget(double[] target);
    public abstract void nextTarget();
    public abstract boolean isAtTarget();
    public abstract void moveToTarget();

    protected void setControllers(Controller2D movementController, Controller1D headingController){
        this.movementController = movementController;
        this.headingController = headingController;
    }

    public void setPathSegment(PathSegment pathSegment){
        this.pathSegment = pathSegment;
    }

}