package frontend.servlets;

import frontend.AccountService.AccountServiceImpl;
import utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignOutServlet extends HttpServlet {
    private AccountServiceImpl accountServiceImpl;

    public SignOutServlet(AccountServiceImpl accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();

        if (AccountServiceImpl.isAuthorised(session.getId())) {
            AccountServiceImpl.delSessions(session.getId());
        }

        response.getWriter().println(PageGenerator.getPage("bystatus.html", pageVariables));
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> pageVariables = new HashMap<>();

        if (AccountServiceImpl.isAuthorised(session.getId())) {
            AccountServiceImpl.delSessions(session.getId());
        }

        response.getWriter().println(PageGenerator.getPage("bystatus.html", pageVariables));
    }
}
