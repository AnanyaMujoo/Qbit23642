package teleutil.button;

import teleutil.GamepadHandler;
import util.codeseg.CodeSeg;

public class WhenOnEventHandler extends OnPressEventHandler {
    /**
     * Occurs (multiple times) when the toggler is on
     */


    /**
     * Is the button on?
     */
    public boolean on = false;

    /**
     * Run the code when on
     */
    private final CodeSeg runWhenOn;

    /**
     * Run the code button
     * @param button
     * @param cs
     * @param gph
     */
    public WhenOnEventHandler(Button button, CodeSeg cs, GamepadHandler gph) {
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
        if (on) runWhenOn.run();
    }
}
