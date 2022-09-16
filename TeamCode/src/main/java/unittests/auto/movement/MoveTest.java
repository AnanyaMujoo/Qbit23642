package unittests.auto.movement;

import unittests.auto.AutoUnitTest;
import util.Timer;

import static global.General.bot;

// TODO 4 REMOVE
@Deprecated
public class MoveTest extends AutoUnitTest {

    Timer timer = new Timer();
    @Override
    public void init() {
        timer.reset();
    }

    @Override
    protected void run() {
        whileTime(() -> bot.tankDrive.move(0.3, 0), 1);
    }

    @Override
    public void stop() {
        bot.tankDrive.move(0,0);
    }
}
