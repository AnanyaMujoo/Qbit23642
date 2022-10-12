package math.linearalgebra;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

import geometry.framework.Point;

public class Vector3D {
    private final VectorF vector;
    public Vector3D(double x, double y, double z){
        vector = new VectorF((float) x, (float) y, (float) z);
    }

    public double getX(){ return vector.get(0); }
    public double getY(){ return vector.get(1); }
    public double getZ(){ return vector.get(2); }


}
