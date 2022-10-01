package autoutil.predictors;

// TODO 4 NEW Make predictors
public class Predictor {
    // A base class for a predictor
    // A predictor predicts the powers of the motors based on the desired target and current position

    public double[] getPows(double x, double y, double h) { return getPows(new double[] { x, y, h }); }

    public double[] getPows(double[] curPos) { return new double[] { }; }
}
