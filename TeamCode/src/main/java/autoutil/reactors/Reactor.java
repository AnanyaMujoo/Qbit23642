package autoutil.reactors;

import automodules.stage.Exit;
import automodules.stage.Main;
import automodules.stage.Stop;
import autoutil.controllers.control1D.Controller1D;
import autoutil.controllers.control2D.Controller2D;
import autoutil.generators.Generator;
import autoutil.generators.PoseGenerator;
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
    public abstract void moveToTarget(PoseGenerator generator);

    public final Main mainTarget(PoseGenerator generator){return new Main(() -> moveToTarget(generator)); }
    public final Exit exitTarget(){ return new Exit(this::isAtTarget); }
    public final Stop stopTarget(){ return new Stop(this::nextTarget); }


    protected void setControllers(Controller2D movementController, Controller1D headingController){
        this.movementController = movementController;
        this.headingController = headingController;
    }

}