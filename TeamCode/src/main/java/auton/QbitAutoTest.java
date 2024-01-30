package auton;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.AutoFramework;
import autoutil.vision.CaseScannerRectBottom;
import elements.Field;
import elements.FieldPlacement;
import elements.FieldSide;
import geometry.position.Pose;

import static global.General.fieldPlacement;
import static global.General.fieldSide;


@Autonomous(name = "QbitAutoTest", group = "Autonomous")
public class QbitAutoTest extends AutoFramework {
    {
        fieldSide = FieldSide.BLUE;
        fieldPlacement = FieldPlacement.LOWER;
        startPose = new Pose(20.5, Field.width / 2.0 - 1.5*Field.tileWidth, 90);
    }


    @Override
    public void initialize() {
        setConfig(mecanumNonstopConfig);
        lift.maintain();
        wait(0.5);
        scan(new CaseScannerRectBottom(), true, "blue", "left");
    }


    @Override
    public void define() {

        addTimedSetpoint(1.0, 0.6, 1.0, 0, 40, 0);
        addTimedSetpoint(1.0, 0.6, 1.0, -20, 40, 0);
        addTimedSetpoint(1.0, 0.6, 1.0, -20, 40, 90);

//        addSegment(0.5, mecanumDefaultWayPoint, 0, 90, 0);
//
//        customFlipped(() -> {
//            addTimedSetpoint(1.0,0.5,1.5, -68,81,56);
//        }, () -> {
//            addTimedSetpoint(1.0,0.5,1.5, -68,81,45);
//        });
//        addConcurrentAutoModuleWithCancel(TerraAutoRam.BackwardFirst, 2.0);
//        addConcurrentAutoModuleWithCancel(TerraAutoRam.Forward, 1.2);

//        addTimedSetpoint(1.0, 0.5,0.6, -57, 70, 90);
//        addConcurrentAutoModule(new AutoModule(outtake.stage(0.2, 0.1), outtake.stageOpenComp(0.0),  lift.stageLift(1.0,  -0.5)));
//        customCase(() -> {
//            addTimedSetpoint(1.0, 0.5,1.0, -57, 70, 0);
//        }, () -> {
//            addSegment(0.5, noStopNewSetPoint, 0, 70, 90);
//            addTimedSetpoint(1.0, 0.5,1.0, 0, 70, 0);
//        }, () -> {
//            addSegment(0.5, noStopNewSetPoint, 58, 70, 90);
//            addTimedSetpoint(1.0, 0.5,1.0, 58, 70, 0);
//        });
//        addPause(0.1);

    }


    @Override
    public void postProcess() {
//        autoPlane.reflectY(); autoPlane.reflectX();
    }
}
