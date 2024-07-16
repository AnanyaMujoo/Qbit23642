package unittests.auto.framework;

import unittests.auto.AutoUnitTest;
import util.template.Iterator;

import static global.General.log;

import java.util.ArrayList;
import java.util.Arrays;

public class IteratorTest extends AutoUnitTest {
    /**
     * Tests the iterator interface by running different methods
     */


    /**
     * Test lists for testing forAll methods
     */
    private final ArrayList<Integer> array1 = new ArrayList<>(Arrays.asList(1,2,3));
    private final ArrayList<Integer> array2 = new ArrayList<>(Arrays.asList(4,5,6));
    private final int[] counts = new int[3];

    @Override
    public void start() {
        Iterator.forAll(array1, i -> counts[0]+=i);
        counts[1] = Iterator.forAllCount(array2, i->i==4);
        counts[2] = Iterator.forAllConditionOR(array2, i->i==5)?1:0;
    }

    @Override
    protected void run() {
        /**
         * Display the time for 1 second
         */
        whileTime(() -> log.show("While time for 1 s", timer.seconds()), 1);

        /**
         * Display the time for another second
         */
        whileActive(() -> timer.seconds() < 2, () -> log.show("While active for 1 s", timer.seconds()));

        /**
         * Pause for a second
         */
        log.show("Pausing for one second");
        pause(1);

        /**
         * Display counts
         */
        whileActive(() -> timer.seconds() < 5, () -> log.show("Counts {6, 1, 1}", Arrays.toString(counts)));
    }
}