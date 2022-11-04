package autoutil.generators;

public class PoseGenerator extends Generator{
    @Override
    public void add(double x, double y, double heading) {
        path.addSegment(new PathPose(x, y, Math.toRadians(heading)));
    }
}