package autoutil.paths;

import java.util.ArrayList;

public class FinalPathArc {
    // Composed of multiple path segments
    // Has the information about where the robot should be at a given time or distance
    // Includes any other relevant information to execute the path

    public final ArrayList<PathSegment> segments = new ArrayList<>();

    public void addSegments(ArrayList<PathSegment> p) { segments.addAll(p); }
    public void addSegment(PathSegment p) { segments.add(p); }

    public ArrayList<PathSegment> getSegments(){return segments;}
}