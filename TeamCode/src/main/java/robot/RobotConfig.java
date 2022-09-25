package robot;

import java.util.ArrayList;
import java.util.Arrays;

import robotparts.RobotPart;
import util.template.Iterator;

public class RobotConfig {
    /**
     * Arraylist for parts
     */
    private final ArrayList<RobotPart> parts;

    /**
     * Config Constructor
     * @param parts
     */
    public RobotConfig(RobotPart... parts){
       this.parts = new ArrayList<>(Arrays.asList(parts));
    }

    /**
     * Method to to set to current config
     */
    private void instantiate(){
        Iterator.forAll(parts, RobotPart::instantiate);
    }

    /**
     * Method to setConfig, must be called once
     * @param config
     */
    public static void setConfig(RobotConfig config){
        config.instantiate();
    }

}
