package teleutil.button;

import java.util.Objects;

import teleutil.GamepadHandler;
import util.codeseg.CodeSeg;
import util.codeseg.ReturnCodeSeg;


public class ButtonEventHandler {
    /**
     * Handles a specific event for a specific button
     * NOTE: Default behavior -> Occurs (multiple times) when the button is currently pressed
     */


    /**
     * Code seg to run
     */
    protected CodeSeg codeToRun;
    /**
     * Button to handle (when the button event state changes the code seg runs)
     */
    public Button button;
    /**
     * Gamepad handler
     */
    protected GamepadHandler gph;

    protected ReturnCodeSeg<Boolean> extraCondition = () -> true;

    /**
     * Was the button held?
     */
    boolean wasHeld = false;

    /**
     * Constructor, pass in button, code to run, and gamepad handler
     * @param button
     * @param codeToRun
     * @param gph
     */
    public ButtonEventHandler(Button button, CodeSeg codeToRun, GamepadHandler gph) {
        this.button = button; this.codeToRun = codeToRun; this.gph = gph;
    }

    public void runAndUpdate() { run(); wasHeld = this.pressed(); }

    protected boolean eventOccurred() { return pressed(); }

    public ButtonEventHandler setExtraCondition(ReturnCodeSeg<Boolean> condition){ extraCondition = condition; return this; }

    /**
     * When the event occurs run the code
     */
    protected void run() {
        if (eventOccurred()) {
            codeToRun.run();
        }
    }

    /**
     * Is the button pressed?
     * @return isPressed
     */
    protected boolean pressed() { return Objects.requireNonNull(gph.pressedMap.get(button)).run() && extraCondition.run(); }

    /**
     * Get the value of the button (only if trigger or similar)
     * @return value
     */
    protected double getValue() { return Objects.requireNonNull(gph.valueMap.get(button)).run(); }
}
