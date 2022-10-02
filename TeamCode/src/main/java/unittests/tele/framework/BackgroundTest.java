package unittests.tele.framework;

import automodules.stage.Exit;
import robot.BackgroundTask;
import unittests.UnitTest;
import unittests.tele.TeleUnitTest;
import util.Timer;
import util.codeseg.CodeSeg;

import static global.General.bot;
import static global.General.log;

public class BackgroundTest extends TeleUnitTest {

    // TODO 4 TEST

    private volatile int a = 3;
    private int b = 4;
    private volatile int c = 5;

    private final Timer timer2 = new Timer();

    private final BackgroundTask task1 = new BackgroundTask(() -> { a = 5; pause(); a = 3; pause(); });

    private final BackgroundTask task2 = new BackgroundTask(
            () -> throttle(() -> (b = b==4?6:4), 250), 2);

    private final BackgroundTask task3 = new BackgroundTask(() -> { c = 7; pause(); c = 5; pause(); }, new Exit(() -> timer2.seconds() > 4));

    private void pause(){
        while (timer.seconds() < 0.25){}
        timer.reset();
    }

    @Override
    protected void start() {
        bot.addBackgroundTask(task1);
        bot.addBackgroundTask(task2);
        bot.addBackgroundTask(task3);
        timer2.reset();
    }

    @Override
    protected void loop() {
        log.show("Number of tasks {3 -> 2 -> 1}", bot.backHandler.tasks.size());
        log.show("a {3 <-> 5}", a);
        log.show("b, stops in 2s {4 <-> 6}", b);
        log.show("c, stops in 4s {5 <-> 7}", c);
    }
}
