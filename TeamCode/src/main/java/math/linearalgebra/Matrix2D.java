package math.linearalgebra;

import org.firstinspires.ftc.robotcore.external.matrices.GeneralMatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
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

    // | tl tr |
    // | bl br |

    private final GeneralMatrixF matrix;

    public Matrix2D(
            double tl, double tr,
            double bl, double br
    ){
        matrix = new GeneralMatrixF(2,2, new float[]{(float) tl, (float) tr, (float) bl, (float) br});
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

    public Matrix2D getInverted(){
        MatrixF mat = matrix.inverted();
        return new Matrix2D(mat.get(0,0), mat.get(0,1), mat.get(1,0), mat.get(1,1));
    }

    public static Vector solve(Matrix2D matrix, Vector out){
        return matrix.getInverted().multiply(out);
    }

    @Override
    public String toString() {
        return "Matrix2D{"
                + matrix.get(0,0) + ", "
                +  matrix.get(0,1) + ", \n         "
                + matrix.get(1,0) + ", "
                + matrix.get(1,1) + '}';
    }
}