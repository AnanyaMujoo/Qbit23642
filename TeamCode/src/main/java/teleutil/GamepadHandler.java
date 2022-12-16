package teleutil;

import static global.General.bot;
import static global.General.gamepad1;
import static global.General.gamepad2;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.Objects;
import java.util.TreeMap;

import javax.crypto.Mac;

import automodules.AutoModule;
import global.Modes;
import teleutil.button.Button;
import teleutil.button.ButtonEventHandler;
import teleutil.button.ButtonHandler;
import teleutil.button.OnPressEventHandler;
import teleutil.button.OnTurnOnEventHandler;
import teleutil.independent.Independent;
import teleutil.independent.Machine;
import util.codeseg.CodeSeg;
import util.codeseg.ReturnCodeSeg;
import util.condition.DecisionList;
import util.condition.OutputList;
import util.template.Iterator;

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
        put(Button.A, () -> !gamepad2.start && !gamepad1.start && gamepad.a);
        put(Button.B, () -> !gamepad2.start && !gamepad1.start && gamepad.b);
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
        put(Button.BACK, () -> gamepad.back);
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



    public double ry, rx, ly, lx, rt, lt;

    /**
     * Constructor to create a gamepad handler
     * @param gp
     */
    public GamepadHandler(Gamepad gp) {
        gamepad = gp;
        defineAllButtons();
    }


    public void link(Button b, CodeSeg code){ link(b, code, Modes.GamepadMode.NORMAL); }
    public void link(Button b, Class<? extends ButtonEventHandler> type, CodeSeg codeSeg) { Objects.requireNonNull(handlerMap.get(b)).addEvent(type, codeSeg); }
    public void link(Button b, AutoModule list) { link(b, () -> bot.addAutoModule(list), Modes.GamepadMode.NORMAL); }
    public void link(Button b, DecisionList decisionList){ link(b,  decisionList::check, Modes.GamepadMode.NORMAL); }
    public void link(Button b, OutputList outputList){ link(b, () -> bot.addAutoModule(outputList.check()), Modes.GamepadMode.NORMAL); }
    public void link(Button b, Independent independent){ link(b, () -> bot.addIndependent(independent), Modes.GamepadMode.NORMAL); }
    public void link(Button b, Machine machine){ link(b, () -> bot.addMachine(machine), Modes.GamepadMode.NORMAL); }
    public void link(Button b, AutoModule list, Modes.GamepadMode mode) { link(b, () -> bot.addAutoModule(list), mode); }
    public void link(Button b, DecisionList decisionList, Modes.GamepadMode mode){  link(b, decisionList::check, mode); }
    public void link(Button b, OutputList outputList, Modes.GamepadMode mode){ link(b, () -> bot.addAutoModule(outputList.check()), mode); }
    public void link(Button b, Independent independent, Modes.GamepadMode mode){ link(b, () -> bot.addIndependent(independent), mode); }
    public void link(Button b, Machine machine, Modes.GamepadMode mode){ link(b, () -> bot.addMachine(machine), mode);}
    public void link(Button b, CodeSeg codeSeg, Modes.GamepadMode mode) {
        switch (mode){
            case NORMAL: Objects.requireNonNull(handlerMap.get(b)).addEvent(OnPressEventHandler.class, codeSeg, () -> !gamepad.back); break;
            case AUTOMATED: Objects.requireNonNull(handlerMap.get(b)).addEvent(OnPressEventHandler.class, codeSeg, () -> gamepad.back); break;
        }
    }
    public void link(Button b, CodeSeg onOn, CodeSeg onOff){
        link(b, OnTurnOnEventHandler.class, onOn);
        link(b, OnTurnOnEventHandler.class, onOff);
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
        Iterator.forAll(Button.values(), b -> handlerMap.put(b, new ButtonHandler(b, this)));
    }

    private void updateValues(){
        ry = -gamepad.right_stick_y;
        rx = gamepad.right_stick_x;
        ly = -gamepad.left_stick_y;
        lx = gamepad.left_stick_x;
        rt = gamepad.right_trigger;
        lt = gamepad.left_trigger;
    }

    /**
     * Run using the handlerMap
     */
    public void run() {
        updateValues();
        Iterator.forAll(handlerMap.values(), ButtonHandler::run);
    }

}
