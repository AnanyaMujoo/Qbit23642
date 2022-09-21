package util.codeseg;

/**
 * Used to return a certain type of object
 * Created as follows: () -> {return <something>}
 * @param <R>
 */
public interface ReturnCodeSeg<R> {
    R run();
}
