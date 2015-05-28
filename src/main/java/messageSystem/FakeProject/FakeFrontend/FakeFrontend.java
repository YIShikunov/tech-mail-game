package messageSystem.FakeProject.FakeFrontend;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;

/**
 * Created by Artem on 5/28/2015.
 */
public class FakeFrontend  implements Abonent, Runnable {
    private final Address address = new Address();
    private final MessageSystem messageSystem;

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

    @Override
    public void run() {
        while (!Thread.interrupted()){
            messageSystem.execForAbonent(this);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
