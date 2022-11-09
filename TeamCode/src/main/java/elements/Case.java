package elements;

import util.condition.Decision;

public enum Case implements Decision {
    FIRST("BLUE, LOCATION 1"),
    SECOND("MAGENTA, LOCATION 2"),
    THIRD("ORANGE, LOCATION 3");

    private final String value;

    Case(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    @Override
    public String toString() { return value; }
}
