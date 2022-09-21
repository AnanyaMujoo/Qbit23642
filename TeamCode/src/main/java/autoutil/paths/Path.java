package autoutil.paths;

import java.util.ArrayList;

import geometry.position.Pose;

public class Path {

    public final ArrayList<PathSegment2> segments = new ArrayList<>();

    public void addSegments(ArrayList<PathSegment2> p) { segments.addAll(p); }
    public void addSegment(PathSegment2 p) { segments.add(p); }

    public ArrayList<PathSegment2> getSegments(){return segments;}
}