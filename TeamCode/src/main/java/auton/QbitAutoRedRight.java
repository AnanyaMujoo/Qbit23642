package auton;

import static global.General.bot;
import static global.General.log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.vision.CaseScannerRectRr;
import elements.TeamProp;
import math.polynomial.Linear;
//import robotparts.sensors.odometry.SecondOdometry;
import util.Timer;

@Autonomous(name = "QbitAutoRedRight", group = "Autonomous", preselectTeleOp = "QbitOp")
public class QbitAutoRedRight extends Auto {
    public CaseScannerRectRr caseScanner;
    protected TeamProp propCaseDetected = TeamProp.LEFT;


    @Override
    public final void initAuto() {
        log.show("yeet");
        scan(true,"red","left");

        log.show(gyro.getHeading());
        while (!isStarted() && !isStopRequested()){ propCaseDetected = caseScanner.getCase(); caseScanner.log(); log.showTelemetry(); }
        camera.halt();
        log.show(propCaseDetected);

        gyro.reset();
        oneOdometry.reset();
//        secondOdometry.reset();
    }

    @Override
    public void runAuto() {
        // Remove this when testing with scanning
//
//        propCaseDetected=TeamProp.CENTER;
        if (propCaseDetected.equals(TeamProp.RIGHT)) {
            outtake.moveClawClose();
            //move to team prop
            moveDistanceForward(0.5, -40);

            moveTurnGyro(0.4,50);
            moveDistanceForward(0.5, -31);
            pause(0.5);
            drive.halt();
            //move to start

            moveDistanceForward(0.5, 31);
            moveTurnGyro(0.4,-50);
            moveDistanceForward(0.5, 48);
            //move away from the start forward
            drive.halt();
            //rotate with the back of the robot moving forward
            moveTurnGyro(0.4,90);
            drive.halt();
            //move to backstage
            moveDistanceForward(0.6,-200);
            //automodule yellow
            bot.addAutoModule(AutoYellow());
            AutoYellow();
            pause(0.5);
            //rotate to original position
            moveTurnGyro(0.4,0);
            //move to right backstage pixel
            moveDistanceForward(0.6,-40);
            //back of the robot moving forward
            moveTurnGyro(0.4,90);
            //move to backstage along y
//            moveDistanceForward(0.6,-41);
            moveTime(-0.35,0,0,1);
            // open the claw
            outtake.moveClawOpen();
            pause(1);
            //move backward
            pause(0.5);
            moveDistanceForward(0.6,10);
            outtake.moveClawClose();
            drive.halt();
            //reset lift and claw
            bot.addAutoModule(WhyWontLiftWork());
            WhyWontLiftWork();
            //moveTime(0,0.4,0.0,1.4);
            moveTurnGyro(0.4,0);
            moveTime(0.5,0,0,1.3);
            //moveTime(0,-0.4,0.0,2.1);
            //moveTurnGyro(0.4,90);
            //moveDistanceForward(0.5,-30);
            pause(30);

//


//            outtake.moveClawClose();
//            moveTurnGyroMoveSmooth(-0.55, 1.1, 0.3, -70);
//            pause(0.5);
//            moveTurnGyroMoveSmooth(0.45, 1.1, 0.4, 0);
//            pause(0.5);
//            moveForward(0.4,0.34);
//            drive.halt();
//            moveTurnGyro(0.4,-90);
//            drive.halt();
//            moveForward(-0.6,3);
//            drive.halt();
//            bot.addAutoModule(AutoYellow());
//            AutoYellow();
//            pause(0.5);
//            moveTime(0,-0.4, 0.0,1.05);
//            pause(0.5);
//            moveForward(-0.4,0.7);
//            pause(0.5);
//            outtake.moveClawOpen();
//            drive.halt();


//            log.show("left");
        }
        else if (propCaseDetected.equals(TeamProp.CENTER)){
            outtake.moveClawClose();
            //move forward
            moveDistanceForward(0.5, -70);
            drive.halt();
            //move to start
            moveDistanceForward(0.5, 67);
            pause(0.5);
            drive.halt();
            //move 7 along y axis
            moveDistanceForward(0.5,-7);
            drive.halt();
            //turn to back moving along y
            moveTurnGyro(0.4,90);
            drive.halt();
            //move to back along y
            moveDistanceForward(0.6,-180);
            drive.halt();
            moveTurnGyro(0.4,0);
            drive.halt();
//            //move to correct april tag
            moveDistanceForward(0.6,-63);
            pause(0.5);
            //drive.halt();
            moveTurnGyro(0.4,90);
            bot.addAutoModule(AutoYellow());
            pause(0.5);
            AutoYellow();
            pause(1);
            outtake.moveClawClose();
//            moveDistanceForward(0.6,-35);
            moveTime(-0.35,0,0,1);
            outtake.moveClawOpen();
            pause(1);
            moveDistanceForward(0.4,10);
            drive.halt();
            bot.addAutoModule(WhyWontLiftWork());
            WhyWontLiftWork();
            //moveTime(0,0.4,0.0,1.4);
            moveTurnGyro(0.4,0);
            moveDistanceForward(0.6,-49);
            //moveTime(0,-0.4,0.0,2.1);
            moveTurnGyro(0.4,90);
            moveDistanceForward(0.4,-17);
            pause(30);

//            moveTime(-0.3, 0.1, 0, 0.94*2);
//            pause(1);
//            moveTime(0.53/2, -0.1, 0, 0.80*2);
//            moveTurnGyro(0.4,-90);
//            drive.halt();
//            moveForward(-0.6/2,3*2);
//            drive.halt();
//            bot.addAutoModule(AutoYellow());
//            AutoYellow();
//            pause(0.5);
//            moveTime(0,-0.4, 0.0,1.45);
//            pause(0.5);
//            moveForward(-0.4/2,0.7*2);
//            pause(0.5);
//            outtake.moveClawOpen();
//            drive.halt();
//            log.show("right");
//            DropPurpleR(0);
        }
        else if(propCaseDetected.equals(TeamProp.LEFT)){
            outtake.moveClawClose();

            moveTurnGyro(0.4,-20);
            moveDistanceForward(0.4, -44);
            drive.halt();
            moveDistanceForward(0.4, 44);
            moveTurnGyro(0.4,20);
            pause(0.5);
            //move 7 along y axis
            moveDistanceForward(0.5,-7);
            drive.halt();
            //turn to back moving along y
            moveTurnGyro(0.4,90);
            drive.halt();
            //move to back along y
            moveDistanceForward(0.6,-180);
            drive.halt();
            moveTurnGyro(0.4,0);
            drive.halt();
//            //move to correct april tag
            moveDistanceForward(0.6,-68);
            pause(0.5);
            //drive.halt();
            moveTurnGyro(0.4,90);
            bot.addAutoModule(AutoYellow());
            pause(0.5);
            AutoYellow();
            pause(1);
            outtake.moveClawClose();
            moveTime(-0.35,0,0,1);
            outtake.moveClawOpen();
            pause(1);
            moveDistanceForward(0.4,10);
            drive.halt();
            bot.addAutoModule(WhyWontLiftWork());
            WhyWontLiftWork();
            //moveTime(0,0.4,0.0,1.4);
            moveTurnGyro(0.4,0);
            moveDistanceForward(0.6,-45);
            //moveTime(0,-0.4,0.0,2.1);
            moveTurnGyro(0.4,90);
            moveDistanceForward(0.4,-17);
            pause(30);
            pause(1);
//            drive.moveTime(0.53, -0.1, 0, 0.8);
//            moveTurnGyro(0.4,-90);
//            drive.halt();
//            moveForward(-0.6,3);
//            drive.halt();
//            bot.addAutoModule(AutoYellow());
//            AutoYellow();
//            pause(0.5);
//            moveTime(0,-0.4, 0.0,1.05);
//            pause(0.5);
//            moveForward(-0.4,0.7);
//            pause(0.5);
//            outtake.moveClawOpen();
//            drive.halt();
//            log.show("right");
//            DropPurpleR(0);
        }



    }
    @Deprecated
    public void moveTime(double f, double s, double t, double time) {
        drive.move(f, s, t);
        pause(time);
        drive.halt();
    }
    @Deprecated
    public void moveForward(double forwardPower, double time) {
        drive.move(forwardPower, 0, 0);
        pause(time);
        drive.halt();
    }
    public void scan(boolean view, String color, String side){
        caseScanner = new CaseScannerRectRr();
        camera.start(true);
        camera.setScanner(caseScanner);
        caseScanner.setColor(color);
        caseScanner.setSide(side);
        caseScanner.start();
    }

    // https://www.desmos.com/calculator/gfycx0gxgw
    // Initial Power is p
    // Minimum Power is m
    // Turning Distance (Degrees) is d
    // X axis is the current error
    // Y axis is the current turning power to output
    public void moveTurnGyro(double initialPower, double target){
        double start = gyro.getHeading();
        final double minPower = 0.1;
        whileActive(() -> Math.abs(target - gyro.getHeading()) > 2,() -> {
            double error = target - gyro.getHeading();
            Linear linear = new Linear(minPower, Math.abs(initialPower), Math.abs(start-target));
            drive.move(0,0,linear.fodd(error));
        });
        drive.halt();
    }


    @Deprecated
    public void moveTurnGyroMoveSmooth(double forwardPower, double forwardTime, double initialTurningPower, double targetDegrees){
        double start = gyro.getHeading();
        final double minimumTurningPower = 0.1;
        Timer timer = new Timer();
        timer.reset();
        Linear linearTurnTarget = new Linear(start, targetDegrees, forwardTime);
        Linear linearTurnPower = new Linear(minimumTurningPower, initialTurningPower, Math.abs(start-targetDegrees));
        whileActive(() -> Math.abs(targetDegrees - gyro.getHeading()) > 2 || timer.seconds() < forwardTime,() -> {
            double currentTarget = linearTurnTarget.f(timer.seconds());
            double error = currentTarget - gyro.getHeading();
            if(timer.seconds() > forwardTime){
                drive.move(0,0, linearTurnPower.fodd(error));
            }else{
                drive.move(forwardPower,0, linearTurnPower.fodd(error));
            }
        });
        drive.halt();
    }


    public void moveTurnGyroMoveSmoothDistanceForward(double initialForwardPower, double forwardDistance, double initialTurningPower, double targetDegrees){
        double start = gyro.getHeading();
        final double minimumTurningPower = 0.1;
        final double minimumForwardPower = 0.06;
        final double minimumStrafePower = 0.07;
        oneOdometry.reset();
        pause(0.1);
        oneOdometry.reset();
        Linear linearStrafe = new Linear(0.05, minimumStrafePower);
        Linear linearTurnTarget = new Linear(start, targetDegrees, Math.abs(forwardDistance));
        Linear linearTurnPower = new Linear(minimumTurningPower, initialTurningPower, Math.abs(start-targetDegrees));
        Linear linearForwardPower = new Linear(minimumForwardPower, initialForwardPower, Math.abs(forwardDistance));
        whileActive(() -> Math.abs(targetDegrees - gyro.getHeading()) > 2 || Math.abs(forwardDistance-oneOdometry.getRightOdometryDistance()) > 1,() -> {
            double currentTarget = linearTurnTarget.f(Math.abs(oneOdometry.getRightOdometryDistance()));
            double errorTurn;
            if(initialTurningPower!=0){
                errorTurn=currentTarget - gyro.getHeading();
            } else{
                errorTurn=0;
            }
            double errorForward = forwardDistance- oneOdometry.getRightOdometryDistance();
            drive.move(linearForwardPower.fodd(errorForward),0, linearTurnPower.fodd(errorTurn));
        });
        drive.halt();

    }


    public void moveDistanceForward(double initialForwardPower, double forwardDistance){
        double start = gyro.getHeading();
        final double minimumForwardPower = 0.15;
        oneOdometry.reset();
        pause(0.1);
        oneOdometry.reset();
        Linear linearForwardPower = new Linear(minimumForwardPower, initialForwardPower, Math.abs(forwardDistance));
        whileActive(() -> Math.abs(forwardDistance-oneOdometry.getRightOdometryDistance()) > 1,() -> {
            double errorTurn=start - gyro.getHeading();
            double errorForward = forwardDistance- oneOdometry.getRightOdometryDistance();
            drive.move(linearForwardPower.fodd(errorForward),0, 0);

        });
        drive.halt();

    }

//
//    public void moveDistanceStrafe(double initialForwardPower, double strafeDistance){
//        final double minimumForwardPower = 0.15;
//        oneOdometry.reset();
//        pause(0.1);
//        oneOdometry.reset();
//        Linear linearForwardPower = new Linear(minimumForwardPower, initialForwardPower, Math.abs(strafeDistance));
//        whileActive(() -> Math.abs(strafeDistance-oneOdometry.getBackOdometryDistance()) > 1,() -> {
//            double errorForward = strafeDistance - oneOdometry.getBackOdometryDistance();
//            drive.move(linearForwardPower.fodd(errorForward),0, 0);
//
//        });
//        drive.halt();
//
//    }

//
//
//    public void moveDistanceForwardInaccurate(double initialForwardPower, double forwardDistance){
//        final double minimumForwardPower = 0.3;
//        oneOdometry.reset();
//        Linear linearForwardPower = new Linear(minimumForwardPower, initialForwardPower, Math.abs(forwardDistance));
//        whileActive(() -> Math.abs(forwardDistance-oneOdometry.getRightOdometryDistance()) > 1,() -> {
//            double errorForward = forwardDistance- oneOdometry.getRightOdometryDistance();
//            if (forwardDistance<0){
//            drive.move(-initialForwardPower,0, 0);}
//            else if (forwardDistance>0){
//                drive.move(initialForwardPower,0, 0);}
//        });
//        drive.halt();
//
//    }
//    public void MoveSmoothDistanceStrafe(double initialStrafePower, double strafeDistance){
//        final double minimumStrafePower = 0.03;
//        secondOdometry.reset();
//        Linear linearStrafePower = new Linear(minimumStrafePower, initialStrafePower, Math.abs(strafeDistance));
//        whileActive(() -> (Math.abs(strafeDistance-secondOdometry.getOdometryDistance()) > 1), () -> {
//            double errorStrafe = strafeDistance- secondOdometry.getOdometryDistance();
//            drive.move(0,linearStrafePower.fodd(errorStrafe), 0);
//            log.show(secondOdometry.getOdometryDistance());
//        });
//        drive.halt();
//
//    }
//
//    public void moveTurnGyroMoveSmoothDistance(double initialForwardPower, double forwardDistance, double initialStrafePower, double strafeDistance, double initialTurningPower, double targetDegrees){
//        double start = gyro.getHeading();
//        final double minimumTurningPower = 0.1;
//        final double minimumStrafePower = 0.06;
//        final double minimumForwardPower = 0.06;
//        oneOdometry.reset();
//        secondOdometry.reset();
//        Linear linearTurnTarget = new Linear(start, targetDegrees, Math.abs(forwardDistance));
//        Linear linearTurnPower = new Linear(minimumTurningPower, initialTurningPower, Math.abs(start-targetDegrees));
//        Linear linearForwardPower = new Linear(minimumForwardPower, initialForwardPower, Math.abs(forwardDistance));
//        Linear linearStrafePower = new Linear(minimumStrafePower, initialStrafePower, Math.abs(strafeDistance));
//        whileActive(() -> Math.abs(targetDegrees - gyro.getHeading()) > 2 || Math.abs(strafeDistance-secondOdometry.getOdometryDistance())> 1 || Math.abs(forwardDistance-oneOdometry.getOdometryDistance()) > 1,() -> {
//            double currentTarget = linearTurnTarget.f(Math.abs(secondOdometry.getOdometryDistance()));
//            double errorTurn;
//            if (targetDegrees!=0){
//                errorTurn = currentTarget - gyro.getHeading();}
//            else{
//                errorTurn= 0;
//            }
//            double errorForward = forwardDistance- oneOdometry.getOdometryDistance();
//            double errorStrafe = strafeDistance- secondOdometry.getOdometryDistance();
//            drive.move(linearForwardPower.fodd(errorForward),linearStrafePower.fodd(errorStrafe), linearTurnPower.fodd(errorTurn));
//        });
//
//        drive.halt();
//
//    }
//
//    public void moveForwardDistance(double initialForwardPower, double forwardDistance) {
//        final double minimumForwardPower = 0.06;
//        oneOdometry.reset();
//        Linear linearForwardPower = new Linear(minimumForwardPower, initialForwardPower, Math.abs(forwardDistance));
//        whileActive(() -> Math.abs(forwardDistance-oneOdometry.getOdometryDistance()) > 1,() -> {
//            double errorForward = forwardDistance- oneOdometry.getOdometryDistance();
//            drive.move(linearForwardPower.fodd(errorForward),0, 0);
//        });
//        drive.halt();
//    }

}

