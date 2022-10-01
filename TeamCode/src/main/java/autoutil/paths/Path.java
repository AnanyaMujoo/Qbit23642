package autoutil.paths;

import java.util.ArrayList;

public class Path {

    public final ArrayList<PathSegment> segments = new ArrayList<>();

    public void addSegments(ArrayList<PathSegment> p) { segments.addAll(p); }
    public void addSegment(PathSegment p) { segments.add(p); }

    public ArrayList<PathSegment> getSegments(){return segments;}
}