package teleutil.button;

import teleutil.GamepadHandler;
import util.codeseg.CodeSeg;

public class NotHeldEventHandler extends ButtonEventHandler {
    /**
     * Occurs (multiple times) when the button is not pressed
     */

    /**
     * Call the super constructor
     * @param button
     * @param codeSeg
     * @param gph
     */
    public NotHeldEventHandler(Button button, CodeSeg codeSeg, GamepadHandler gph) {
        super(button, codeSeg, gph);
    }

    /**
     * Opposite of normal button event handler
     * @return not pressed
     */
    @Override
    protected boolean eventOccurred() {
        return !super.eventOccurred();
    }
}
