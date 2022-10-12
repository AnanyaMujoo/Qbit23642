package math.polynomial;
import geometry.framework.Point;

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
        super((p2.getY()-p1.getY())/(p2.getX()-p1.getX()) , p1.getY() - (p1.getX()*(p2.getY()-p1.getY())/(p2.getX()-p1.getX())));
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