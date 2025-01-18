package unittests.tele.framework;

import automodules.stage.Exit;
import robot.BackgroundTask;
import unittests.UnitTest;
import unittests.tele.TeleUnitTest;
import util.Timer;
import util.codeseg.CodeSeg;

import static global.General.bot;
import static global.General.log;

@SuppressWarnings("all")
public class BackgroundTest extends TeleUnitTest {

    private volatile int a = 3;
    private int b = 4;
    private volatile int c = 5;

    private final Timer timer1 = new Timer();
    private final Timer timer2 = new Timer();

    private final BackgroundTask task1 = new BackgroundTask(() -> { a = 5; pause1(); a = 3; pause1();});
    private final BackgroundTask task2 = new BackgroundTask(() -> precision.throttle(() -> {b = b==4?6:4;}, 500), 3);
    private final BackgroundTask task3 = new BackgroundTask(() -> { c = 7; pause2(); c = 5; pause2(); }, new Exit(() -> timer.seconds() > 6));

    private void pause1(){ timer1.reset(); while (timer1.seconds() < 0.5){}}
    private void pause2(){ timer2.reset(); while (timer2.seconds() < 0.5){} }

    @Override
    protected void start() {
        bot.cancelBackgroundTasks();
        bot.addBackgroundTask(task1);
        bot.addBackgroundTask(task2);
        bot.addBackgroundTask(task3);
    }

    @Override
    protected void loop() {
        log.show("Number of tasks {3 -> 2 -> 1}", bot.backHandler.tasks.size());
        log.show("a {3 <-> 5}", a);
        log.show("b, stops in 3s {4 <-> 6}", b);
        log.show("c, stops in 6s {5 <-> 7}", c);
    }
}
