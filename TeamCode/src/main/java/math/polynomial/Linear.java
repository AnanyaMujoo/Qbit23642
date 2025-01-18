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
        super((p2.getY()-p1.getY())/(p2.getX()-p1.getX()), p1.getY() - (p1.getX()*(p2.getY()-p1.getY())/(p2.getX()-p1.getX())));
        this.m = a(0);
        this.b = a(1);
    }

    public Linear(double b, double y2, double x2){
        super((y2-b)/x2, b);
        this.m = (y2-b)/x2;
        this.b = b;
    }

    public static Linear one(double y1, double y2){
        return new Linear(y1, y2, 1.0);
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