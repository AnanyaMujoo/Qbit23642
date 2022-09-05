package util.codeseg;

/**
 * Used to pass in code with a type of parameter
 * Created as follows: input -> <do something with input>
 * @param <P>
 */
public interface ParameterCodeSeg<P> {
    void run(P input);
}
