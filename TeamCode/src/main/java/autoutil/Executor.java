package autoutil;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import autoutil.generators.Generator;
import autoutil.reactors.Reactor;
import util.codeseg.ParameterCodeSeg;
import util.codeseg.ReturnCodeSeg;
import util.template.Iterator;

import static global.General.bot;

public class Executor implements Iterator {

    protected final Reactor reactor;
    protected final Generator generator;
    protected ReturnCodeSeg<Boolean> whileOpModeIsActive;
    protected boolean isIndependent = false;

    public Executor(LinearOpMode opMode, Generator generator, Reactor reactor){
        whileOpModeIsActive = opMode::opModeIsActive; this.generator = generator; this.reactor = reactor;
    }

    public final void followPath() {
        reactor.init();
        reactor.setTarget(generator.getTarget());

        Iterator.forAll(generator.getSegments(), pathSegment -> {
            reactor.setPathSegment(pathSegment);
            addPathCase(PathPose.class, pathPose -> {
                Iterator.forAll(pathPose.getPoses(), pose -> {
                    reactor.setTarget(pose);
                    whileActive(() -> !reactor.isAtTarget(), reactor::moveToTarget);
                    reactor.nextTarget();
                });
            });
            addPathCase(PathAutoModule.class, pathAutoModule -> {
                if(!pathAutoModule.isCancel){
                    pathAutoModule.runAutoModule();
                    if (!pathAutoModule.isConcurrent()) {
                        bot.halt();
                        whileActive(() -> !pathAutoModule.isDoneWithAutoModule(), () -> {});
                    }
                }else{
                    bot.cancelAutoModules();
                }
            });
            addPathCase(PathPause.class, pathPause -> {
                pathPause.startPausing();
                bot.halt();
                whileActive(() -> !pathPause.isDonePausing(), () -> {});
            });
        });
        bot.halt();
    }

    @SuppressWarnings("unchecked")
    private <T extends PathSegment> void addPathCase(Class<T> type, ParameterCodeSeg<T> code){
        if(reactor.getPathSegment().getClass().isInstance(type)){ code.run((T) reactor.getPathSegment()); }
    }

    public void makeIndependent(){ isIndependent = true; }

    @Override
    public boolean condition() {
        if(isIndependent){
            return !bot.independentRunner.disabled;
        }else{
            return whileOpModeIsActive.run();
        }
    }
}