package auton;

public class mahiyaAUTO extends Auto {

    @Override
    public void initAuto() {

    }

    @Override
    public void runAuto() {

    }

    public void moveTime(double pow, double time) {
        mahiya.move(pow);
        pause(time);
        mahiya.halt();
    }
}
