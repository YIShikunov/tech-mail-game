package frontend.servlets;

import base.AccountService.AccountService;
import frontend.AccountService.AccountServiceImpl;
import org.junit.*;

import java.sql.SQLException;

public class SignUpServletTest
{
    private AccountService accountService;
    SignUpServlet servlet;

    @Before
    public void setUp()
    {
        accountService = new AccountServiceImpl();
        servlet = new SignUpServlet(accountService);
    }

    @After
    public void tearDown()
    {
        servlet = null;
        accountService = null;
    }

    @Test
    public void test()
    {
        try {
            servlet.addUser("__TEST_USERNAME", "__TEST@EMAIL.EMAIL", "password");
            Assert.assertEquals(accountService.getUserByName("__TEST_USERNAME").getEmail(), "__TEST@EMAIL.EMAIL");
            Assert.assertEquals(accountService.getUserByName("__TEST_USERNAME").getPassword(), "password");
            Assert.assertEquals(accountService.getUserByName("__TEST_USERNAME").getUsername(), "__TEST_USERNAME");
        } catch (SQLException e) {
            Assert.fail();
        } finally {
            accountService.deleteUser("__TEST_USERNAME");
        }
    }

}
