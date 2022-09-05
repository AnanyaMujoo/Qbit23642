package teleutil.button;

import teleutil.GamepadHandler;
import util.codeseg.CodeSeg;


public class ChangeHoldEventHandler extends ButtonEventHandler {
    /**
     * Occurs (once) when the user either first clicks the button or the user releases the button
     */


    /**
     * Call the super constructor
     * @param button
     * @param codeSeg
     * @param gph
     */
    public ChangeHoldEventHandler(Button button, CodeSeg codeSeg, GamepadHandler gph) { super(button, codeSeg, gph); }

    /**
     * Did the state change?
     * @return stateChange
     */
    @Override
    protected boolean eventOccurred() {
        return this.pressed() != wasHeld;
    }
}

