package unittests.auto.framework.movement;

import autoutil.AutoFramework;
import unittests.auto.AutoUnitTest;


public class OdometryTest {
    private static final double distance = 200;
    private static final double power = 0.1;
    public static class ForwardTest extends AutoUnitTest { @Override protected void start() { setAuto(new AutoFramework() { @Override public void initialize() { setConfig(mecanumDefaultConfig); } @Override  public void define() {
        addSetpoint(0.1, power, 0, distance, 0);
    }});}}
    public static class StrafeTest extends AutoUnitTest { @Override protected void start() { setAuto(new AutoFramework() { @Override public void initialize() { setConfig(mecanumDefaultConfig); } @Override  public void define() {
        addSetpoint(0.1, power, distance, 0, 0);
    }});}}
    public static class TurnTest extends AutoUnitTest { @Override protected void start() { setAuto(new AutoFramework() { @Override public void initialize() { setConfig(mecanumDefaultConfig); } @Override  public void define() {
        addSetpoint(0.1, power, 0, 0, 180);
    }});}}
}
