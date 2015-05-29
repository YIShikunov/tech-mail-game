package messageSystem.FakeProject.FakeFrontend;

import messageSystem.Recipient;
import messageSystem.Address;
import messageSystem.MessageSystem;

public class FakeFrontend extends Thread implements Recipient {
    private final Address address = new Address();
    private final MessageSystem messageSystem;

    private String authResult;

    public FakeFrontend(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
        messageSystem.getAddressService().registerFrontEnd(this);
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    public void isAuthorized(boolean auth)
    {
        authResult = (auth ? "True" : "False");
    }

    public String getAuthResult() {return authResult;}

    @Override
    public void run() {
        while (!Thread.interrupted()){
            messageSystem.execForRecipient(this);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
