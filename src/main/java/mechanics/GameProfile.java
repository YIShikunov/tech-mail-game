package mechanics;

public class GameProfile {
    private final String name;
    private String enemy;

    public GameProfile(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getEnemy() {
        return enemy;
    }

    public void setEnemy(String enemy) {
        this.enemy = enemy;
    }
}