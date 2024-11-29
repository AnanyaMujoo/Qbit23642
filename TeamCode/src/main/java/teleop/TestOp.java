package teleop;

import static java.lang.Math.abs;
import static global.General.bot;
import static global.General.gph1;
import static global.General.gph2;
import static global.General.log;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import teleutil.button.Button;


@TeleOp(name = "TestOp", group = "TeleOp")
public class TestOp extends Tele {

    @Override
    public void initTele() {



    }

    @Override
    public void startTele() {
    }

    @Override
    public void loopTele() {
//        log.show("Flip (buttons):   b:start, y:middle, x:end");
//        log.show("Claw (bumpers):   right:close, left:open");
//        log.show("Drone (dpad):   up:start, down:release");
        //     drive.move((gph1.rt-gph1.lt)*0.5,gph1.lx*0.5,gph1.rx*0.4);

        intake2.move(gph1.ry);
        //  lift.move(gph2.ly*0.2);
        // TODO COMMENT THIS OUT WHEN DONE
//        log.show(odometry.getEncX());
//        log.show(odometry.getEncY());
//        log.show(odometry.getPose());
//        log.show("Right Odometry Distance: ", oneOdometry.getRightOdometryDistance());
//        log.show("Back Odometry: ", oneOdometry.getBackOdometryDistance());
//        log.show("Gyro Heading: ", gyro.getHeading());

//        log.show(colorSensors.leftPixelDistance());
//        log.show(colorSensors.rightPixelDistance());
//        log.show("Claw Left");
//        log.show(outtake.clawLeft.getPosition());
//        log.show("Claw Right");
//        log.show(outtake.clawRight.getPosition());
//        log.show(gyro.getHeading());











//        log.show(lift.liftLeft.getPosition());
//        log.show(lift.liftRight.getPosition());

    }

}


