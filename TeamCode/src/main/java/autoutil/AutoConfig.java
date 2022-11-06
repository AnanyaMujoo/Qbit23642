package autoutil;

import autoutil.reactors.Reactor;
import autoutil.vision.CaseScanner;
import util.codeseg.ReturnCodeSeg;

public class AutoConfig {
    private final AutoSegment<?,?> getWaypointSegment, getSetpointSegment;
    private final ReturnCodeSeg<CaseScanner> getCaseScanner;
    public <T extends CaseScanner> AutoConfig(AutoSegment<?,?> set, AutoSegment<?,?> way, ReturnCodeSeg<T> scanner){
        this.getWaypointSegment = way; this.getSetpointSegment = set; this.getCaseScanner = (ReturnCodeSeg<CaseScanner>) scanner;
    }

    public AutoSegment<?,?> getWaypointSegment(){ return getWaypointSegment; }
    public AutoSegment<?, ?> getSetpointSegment(){ return getSetpointSegment; }
    public CaseScanner getCaseScanner(){ return getCaseScanner.run(); }
}
