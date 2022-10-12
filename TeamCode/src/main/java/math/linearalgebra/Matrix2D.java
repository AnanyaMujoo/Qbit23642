package math.linearalgebra;

import org.firstinspires.ftc.robotcore.external.matrices.GeneralMatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

import geometry.framework.Point;
import geometry.position.Vector;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Matrix2D {

    // TOD4 NEW
    // Make linear algebra classes

    // | a b |
    // | c d |

    // | tr tl |
    // | br bl |

    private final GeneralMatrixF matrix;

    public Matrix2D(
            double tr, double tl,
            double br, double bl
    ){
        matrix = new GeneralMatrixF(2,2, new float[]{(float) tr, (float) tl, (float) br, (float) bl});
    }

    public Vector multiply(Vector vector){
        VectorF out = matrix.multiplied(new VectorF(new float[]{(float) vector.getX(), (float) vector.getY()}));
        return new Vector(out.get(0), out.get(1));
    }

    public Point multiply(Point point){
        VectorF out = matrix.multiplied(new VectorF(new float[]{(float) point.getX(), (float) point.getY()}));
        return new Point(out.get(0), out.get(1));
    }

    public static Matrix2D getScaleMatrix(double scale){
        return new Matrix2D(scale, 0, 0, scale);
    }

    public static Matrix2D getRotationMatrix(double angle){
        return new Matrix2D(cos(angle), -sin(angle), sin(angle), cos(angle));
    }

}