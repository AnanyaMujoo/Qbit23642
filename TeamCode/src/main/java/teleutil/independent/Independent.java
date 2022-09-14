package teleutil.independent;

import auton.MecanumAuto;
import util.codeseg.CodeSeg;
import util.codeseg.ParameterCodeSeg;

import static global.General.bot;

public class Independent extends MecanumAuto {
    // TODO clean up independents
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
