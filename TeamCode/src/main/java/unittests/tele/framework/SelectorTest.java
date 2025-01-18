package unittests.tele.framework;

import java.util.Arrays;

import teleutil.Selector;
import teleutil.button.Button;
import unittests.tele.TeleUnitTest;
import util.store.Item;

import static global.General.gamepad1;
import static global.General.gph1;
import static global.General.log;

public class SelectorTest extends TeleUnitTest {
    /**
     * Test selector by running important methods
     */

    /**
     * Selector objects, only stringSelector is no wrap around
     */
    Selector<Boolean> booleanSelector = new Selector<>(true);
    Selector<String> stringSelector = new Selector<>(false);
    Selector<Integer> integerSelector = new Selector<>(true);

    @Override
    public void init() {
        /**
         * Initialize the selectors
         */
        booleanSelector.init(3);
        stringSelector.init(() -> gamepad1.right_bumper, () -> false);
        integerSelector.init(gph1, Button.Y, Button.B, Button.A);

        /**
         * Add items to the selectors
         */
        addItemsToSelector(booleanSelector, "Boolean", new Boolean[]{false, true});
        addItemsToSelector(stringSelector, "String", new String[]{"First", "Second", "Third", "Fourth"});
        addItemsToSelector(integerSelector, "Integer", new Integer[]{1, 2, 3, 4});

    }

    @Override
    protected void loop() {
        /**
         * Should be true, false until 3 secs has elapsed and then false, true, etc.
         */
        log.show("Boolean selector is on first", Arrays.toString(new Boolean[]{booleanSelector.isOnFirst(), booleanSelector.isOnLast()}));
        /**
         * Should be String
         */
        log.show("String selector item class names", stringSelector.getItemClassNames().get(0));
        /**
         * String selector current item, should be First, then Second, etc.
         */
        log.show("Press right bumper to go up", stringSelector.getCurrentItem().getValue());
        /**
         * Integer selector current item, should be IntegerItem1, then IntegerItem2, etc.
         * This should also wrap around properly
         */
        log.show("Press Y to go up and B to go down and A to activate", integerSelector.getCurrentItem().getName());
        /**
         * Is the selector active? Should be true when A is pressed
         */
        log.show("Press A to turn selector active", integerSelector.isActive());



        /**
         * Update the selectors
         */
        booleanSelector.update();
        stringSelector.update();
        integerSelector.update();
    }

    /**
     * Private method to add items to the specified selector
     * @param in
     * @param name
     * @param items
     * @param <T>
     */
    private  <T> void addItemsToSelector(Selector<T> in, String name, T[] items){
        for(int i = 0; i < items.length; i++){
            in.addItem(new Item<>(name + "Item" + i, items[i]));
        }
    }
}
