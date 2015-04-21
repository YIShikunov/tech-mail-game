package frontend.servlets;

import frontend.AccountService.AccountServiceImpl;
import frontend.AccountService.UserProfile;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class AdminPageServletTest {
    private AccountServiceImpl accountService;
    SignInServlet signInServlet;
    AdminPageServlet adminPageServlet;

    @Before
    public void setUp()
    {
        accountService = new AccountServiceImpl();
        signInServlet = new SignInServlet(accountService);
        adminPageServlet = new AdminPageServlet(accountService);

    }

    @After
    public void tearDown()
    {
        signInServlet = null;
        accountService = null;
        adminPageServlet = null;
    }

    @Test
    public void testAdminInfo() {
        accountService.addUser("username", new UserProfile("username", "password", "em@i.l"));
        accountService.addUser("another", new UserProfile("another", "dude", "p@ch.ta"));
        signInServlet.signInUser("username", "password", "em@i.l", "ayedee");
        HashMap<String, Object> resultSet = adminPageServlet.getAdminInfo();
        Assert.assertEquals(resultSet.get("users"), 2+2); // +2 are the debug users - see AccountServiceImpl constructor
        Assert.assertEquals(resultSet.get("logusers"), 1);
        Assert.assertEquals(resultSet.get("status"), "run");
    }
}
