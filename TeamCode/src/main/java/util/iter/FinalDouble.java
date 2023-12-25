package util.iter;

public class FinalDouble extends Final<Double> {
    public FinalDouble() { super(0.0); }
    public FinalDouble(double d){ super(d);}
    public void increment(double inc){ set(get()+inc);}
}