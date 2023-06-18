package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static global.General.log;

@Autonomous(name = "YES")
public class OdoAuto extends Auto{
    @Override
    public void initAuto() {


    }

    @Override
    public void runAuto() {
        odometry.totalY = 0;
        odometry.totalX = 0;
        drive.move(0.3, -0.2, 0);
        whileActive(() -> odometry.totalY < 50, () -> {
            drive.fl.getMotor().getCurrentPosition();
        });
        drive.halt();

        log.show("Ratio (X/Y)", odometry.totalX/odometry.totalY);
        pause(10);
    }
}
