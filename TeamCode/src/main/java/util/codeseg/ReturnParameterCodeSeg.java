package util.codeseg;

/**
 * Used to return and object and input a parameter
 * Created as follows: input -> {<do something with input> return <something>}
 * @param <P>
 * @param <R>
 */
public interface ReturnParameterCodeSeg<P, R>{
    R run(P input);
}
