package messageSystem.FakeProject.FakeFrontend;

import messageSystem.Address;

public class MessageIsAuthorized extends MessageToFrontend
{
    private boolean auth;

    public MessageIsAuthorized(Address from, Address to, boolean auth) {
        super(from, to);
        this.auth = auth;
    }

    @Override
    protected void exec(FakeFrontend frontend) {
        frontend.isAuthorized(auth);
    }
}
