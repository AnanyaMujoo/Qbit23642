package unused.tankold;

import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IEncoder;
import util.codeseg.ExceptionCodeSeg;

import static java.lang.Math.*;
import static robot.RobotFramework.*;

/**
 * NOTE: Uncommented
 */

// NEW Make odometry good
@Deprecated
public class TankOdometry extends RobotPart {

    private final double ODO1_TO_CENTER_X;

    private double prevOdoOnePos = 0;

    private final ExceptionCodeSeg<RuntimeException> odometryUpdateCode = this::update;

    private IEncoder yEnc;

    public double[] curPos = new double[] { 0, 0, 0 };
    public double[] lastChangePos = new double[] { 0, 0, 0 };

    public TankOdometry() {
        ODO1_TO_CENTER_X = 4.6;
    }

    public TankOdometry(double dis) {
        ODO1_TO_CENTER_X = dis;
    }

    @Override
    public void init() {
        yEnc = create("blEnc", ElectronicType.IENCODER_NORMAL);
        update();
        curPos = new double[] { 0, 0, 0 };
        odometryThread.setExecutionCode(odometryUpdateCode);

    }

    private double getDeltaOdoOne() {
        double delta = -yEnc.getPos() - prevOdoOnePos;
        prevOdoOnePos += delta;
        return ticksToCm(delta);
    }

    private double ticksToCm(double ticks) {
        return ticks/8192 * 3.5 * Math.PI;
    }

    public double getCurX() { return curPos[0]; }
    public double getCurY() { return curPos[1]; }
    public double getCurThetaRad() {
        return curPos[2];
    }

    public void processTheta() {
        curPos[2] %= (2 * Math.PI);
        if (curPos[2] < 0) { curPos[2] += 2 * Math.PI; }
        if (curPos[2] > Math.PI) { curPos[2] -= 2 * Math.PI; }
    }

    public double getCurThetaDeg() {
        return getCurThetaRad() * 180/PI;
    }

    public void update() {
        double[] change = getPosChange();
        curPos[0] += change[0];
        curPos[1] += change[1];
        curPos[2] += change[2];
    }

    public double[] getPosChange() {
        lastChangePos = getPosChangeCenter();
        double[] posChange = new double[3];
        posChange[0] = lastChangePos[0] * cos(curPos[2]) + lastChangePos[1] * cos(curPos[2] + PI/2);
        posChange[1] = lastChangePos[0] * sin(curPos[2]) + lastChangePos[1] * sin(curPos[2] + PI/2);
        posChange[2] = lastChangePos[2];
        return posChange;
    }

    // NOTE: Odometry modules are to the left and to the back of the center of the robot
    public double[] getPosChangeCenter() {
        processTheta();
        double gyroReading = gyro.getRightHeadingRad();
        double dtheta = gyroReading - curPos[2];

        while (abs(dtheta) > PI) {
            if (gyroReading < 0) {
                gyroReading += 2 * PI;
            } else {
                gyroReading -= 2 * PI;
            }
            dtheta = gyroReading - curPos[2];
        }

        double dt = getDeltaOdoOne() - dtheta * ODO1_TO_CENTER_X;
//        double d2 = getDeltaOdoTwo() - theta * (ODO1_TO_CENTER_X + R);

        return new double[] { 0, dt, dtheta };

    }

    public double[] getPose() {
        return curPos;
    }
}
