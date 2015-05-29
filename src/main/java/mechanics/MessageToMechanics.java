package mechanics;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;

/**
 * @author e.shubin
 */
public abstract class MessageToMechanics extends Message {
    public MessageToMechanics(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GameProtocol) {
            exec((GameProtocol) abonent);
        }
    }

    protected abstract void exec(GameProtocol gameMechanics);
}
