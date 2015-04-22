package frontend.servlets;

import frontend.AccountService.AccountServiceImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SignOutServletTest
{
    private AccountServiceImpl accountService;
    SignInServlet signInServlet;
    SignOutServlet signOutServlet;

    @Before
    public void setUp()
    {
        accountService = AccountServiceImpl.getInstance();
        signInServlet = new SignInServlet(accountService);
        signOutServlet = new SignOutServlet(accountService);
    }

    @After
    public void tearDown()
    {
        signInServlet = null;
        accountService = null;
        signOutServlet = null;
    }

    @Test
    public void testAddUser()
    {
        accountService.addUser("username", "em@i.l", "password");
        signInServlet.signInUser("username", "em@i.l", "password","ayedee");
        signOutServlet.signOut("ayedee");
        Assert.assertEquals(accountService.getCountLoggedInUsers(), Integer.valueOf(0));
    }

}