package teleutil;

import static global.General.bot;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.Objects;
import java.util.TreeMap;

import automodules.StageList;
import teleutil.button.Button;
import teleutil.button.ButtonEventHandler;
import teleutil.button.ButtonHandler;
import teleutil.button.OnPressEventHandler;
import teleutil.independent.Independent;
import util.codeseg.CodeSeg;
import util.codeseg.ReturnCodeSeg;
import util.condition.DecisionList;

public class GamepadHandler {
    /**
     * Used to handle the gamepads and make using them easier
     * NOTE: Use the link method of this class to link a button to a button event handler to an action
     * Ex: gph1.link(Button.A, OnPressEventHandler.class, <code to run>)
     */


    /**
     * Private gamepad object depending on which gamepad handler this is
     */
    private Gamepad gamepad;
    /**
     * Map from buttons to gamepad buttons
     */
    public final TreeMap<Button, ReturnCodeSeg<Boolean>> pressedMap = new TreeMap<Button, ReturnCodeSeg<Boolean>>() {{
        put(Button.A, () -> gamepad.a);
        put(Button.B, () -> gamepad.b);
        put(Button.X, () -> gamepad.x);
        put(Button.Y, () -> gamepad.y);
        put(Button.RIGHT_BUMPER, () -> gamepad.right_bumper);
        put(Button.LEFT_BUMPER, () -> gamepad.left_bumper);
        put(Button.DPAD_DOWN, () -> gamepad.dpad_down);
        put(Button.DPAD_UP, () -> gamepad.dpad_up);
        put(Button.DPAD_LEFT, () -> gamepad.dpad_left);
        put(Button.DPAD_RIGHT, () -> gamepad.dpad_right);
        put(Button.LEFT_TRIGGER, () -> gamepad.left_trigger > 0.5);
        put(Button.RIGHT_TRIGGER, () -> gamepad.right_trigger > 0.5);
        put(Button.LEFT_STICK_BUTTON, () -> gamepad.left_stick_button);
        put(Button.RIGHT_STICK_BUTTON, () -> gamepad.right_stick_button);
    }};
    /**
     * Map from buttons to gamepad values
     */
    public final TreeMap<Button, ReturnCodeSeg<Float>> valueMap = new TreeMap<Button, ReturnCodeSeg<Float>>() {{
        put(Button.LEFT_TRIGGER, () -> gamepad.left_trigger);
        put(Button.RIGHT_TRIGGER, () -> gamepad.right_trigger);
        put(Button.LEFT_STICK_Y, () -> gamepad.left_stick_y);
        put(Button.LEFT_STICK_X, () -> gamepad.left_stick_x);
        put(Button.RIGHT_STICK_Y, () -> gamepad.right_stick_y);
        put(Button.RIGHT_STICK_X, () -> gamepad.right_stick_x);
    }};
    /**
     * Map of buttons to handlers
     */
    public TreeMap<Button, ButtonHandler> handlerMap = new TreeMap<>();

    /**
     * Constructor to create a gamepad handler
     * @param gp
     */
    public GamepadHandler(Gamepad gp) {
        gamepad = gp;
        defineAllButtons();
    }

    /**
     * Link method used to link a button to a button handler to run some code
     * @param b
     * @param type
     * @param codeSeg
     */
    public void link(Button b, Class<? extends ButtonEventHandler> type, CodeSeg codeSeg) {
        Objects.requireNonNull(handlerMap.get(b)).addEvent(type, codeSeg);
    }

    public void link(Button b, StageList list) {
        link(b, OnPressEventHandler.class, () -> bot.addAutoModule(list));
    }

    public void link(Button b, DecisionList decisionList){
        link(b, OnPressEventHandler.class, decisionList::check);
    }

    public void link(Button b, Independent independent){
        link(b, OnPressEventHandler.class, () -> bot.addIndependent(independent));
    }

    /**
     * Unlink all of the button handlers
     */
    public void unlinkAll() {
        handlerMap = new TreeMap<>();
        defineAllButtons();
    }

    /**
     * Define all of the buttons
     */
    public void defineAllButtons() {
        for (Button b : Button.values()) {
            handlerMap.put(b, new ButtonHandler(b, this));
        }
    }

    /**
     * Run using the handlerMap
     */
    public void run() {
        for (ButtonHandler handler : handlerMap.values()) {
            handler.run();
        }
    }
}
