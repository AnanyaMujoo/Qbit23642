package elements;

import util.template.Mode;

public enum TeamProp implements Mode.ModeType{
    LEFT("Left, Location 1"),
    CENTER("Center, Location 2"),
    RIGHT("Right, Location 3");

    private final String value;

    TeamProp(String value){ this.value = value; }

    public String getValue(){ return value; }


    @Override
    public String toString() { return getValue(); }


}