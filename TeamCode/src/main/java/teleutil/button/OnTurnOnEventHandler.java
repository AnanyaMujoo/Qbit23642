package teleutil.button;

import teleutil.GamepadHandler;
import util.codeseg.CodeSeg;

public class OnTurnOnEventHandler extends OnPressEventHandler {
    /**
     * Occurs (once) when the toggler turns on
     */


    /**
     * Is the button on?
     */
    public boolean on = false;

    /**
     * When the button is toggled change the state of on and run the code if on
     * @param button
     * @param cs
     * @param gph
     */
    public OnTurnOnEventHandler(Button button, CodeSeg cs, GamepadHandler gph) {
        super(button, cs, gph);
        this.codeToRun = () -> {
            on = !on;
            if (on) {
                cs.run();
            }
        };
    }
}
