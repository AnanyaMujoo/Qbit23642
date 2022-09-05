package autoutil.reactors;

import java.util.ArrayList;
import java.util.Arrays;

import autoutil.controllers.Controller1D;
import autoutil.controllers.Controller2D;
import autoutil.paths.PathSegment2;
import geometry.position.Pose;

public abstract class Reactor {

    protected Controller2D movementController;
    protected Controller1D headingController;
    protected PathSegment2 pathSegment;

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

    public void setPathSegment(PathSegment2 pathSegment){
        this.pathSegment = pathSegment;
    }

}