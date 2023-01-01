package teleutil.independent;

import autoutil.AutoFramework;
import util.codeseg.ParameterCodeSeg;

public abstract class Independent extends AutoFramework {
    /**
     * Class to run an auton program in teleop, independent of teleop
     */


    /**
     * Should the independent exit?
     */
    private volatile boolean shouldExit = false;

    /**
     * Set to default config, can be overriden, reset should exit
     */
    @Override
    public final void initialize() { setConfig(mecanumDefaultConfig); shouldExit = false; }

    /**
     * Add should exit to the exit condition
     * @return oldCondition and should exit
     */
    @Override
    public boolean condition() { return super.condition() && !shouldExit; }

    /**
     * Set should exit to true
     */
    public void exit(){ shouldExit = true; }

    /**
     * Reset should exit and the auton
     */
    @Override
    public final void reset(){ super.reset(); shouldExit = false; }
}
