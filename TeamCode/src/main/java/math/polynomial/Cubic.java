package math.polynomial;

/**
 * NOTE: Uncommented
 */

public class Cubic extends Polynomial{
    double A;
    double B;
    double C;
    double D;

    public Cubic(double a, double b, double c, double d){
        super(a,b,c,d);
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
    }

    @Override
    public double[] roots() {
        // TOD4 FIX ARNAV
        // Use newtons method to find roots of cubic
        double x1 = 0;
        double x2 = 0;
        double x3 = 0;
        double a = (double) B / A;
        double b = (double) C / A;
        double c = (double) D / A;

        double p = b - ((a * a) / 3.0);

        double q = (2 * Math.pow(a, 3) / 27.0) - (a * b / 3.0) + c;

        double delta = (Math.pow(q, 2) / 4) + (Math.pow(p, 3) / 27);

        if (delta > 0.001) {

            double mt1, mt2;

            double t1 = (-q / 2.0) + Math.sqrt(delta);
            double t2 = (-q / 2.0) - Math.sqrt(delta);

            if (t1 < 0) {
                mt1 = (-1) * (Math.pow(-t1, (double) 1 / 3));
            } else {
                mt1 = (Math.pow(t1, (double) 1 / 3));
            }

            if (t2 < 0) {
                mt2 = (-1) * (Math.pow(-t2, (double) 1 / 3));
            } else {
                mt2 = (Math.pow(t2, (double) 1 / 3));
            }

            x1 = mt1 + mt2 - (a / 3.0);

        } else if (delta < 0.001 && delta > -0.001) {

            if (q < 0) {

                x1 = 2 * Math.pow(-q / 2, (double) 1 / 3) - (a / 3);
                x2 = -1 * Math.pow(-q / 2, (double) 1 / 3) - (a / 3);

            } else {
                x1 = -2 * Math.pow(q / 2, (double) 1 / 3) - (a / 3);
                x2 = Math.pow(q / 2, (double) 1 / 3) - (a / 3);

            }

        } else {

            x1 = (2.0 / Math.sqrt(3)) * (Math.sqrt(-p) * Math.sin((1 / 3.0) * Math.asin(((3 * Math.sqrt(3) * q) / (2 * Math.pow(Math.pow(-p, (double) 1 / 2), 3)))))) - (a / 3.0);
            x2 = (-2.0 / Math.sqrt(3)) * (Math.sqrt(-p) * Math.sin((1 / 3.0) * Math.asin(((3 * Math.sqrt(3) * q) / (2 * Math.pow(Math.pow(-p, (double) 1 / 2), 3)))) + (Math.PI / 3))) - (a / 3.0);
            x3 = (2.0 / Math.sqrt(3)) * (Math.sqrt(-p) * Math.cos((1 / 3.0) * Math.asin(((3 * Math.sqrt(3) * q) / (2 * Math.pow(Math.pow(-p, (double) 1 / 2), 3)))) + (Math.PI / 6))) - (a / 3.0);

        }
        return new double[]{x1,x2,x3};
    }
}
