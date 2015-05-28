package messageSystem.FakeProject.FakeAccountService;

import messageSystem.Abonent;
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
    public final void exec(Abonent abonent) {
        if (abonent instanceof FakeAccountService) {
            exec((FakeAccountService) abonent);
        }
    }

    protected abstract void exec(FakeAccountService service);
}
