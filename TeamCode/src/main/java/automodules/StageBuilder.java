package automodules;

import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import automodules.stage.Stop;
import robotparts.electronics.positional.PMotor;
import util.User;
import util.codeseg.CodeSeg;
import util.codeseg.ReturnCodeSeg;

import static global.General.bot;
import static global.General.mainUser;

public class StageBuilder {
    /**
     * Exit based on time
     * @param s
     * @return exit
     */
    public static Exit exitTime(double s){return new Exit(() -> bot.rfsHandler.timer.seconds() > s);}

    /**
     * Exit always
     * @return exit
     */
    public static Exit exitAlways(){return new Exit(() -> true);}

    /**
     * Exit never
     * @return exit
     */
    public static Exit exitNever(){return new Exit(() -> false);}


    /**
     * Pause for some amount of time
     * @param time
     * @return
     */
    public static Stage pause(double time){
        return new Stage(exitTime(time));
    }

    /**
     * Stop the part
     * @return stop
     */
    protected Stop stop(){ return new Stop(() -> {}); }


    /**
     * Use this robot part
     * NOTE: This must be called before the robot part can be used in a stage
     * @return initial
     */
    public Initial usePart(){return new Initial(() -> {});}

    /**
     * Return the robot part to the main user
     * NOTE: This must be called after the robot part is use in a stage
     * @return stop
     */
    public Stop returnPart(){return new Stop(() -> {});}


    /**
     * Methods to override and use to create stages
     */

    protected void move(double fp, double sp, double tp){}
    protected Main main(double fp, double sp, double tp){ return new Main(() -> move(fp, sp, tp)); }
    protected Stage moveTime(double fp, double sp, double tp, double t){ return new Stage(usePart(), main(fp, sp, tp), exitTime(t), stop(), returnPart()); }
    protected AutoModule MoveTime(double fp, double sp, double tp, double t){ return new AutoModule(moveTime(fp, sp, tp, t)); }
    protected final Stage moveCustomExit(double fp, double sp, double tp, Exit exit){ return new Stage(usePart(), main(fp, sp, tp), exit, stop(), returnPart()); }

    protected void move(double p){}
    protected Main main(double p){ return new Main(() -> move(p)); }
    protected Stage moveTime(double p, double t){ return new Stage(usePart(), main(p), exitTime(t), stop(), returnPart()); }
    protected Stage moveNow(double p){ return new Stage(usePart(), main(p), exitAlways(), stop(), returnPart()); }
    protected AutoModule MoveTime(double p, double t){ return new AutoModule(moveTime(p, t)); }
    protected final Stage customExit(double p, Exit exit){ return new Stage(usePart(), main(p), exit, stop(), returnPart()); }
    protected final Stage customExit(double p, ReturnCodeSeg<Boolean> exit){ return new Stage(usePart(), main(p), new Exit(exit), stop(), returnPart()); }


    protected final Stage customTime(CodeSeg m, double t){ return new Stage(usePart(), new Main(m), exitTime(t), stop(), returnPart()); }
    protected final Stage customTime(Main m, double t){ return new Stage(usePart(), m, exitTime(t), stop(), returnPart()); }


    protected Initial setTarget(double target){ return new Initial(() -> {}); }
    protected Exit exitTarget(){ return exitAlways(); }
    protected Stop stopTarget(){ return new Stop(() -> {});}
    protected Stage moveTarget(double power, double target){ return new Stage(); }

}
