package elements;

import util.condition.Decision;

public enum FieldPlacement implements Decision {

    UPPER("Further from audience"), LOWER("Closer to audience"), UNKNOWN("Unknown");

    private final String placement;

    FieldPlacement(String p) { this.placement = p; }

    public String getPlacement() { return placement; }

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

    @Override
    public String toString() {
        return getPlacement();
    }
}
