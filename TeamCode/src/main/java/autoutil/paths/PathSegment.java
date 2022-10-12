package autoutil.paths;

import java.util.ArrayList;

import geometry.position.Pose;

public abstract class PathSegment {
    protected ArrayList<Pose> poses = new ArrayList<>();

    public ArrayList<Pose> getPoses(){
        return poses;
    }
}