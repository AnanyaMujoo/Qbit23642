package teleutil.button;

import teleutil.GamepadHandler;
import util.codeseg.CodeSeg;

public class OnTurnOffEventHandler extends OnPressEventHandler {
    /**
     * Occurs (once) when the toggler is turned off
     */


    /**
     * Is the button on?
     */
    public boolean on = false;

    /**
     * When the button is toggled change the state of on and run the code if off
     * @param button
     * @param cs
     * @param gph
     */
    public OnTurnOffEventHandler(Button button, CodeSeg cs, GamepadHandler gph) {
        super(button, cs, gph);
        this.codeToRun = () -> {
            on = !on;
            if (!on) {
                cs.run();
            }
        };
    }
}
