package math.statistics;

    // TOD4 NEW ARNAV
    // Make this, given set of points it finds line of least squares
    // Function to calculate b


public class LeastSquares {

    // Function to calculate b
    private static double calculateB(
            int[] x, int[] y)
    {
        int n = x.length;

        // sum of array x
        int sx = 0;
        for (int i = 0; i < x.length; i++) {
            sx = sx + x[i];
        }

        // sum of array y
        int sy = 0;
        for (int i = 0; i < y.length; i++) {
            sy = sy + x[i];
        }

        // for sum of product of x and y
        int sxsy = 0;

        // sum of square of x
        int sx2 = 0;
        for (int i = 0; i < n; i++) {
            sxsy += x[i] * y[i];
            sx2 += x[i] * x[i];
        }
        double b = (double)(n * sxsy - sx * sy)
                / (n * sx2 - sx * sx);

        return b;
    }

    // Function to find the
    // least regression line
    public static void leastRegLine(
            int X[], int Y[])
    {

        // Finding b
        double b = calculateB(X, Y);

        int n = X.length;
        int meanY = 0;
        int meanX = 0;

        for (int i = 0; i < X.length; i++) {
            meanX = meanX + X[i];
        }
        meanX = meanX / n;

        for (int i = 0; i < Y.length; i++) {
            meanY = meanY + X[i];
        }
        meanY = meanY / n;


        // calculating a
        double a = meanY - b * meanX;

        // a and b are both there for y = a + bx

    }

    // Driver code
    public static void main(String[] args)
    {
        // statistical data
        int X[] = { 95, 85, 80, 70, 60 };
        int Y[] = { 90, 80, 70, 65, 60 };

        leastRegLine(X, Y);
    }
}