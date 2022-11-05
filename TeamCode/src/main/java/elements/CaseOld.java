package elements;
import util.condition.Decision;

public enum CaseOld implements Decision {
    /**
     * Which case is it?
     */
    LEFT(Level.BOTTOM),
    CENTER(Level.MIDDLE),
    RIGHT(Level.TOP);

    private final Level level;

    CaseOld(Level l){
        this.level = l;
    }

    public Level getLevel(){
        return level;
    }

    public static CaseOld create(Level level){
        switch (level) {
            case BOTTOM:
                return LEFT;
            case MIDDLE:
                return CENTER;
            case TOP:
                return RIGHT;
            default:
                return null;
        }
    }
}
