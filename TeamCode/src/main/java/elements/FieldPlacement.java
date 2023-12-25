package elements;

import util.condition.Decision;

public enum FieldPlacement implements Decision {
    /**
     * Represents the start position of the robot on the field
     */
    UPPER("Further from audience"), LOWER("Closer to audience"), UNKNOWN("Unknown");

    /**
     * String value
     */
    private final String placement;

    /**
     * Constructor
     * @param p
     */
    FieldPlacement(String p) { this.placement = p; }

    /**
     * Get string value
     * @return value
     */
    public String getPlacement() { return placement; }

    /**
     * Create placement from string
     * @param placement
     * @return placement
     */
    public static FieldPlacement create(String placement){
        switch (placement) {
            case "Further from audience":
                return UPPER;
            case "Closer to audience":
                return LOWER;
            case "Unknown":
                return UNKNOWN;
            default:
                return null;
        }
    }

    /**
     * To string returns placement
     * @return placement
     */
    @Override
    public String toString() { return getPlacement(); }
}
