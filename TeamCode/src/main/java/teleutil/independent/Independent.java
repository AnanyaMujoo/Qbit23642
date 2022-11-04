package teleutil.independent;

import autoutil.AutoFramework;
import util.codeseg.ParameterCodeSeg;

public class Independent extends AutoFramework {

    private final ParameterCodeSeg<Independent> define;

    public Independent(ParameterCodeSeg<Independent> define){
        this.define = define;
    }

    @Override
    public void initAuto() {
        makeIndependent();
    }

    @Override
    public void define() {
        define.run(this);
    }
}
