package util;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import util.condition.Expectation;
import util.condition.Magnitude;

import static global.General.*;
public class ExceptionCatcher {
    /**
     * Class with static methods to catch exceptions
     * Ex: for code that throws interrupted exception,
     * Use by doing ExceptionCatcher.catchInterrupted(() -> <code to run>)
     */


    /**
     * Interfaces for different exceptions
     */
    public interface InterruptedExceptionRunnable {
        void run() throws InterruptedException;
    }
    public interface NewInstanceRunnable{
        void run() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException;
    }
    public interface IOExceptionRunnable{
        void run() throws IOException;
    }

    /**
     * Methods to catch exceptions
     * NOTE: This will show a warning if the exception is thrown
     * @param runnable
     */
    public static void catchInterrupted(InterruptedExceptionRunnable runnable) {
        try { runnable.run(); } catch (InterruptedException ignored) {
            fault.warn("Illegal Access or Not Instantiated new Instance", Expectation.UNEXPECTED, Magnitude.CRITICAL);
        }
    }
    public static void catchNewInstance(NewInstanceRunnable runnable) {
        try {runnable.run(); } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ignored) {
            fault.warn("Illegal Access or Not Instantiated new Instance", Expectation.UNEXPECTED, Magnitude.CRITICAL);
        }
    }
    public static void catchIO(IOExceptionRunnable runnable) {
        try {runnable.run(); } catch (IOException ignored) {
            fault.warn("IO exception, file does not exist?", Expectation.UNEXPECTED, Magnitude.CRITICAL);
        }
    }
}