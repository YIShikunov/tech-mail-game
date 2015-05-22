package frontend.servlets;

import base.AccountService.AccountService;
import frontend.AccountService.AccountServiceImpl;
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
        accountService.addUser("__TEST_USERNAME", "__TEST@EMAIL.EMAIL", "password");
    }

    @After
    public void tearDown()
    {
        accountService.deleteUser("__TEST_USERNAME");
        servlet = null;
        accountService = null;
    }

    @Test
    public void test()
    {
        HashMap<String, Object> response = servlet.signInUser("__TEST_USERNAME", "password", "ayedee");
        Assert.assertEquals(response.get("email"), "__TEST@EMAIL.EMAIL");
        Assert.assertEquals(response.get("login"), "__TEST_USERNAME");
        Assert.assertEquals(response.get("online"), 1);
        Assert.assertEquals(response.get("email"), "__TEST@EMAIL.EMAIL");
        accountService.deleteSession("ayedee");

        response = servlet.signInUser("__TEST_USERNAME", "wrongPassword", "ayedee");
        Assert.assertEquals(response.get("online"), 0);
    }

}
