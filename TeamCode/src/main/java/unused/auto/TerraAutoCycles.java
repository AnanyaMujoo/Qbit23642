package unused.auto;

import autoutil.AutoFramework;
import robot.BackgroundTask;

import static global.General.bot;


public class TerraAutoCycles extends AutoFramework {

    @Override
    public void initAuto() {
//        setConfig(mecanumDefaultConfig());
        scan();
        bot.addBackgroundTask(new BackgroundTask(() -> {
            mecanumLift.holdPosition();
            mecanumIntake.move(-0.8);
        }));
        mecanumIntake.scale = 0.6;
    }

    @Override
    public void define() {
//        mecanumOuttake.midCap();
//        addWaypoint(20, 20, -60);
//        customNumber(5, i -> {
//            addCancelAutoModules();
//            customCase(CaseOld.RIGHT, () -> {
//                addConcurrentAutoModule(AllianceLiftUp(LiftUpTopFast));
//                if(i==0) {
//                    addSetpoint(30, 47, -137);
//                }else if(i>0){
//                    addSetpoint(30+i, 35+i, -140);
//                }
//            }, CaseOld.CENTER, () -> {
//                addConcurrentAutoModule(AllianceLiftUp(LiftUpMiddleFast));
//                addPause(0.5);
//                addSetpoint(37, 54, -137);
//            }, CaseOld.LEFT, () -> {
//                addConcurrentAutoModule(AllianceLiftUp(LiftUpBottomFast));
//                addPause(0.5);
//                addSetpoint(44, 61, -137);
//            });
//            addCancelAutoModules();
//            addConcurrentAutoModule(ResetLiftAndOuttake);
//            addPause(0.6);
//            addWaypoint(45, 25, -115);
//            addWaypoint(45, -10, -95);
//            addWaypoint(0, -10, -95);
//            if(i < 4) {
//                addConcurrentAutoModule(IntakeCombined);
//                addWaypoint(-50 - (3*i), -10, -90);
//                addPause(0.5);
//                addWaypoint(0, -10, -85);
//                addWaypoint(35, -10, -85);
//                addWaypoint(35, 25, -115);
//                addPause(0.1);
//            }
//            caseDetected = Case.FIRST;
//        });
//        addCancelAutoModules();
//        addWaypoint(-45, -10, -90);
//        addSetpoint(-45, 50, -90);
    }

//
//    @Autonomous(name = "TerraAutoCyclesBlue", group = "auto")
//    public static class TerraAutoCyclesBlue extends TerraAutoCycles {{ fieldSide = FieldSide.BLUE; }}
//    @Autonomous(name = "TerraAutoCyclesRed", group = "auto")
//    public static class TerraAutoCyclesRed extends TerraAutoCyclesBlue {{ fieldSide = FieldSide.RED; }}
}
