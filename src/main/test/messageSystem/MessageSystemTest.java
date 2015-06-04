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
    }

    @After
    public void tearDown() {
        messageSystem = null;
        accountService = null;
        frontend = null;
    }
    @Test
    public void testFakeProject()
    {
        accountService.start();
        frontend.start();
        Message msg = new MessageRegisterUser(frontend.getAddress(), accountService.getAddress(),"impostor", "fake");
        messageSystem.sendMessage(msg);
        try
        {
            Thread.sleep(300);
        }catch (InterruptedException e)
        {
            Assert.fail();
        }
        Assert.assertEquals(accountService.getPassword("impostor"), "fake");
        msg = new MessageVerifyUser(frontend.getAddress(), accountService.getAddress(), "impostor", "REAL");
        messageSystem.sendMessage(msg);
        try
        {
            Thread.sleep(400);
        }catch (InterruptedException e)
        {
            Assert.fail();
        }
        Assert.assertEquals(frontend.getAuthResult(), "False");
        frontend.interrupt();
        accountService.interrupt();
    }
}
