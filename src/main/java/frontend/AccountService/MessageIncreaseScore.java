package frontend.AccountService;

import mechanics.MessageScoreIncreased;
import messageSystem.Address;
import messageSystem.Message;

/**
 * Created by nano on 29.05.2015.
 */

public final class MessageIncreaseScore extends MessageToAS {
    private int delta;
    private String name;

    public MessageIncreaseScore(Address from, Address to, String name, int delta) {
        super(from, to);
        this.delta = delta;
        this.name = name;
    }

    @Override
    protected void exec(AccountServiceImpl accountService) {
        int score = accountService.increaseScore(name, delta);
        Message back = new MessageScoreIncreased(getTo(), getFrom(), score);
        accountService.getMessageSystem().sendMessage(back);
    }
}
