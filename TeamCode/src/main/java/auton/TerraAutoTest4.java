package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "TerraAutoTest4", group = "auto")
public class TerraAutoTest4 extends Auto{
    @Override
    public void initAuto() {

    }

    @Override
    public void runAuto() {
        whileActive(() -> {
            drive.move(0, 0, (180 - odometry.getHeading())/90);
        });
    }
}
