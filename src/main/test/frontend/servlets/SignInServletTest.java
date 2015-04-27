package frontend.servlets;

import base.AccountService;
import frontend.AccountService.AccountServiceImpl;
import frontend.AccountService.UserProfile;
import org.junit.*;

import java.util.HashMap;

public class SignInServletTest
{
    private AccountService accountService;
    SignInServlet servlet;

    @Before
    public void setUp()
    {
        accountService = new AccountServiceImpl();
        servlet = new SignInServlet(accountService);
    }

    @After
    public void tearDown()
    {
        servlet = null;
        accountService = null;
    }

    @Test
    public void testAddUser()
    {
        accountService.addUser("username", new UserProfile("username", "password", "em@i.l"));
        HashMap<String, Object> response = servlet.signInUser("username", "password", "em@i.l", "ayedee");
        Assert.assertEquals(response.get("email"), "em@i.l");
        Assert.assertEquals(response.get("login"), "username");
        Assert.assertEquals(response.get("online"), 1);
        Assert.assertEquals(response.get("email"), "em@i.l");

        response = servlet.signInUser("username", "wrongPassword", "em@i.l", "ayedee");
        Assert.assertEquals(response.get("online"), 0);
    }

}
