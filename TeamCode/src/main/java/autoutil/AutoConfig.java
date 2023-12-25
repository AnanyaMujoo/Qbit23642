package autoutil;

import autoutil.reactors.Reactor;
import autoutil.vision.CaseScanner;
import util.codeseg.ReturnCodeSeg;

public class AutoConfig {
    private final AutoSegment<?,?> getWaypointSegment, getSetpointSegment;
    public <T extends CaseScanner> AutoConfig(AutoSegment<?,?> set, AutoSegment<?,?> way){
        this.getWaypointSegment = way; this.getSetpointSegment = set;
    }

    public AutoSegment<?,?> getWaypointSegment(){ return getWaypointSegment; }
    public AutoSegment<?, ?> getSetpointSegment(){ return getSetpointSegment; }
}
