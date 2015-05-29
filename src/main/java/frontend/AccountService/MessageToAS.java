package frontend.AccountService;

import mechanics.GameProtocol;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;

/**
 * Created by nano on 29.05.2015.
 */


public abstract class MessageToAS extends Message {
    public MessageToAS(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof AccountServiceImpl) {
            exec((AccountServiceImpl) abonent);
        }
    }

    protected abstract void exec(AccountServiceImpl accountService);
}
