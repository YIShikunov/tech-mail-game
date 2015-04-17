package frontend.servlets;

import frontend.AccountService.AccountService;
import org.junit.*;

public class SignUpServletTest
{
    private AccountService accountService;
    SignUpServlet servlet;

    @Before
    public void setUp()
    {
        accountService = new AccountService();
        servlet = new SignUpServlet(accountService);
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
        servlet.addUser("email@email.email", "password", "login");
        Assert.assertEquals(accountService.getUser("login").getEmail(), "email@email.email");
        Assert.assertEquals(accountService.getUser("login").getPassword(), "password");
        Assert.assertEquals(accountService.getUser("login").getLogin(), "login");
    }

}
