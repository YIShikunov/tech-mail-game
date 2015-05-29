package messageSystem.FakeProject.FakeAccountService;

import messageSystem.Recipient;
import messageSystem.Address;
import messageSystem.Message;

/**
 * Created by Artem on 5/28/2015.
 */
public abstract class MessageToAccountService extends Message {
    public MessageToAccountService(Address from, Address to) {
        super(from, to);
    }

    @Override
    public final void exec(Recipient recipient) {
        if (recipient instanceof FakeAccountService) {
            exec((FakeAccountService) recipient);
        }
    }

    protected abstract void exec(FakeAccountService service);
}
