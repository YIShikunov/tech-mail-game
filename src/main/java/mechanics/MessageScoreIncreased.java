package mechanics;

import messageSystem.Address;

/**
 * @author e.shubin
 */
public final class MessageScoreIncreased extends MessageToMechanics {
    private int score;

    public MessageScoreIncreased(Address from, Address to, int score) {
        super(from, to);
        this.score = score;
    }

    @Override
    protected void exec(GameProtocol gameProtocol) {
        gameProtocol.setScore(score);
    }
}
