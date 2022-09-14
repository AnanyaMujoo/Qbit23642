package teleutil;

import java.util.ArrayList;

import teleutil.button.Button;
import util.Timer;
import util.codeseg.CodeSeg;
import util.codeseg.ReturnCodeSeg;
import util.codeseg.ParameterCodeSeg;
import util.condition.Status;
import util.store.Item;
import util.template.Iterator;

public class Selector<T> {
    /**
     * Selector class acts like a list of things like a menu which you can scroll through
     */


    /**
     * ArrayList of items to use for selecting
     */
    private ArrayList<Item<T>> items = new ArrayList<>();
    /**
     * List of the names of the items
     */
    private ArrayList<String> itemClassNames = new ArrayList<>();
    /**
     * Code segs for moving up and down the selector
     */
    private ReturnCodeSeg<Boolean> up;
    private ReturnCodeSeg<Boolean> down;
    private ReturnCodeSeg<Boolean> select;
    /**
     * Current index of the selector
     */
    private int currentIndex;
    /**
     * Should the selector wrap around (i.e. the selector acts like a circle instead of a line)
     */
    private final boolean wrapAround;
    /**
     * Delay between updates, default is 0.2 seconds
     */
    private double delay = 0.2;
    /**
     * Use the update timer to check when it updates
     */
    private final Timer updateTimer = new Timer();
    /**
     * Code to be run when the selection changes
     */
    private CodeSeg next = () -> {};
    /**
     * Done running last
     */
    private boolean isDoneWithLast = false;
    /**
     * Status of the selector
     */
    private Status status = Status.IDLE;

    /**
     * Constructor that sets if wraparound is true
     * @param shouldWrapAround
     */
    public Selector(boolean shouldWrapAround){
        wrapAround = shouldWrapAround;
    }

    /**
     * Add a item to the list of items
     * NOTE: Init must be called before this method
     * @param item
     */
    public void addItem(Item<T> item){
        items.add(item);
        itemClassNames.add(item.getValue().getClass().getSimpleName());
    }

    /**
     * Initialize the selector
     */
    private void init(){
        items = new ArrayList<>();
        resetUpdateTimer();
    }

    /**
     * Initialize the selector with the gamepad handler and the up and down buttons
     * @param gph
     * @param upButton
     * @param downButton
     */
    public void init(GamepadHandler gph, Button upButton, Button downButton, Button selectButton){
        up = gph.pressedMap.get(upButton); down = gph.pressedMap.get(downButton); select = gph.pressedMap.get(selectButton);
        init();
    }

    /**
     * Initialize the selector with up and down code segs
     * @param upSeg
     * @param downSeg
     */
    public void init(ReturnCodeSeg<Boolean> upSeg, ReturnCodeSeg<Boolean> downSeg){
        up = upSeg; down = downSeg; select = () -> true;
        init();
    }

    /**
     * Initialize the selector with the time between updates
     * @param timeBetweenUpdates
     */
    public void init(double timeBetweenUpdates){
        this.delay = timeBetweenUpdates;
        init(() -> true, () -> false);
    }

    /**
     * Resets the selector
     */
    public void reset() {
        init();
        itemClassNames = new ArrayList<>();
        currentIndex = 0;
        isDoneWithLast = false;
        status = Status.IDLE;
    }

    /**
     * Reset the update timer
     */
    public void resetUpdateTimer(){
        updateTimer.reset();
    }

    /**
     * Is the selector on the first value?
     * @return isOnFirst
     */
    public boolean isOnFirst(){ return currentIndex == 0; }

    /**
     * Is the selector on the last value?
     * @return isOnLast
     */
    public boolean isOnLast(){ return currentIndex == items.size()-1; }

    /**
     * Is the selector done with the last test
     * @return isDoneWithLast
     */
    public boolean isDoneWithLast(){ return isDoneWithLast; }

    /**
     * Go up on the selector list
     */
    public void up(){
        if(isOnLast()) {
            /**
             * If its on the last index
             * Set done with last true
             */
            isDoneWithLast = true;
            if(wrapAround){
                currentIndex = 0;
            }
        }else {
            currentIndex++;
        }
    }

    /**
     * Go down on the selector list
     */
    public void down(){
        if(isOnFirst()){
            if(wrapAround){
                currentIndex = items.size()-1;
            }
        }else{
            currentIndex--;
        }
    }

    /**
     * Update the selector with the codesegs to go up and down
     */
    public void update(){
        if(updateTimer.seconds() > delay) {
            if(select.run()){
                /**
                 * If the selector is selected activate it
                 */
                active();
            }
            if (up.run()) {
                updateTimer.reset();
                next.run();
                up();
            } else if (down.run()) {
                updateTimer.reset();
                next.run();
                down();
            }
        }
    }

    /**
     * Get current index
     * @return current index
     */
    public int getCurrentIndex(){
        return currentIndex;
    }

    /**
     * Get the current item
     * @return current item
     */
    public Item<T> getCurrentItem(){
        return items.get(currentIndex);
    }

    /**
     * Get the number of items in this selector
     * @return length of the items arraylist
     */
    public int getNumberOfItems(){ return items.size(); }

    /**
     * Run the codeseg to all of the items
     * @param seg
     */
    public void runToAll(ParameterCodeSeg<T> seg){
        Iterator.forAll(items, item -> seg.run(item.getValue()));
    }

    /**
     * Run the codeseg to the current items
     * @param seg
     */
    public void runToCurrentItem(ParameterCodeSeg<T> seg){
        seg.run(getCurrentItem().getValue());
    }

    /**
     * Define the next codeseg
     * @param seg
     * @link next
     */
    public void runOnNext(CodeSeg seg){
         next = seg;
    }

    /**
     * Get the status of the selector
     * @return
     */
    public Status getStatus(){
        return status;
    }

    /**
     * Is the selector active?
     * @return isActive
     */
    public boolean isActive(){ return status.equals(Status.ACTIVE); }

    /**
     * Is the selector inactive?
     * @return isInActive
     */
    public boolean isInActive(){ return !status.equals(Status.ACTIVE); }

    /**
     * Set the status to idle
     */
    public void idle(){status = Status.IDLE;}

    /**
     * Set the status to active
     */
    public void active(){status = Status.ACTIVE;}

    /**
     * Get the names of all the item classes
     * @return itemClassNames
     */
    public ArrayList<String> getItemClassNames(){ return itemClassNames; }

}
