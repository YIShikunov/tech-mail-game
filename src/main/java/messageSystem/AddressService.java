package messageSystem;

import messageSystem.FakeProject.FakeAccountService.FakeAccountService;
import messageSystem.FakeProject.FakeFrontend.FakeFrontend;

/**
 * Created by Artem on 5/28/2015.
 */

public final class AddressService {
    private Address frontend;
    private Address accountService;

    public void registerFrontEnd(FakeFrontend frontend) {
        this.frontend = frontend.getAddress();
    }

    public void registerAccountService(FakeAccountService accountService) {
        this.accountService = accountService.getAddress();
    }


    public Address getFrontEndAddress() {
        return frontend;
    }
    public Address getAccountServiceAddress() {
        return accountService;
    }
}