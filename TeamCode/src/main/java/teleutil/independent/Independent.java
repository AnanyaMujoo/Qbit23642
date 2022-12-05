package teleutil.independent;

import autoutil.AutoFramework;
import util.codeseg.ParameterCodeSeg;

public abstract class Independent extends AutoFramework {
    private volatile boolean shouldExit = false;

    @Override
    public final void initAuto() { setConfig(mecanumDefaultConfig); shouldExit = false; }

    @Override
    public boolean condition() { return super.condition() && !shouldExit; }

    public void exit(){ shouldExit = true; }

    @Override
    public final void reset(){ super.reset(); shouldExit = false; }
}
