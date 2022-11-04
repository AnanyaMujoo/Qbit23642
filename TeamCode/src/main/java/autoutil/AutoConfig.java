package autoutil;

import autoutil.reactors.Reactor;
import autoutil.vision.CaseScanner;

public class AutoConfig {
    private final AutoSegment<?,?> waypointSegment, setpointSegment;
    private final CaseScanner caseScanner;
    public AutoConfig(AutoSegment<?,?> set, AutoSegment<?,?> way, CaseScanner scanner){
        this.waypointSegment = way; this.setpointSegment = set; this.caseScanner = scanner;
    }

    public AutoSegment<?,?> getWaypointSegment(){ return waypointSegment; }
    public AutoSegment<?, ?> getSetpointSegment(){ return setpointSegment; }
    public CaseScanner getCaseScanner(){ return caseScanner; }
}
