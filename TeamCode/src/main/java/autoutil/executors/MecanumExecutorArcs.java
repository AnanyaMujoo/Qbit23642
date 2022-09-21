package autoutil.executors;

import static global.General.bot;

public class MecanumExecutorArcs extends Executor {
    @Override
    public void move(double f, double t) {
        bot.drive.move(f, 0, t);
    }
}
