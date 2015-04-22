package frontend.servlets;

import frontend.AccountService.AccountServiceImpl;
import org.junit.*;

public class SignUpServletTest
{
    private AccountServiceImpl accountService;
    SignUpServlet servlet;

    @Before
    public void setUp()
    {
        accountService = AccountServiceImpl.getInstance();
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
        servlet.addUser("email@email.email", "login", "password");
        Assert.assertEquals(accountService.getUserByName("login").getEmail(), "email@email.email");
        Assert.assertEquals(accountService.getUserByName("login").getPassword(), "password");
        Assert.assertEquals(accountService.getUserByName("login").getUsername(), "login");
    }

}
