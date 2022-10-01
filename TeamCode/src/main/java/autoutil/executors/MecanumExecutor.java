package autoutil.executors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import autoutil.paths.PathAutoModule;
import autoutil.paths.PathPause;
import autoutil.paths.PathPose;
import autoutil.paths.PathSegment;
import geometry.position.Pose;
import util.codeseg.ReturnCodeSeg;
import util.template.Iterator;

import static global.General.bot;

public class MecanumExecutor extends Executor implements Iterator {

    public MecanumExecutor(LinearOpMode opMode) {
        super(opMode);
    }

    public MecanumExecutor(ReturnCodeSeg<Boolean> active) {
        super(active);
    }

    @Override
    public void followPath() {
        reactor.init();
        for(PathSegment pathSegment: path.getSegments()){
            reactor.setPathSegment(pathSegment);
            if(pathSegment instanceof PathPose) {
                for (Pose pose : pathSegment.getPoses()) {
                    reactor.setTarget(pose.asArray());
                    whileActive(() -> !reactor.isAtTarget(), ()-> {
                        reactor.moveToTarget();
                        backgroundTasks.run();
                    });
                    reactor.nextTarget();
                }
            }else if(pathSegment instanceof PathAutoModule){
                if(!((PathAutoModule) pathSegment).isCancel){
                    ((PathAutoModule) pathSegment).runAutoModule();
                    if (!((PathAutoModule) pathSegment).isConcurrent()) {
                        bot.mecanumDrive.move(0, 0, 0);
                        whileActive(() -> !((PathAutoModule) pathSegment).isDoneWithAutoModule(), () -> {
                            backgroundTasks.run();
                        });
                    }
                }else{
                    bot.cancelAutoModules();
                }
            }else if(pathSegment instanceof PathPause){
                ((PathPause) pathSegment).startPausing();
                bot.mecanumDrive.move(0,0,0);
                whileActive(() -> !((PathPause) pathSegment).isDonePausing(), () -> {
                    backgroundTasks.run();
                });
            }
        }
        bot.mecanumDrive.move(0,0,0);
    }

    @Override
    public boolean condition() {
        if(isIndependent){
            return !bot.independentRunner.disabled;
        }else{
            return whileOpModeIsActive.run();
        }
    }
}