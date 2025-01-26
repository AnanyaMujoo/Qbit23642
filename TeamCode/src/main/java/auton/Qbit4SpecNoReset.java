package auton;

import static global.General.log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import automodules.AutoModule;
import autoutil.AutoFramework;


//@Autonomous(name = "QbitAutoSpec", group = "auto", preselectTeleOp = "TerraOp")
@Autonomous(name = "Qbit4SpecNoReset", group = "auto")
public class Qbit4SpecNoReset extends AutoFramework {

    double x = 0;

    @Override
    public void initialize() {
        setConfig(mecanumMixedConfig);
        liftOuttake.setToAuto();
        liftOuttake.maintain();
        x=0;

//        wait(0.1);
    }

    AutoModule Up = new AutoModule(
            liftOuttake.stageLift(0.56 , 44),
            claw.stageDisable(0.05)
    ).setStartCode(() -> {
        intake.flipAlmost();
        claw.hold2();
    });

    AutoModule Specimen = new AutoModule(
            liftOuttake.stageDown(0.7, 33),
            claw.stageReady(0.3),
            claw.stageHold2(0.05),
            liftOuttake.stageDown(-0.85,3)
    );

            AutoModule SpecimenNoHold = new AutoModule(
            liftOuttake.stageDown(0.7, 33),
            claw.stageReady(0.2),
            liftOuttake.stageDown(-0.85,3)
    );
    AutoModule SpecimenNoHoldFinal = new AutoModule(
            liftOuttake.stageDown(0.7, 33),
            claw.stageReady(0.2),
            liftOuttake.stageDown(-0.85,0)
    );
//    AutoModule Reset = new AutoModule(
////            intake.stageFlipAlmostOut(0.1),
//            claw.stageHold(0.05),
//            liftOuttake.stageDown(-0.3,0)
//    );
//
//    AutoModule ResetWithFirstBlock = new AutoModule(
//            intake.stageFlipAlmostOut(0.05),
//            claw.stageHold(0.05),
//            liftIntake.stageLift(0.6, 15),
//            liftOuttake.stageDown(-0.4,0)
//    );
//
//
//
//    AutoModule FirstBlock = new AutoModule(
//            liftIntake.stageLift(0.3, 15),
//            intake.stageFlipOut(0.05),
//            intake.moveTime(1,1)
//        );
//
//    AutoModule ExtendLift = new AutoModule(
//            intake.stageFlipOut(0.05),
//            intake.moveTime(1,0.3),
//            liftIntake.stageLift(0.3, 35),
//            intake.stageFlipAlmostOut(0.05),
//            intake.moveTime(-1,0.5),
//            intake.stageFlipHalf(0.1)
//
//    );
//
//    AutoModule ExtendLiftSecond = new AutoModule(
//            liftIntake.stageLift(0.3, 35),
//            intake.stageFlipAlmostOut(0.05),
//            intake.moveTime(-1,2),
//            intake.stageFlipHalf(0.1),
//            liftIntake.stageLift(0.3,20)
//
//    );
//    AutoModule SecondBlock = new AutoModule(
//            intake.stageFlipHalf(0.1),
//            liftIntake.stageLift(0.3, 26),
//            intake.stageFlipOut(0.1),
//            intake.moveTime(1,2)
//
//    );
//    AutoModule ThirdBlock = new AutoModule(
//            intake.stageFlipAlmost(0.1),
//            liftIntake.stageLift(0.3, 23),
//            intake.stageFlipOut(0.1),
//            intake.moveTime(1,2)
//
//    );
//    AutoModule ExtendLiftThird = new AutoModule(
//            intake.stageFlipHalf(0.05),
//            liftIntake.stageLift(0.3, 5)
//
//    );
//    AutoModule ThirdOut = new AutoModule(
//            liftIntake.stageLift(0.3, 15),
//            intake.stageFlipAlmostOut(0.1),
//            intake.moveTime(-1,2)
//    );

    AutoModule Ram = new AutoModule(
            claw.stageReady(0.5),
            claw.stageDisable(0.05)
    ).setStartCode(bucket::specimen);

    AutoModule LiftAway = new AutoModule(
            claw.stageSqueeze(0.15),
            liftOuttake.moveTime(0.7, 0.3),
            claw.stageHold(0.05),
            liftOuttake.stageLift(0.35, 44),
            claw.stageDisable(0.05)
    );

    @Override
    public void define() {
        addCustomCode(intake::flipAlmost);
        addAutoModule(Up);
        addWaypoint(0.6, -5, 45, 0);
        addWaypoint(0.25, -5, 70, 0);
        addTimedSetpoint(0.1, 0.42, -5, 87, 0);
        addAutoModule(Specimen);
        addTimedSetpoint(0.05, 0.10, -5, 90, 0);
        addPause(0.19);
        addWaypoint(1.0,40,70,90);
        addWaypoint(1,80,80,145);
        addWaypoint(1,90,105,180);
        addWaypoint(1,120,118,180);
        addWaypoint(1,120,30,185);
        addWaypoint(1,120,100,180);
        addWaypoint(1,145,114,180);
        addTimedSetpoint(1,0.3,145,40,180);
        addTimedSetpoint(1,0.3,120,40,180);

//        addTimedSetpoint(0.09, 0.8, 170, 122, 180);
//        addPause(0.05);
//        addCustomCode(() -> {
//            odometry.resetX(-160);
//            odometry.resetH(180);
//        });
//        addTimedSetpoint(1.0, 0.05, 159, 122, 180);
//        addPause(0.05);
//        addWaypoint(1, 160, 130, 180);
//        addWaypoint(1, 145, 55, 180);


        customNumber(3, i -> {
            if(i == 1){
                x += 3;
            }else if(i == 2){
                x+= 2;
            }else if(i == 3){
                x+= 2.5;
            }
            if(i == 0) {
                addAutoModule(Ram);
                addWaypoint(1.0, 100+x, 35, 180);
                addWaypoint(0.7, 92+x, 20, 180);
            }else{
                addWaypoint(1,40+x,45,170);
                addWaypoint(0.8, 86+x, 40, 180);
                addAutoModule(Ram);
                addWaypoint(0.8, 84+x, 20, 180);
            }
            // 0.35
            addTimedSetpoint(0.15, 0.5, 92+x,15,180);
            addTimedSetpoint(0.15, 0.4, 92+x,-7,180);

            addAutoModule(LiftAway);
            addPause(0.4);
//            addCustomCode(()->{
//
//                log.show("X", Math.round(1000.0*odometry.getX())/1000.0);
//                log.show("Y", Math.round(1000.0*odometry.getY())/1000.0);
//                log.show("Heading", Math.round(1000.0*odometry.getHeading())/1000.0);
//                log.show("Y", odometry.getY());
//                log.show("Hoff", odometry.hOffset);
//                log.show("Headin", odometry.getHeading());
//                log.show("H", odometry.h);
//            });

            double delta;
            if(i == 0){
                delta = -8;
            }else {
                delta = 3.7 * i;
            }

            addWaypoint(0.6,60+x+delta,35,30);
            addWaypoint(0.6,15+x+delta,50,0);
            addWaypoint(0.5, 10+x+delta, 50, 0);
            addWaypoint(0.6, 10+x+delta, 70, 0);
            addTimedSetpoint(0.1, 0.38, 5 + x + delta, 90, 0);

            if (i<2){
                addAutoModule(SpecimenNoHold);
            }
            else{
            addAutoModule(SpecimenNoHoldFinal);}
        addTimedSetpoint(0.05, 0.17, 5 + x + delta, 100, 0);

        addPause(0.2);

            addCustomCode(() -> {
                if(i < 2) {
                    odometry.resetY(-80);
                }
            });
            //addTimedSetpoint(0.1, 0.5, 5 + x + delta, 100, 0);
            if(i < 2){
                addWaypoint(0.6,-5 + x + delta,70,170);
            }else{
//                addPause(0.3);
                addWaypoint(1.0,5 + x + delta,70,0);
                //add claw hold
                addWaypoint(1.0,100,20,45);
            }
        });






//
//        addCustomCode(() -> {
//            log.show("Time", timer.seconds());
//        }, 10);










//        addWaypoint(0.8, 20, 60, 45);
//        addAutoModule(ResetWithFirstBlock);
//        addWaypoint(0.8, 60,60,90);
//        addTimedSetpoint(0.6, 1.4,100, 60, 145);
//        addAutoModule(ExtendLift);
//        addPause(0.3);
//        addTimedSetpoint(0.3, 1.4, 101, 61, 30);
//        addAutoModule(SecondBlock);
//        addTimedSetpoint(0.3, 3, 107, 57, 130);
//        addAutoModule(ExtendLiftSecond);
//        addTimedSetpoint(0.3, 3, 108, 58, 25);
//
//        addWaypoint(0.3, 118, 48, 160);
//        addAutoModule(ThirdBlock);
//        addTimedSetpoint(0.3, 3, 143, 48, 150);
//        addAutoModule(ExtendLiftThird);
//        addPause(1);
//        addTimedSetpoint(0.3, 2, 143, 48, 0);
//        addAutoModule(ThirdOut);
//        addPause(3);




    }


    @Override
    public void postProcess() { autoPlane.reflectY(); autoPlane.reflectX(); }
}