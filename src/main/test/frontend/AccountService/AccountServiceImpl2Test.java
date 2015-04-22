package frontend.AccountService;

import org.eclipse.jetty.server.Authentication;
import org.junit.*;

public class AccountServiceImpl2Test
{
    private AccountServiceImpl accountService;
/*
    @Before
    public void setUp()
    {
        accountService = new AccountServiceImpl();
    }

    @After
    public void tearDown()
    {
        accountService = null;
    }

    @Test
    public void testAddUser()
    {
        boolean success = accountService.addUser("username", new UserProfile("username", "password", "em@i.l"));
        Assert.assertTrue(success);
        boolean failure = accountService.addUser("username", new UserProfile("duplicate", "ord", "m@i.l"));
        Assert.assertFalse(failure);
        UserProfile user = accountService.getUser("sneak");
        Assert.assertNull(user);
        user = accountService.getUser("username");
        Assert.assertEquals(user.getEmail(), "em@i.l");
        Assert.assertEquals(user.getLogin(), "username");
        Assert.assertEquals(user.getPassword(), "password");
    }*/

    @Before
    public void setUp()
    {
        accountService = AccountServiceImpl.getInstance();
    }

    @After
    public void tearDown()
    {
        accountService = null;
    }

    @Test
    public void testAddUser()
    {
        accountService.addUser("username", "email", "password");
        UserDataSet user = accountService.getUser(1);
        Assert.assertNotNull(user);
    }

}
