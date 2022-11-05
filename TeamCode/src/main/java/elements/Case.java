package elements;

import util.condition.Decision;

public enum Case implements Decision {
    FIRST("LOCATION 1"),
    SECOND("LOCATION 2"),
    THIRD("LOCATION 3");

    private final String value;

    Case(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
