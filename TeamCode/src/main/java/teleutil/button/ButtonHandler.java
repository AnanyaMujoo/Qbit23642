package teleutil.button;

import java.util.ArrayList;

import teleutil.GamepadHandler;
import util.ExceptionCatcher;
import util.codeseg.CodeSeg;
import util.condition.Expectation;
import util.condition.Magnitude;

import static global.General.*;


public class ButtonHandler {
    /**
     * Handles a specific button
     */
    private final Button button;
    /**
     * Arraylist of button event handlers
     */
    private final ArrayList<ButtonEventHandler> eventHandlers = new ArrayList<>();

    /**
     * Gamepad handler to use
     */
    private final GamepadHandler gph;

    /**
     * Constructor using the button and gamepad handler
     * @param b
     * @param gph
     */
    public ButtonHandler(Button b, GamepadHandler gph) {
        button = b; this.gph = gph;
    }

    /**
     * Add an event using the type of event handler and the codeseg to run
     * @param type
     * @param codeToRun
     * @param <T>
     */
    public <T> void addEvent(Class<T> type, CodeSeg codeToRun) {
        fault.check("YOU USED BUTTON HANDLER IN LOOP", Expectation.UNEXPECTED, Magnitude.CATASTROPHIC, eventHandlers.size() < 50, true);
        ExceptionCatcher.catchNewInstance(() -> {
            T obj = type
                .getDeclaredConstructor(Button.class, CodeSeg.class, GamepadHandler.class)
                .newInstance(button, codeToRun, gph);
            eventHandlers.add((ButtonEventHandler) obj);
        });

    }

    /**
     * Run the code for each event handler
     */
    public void run() {
        for (ButtonEventHandler handler : eventHandlers) {
            handler.runAndUpdate();
        }
    }
}
