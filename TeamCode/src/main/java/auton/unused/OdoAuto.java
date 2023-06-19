package auton.unused;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import java.util.ArrayList;

import auton.Auto;
import autoutil.controllers.control1D.Controller1D;
import autoutil.controllers.control1D.RV;
import autoutil.controllers.control2D.NoStopNew;
import autoutil.generators.LineGenerator;
import autoutil.generators.PoseGenerator;
import geometry.framework.Point;
import geometry.position.Pose;
import util.template.Iterator;

import static global.General.log;


@Autonomous(name = "OdoAuto")
public class OdoAuto extends Auto {

    public static RV controller;

    public static NoStopNew noStopNew;
    public static RV rvHeading;


    @Override
    public void initAuto() {

//        controller = new RV(0.07, 0.014,  10);
//        controller.scale(1.0);

        noStopNew = new NoStopNew(0.02, 0.08,50, 0.4,  0.5);


        rvHeading = new RV(0.012, 0.08, 40);
        rvHeading.setProcessVariable(odometry::getHeading);
        rvHeading.setMinimumTime(0.05);
        rvHeading.setAccuracy(1);
        rvHeading.set1D();
        rvHeading.setStopConstant(90);



//        noStopNew.setProcessVariable(odometry::getX, odometry::getY);


//
//        controller.setProcessVariable(odometry::getY);
//        controller.setAccuracy(0.5);
//        controller.setMinimumTime(0.2);

    }

    @Override
    public void runAuto() {

//        controller.setTarget(100);
//        controller.setTargetTime(2);
//        controller.reset();
//        whileActive(controller::notAtTarget,() -> {
//            controller.update();
//            drive.move(controller.getOutput(),0,0);
//            log.show("Velocity", controller.velocity);
//            log.show("StopDis", controller.stopDis);
//            log.show("StopMode", controller.stopMode);
//        });
//        drive.halt();
//        controller.reset();

//        rvHeading.reset();
//        rvHeading.scale(1);
//        rvHeading.setTarget(180);
//        whileActive(rvHeading::notAtTarget, () -> {
//            rvHeading.update();
//            drive.move(0,0, -rvHeading.getOutput());
////            log.show("Heading", odometry.getHeading());
////            log.show("Target", rvHeading.getTarget());
////            log.show("Error", rvHeading.getError());
////            log.show("StopDis", rvHeading.stopDis);
////            log.show("StopMode",rvHeading.stopMode);
//        });
//        drive.halt();
//        rvHeading.reset();

//        noStopNew.setTarget(new Point(0,100));


        noStopNew.scale(1.0);
        noStopNew.reset();
        LineGenerator lineGenerator = new LineGenerator();
        lineGenerator.add(new Pose(), new Pose(0,100, 0));

        rvHeading.reset();
        rvHeading.scale(1.0);
        rvHeading.setTarget(0);
        whileActive(() -> noStopNew.notAtTarget() || rvHeading.notAtTarget(),() -> {
            noStopNew.updateController(odometry.getPose(), lineGenerator);
            rvHeading.update();
//            drive.move(noStopNew.getOutputY(), 1.2*noStopNew.getOutputX(), 0);
            drive.move(noStopNew.getOutputY(), 1.2*noStopNew.getOutputX(),-rvHeading.getOutput());
        });
        drive.halt();
        noStopNew.reset();
        rvHeading.reset();





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

//
//
//        drive.move(0.3, -0.2, 0.0);
//        whileActive(() -> odometry.getY() < 100, () -> {});
//        drive.halt();
//
//
//        whileTime(() -> {
//            log.show("Total X", odometry.getX());
//            log.show("Total Y", odometry.getY());
//            log.show("Total H", odometry.getHeading());
//        }, 20);




    }
}
