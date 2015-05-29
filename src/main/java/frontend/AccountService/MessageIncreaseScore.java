package frontend.AccountService;

import mechanics.MessageScoreIncreased;
import messageSystem.Address;
import messageSystem.Message;

/**
 * Created by nano on 29.05.2015.
 */

public final class MessageIncreaseScore extends MessageToAS {
    private int delta;

    public MessageIncreaseScore(Address from, Address to, int delta) {
        super(from, to);
        this.delta = delta;
    }

    @Override
    protected void exec(AccountServiceImpl accountService) {
        int score = accountService.increaseScore(delta);
        Message back = new MessageScoreIncreased(getTo(), getFrom(), score);
        accountService.getMessageSystem().sendMessage(back);
    }
}
