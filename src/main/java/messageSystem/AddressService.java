package messageSystem;

import base.AccountService.AccountService;
import mechanics.GameProtocol;

/**
 * Created by Artem on 5/28/2015.
 */

public final class AddressService {
    private Address gameMechanics;
    private Address accountService;


    public void registerAccountService(AccountService accountService) {
        this.accountService = accountService.getAddress();
    }

    public void registerGameMechanics(GameProtocol gameMechanics) {
        this.gameMechanics = gameMechanics.getAddress();
    }

    public Address getGameMechanicsAddress() {
        return gameMechanics;
    }
    public Address getAccountServiceAddress() {
        return accountService;
    }
}