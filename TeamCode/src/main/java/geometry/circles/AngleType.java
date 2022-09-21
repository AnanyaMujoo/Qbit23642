package geometry.circles;

/**
 * NOTE: Uncommented
 */

public enum AngleType {
    DEGREES,
    RADIANS;

    public static double radToDeg(double rads){return Math.toDegrees(rads);}
    public static double degToRad(double degs){return Math.toRadians(degs);}
}