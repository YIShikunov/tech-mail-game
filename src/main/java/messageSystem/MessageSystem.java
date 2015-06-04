package messageSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Artem on 5/28/2015.
 */
public final class MessageSystem {
    private final Map<Address, ConcurrentLinkedQueue<Message>> messages = new HashMap<>();
    private final AddressService addressService = new AddressService();

    public MessageSystem() {
    }

    public AddressService getAddressService() {
        return addressService;
    }

    public void addService(Recipient recipient) {
        messages.put(recipient.getAddress(), new ConcurrentLinkedQueue<Message>());
    }

    public void sendMessage(Message message) {
        messages.get(message.getTo()).add(message);
    }

    public void execForRecipient(Recipient recipient) {
        ConcurrentLinkedQueue<Message> queue = messages.get(recipient.getAddress());
        while (!queue.isEmpty()) {
            Message message = queue.poll();
            message.exec(recipient);
        }
    }
}
