package auton;

import static global.General.log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import java.util.ArrayList;

import automodules.AutoModule;
import autoutil.AutoFramework;
import geometry.position.Pose;
import global.Modes;
import util.template.Iterator;


@Autonomous(name = "QbitAutoSamp", group = "auto")
public class QbitAutoSamp extends AutoFramework {

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
            intake.stageFlipHalf(0.1),
            claw.stageHold(0.05),
            bucket.stageBottom(0.05),
            liftOuttake.stageLift(0.9,93.8),
            claw.stageSqueeze(0.05)
    );

    AutoModule Dep = new AutoModule(
            bucket.stageTop(0.9),
            claw.stageHold(0.05),
            bucket.stageBottom(0.3),
            liftOuttake.stageDown(-0.85, 0)

    );
    AutoModule In = new AutoModule(
            liftIntake.stageLift(0.9,5),
            intake.stageOpen(0.05),
            bucket.stageBottom(0.05),
            intake.stageFlipOut(0.05),
            intake.moveUntilColor(),
            intake.stagepPickUp(),
            liftIntake.stageDown(0.6, 0),
            intake.stageFlipIn(0.05),
            intake.stageOpen(0.05),
            intake.moveTime(1,1.0),
            intake.stageFlipHalf(0.05)
    );

    AutoModule Specimen = new AutoModule(
            liftOuttake.stageDown(0.7, 33),
            claw.stageReady(0.3),
            claw.stageHold(0.05),
            liftOuttake.stageDown(-0.7,3),
            liftIntake.stageUp(0.7,5)

    );

    AutoModule SpecimenNoHold = new AutoModule(
            liftOuttake.stageDown(0.7, 33),
            claw.stageReady(0.2),
            liftOuttake.stageDown(-0.7,3)
    );

    AutoModule Ram = new AutoModule(
            claw.stageReady(0.3),
            claw.stageDisable(0.05)
    ).setStartCode(bucket::specimen);

    AutoModule LiftAway = new AutoModule(
            claw.stageSqueeze(0.15),
            liftOuttake.moveTime(1.0, 0.1),
            claw.stageHold2(0.05),
            liftOuttake.stageLift(1.0, 44),
            claw.stageDisable(0.05)
    );

    @Override
    public void define() {
//        addCustomCode(intake::flipAlmost);
        addAutoModule(Up);
        addTimedSetpoint(0.5, 2, -34, 24, -45);
        addTimedSetpoint(0.5, 0.5, -54, 4, -45);
        addAutoModule(Dep);

        addPause(0.4);

        addTimedSetpoint(0.5, 0.4, -34, 24, -45);
        addTimedSetpoint(0.5, 1, -22, 55, 0);
        addAutoModuleDONOTUSE(In);
        addWaypoint(0.7, -22, 80, 0);
        addWaypoint(1, -22, 50, 0);
        addAutoModule(Up);
        addTimedSetpoint(0.5, 1.8, -34, 24, -45);
        addTimedSetpoint(0.5, 0.5, -55, 3, -45);
        addAutoModule(Dep);
        addPause(0.4);

        addTimedSetpoint(0.5, 0.4, -34, 24, -45);
        addTimedSetpoint(0.5, 1, -45, 55, 0);
        addAutoModuleDONOTUSE(In);
        addWaypoint(0.7, -44, 80, 0);
        addWaypoint(1, -44, 50, 0);
        addAutoModule(Up);
        addTimedSetpoint(0.5, 1.8, -34, 24, -45);
        addTimedSetpoint(0.5, 0.5, -55, 3, -45);

        addAutoModule(Dep);

        addPause(0.4);

        addTimedSetpoint(0.5, 0.4, -34, 24, -45);
        addTimedSetpoint(0.5, 1, -54, 65, 30);
        addAutoModuleDONOTUSE(In);
//        addWaypoint(0.7, -30, , 45);
        addWaypoint(1, -35, 40, 30);
        addAutoModule(Up);
        addTimedSetpoint(0.5, 1.8, -36, 27, -45);
        addTimedSetpoint(0.5, 0.5, -57, 6, -45);
        addAutoModule(Dep);
        addPause(0.45);

        addTimedSetpoint(0.5, 3, -34, 24, -45);


//        addWaypoint(1.0, -5, 45, 0);



    }
}