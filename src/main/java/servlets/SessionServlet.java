package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/auth")
public class SessionServlet extends HttpServlet {
    //get logged user profile
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AccountService accountService = AccountService.getInstance();
        String form = req.getParameter("form");
        if (form == null) {
            form = "login";
        }

        UserProfile profile = accountService.getUserBySessionId(req.getSession().getId());
        if (profile == null) {
            if (form.equalsIgnoreCase("registration")) {
                req.getRequestDispatcher("registration.jsp").forward(req, resp);
            } else {
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/files");
        }
    }

    //sign in
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AccountService accountService = AccountService.getInstance();
        String form = req.getParameter("form");
        if (form == null) {
            form = "login";
        }

        if (form.equalsIgnoreCase("logout")) {
            accountService.deleteSession(req.getSession().getId());
            resp.sendRedirect(req.getContextPath() + "/auth");
            return;
        }

        String login = req.getParameter("login");
        String email = req.getParameter("email");
        String pass = req.getParameter("pass");

        if (form.equalsIgnoreCase("registration")) {

            if (login.equals("") || pass.equals("") || email.equals("")) {
                req.getRequestDispatcher("registration.jsp").forward(req, resp);
                return;
            }
            UserProfile newProfile = new UserProfile(login, pass, email);
            accountService.addNewUser(newProfile);
            accountService.addSession(req.getSession().getId(), newProfile);

        } else {
            UserProfile profile = accountService.getUserByLogin(login);
            if (profile == null || !profile.getPass().equals(pass)) {
                req.getRequestDispatcher("login.jsp").forward(req, resp);
                return;
            }
            accountService.addSession(req.getSession().getId(), profile);
        }

        resp.sendRedirect(req.getContextPath() + "/files");
    }
}