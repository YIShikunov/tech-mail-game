package frontend.servlets;

import base.AccountService;
import frontend.AccountService.AccountServiceImpl;
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
        accountService = AccountServiceImpl.getInstance();
        signInServlet = new SignInServlet(accountService);
        signOutServlet = new SignOutServlet(accountService);
        accountService.addUser("__TEST_USERNAME", "__TEST@EMAIL.EMAIL", "password");
    }

    @After
    public void tearDown()
    {
        accountService.deleteUser("__TEST_USERNAME");
        signInServlet = null;
        accountService = null;
        signOutServlet = null;
    }

    @Test
    public void test()
    {
        Integer logUsers = accountService.getCountLoggedInUsers();
        signInServlet.signInUser("__TEST_USERNAME", "password","ayedeee");
        signOutServlet.signOut("ayedeee");
        Assert.assertEquals(accountService.getCountLoggedInUsers(), logUsers);
    }

}