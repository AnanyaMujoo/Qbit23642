package math.polynomial;
import geometry.position.Point;

/**
 * NOTE: Uncommented
 */

public class Linear extends Polynomial{
    public final double m;
    public final double b;
    public Linear(double m, double b){
        super(m, b);
        this.m = m;
        this.b = b;
    }
    public Linear(Point p1, Point p2){
        super((p2.y-p1.y)/(p2.x-p1.x) , p1.y - (p1.x*(p2.y-p1.y)/(p2.x-p1.x)));
        this.m = a(0);
        this.b = a(1);
    }

    @Override
    public double f(double x) {
        return  (m*x) + b;
    }

    @Override
    public double[] roots() {
        return new double[]{-b/m};
    }
}