package base;

import frontend.AccountService.AccountServiceImpl;
import frontend.AccountService.UserDataSet;
import org.junit.*;

import java.sql.SQLException;

public class AccountServiceTest
{
    private AccountService accountService;

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
        try {
            boolean success = accountService.addUser("username", "em@i.l", "password");
            Assert.assertTrue(success);
            boolean failure = accountService.addUser("username", "duplicate@i.l", "pwd");
            Assert.assertFalse(failure);
            UserDataSet user = accountService.getUserByName("sneak");
            Assert.assertNull(user);
            user = accountService.getUserByName("username");
            Assert.assertEquals(user.getEmail(), "em@i.l");
            Assert.assertEquals(user.getUsername(), "username");
            Assert.assertEquals(user.getPassword(), "password");
        } catch (SQLException e) {
            Assert.fail();
        }
    }
}