package frontend.servlets;

import frontend.AccountService.AccountServiceImpl;
import org.junit.*;

import java.util.HashMap;

public class SignInServletTest
{
    private AccountServiceImpl accountService;
    SignInServlet servlet;

    @Before
    public void setUp()
    {
        accountService = AccountServiceImpl.getInstance();
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
        accountService.addUser("username", "em@i.l", "password");
        HashMap<String, Object> response = servlet.signInUser("username", "em@i.l", "password", "ayedee");
        Assert.assertEquals(response.get("email"), "em@i.l");
        Assert.assertEquals(response.get("login"), "username");
        Assert.assertEquals(response.get("online"), 1);
        Assert.assertEquals(response.get("email"), "em@i.l");

        response = servlet.signInUser("username", "em@i.l", "wrongPassword", "ayedee");
        Assert.assertEquals(response.get("online"), 0);
    }

}
