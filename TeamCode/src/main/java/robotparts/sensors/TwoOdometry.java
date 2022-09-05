package robotparts.sensors;

import geometry.position.Vector2;
import robotparts.electronics.input.IEncoder;
import robotparts.unused.TankOdometry;

import static global.General.bot;
import static robot.RobotFramework.odometryThread;

// TOD4
// make proper reset method
public class TwoOdometry extends TankOdometry {

    private volatile IEncoder horizontalEncoder;
    private volatile IEncoder verticalEncoder;

    private volatile double startHorz = 0;
    private volatile double startVert = 0;


    private volatile double lastHorizontalEncoderPos;
    private volatile double lastVerticalEncoderPos;

    private volatile Vector2 positionOdometryCenter = new Vector2(0,0);
    private volatile Vector2 positionRobotCenter = new Vector2(0,0);
    private volatile Vector2 localOdometryCenterOffset;
    private volatile double heading = 0;

    public TwoOdometry() {
        super(0.0);
        localOdometryCenterOffset = new Vector2(0.0, 12.6);
    }

    public synchronized void reset(){
        resetOnce();
        resetOnce();
    }

    public synchronized void resetOnce(){
        positionOdometryCenter = new Vector2(0,0);
        positionRobotCenter = new Vector2(0,0);
        localOdometryCenterOffset = new Vector2(0.0, 12.6);
        horizontalEncoder.reset();
        verticalEncoder.reset();
        startHorz = getHorizontalEncoderPositionRaw();
        startVert = getVerticalEncoderPositionRaw();
        lastHorizontalEncoderPos = 0;
        lastVerticalEncoderPos = 0;
        heading = 0;
        bot.gyro.reset();
    }


    @Override
    public void init() {
        horizontalEncoder = createEncoder("bl", "cEnc", IEncoder.EncoderType.NORMAL);
        verticalEncoder = createEncoder("fl", "rEnc", IEncoder.EncoderType.NORMAL);
        horizontalEncoder.reset();
        verticalEncoder.reset();
        lastHorizontalEncoderPos = horizontalEncoder.getPos();
        lastVerticalEncoderPos = verticalEncoder.getPos();
        odometryThread.setExecutionCode(this::update);
    }

    public double getHorizontalEncoderPosition(){
        return getHorizontalEncoderPositionRaw()-startHorz;
    }

    public double getVerticalEncoderPosition(){
        return getVerticalEncoderPositionRaw()-startVert;
    }


    public double getHorizontalEncoderPositionRaw(){
        return horizontalEncoder.getPos();
    }

    public double getVerticalEncoderPositionRaw(){
        return -verticalEncoder.getPos();
    }

    private double getLocalHorizontalDelta(){
        double delta = getHorizontalEncoderPosition() - lastHorizontalEncoderPos;
        lastHorizontalEncoderPos = getHorizontalEncoderPosition();
        return ticksToCm(delta);
    }

    private double getLocalVerticalDelta(){
        double delta = getVerticalEncoderPosition() - lastVerticalEncoderPos;
        lastVerticalEncoderPos = getVerticalEncoderPosition();
        return ticksToCm(delta);
    }

    public double ticksToCm(double ticks) {
        return ticks/8192 * 3.5 * Math.PI;
    }

    private void updateHeading(){
        heading = bot.gyro.getRightHeadingRad();
    }

    public void updatePosition(){
        Vector2 localDelta = new Vector2(getLocalHorizontalDelta(), getLocalVerticalDelta());
        Vector2 globalDelta = localDelta.getRotated(-heading);
        positionOdometryCenter.add(globalDelta);
        Vector2 globalOdometryCenterOffset = localOdometryCenterOffset.getRotated(heading);
        positionRobotCenter = positionOdometryCenter.getAdded(globalOdometryCenterOffset).getAdded(localOdometryCenterOffset.getNegative());
    }

    public void update(){
        updateHeading();
        updatePosition();
    }

    public double getCurX(){
        return positionRobotCenter.getX();
    }
    public double getCurY(){
        return positionRobotCenter.getY();
    }
    public double getCurThetaRad(){
        return heading;
    }

    @Override
    public double[] getPose(){
        return new double[]{getCurX(), getCurY(), getCurThetaRad()};
    }

}