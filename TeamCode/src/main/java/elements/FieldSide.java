package elements;
import org.firstinspires.ftc.robotcore.internal.android.dex.Code;

import util.codeseg.CodeSeg;
import util.condition.Decision;

import static global.General.fieldSide;

public enum FieldSide implements Decision {
    /**
     * Enum to represent which side of the field we are on
     * The directions are from the audience perspective
     * Note that toString will return the side as in left or right
     */
    BLUE("Left"), RED("Right"), UNKNOWN("Unknown");

    /**
     * String to represent the side the robot is on
     */
    private final String side;

    /**
     * Constructor to create the enum
     * @param s
     */
    FieldSide(String s){
        this.side = s;
    }

    /**
     * Gets the side
     * @return side
     */
    public String getSide(){
        return side;
    }

    /**
     * Creates the enum using the string representation
     * @param side
     * @return
     */
    public static FieldSide create(String side){
        switch (side) {
            case "Left":
                return BLUE;
            case "Right":
                return RED;
            case "Middle":
                return UNKNOWN;
            default:
                return null;
        }
    }

    public static boolean isBlue(){ if(fieldSide != null) { return fieldSide.equals(BLUE) || fieldSide.equals(UNKNOWN);}else {return true; } }

    public static void on(CodeSeg blue, CodeSeg red){
        if(isBlue()){blue.run();}else {red.run();};
    }

    @Override
    public String toString() {
        return getSide();
    }
}
