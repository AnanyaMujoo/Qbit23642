package unittests.tele.framework;

import java.util.Arrays;

import math.misc.Logistic;
import unittests.tele.TeleUnitTest;

import static global.General.log;

public class MathTest extends TeleUnitTest {
    // TODO 4 NEW Finish MathTest

    /**
     * Logistic objects to test
     */
    private final Logistic logistic = new Logistic(Logistic.LogisticParameterType.STANDARD_FORM, 1.0, 1.0, 2.0);
    private final Logistic logistic2 = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 3.0, 5.0);

    /**
     * Variable to represent infinity (1 million is close enough to infinity)
     */
    private final double inf = 1000000;


    /**
     * Display logistic values
     */
    @Override
    protected void loop() {
        log.show("Logistic Test 1 {1.0, 0.0}", Arrays.toString(new Double[]{logistic.f(inf), logistic.f(-inf)}));
        log.show("Logistic Test 2 {1.0, 0.0}", Arrays.toString(new Double[]{logistic2.f(1.0), logistic2.f(-inf)}));
    }
}
