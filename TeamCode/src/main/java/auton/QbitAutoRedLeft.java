package auton;

import static global.General.bot;
import static global.General.log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.vision.CaseScannerRect;
import elements.TeamProp;
import math.polynomial.Linear;
//import robotparts.sensors.odometry.SecondOdometry;
import util.Timer;

@Autonomous(name = "QbitAutoRedLeft", group = "Autonomous", preselectTeleOp="QbitOp")
public class QbitAutoRedLeft extends Auto {
    public CaseScannerRect caseScanner;
    protected TeamProp propCaseDetected = TeamProp.LEFT;


    @Override
    public final void initAuto() {
        log.show("yeet");
        scan(true,"blue","left");

        while (!isStarted() && !isStopRequested()){ propCaseDetected = caseScanner.getCase(); caseScanner.log(); log.showTelemetry(); }
        camera.halt();
        log.show(propCaseDetected);

        gyro.reset();
        oneOdometry.reset();
//        secondOdometry.reset();
    }

    @Override
    public void runAuto() {
        pause(1);

        // Remove this when testing with scanning
//        propCaseDetected = TeamProp.LEFT;


        if (propCaseDetected.equals(TeamProp.LEFT)) {
            outtake.moveClawClose();
            // Use this for curved paths (distance in cm) backward is negative distance
            moveTurnGyroMoveSmoothDistanceForward(0.5, -50, 0.3, -70);
            pause(0.5);
            drive.halt();
            moveTurnGyroMoveSmoothDistanceForward(0.5, 60, 0.3, 0);
            drive.halt();
            moveDistanceForward(0.3,-7);
            drive.halt();
            moveTurnGyro(0.4,-90);
            drive.halt();
            moveDistanceForward(0.5,-142);
            bot.addAutoModule(AutoYellow());
            AutoYellow();
            pause(0.5);
            moveTurnGyro(0.4,0);
            moveDistanceForward(0.2,-34);
            moveTurnGyro(0.4,-90);
            outtake.moveClawOpen();
            drive.halt();
            moveTime(-0.2,0.4,0.0,0.7);
            lift.reset();
            outtake.moveFlipStart();
            pause(10);

            // Use this for forward backward straight
            //moveForwardDistance(0.2, 20);

            // Use this for turn in place
            //moveTurnGyro(0.4, -90);

            //use this to turn and move forward and strafe with odometry

//            moveTurnGyroMoveSmoothDistanceStrafe(0.4,-20, 0.0, 0);
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
            moveDistanceForward(0.4, -68);
            drive.halt();
            moveDistanceForward(0.4, 63);
            pause(0.5);
            drive.halt();
            moveDistanceForward(0.3,-7);
            drive.halt();
            moveTurnGyro(0.4,-90);
            drive.halt();
            moveDistanceForward(0.5,-121);
            bot.addAutoModule(AutoYellow());
            AutoYellow();
            pause(0.5);
            moveTurnGyro(0.4,0);
            moveDistanceForward(0.2,-40);
            moveTurnGyro(0.4,-90);
            outtake.moveClawOpen();
            drive.halt();
            moveTime(0.0,0.3,0.0,1.2);
            moveDistanceForward(0.5,-23);
            lift.stageLift(0.2,-5);
            outtake.moveFlipStart();
            pause(10);
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
        else if(propCaseDetected.equals(TeamProp.RIGHT)){
            outtake.moveClawClose();
            moveTurnGyro(0.4,20);
            moveDistanceForward(0.4, -60);
            drive.halt();
            moveDistanceForward(0.4, 60);
            moveTurnGyro(0.4,-20);
            pause(0.5);
            drive.halt();
            moveDistanceForward(0.3,-7);
            drive.halt();
            moveTurnGyro(0.4,-90);
            drive.halt();
            moveDistanceForward(0.5,-121);
            bot.addAutoModule(AutoYellow());
            AutoYellow();
            pause(0.5);
            moveTurnGyro(0.4,0);
            moveDistanceForward(0.2,-50);
            moveTurnGyro(0.4,-90);
            outtake.moveClawOpen();
            drive.halt();
            moveTime(0.0,0.3,0.0,1.2);
            moveDistanceForward(0.5,-23);
            lift.stageLift(0.2,-5);
            outtake.moveFlipStart();
            pause(10);
//            pause(1);
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
        caseScanner = new CaseScannerRect();
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
        oneOdometry.reset();
        Linear linearTurnTarget = new Linear(start, targetDegrees, Math.abs(forwardDistance));
        Linear linearTurnPower = new Linear(minimumTurningPower, initialTurningPower, Math.abs(start-targetDegrees));
        Linear linearForwardPower = new Linear(minimumForwardPower, initialForwardPower, Math.abs(forwardDistance));
        whileActive(() -> Math.abs(targetDegrees - gyro.getHeading()) > 2 || Math.abs(forwardDistance-oneOdometry.getOdometryDistance()) > 1,() -> {
            double currentTarget = linearTurnTarget.f(Math.abs(oneOdometry.getOdometryDistance()));
            double errorTurn=currentTarget - gyro.getHeading();
            if(initialTurningPower!=0 && linearTurnPower.fodd(errorTurn)>0.11){
                errorTurn=currentTarget - gyro.getHeading();
            }
            else{
                errorTurn=0;
            }
            double errorForward = forwardDistance- oneOdometry.getOdometryDistance();
            drive.move(linearForwardPower.fodd(errorForward),0, linearTurnPower.fodd(errorTurn));
        });
        drive.halt();

    }


    public void moveDistanceForward(double initialForwardPower, double forwardDistance){
        final double minimumForwardPower = 0.3;
        oneOdometry.reset();
        Linear linearForwardPower = new Linear(minimumForwardPower, initialForwardPower, Math.abs(forwardDistance));
        whileActive(() -> Math.abs(forwardDistance-oneOdometry.getOdometryDistance()) > 1,() -> {
            double errorForward = forwardDistance- oneOdometry.getOdometryDistance();
            drive.move(linearForwardPower.fodd(errorForward),0, 0);
        });
        drive.halt();

    }

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

