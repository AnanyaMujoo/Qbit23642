package unittests.auto.framework;

import java.util.Arrays;

import unittests.auto.AutoUnitTest;
import util.template.ParameterConstructor;

import static global.General.log;

public class ParameterConstructorTest extends AutoUnitTest {
    /**
     * Tests the parameter constructor interface by creating a class that implements it
     */


    /**
     * Class that implements Parameter Constructor
     */
    private static class ConstructorTest implements ParameterConstructor<Double> {
        public double a;
        public double b;
        public double c;

        public ConstructorTest(ConstructorTestType type, Double... parameters){
            addConstructor(ConstructorTestType.TEST_DEFAULT, 1);
            addConstructor(ConstructorTestType.TEST_SECOND, 2, input -> new Double[]{input[0]/2.0, input[1]/2.0});
            addConstructor(ConstructorTestType.TEST_ALL, 3);
            createConstructors(type, parameters, new Double[]{1.0,2.0,3.0});
        }

        @Override
        public void construct(Double[] in) {
            a = in[0]; b = in[1]; c = in[2];
        }

        public double[] getValues(){
            return new double[]{a, b, c};
        }

        public enum ConstructorTestType implements ParameterType {
            TEST_DEFAULT,
            TEST_SECOND,
            TEST_ALL
        }
    }

    /**
     * Constructor Test objects to test
     */
    ConstructorTest testDefault = new ConstructorTest(ConstructorTest.ConstructorTestType.TEST_DEFAULT, 2.0);
    ConstructorTest testSecond = new ConstructorTest(ConstructorTest.ConstructorTestType.TEST_SECOND, 3.0, 4.0);
    ConstructorTest testAll = new ConstructorTest(ConstructorTest.ConstructorTestType.TEST_ALL, 4.0, 5.0, 6.0);

    /**
     * Test the parameter constructors by displaying the values
     */
    @Override
    protected void run() {
        whileActive(()->{
            /**
             * Display the expected and actual values for each
             */
            log.show("Default Values, should be {2.0, 2.0, 3.0}", Arrays.toString(testDefault.getValues()));
            log.show("Second Values, should be {1.5, 2.0, 3.0}", Arrays.toString(testSecond.getValues()));
            log.show("All Values, should be {4.0, 5.0, 6.0}", Arrays.toString(testAll.getValues()));
        });
    }
}
