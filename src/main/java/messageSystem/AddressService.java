package messageSystem;

import messageSystem.FakeProject.FakeAccountService.FakeAccountService;
import messageSystem.FakeProject.FakeFrontend.FakeFrontend;
import messageSystem.FakeProject.FakeGameMechanics.FakeGameMechanics;

/**
 * Created by Artem on 5/28/2015.
 */

public final class AddressService {
    private Address frontend;
    private Address gameMechanics;
    private Address accountService;

    public void registerFrontEnd(FakeFrontend frontend) {
        this.frontend = frontend.getAddress();
    }

    public void registerAccountService(FakeAccountService accountService) {
        this.accountService = accountService.getAddress();
    }

    public void registerGameMechanics(FakeGameMechanics gameMechanics) {
        this.gameMechanics = gameMechanics.getAddress();
    }

    public Address getFrontEndAddress() {
        return frontend;
    }
    public Address getGameMechanicsAddress() {
        return gameMechanics;
    }
    public Address getAccountServiceAddress() {
        return accountService;
    }
}