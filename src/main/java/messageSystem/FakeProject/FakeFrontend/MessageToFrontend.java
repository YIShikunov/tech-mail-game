package messageSystem.FakeProject.FakeFrontend;

import messageSystem.Address;
import messageSystem.FakeProject.FakeAccountService.FakeAccountService;
import messageSystem.Message;
import messageSystem.Recipient;

public abstract class MessageToFrontend extends Message {
    public MessageToFrontend(Address from, Address to) {
        super(from, to);
    }

    @Override
    public final void exec(Recipient recipient) {
        if (recipient instanceof FakeFrontend) {
            exec((FakeFrontend) recipient);
        }
    }

    protected abstract void exec(FakeFrontend service);
}
