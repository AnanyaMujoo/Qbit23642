package auton.old;

import autoutil.executors.MecanumExecutorArcsPID;
import elements.FieldSide;

import static java.lang.Math.*;
import static global.General.*;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
@Autonomous(name="TerraAutoBlueMec")
public class TerraAutoBlueMec extends CompleteAuto {

    boolean inEndMode = false;

    @Override
    public FieldSide getSide() { return FieldSide.BLUE; }

    @Override
    public void defineExecutorAndAddPoints() {
        executor = new MecanumExecutorArcsPID(this);
        addExecutorFuncs(
//                setPoint(70, 30, PI/2),
//                unsyncedRF(MecanumAutoModules.SpinCarousel),
//                setPoint(-50, 43, -PI/2),
//                setPoint(-60, 30, PI),
//                unsyncedRF(MecanumAutoModules.SetUpForAllianceShippingHub),
//                unsyncedRF(MecanumAutoModules.Release),
//                setPoint(-90, 0, -PI/2),
//                unsyncedRF(MecanumAutoModules.IntakeAndMoveForwardUntilFreight)
        );
        for (int i = 0; i < 2; i++) {
            addExecutorFuncs(
//                syncedRF(MecanumAutoModules.SetUpForAllianceShippingHub),
//                setPoint(-90, 0, -PI/2),
//                setPoint(-60, 30, PI),
//                unsyncedRF(MecanumAutoModules.Release),
//                setPoint(-90, 0, -PI/2),
//                unsyncedRF(MecanumAutoModules.IntakeAndMoveForwardUntilFreight)
            );
        }
    }

    @Override
    public void duringLoop() {
        if (!inEndMode && gameTime.seconds() > 25) {
            inEndMode = true;
            executor = new MecanumExecutorArcsPID(this);
            addExecutorFuncs(
                setPoint(bot.odometry.getCurX(), bot.odometry.getCurY(), bot.odometry.getCurThetaRad()),
                setPoint(-90, 0, -PI/2),
                setPoint(-100, 0, -PI/2)
            );
            executor.complete();
            executor.resumeMove();
        }
    }

    @Override
    public void onEnd() {
        bot.mecanumDrive.move(0, 0, 0);
    }
}
