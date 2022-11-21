package util.template;

import automodules.stage.Main;
import automodules.stage.Stage;
import global.Modes;
import util.condition.Decision;

import static automodules.StageBuilder.exitAlways;

public class ValueMode {
    private ModeType mode;
    private ModeType[] values;
    public void cycleUp() { for (int i = 0; i < values.length-1; i++) { if(modeIs(values[i])){ set(values[i+1]); return; }} if(modeIs(values[values.length-1])){set(values[0]);} }
    public void cycleDown() { for (int i = 1; i < values.length; i++) { if(modeIs(values[i])){ set(values[i-1]); return; }} if(modeIs(values[0])){set(values[values.length-1]);} }
    public boolean modeIs(ModeType other){ return mode.equals(other); }
    public ModeType get(){ return mode; }
    public Stage ChangeMode(ModeType newMode){return new Stage(new Main(() -> mode = newMode), exitAlways()); }
    public void set(ModeType mode){ this.mode = mode; }

    public <T extends ValueMode> T enableCycling(ModeType... values){ this.values = values; return (T) this; }

    public interface ModeType extends Decision {  double getValue(); };
}
