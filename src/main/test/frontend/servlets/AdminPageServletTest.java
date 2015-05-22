package frontend.servlets;

import base.AccountService.AccountService;
import frontend.AccountService.AccountServiceImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class AdminPageServletTest {
    private AccountService accountService;
    SignInServlet signInServlet;
    SignOutServlet signOutServlet;
    AdminPageServlet adminPageServlet;

    Integer users;
    Integer logUsers;

    @Before
    public void setUp()
    {
        accountService =  new AccountServiceImpl();
        signInServlet = new SignInServlet(accountService);
        signOutServlet = new SignOutServlet(accountService);
        adminPageServlet = new AdminPageServlet(accountService);

        users = accountService.getCountUsers();
        logUsers = accountService.getCountLoggedInUsers();

        accountService.addUser("__TEST_USERNAME", "__TEST@EMAIL.EMAIL", "password");
        accountService.addUser("__OTHER_USERNAME", "__OTHER@EMAIL.EMAIL", "dude");
        signInServlet.signInUser("__TEST_USERNAME", "password", "ayedee");
    }

    @After
    public void tearDown()
    {
        accountService.deleteUser("__TEST_USERNAME");
        accountService.deleteUser("__OTHER_USERNAME");
        signOutServlet.signOut("ayedee");

        signInServlet = null;
        accountService = null;
        adminPageServlet = null;
    }

    @Test
    public void testAdminInfo() {
        HashMap<String, Object> resultSet = adminPageServlet.getAdminInfo();
        Assert.assertEquals(resultSet.get("users"), users+2);
        Assert.assertEquals(resultSet.get("logusers"), logUsers+1);
        Assert.assertEquals(resultSet.get("status"), "run");
    }
}
