package geometry;
import geometry.circles.AngleType;
import geometry.position.Pose;

import static java.lang.Math.*;

/**
 * NOTE: Uncommented
 */

// TODO 4 FIX GEOMETRY PACKAGE

public abstract class GeometryObject {
    public GeometryObject getRelativeTo(Pose origin) {
        return this;
    }
    protected double boundAngleTo2Pi(double ang){
        return ang % (2*PI);
    }
    protected double toRad(double in, AngleType type){ return type.equals(AngleType.RADIANS) ? in : AngleType.degToRad(in); }
    protected double toDeg(double in, AngleType type){ return type.equals(AngleType.DEGREES) ? in : AngleType.radToDeg(in); }
}
