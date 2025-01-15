package auton;

import static global.General.log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import automodules.AutoModule;
import autoutil.AutoFramework;
import geometry.position.Pose;


//@Autonomous(name = "QbitAutoSpec", group = "auto", preselectTeleOp = "TerraOp")
@Autonomous(name = "QbitAutoSpec", group = "auto")
public class QbitAutoSpec extends AutoFramework {

    @Override
    public void initialize() {
        setConfig(mecanumMixedConfig);
        liftDONOTUSE.maintain();
//        wait(0.1);
    }

    AutoModule Up = new AutoModule(
            intake.stageFlipAuto(0.05),
            claw.stageHold(0.05),
            liftOuttake.stageLift(0.9, 42)
    );

    AutoModule Specimen = new AutoModule(
            liftOuttake.stageDown(0.3, 32),
            claw.stageReady(0.3),
            claw.stageHold(0.05),
            liftOuttake.stageDown(-0.3,0)
    );

    AutoModule SpecimenNoHold = new AutoModule(
            liftOuttake.stageDown(0.3, 32),
            claw.stageReady(0.2),
            liftOuttake.stageDown(-0.6,0)
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
            bucket.stageSpecimen(0.05),
            claw.stageReady(0.3),
            claw.stageDisable(0.05)
    );
    AutoModule LiftAway = new AutoModule(
            claw.stageSqueeze(0.3),
            liftOuttake.moveTime(1.0, 0.2),
            claw.stageHold(0.05),
            liftOuttake.stageLift(0.8, 42)
    );

    @Override
    public void define() {
        addAutoModule(Up);
        addWaypoint(0.6, 0, 60, 0);
        addTimedSetpoint(0.2, 0.7, 0, 85, 0);
        addAutoModule(Specimen);
        addPause(0.3);
//        addAutoModule(Reset);
        addWaypoint(0.8,40,60,45);
        addWaypoint(0.8,70,60,135);
        addWaypoint(0.8,90,100,180);
        addWaypoint(0.8,120,130,180);
        addWaypoint(0.8,120,30,180);
        addWaypoint(0.8,120,100,180);
        addWaypoint(0.8,145,130,180);
        addWaypoint(0.8,145,30,180);
        addWaypoint(0.8,145,100,180);
        addTimedSetpoint(0.4, 0.7, 170,130,180);
        addCustomCode(() -> odometry.resetX(-155), 0.1);
        addWaypoint(0.8,153,40,180);
        addAutoModule(Ram);
        addWaypoint(0.8,100,45,180);
        addTimedSetpoint(0.4,0.7, 92,-3,180);
        addAutoModule(LiftAway);
        addPause(0.5);
        addWaypoint(0.8,60,35,90);
        addWaypoint(0.8,5,50,0);
        addTimedSetpoint(0.2, 1.0, 5, 90, 0);
        addCustomCode(() -> odometry.resetY(80), 0.1);
        addAutoModule(SpecimenNoHold);
        addPause(0.3);
        addWaypoint(0.8,10,50,0);
        addWaypoint(0.6,60,35,90);
        addAutoModule(Ram);
        addTimedSetpoint(0.3,1.2, 92,-5,180);
        addAutoModule(LiftAway);
        addPause(0.5);
        addWaypoint(0.8,60,35,90);
        addWaypoint(0.8,10,50,0);
        addTimedSetpoint(0.2, 1.0, 10, 90, 0);
        addAutoModule(SpecimenNoHold);
        addPause(0.3);
        addWaypoint(0.8,15,50,0);
        addWaypoint(0.8,30,50,0);
        addWaypoint(0.6,80,40,90);
        addAutoModule(Ram);
        addTimedSetpoint(0.3,1.0, 92,-5,180);
        addAutoModule(LiftAway);
        addPause(0.5);
        addWaypoint(0.8,60,35,90);
        addWaypoint(0.8,15,50,0);
        addTimedSetpoint(0.2, 1.0, 15, 90, 0);
        addAutoModule(SpecimenNoHold);
        addPause(0.3);
        addWaypoint(0.8,15,50,0);
















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