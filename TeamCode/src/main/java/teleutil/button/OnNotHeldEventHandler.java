package teleutil.button;

import teleutil.GamepadHandler;
import util.codeseg.CodeSeg;

public class OnNotHeldEventHandler extends ChangeHoldEventHandler {
    /**
     * Occurs (once) when the user releases the button
     */


    /**
     * Call the super constructor
     * @param button
     * @param codeSeg
     * @param gph
     */
    public OnNotHeldEventHandler(Button button, CodeSeg codeSeg, GamepadHandler gph) {
        super(button, codeSeg, gph);
    }

    /**
     * Did the event change and is it not pressed?
     * @return wasReleased
     */
    @Override
    protected boolean eventOccurred() {
        return super.eventOccurred() && !this.pressed();
    }
}
