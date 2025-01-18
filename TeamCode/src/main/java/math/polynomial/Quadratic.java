package math.polynomial;

/**
 * NOTE: Uncommented
 */

public class Quadratic extends Polynomial{
    double a;
    double b;
    double c;

    public Quadratic(double a, double b, double c){
        super(a,b,c);
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double[] roots() {
        double disc = (Math.pow(b,2)-4*a*c);
        double root1 = (-b + Math.sqrt(disc))/(2*a);
        double root2 = (-b - Math.sqrt(disc))/(2*a);
        return new double[]{root1, root2};
    }
}
