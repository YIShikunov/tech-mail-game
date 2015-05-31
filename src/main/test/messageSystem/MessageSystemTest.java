package messageSystem;

import messageSystem.FakeProject.FakeAccountService.FakeAccountService;
import messageSystem.FakeProject.FakeAccountService.MessageRegisterUser;
import messageSystem.FakeProject.FakeAccountService.MessageVerifyUser;
import messageSystem.FakeProject.FakeFrontend.FakeFrontend;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessageSystemTest
{
    MessageSystem messageSystem;
    FakeAccountService accountService;
    FakeFrontend frontend;

    @Before
    public void setUp() {
        messageSystem = new MessageSystem();
        accountService = new FakeAccountService(messageSystem);
        frontend = new FakeFrontend(messageSystem);
        accountService.start();
        frontend.start();
    }

    @After
    public void tearDown() {
        frontend.interrupt();
        accountService.interrupt();
        messageSystem = null;
        accountService = null;
        frontend = null;
    }
    @Test
    public void testFakeProject() {
        Message msg = new MessageRegisterUser(frontend.getAddress(), accountService.getAddress(), "impostor", "fake");
        messageSystem.sendMessage(msg);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Assert.fail();
        }
        Assert.assertEquals(accountService.getPassword("impostor"), "fake");

        msg = new MessageVerifyUser(frontend.getAddress(),
                messageSystem.getAddressService().getAccountServiceAddress(), "impostor", "REAL");
        messageSystem.sendMessage(msg);
        try
        {
            Thread.sleep(400);
        }catch (InterruptedException e)
        {
            Assert.fail();
        }
        Assert.assertEquals(frontend.getAuthResult(), "False");

        msg = new MessageVerifyUser(frontend.getAddress(),
                messageSystem.getAddressService().getAccountServiceAddress(), "impostor", "fake");
        messageSystem.sendMessage(msg);
        try
        {
            Thread.sleep(400);
        }catch (InterruptedException e)
        {
            Assert.fail();
        }
        Assert.assertEquals(frontend.getAuthResult(), "True");
    }
}
