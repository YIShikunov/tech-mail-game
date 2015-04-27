package frontend.servlets;

import base.AccountService;
import frontend.AccountService.AccountServiceImpl;
import frontend.AccountService.UserProfile;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SignOutServletTest
{
    private AccountService accountService;
    SignInServlet signInServlet;
    SignOutServlet signOutServlet;

    @Before
    public void setUp()
    {
        accountService = new AccountServiceImpl();
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
        accountService.addUser("username", new UserProfile("username", "password", "em@i.l"));
        signInServlet.signInUser("username", "password", "em@i.l", "ayedee");
        signOutServlet.signOut("ayedee");
        Assert.assertEquals(accountService.getCountLogUsers(), Integer.valueOf(0));
    }

}