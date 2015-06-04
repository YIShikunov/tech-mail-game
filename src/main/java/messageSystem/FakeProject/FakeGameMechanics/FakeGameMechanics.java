package messageSystem.FakeProject.FakeGameMechanics;

import messageSystem.Recipient;
import messageSystem.Address;
import messageSystem.MessageSystem;

public class FakeGameMechanics implements Recipient, Runnable {
    private final Address address = new Address();
    private final MessageSystem messageSystem;

    public FakeGameMechanics(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
        messageSystem.getAddressService().registerGameMechanics(this);
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    @Override
    public Address getAddress() {
        return address;
    }

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