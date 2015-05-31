package messageSystem.FakeProject.FakeAccountService;

import messageSystem.Address;
import messageSystem.FakeProject.FakeFrontend.MessageIsAuthorized;
import messageSystem.Message;


public final class MessageVerifyUser extends MessageToAccountService {
    private String name;
    private String password;

    public MessageVerifyUser(Address from, Address to, String name, String password) {
        super(from, to);
        this.name = name;
        this.password = password;
    }

    @Override
    protected void exec(FakeAccountService service) {
        final boolean auth = service.verifyUser(name, password);
        final Message back = new MessageIsAuthorized(getTo(), getFrom(), auth);
        service.getMessageSystem().sendMessage(back);
    }
}
