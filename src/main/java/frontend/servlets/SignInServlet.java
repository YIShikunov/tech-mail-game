package frontend.servlets;

import frontend.AccountService.AccountServiceImpl;
import frontend.AccountService.UserProfile;
import utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SignInServlet extends HttpServlet {
    private AccountServiceImpl accountServiceImpl;

    public SignInServlet(AccountServiceImpl accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
    }

    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String email = "none";
        HttpSession session = request.getSession();

        response.setStatus(HttpServletResponse.SC_OK);

        UserProfile profile = accountServiceImpl.getUser(login);

        Map<String, Object> pageVariables = new HashMap<>();

        if (profile != null && profile.getPassword().equals(password)) {
            pageVariables.put("loginStatus", "Hello, "+profile.getLogin()+"<br>"+profile.getEmail());
            pageVariables.put("online", 1);

            final Random random = new Random();
            session.setAttribute("ID" + Integer.toString(random.nextInt()), profile);

            accountServiceImpl.addSessions(session.getId(), profile);
        } else {
            pageVariables.put("loginStatus", "Wrong login/password");
            pageVariables.put("online", 0);
        }


        pageVariables.put("email", email);
        pageVariables.put("login", login == null ? "none" : login);

        response.getWriter().println(PageGenerator.getPage("authstatus.html", pageVariables));
    }
}
