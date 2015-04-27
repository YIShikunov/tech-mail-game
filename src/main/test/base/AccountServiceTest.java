package base;

import frontend.AccountService.AccountServiceImpl;
import frontend.AccountService.UserProfile;
import org.junit.*;

public class AccountServiceTest
{
    private AccountService accountService;

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
    }

}
