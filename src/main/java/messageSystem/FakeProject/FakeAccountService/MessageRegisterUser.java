package messageSystem.FakeProject.FakeAccountService;

import messageSystem.Address;
import messageSystem.Message;

/**
 * Created by Artem on 5/28/2015.
 */
public final class MessageRegisterUser extends MessageToAccountService {
    private String name;
    private String password;

    public MessageRegisterUser(Address from, Address to, String name, String password) {
        super(from, to);
        this.name = name;
        this.password = password;
    }

    @Override
    protected void exec(FakeAccountService service) {
        service.registerUser(name, password);
    }
}
