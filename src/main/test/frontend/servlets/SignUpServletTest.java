package frontend.servlets;

import frontend.AccountService.AccountServiceImpl;
import org.junit.*;

import java.sql.SQLException;

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
        try {
            servlet.addUser("login", "email@email.email", "password");
            Assert.assertEquals(accountService.getUserByName("login").getEmail(), "email@email.email");
            Assert.assertEquals(accountService.getUserByName("login").getPassword(), "password");
            Assert.assertEquals(accountService.getUserByName("login").getUsername(), "login");
        } catch (SQLException e) {
            Assert.fail();
        }
    }

}
