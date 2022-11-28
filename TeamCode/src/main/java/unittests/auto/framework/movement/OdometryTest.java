package unittests.auto.framework.movement;

import autoutil.AutoFramework;
import unittests.auto.AutoUnitTest;


public class OdometryTest {
    private static final double distance = 100;
    private static final double power = 0.3;
    public static class ForwardTest extends AutoUnitTest { @Override protected void start() { setAuto(new AutoFramework() { @Override public void initAuto() { setConfig(mecanumDefaultConfig); } @Override  public void define() {
        addScaledSetpoint(power, 0, distance, 0);
    }});}}
    public static class StrafeTest extends AutoUnitTest { @Override protected void start() { setAuto(new AutoFramework() { @Override public void initAuto() { setConfig(mecanumDefaultConfig); } @Override  public void define() {
        addScaledSetpoint(power, distance, 0, 0);
    }});}}
    public static class TurnTest extends AutoUnitTest { @Override protected void start() { setAuto(new AutoFramework() { @Override public void initAuto() { setConfig(mecanumDefaultConfig); } @Override  public void define() {
        addScaledSetpoint(power, 0, 0, 180);
    }});}}
}
