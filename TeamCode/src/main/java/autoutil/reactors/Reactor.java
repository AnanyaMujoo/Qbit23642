package autoutil.reactors;

import autoutil.controllers.control1D.Controller1D;
import autoutil.controllers.control2D.Controller2D;
import autoutil.generators.Generator;
import geometry.position.Pose;
import robot.RobotUser;

public abstract class Reactor implements RobotUser {

    protected Controller2D movementController;
    protected Controller1D headingController;

    public abstract void init();
    public abstract Pose getPose();
    public abstract void setTarget(Pose target);
    public abstract void nextTarget();
    public abstract boolean isAtTarget();
    public abstract void moveToTarget(Generator generator);

    protected void setControllers(Controller2D movementController, Controller1D headingController){
        this.movementController = movementController;
        this.headingController = headingController;
    }

}