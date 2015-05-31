package messageSystem.FakeProject.FakeAccountService;

import javafx.util.Pair;
import messageSystem.Recipient;
import messageSystem.Address;
import messageSystem.MessageSystem;

import java.util.ArrayList;
import java.util.HashMap;

public final class FakeAccountService extends Thread implements Recipient{
    private final Address address = new Address();
    private final MessageSystem messageSystem;

    protected final HashMap<String, String> users = new HashMap<>();

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
            messageSystem.execForRecipient(this);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public void registerUser(String username, String password) {
        users.put(username, password);
    }
    public boolean verifyUser(String username, String password)
    {
        return users.getOrDefault(username, "") == password;
    }
    public String getPassword(String username) {return users.get(username);}
}
