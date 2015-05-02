package mechanics.GameState;

public enum FieldType {
    THRONE,BASE,NEUTRAL,ERROR;

    public static FieldType fromString(String s) {
        switch (s) {
            case ("throne"): return THRONE;
            case ("base"): return BASE;
            case ("neutral"): return NEUTRAL;
            default: return ERROR;
        }
    }
}
