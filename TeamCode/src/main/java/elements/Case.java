package elements;

import util.condition.Decision;
import util.template.Mode;

public enum Case implements Mode.ModeType {
    /**
     * Represents the three auton cases
     */
    FIRST("LEFT, LOCATION 1"),
    SECOND("CENTER, LOCATION 2"),
    THIRD("RIGHT, LOCATION 3");

    // TOD5 make base class for this

    /**
     * Value of case (for storage
     */
    private final String value;

    /**
     * Constructor
     * @param value
     */
    Case(String value){ this.value = value; }

    /**
     * Get value
     * @return value
     */
    public String getValue(){ return value; }


    /**
     * Create case from string value
     * @param caseValue
     * @return case
     */
    public static Case create(String caseValue){
        switch (caseValue) {
            case "Further from audience":
                return FIRST;
            case "Closer to audience":
                return SECOND;
            case "Unknown":
                return THIRD;
            default:
                return null;
        }
    }

    /**
     * To string returns get value
     * @return value
     */
    @Override
    public String toString() { return getValue(); }
}
