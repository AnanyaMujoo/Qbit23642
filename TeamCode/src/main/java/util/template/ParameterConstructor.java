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

// TOD4 NEW
// Implement this class everywhere else it needs to be

@SuppressWarnings("all")
public interface ParameterConstructor<T> {
    HashMap<ParameterType, Integer> constructorMap = new HashMap<>();
    HashMap<ParameterType, ReturnParameterCodeSeg> preprocessorMap = new HashMap<>();

    void construct(T[] in);

    default void addConstructor(ParameterType parameterType, int numberOfParameters){
        constructorMap.put(parameterType, numberOfParameters);
        preprocessorMap.put(parameterType, input -> input);
    }

    default void addConstructor(ParameterType parameterType, int numberParameters, ReturnParameterCodeSeg<T[], T[]> preprocessor){
        constructorMap.put(parameterType, numberParameters);
        preprocessorMap.put(parameterType, preprocessor);
    }

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

    public interface ParameterType {};
}
