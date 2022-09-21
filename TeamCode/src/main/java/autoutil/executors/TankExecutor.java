package autoutil.executors;

import static global.General.*;

public class TankExecutor extends Executor {
    @Override
    public void move(double f, double t) {
        bot.tankDrive.move(f, t);
    }
}
