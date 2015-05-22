package mechanics.GameState;

public enum Element {
    FIRE(0),METAL(1),WOOD(2),EARTH(3),WATER(4),ERROR(-1),BLANK(-2);

    private final int id;

    Element(int id){
        this.id = id;
    }

    public static Outcome battle(Element attacker, Element defender) {
        if (defender == BLANK && attacker == BLANK)
            return Outcome.DRAW;
        else if (attacker == BLANK)
            return Outcome.LOSS;
        else if (defender == BLANK)
            return Outcome.WIN;

        int outcome = defender.id - attacker.id;
        if (outcome < 0) outcome = 5 + outcome;
        switch (outcome) {
            case 0: return Outcome.DESTRUCTION;
            case 1: return Outcome.WIN;
            case 2: return Outcome.WIN;
            case 3: return Outcome.DRAW;
            case 4: return Outcome.LOSS;
            }
        return Outcome.ERROR;
    }

    public static Element value(int value) {
        switch (value) {
            case -2: return BLANK;
            case 0: return FIRE;
            case 1: return METAL;
            case 2: return WOOD;
            case 3: return EARTH;
            case 4: return WATER;
        }
        return ERROR;
    }
}
