package messageSystem.FakeProject.FakeAccountService;

import javafx.util.Pair;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;

import java.util.ArrayList;

/**
 * Created by Artem on 5/28/2015.
 */
public final class FakeAccountService implements Abonent, Runnable {
    private final Address address = new Address();
    private final MessageSystem messageSystem;

    private final ArrayList<Pair<String, String>> users = new ArrayList<>();

    public FakeAccountService(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
        messageSystem.getAddressService().registerAccountService(this);
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

    public boolean registerUser(String username, String password) {
        users.add(new Pair<String, String>(username, password));
        return true;
    }
}
