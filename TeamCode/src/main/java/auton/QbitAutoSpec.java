package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import automodules.AutoModule;
import autoutil.AutoFramework;
import elements.Case;
import elements.Field;
import elements.FieldPlacement;
import elements.FieldSide;
import elements.GameItems;
import geometry.position.Pose;
import global.Modes;
import robotparts.RobotPart;
import util.User;
import util.template.Precision;

import static global.General.bot;
import static global.General.fieldPlacement;
import static global.General.fieldSide;
import static global.General.log;
import static global.Modes.Height.HIGH;


//@Autonomous(name = "QbitAutoSpec", group = "auto", preselectTeleOp = "TerraOp")
@Autonomous(name = "QbitAutoSpec", group = "auto")
public class QbitAutoSpec extends AutoFramework {

    @Override
    public void initialize() {
        setConfig(mecanumMixedConfig);
        lift.maintain();
//        wait(0.1);
    }

    AutoModule Up = new AutoModule(
            claw.stageHold(0.05),
            liftOuttake.stageLift(0.8, 42)
    );

    AutoModule Specimen = new AutoModule(
            liftOuttake.moveTime(-0.5, 0.3),
            claw.stageReady(0.3)
    );
    AutoModule Reset = new AutoModule(
            intake.stageFlipAlmostOut(0.1),
            claw.stageHold(0.05),
            liftOuttake.stageDown(-0.3,0)
    );

    AutoModule ResetWithFirstBlock = new AutoModule(
            intake.stageFlipAlmostOut(0.05),
            claw.stageHold(0.05),
            liftIntake.stageLift(0.6, 15),
            liftOuttake.stageDown(-0.4,0)
    );



    AutoModule FirstBlock = new AutoModule(
            liftIntake.stageLift(0.3, 15),
            intake.stageFlipOut(0.05),
            intake.moveTime(1,1)
        );

    AutoModule ExtendLift = new AutoModule(
            intake.stageFlipOut(0.05),
            intake.moveTime(1,0.3),
            liftIntake.stageLift(0.3, 35),
            intake.stageFlipAlmostOut(0.05),
            intake.moveTime(-1,0.5),
            intake.stageFlipHalf(0.1)

    );

    AutoModule ExtendLiftSecond = new AutoModule(
            liftIntake.stageLift(0.3, 35),
            intake.stageFlipAlmostOut(0.05),
            intake.moveTime(-1,2),
            intake.stageFlipHalf(0.1),
            liftIntake.stageLift(0.3,20)

    );
    AutoModule SecondBlock = new AutoModule(
            intake.stageFlipHalf(0.1),
            liftIntake.stageLift(0.3, 26),
            intake.stageFlipOut(0.1),
            intake.moveTime(1,2)

    );
    AutoModule ThirdBlock = new AutoModule(
            intake.stageFlipAlmost(0.1),
            liftIntake.stageLift(0.3, 23),
            intake.stageFlipOut(0.1),
            intake.moveTime(1,2)

    );
    AutoModule ExtendLiftThird = new AutoModule(
            intake.stageFlipHalf(0.05),
            liftIntake.stageLift(0.3, 5)

    );
    AutoModule ThirdOut = new AutoModule(
            liftIntake.stageLift(0.3, 15),
            intake.stageFlipAlmostOut(0.1),
            intake.moveTime(-1,2)
    );
    @Override
    public void define() {
        addAutoModule(Up);
        addWaypoint(0.6, 0, 60, 0);
        addTimedSetpoint(0.2, 0.7, 0, 85, 0);
        addAutoModule(Specimen);
        addPause(0.3);
        addWaypoint(0.8, 20, 60, 45);
        addAutoModule(ResetWithFirstBlock);
        addWaypoint(0.8, 60,60,90);
        addTimedSetpoint(0.6, 1.4,100, 60, 145);
        addAutoModule(ExtendLift);
        addPause(0.3);
        addTimedSetpoint(0.3, 1.4, 101, 61, 30);
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