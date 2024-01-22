package elements;

import util.template.Mode;

public enum TeamProp implements Mode.ModeType{
    FIRST("Left, Location 1"),
    SECOND("Center, Location 2"),
    THIRD("Right, Location 3");

    private final String value;

    TeamProp(String value){ this.value = value; }

    public String getValue(){ return value; }


    @Override
    public String toString() { return getValue(); }


}