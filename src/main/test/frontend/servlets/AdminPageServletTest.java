package frontend.servlets;

import base.AccountService;
import frontend.AccountService.AccountServiceImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class AdminPageServletTest {
    private AccountService accountService;
    SignInServlet signInServlet;
    AdminPageServlet adminPageServlet;

    @Before
    public void setUp()
    {
        accountService = AccountServiceImpl.getInstance();
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
        accountService.addUser("username", "em@i.l", "password");
        accountService.addUser("another", "p@ch.ta", "dude");
        signInServlet.signInUser("username", "password", "ayedee");
        HashMap<String, Object> resultSet = adminPageServlet.getAdminInfo();
        Assert.assertEquals(resultSet.get("users"), 2);
        Assert.assertEquals(resultSet.get("logusers"), 1);
        Assert.assertEquals(resultSet.get("status"), "run");
    }
}
