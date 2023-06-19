package auton.unused;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import java.util.ArrayList;

import auton.Auto;
import autoutil.controllers.control1D.Controller1D;
import autoutil.controllers.control1D.RV;
import util.template.Iterator;

import static global.General.log;


@Autonomous(name = "OdoAuto")
public class OdoAuto extends Auto {

    public static RV controller;


    @Override
    public void initAuto() {

        controller = new RV(0.05, 0.01);


        controller.setProcessVariable(odometry::getY);
        controller.setAccuracy(0.5);
        controller.setMinimumTime(0.2);

    }

    @Override
    public void runAuto() {

        controller.setTarget(100);
        controller.setTargetTime(2);
        whileActive(controller::notAtTarget,() -> {
            controller.update();
            drive.move(controller.getOutput(),0,0);
        });
        controller.reset();
        drive.halt();




//        ArrayList<Double> ratio = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {


//            odometry.totalY = 0;
//            odometry.totalX = 0;
//            final double startX = drive.bl.getMotor().getCurrentPosition();
//            final double startY = drive.fl.getMotor().getCurrentPosition();
//            drive.move(0.3, -0.2, 0);
//            whileActive(() -> odometry.totalY > -100000, () -> {
//                odometry.totalX = drive.bl.getMotor().getCurrentPosition() - startX;
//                odometry.totalY = drive.fl.getMotor().getCurrentPosition() - startY;
//                log.show("Total X", odometry.totalX);
//                log.show("Total Y", odometry.totalY);
//            });
//            log.show("Ratio (X/Y)", odometry.totalX/odometry.totalY);
//            ratio.add(odometry.totalX/odometry.totalY);
//            drive.move(0, -0.2, 0);
//            pause(0.3);
//
//            odometry.totalY = 0;
//            odometry.totalX = 0;
//            final double startX2 = drive.bl.getMotor().getCurrentPosition();
//            final double startY2 = drive.fl.getMotor().getCurrentPosition();
//            drive.move(-0.3, -0.2, 0);
//            whileActive(() -> odometry.totalY < 100000, () -> {
//                odometry.totalX = drive.bl.getMotor().getCurrentPosition() - startX2;
//                odometry.totalY = drive.fl.getMotor().getCurrentPosition() - startY2;
//                log.show("Total X", odometry.totalX);
//                log.show("Total Y", odometry.totalY);
//            });
//            log.show("Ratio (X/Y)", odometry.totalX / odometry.totalY);
//            ratio.add(odometry.totalX/odometry.totalY);
//            drive.move(0, -0.2, 0);
//            pause(0.3);

//            odometry.totalY = 0;
//            odometry.totalX = 0;
//            final double startX = drive.bl.getMotor().getCurrentPosition();
//            final double startY = drive.fl.getMotor().getCurrentPosition();
//            drive.move(-0.2, -0.3, 0);
//            whileActive(() -> odometry.totalX > -100000, () -> {
//                odometry.totalX = drive.bl.getMotor().getCurrentPosition() - startX;
//                odometry.totalY = drive.fl.getMotor().getCurrentPosition() - startY;
//                log.show("Total X", odometry.totalX);
//                log.show("Total Y", odometry.totalY);
//            });
//            log.show("Ratio (Y/X)", odometry.totalY/odometry.totalX);
//            ratio.add(odometry.totalY/odometry.totalX);
//            drive.move(-0.3, 0, 0);
//            pause(0.5);
//
//            odometry.totalY = 0;
//            odometry.totalX = 0;
//            final double startX2 = drive.bl.getMotor().getCurrentPosition();
//            final double startY2 = drive.fl.getMotor().getCurrentPosition();
//            drive.move(-0.2, 0.3, 0);
//            whileActive(() -> odometry.totalX < 100000, () -> {
//                odometry.totalX = drive.bl.getMotor().getCurrentPosition() - startX2;
//                odometry.totalY = drive.fl.getMotor().getCurrentPosition() - startY2;
//                log.show("Total X", odometry.totalX);
//                log.show("Total Y", odometry.totalY);
//            });
//            log.show("Ratio (X/Y)", odometry.totalY / odometry.totalX);
//            ratio.add(odometry.totalY/odometry.totalX);
//            drive.move(-0.3, 0, 0);
//            pause(0.5);
//        }
//        drive.halt();
//        double avgRatio = Iterator.forAllAverage(ratio);
//        log.show("Average Ratio (X/Y)", avgRatio);
//        pause(10);

//        final double startX = drive.bl.getMotor().getCurrentPosition();
//        final double startY = drive.fl.getMotor().getCurrentPosition();
//        whileActive(() -> {
//            log.show("Total X", drive.bl.getMotor().getCurrentPosition()-startX);
//            log.show("Total Y", drive.fl.getMotor().getCurrentPosition()-startY);
//        });


        // Ratio = 0.002265

        // Ratio 2 0.003734

        // degs = 0.21


        // degs = 89.87

        // degs = 0.13

        // delta = 0.34

        // angleY = -0.21 from Y axis CW
        // angleX = 91.13 from Y axis CW



        drive.move(0.3, -0.2, 0.0);
        whileActive(() -> odometry.getY() < 100, () -> {});
        drive.halt();


        whileTime(() -> {
            log.show("Total X", odometry.getX());
            log.show("Total Y", odometry.getY());
            log.show("Total H", odometry.getHeading());
        }, 20);




    }
}
