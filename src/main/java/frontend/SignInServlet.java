package frontend;

import main.AccountService;
import main.UserProfile;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author v.chibrikov
 */
public class SignInServlet extends HttpServlet {
    private AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String login = "none";
        String email = "none";
        HttpSession session = request.getSession();



        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> pageVariables = new HashMap<>();

        pageVariables.put("loginStatus", "WHO ARE YOU???");
        pageVariables.put("online", 0);
        pageVariables.put("email", email);
        pageVariables.put("login", login);

        if (AccountService.isAuthorised(session.getId())) {
            pageVariables.put("loginStatus", "Hello, "+ AccountService.getSessions(session.getId()).getLogin());
            pageVariables.put("online", 1);
        }

        response.getWriter().println(PageGenerator.getPage("authstatus.html", pageVariables));
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String email = "";
        HttpSession session = request.getSession();


        response.setStatus(HttpServletResponse.SC_OK);

        UserProfile profile = accountService.getUser(login);

        Map<String, Object> pageVariables = new HashMap<>();

        if (profile != null && profile.getPassword().equals(password)) {
            pageVariables.put("loginStatus", "Hello, "+profile.getLogin());
            pageVariables.put("online", 1);

            final Random random = new Random();
            session.setAttribute("ID" + Integer.toString(random.nextInt()), profile);

            accountService.addSessions(session.getId(), profile);
        } else {
            pageVariables.put("loginStatus", "Wrong login/password");
            pageVariables.put("online", 0);
        }



        pageVariables.put("email", email == null ? "none" : email);
        pageVariables.put("login", login == null ? "none" : login);

        response.getWriter().println(PageGenerator.getPage("authstatus.html", pageVariables));
    }
}
