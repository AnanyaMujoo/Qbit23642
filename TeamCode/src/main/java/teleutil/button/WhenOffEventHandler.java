package teleutil.button;

import teleutil.GamepadHandler;
import util.codeseg.CodeSeg;

public class WhenOffEventHandler extends OnPressEventHandler {
    /**
     * Occurs (multiple times) when the toggler is off
     */


    /**
     * Is the button on?
     */
    public boolean on = false;

    /**
     * Code to run when on
     */
    private final CodeSeg runWhenOn;

    /**
     * Call the super constructor and and set the codeseg
     * @param button
     * @param cs
     * @param gph
     */
    public WhenOffEventHandler(Button button, CodeSeg cs, GamepadHandler gph) {
        super(button, cs, gph);
        runWhenOn = cs;
        this.codeToRun = () -> on = !on;
    }

    /**
     * Run the code when on
     */
    @Override
    protected void run() {
        super.run();
        if (!on) runWhenOn.run();
    }
}
