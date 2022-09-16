package automodules.stage;

import util.codeseg.CodeSeg;
// TODO 3 TEST Check if initials works more?
public class Initial extends StageComponent{
    /**
     * Initial runs once in the start of a stage
     */
    private final CodeSeg start;
    public Initial(CodeSeg startCode){
        start = startCode;
    }
    @Override
    public void start(){
        start.run();
    }
}
