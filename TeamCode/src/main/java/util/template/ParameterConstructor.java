package util.template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import util.codeseg.ReturnParameterCodeSeg;
import util.condition.Expectation;
import util.condition.Magnitude;

import static global.General.fault;

// TODO 4 NEW Implement this class everywhere else it needs to be

@SuppressWarnings("all")
public interface ParameterConstructor<T> {
    /**
     * Used to create a constructor with lots of parameters
     */

    /**
     * Map from parameter type to number of constructors
     */
    HashMap<ParameterType, Integer> constructorMap = new HashMap<>();
    /**
     * Map from parameter type to preproccessing codesegs
     */
    HashMap<ParameterType, ReturnParameterCodeSeg> preprocessorMap = new HashMap<>();

    void construct(T[] in);

    /**
     * Add constructor given the type and the number of parameters
     * @param parameterType
     * @param numberOfParameters
     */
    default void addConstructor(ParameterType parameterType, int numberOfParameters){
        constructorMap.put(parameterType, numberOfParameters);
        preprocessorMap.put(parameterType, input -> input);
    }

    /**
     * Add constructor given type, number of parameters, and preprocessing codeseg
     * @param parameterType
     * @param numberParameters
     * @param preprocessor
     */
    default void addConstructor(ParameterType parameterType, int numberParameters, ReturnParameterCodeSeg<T[], T[]> preprocessor){
        constructorMap.put(parameterType, numberParameters);
        preprocessorMap.put(parameterType, preprocessor);
    }

    /**
     * Method to create all construcors, must be called before constructors can be used
     * @param inputType
     * @param inputParameters
     * @param defaults
     */
    default void createConstructors(ParameterType inputType, T[] inputParameters, T[] defaults){
        if(constructorMap.containsKey(inputType)){
            int numberOfParameters = Objects.requireNonNull(constructorMap.get(inputType));
            fault.check("Wrong number of parameters for constructor of type: " + inputType.toString(),
                    Expectation.UNEXPECTED, Magnitude.MAJOR, numberOfParameters == inputParameters.length, true);
            T[] inputPart = (T[]) preprocessorMap.get(inputType).run(inputParameters);
            T[] defaultPart = new ArrayList<>(Arrays.asList(defaults)).subList(numberOfParameters, defaults.length).toArray(defaults);
            List<T> combined = new ArrayList<>(Arrays.asList(inputPart));
            combined.addAll(Arrays.asList(defaultPart));
            construct(combined.toArray(inputPart));
        }else{
            fault.check("Constructor " + inputType.toString() + " not found for constructor of type: " + inputType.toString(), Expectation.UNEXPECTED, Magnitude.MAJOR);
        }
    }

    /**
     * Interface to override with specific types of constructors
     */
    public interface ParameterType {};
}
