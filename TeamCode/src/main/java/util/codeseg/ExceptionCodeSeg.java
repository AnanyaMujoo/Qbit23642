package util.codeseg;

/**
 * Used to pass in code that throws an exception
 * Created as follows: () -> <do something that could throw an exception>
 * @param <E>
 */
public interface ExceptionCodeSeg<E extends Throwable> {
    void run() throws E;
}
