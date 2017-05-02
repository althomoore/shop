package shopcart.servlets.servlet;

import shopcart.servlets.login.MemUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

public class LoginServlet extends BaseServlet {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(LoginServlet.class);
    private static final long serialVersionUID = 4874813555417592107L;

    private static final String LOGIN_TEMPLATE = "login.mustache";

    private final MemUser loginService;

    public LoginServlet(MemUser loginService) {
        this.loginService = loginService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = user(request);
        if (user != null) {
            response.sendRedirect(response.encodeRedirectURL("/index.must"));
        } else {
            Map<String,Object> map = baseMap(request);
            showView(response, LOGIN_TEMPLATE, map);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String action = request.getParameter("act");
        if (isEmpty(userName)) {
            issue(PLAIN_TEXT_UTF_8, HttpServletResponse.SC_BAD_REQUEST, "User name is not set", response);
            return;
        } else if (isEmpty(password)) {
            issue(PLAIN_TEXT_UTF_8, HttpServletResponse.SC_BAD_REQUEST, "Password is not set", response);
            return;
        } else if ("login".equals(action)) {
            if (!doLogin(request, userName, password)) {
                response.sendRedirect(response.encodeRedirectURL("/login"));
                return;
            }
        } else if ("register".equals(action)) {
            if (!doRegister(request, userName, password)) {
                response.sendRedirect(response.encodeRedirectURL("/login"));
                return;
            }
        } else {
            response.sendRedirect(response.encodeRedirectURL("/login"));
            return;
        }
        response.sendRedirect(response.encodeRedirectURL(redirect(request)));
    }

    private boolean doLogin(HttpServletRequest request, String userName, String password) throws IOException {
        if (loginService.login(userName, password)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", userName);
            LOG.info("logged in as " + userName);
            return true;
        }
        return false;
    }

    private boolean doRegister(HttpServletRequest request, String userName, String password) throws IOException {
        if (loginService.register(userName, password)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", userName);
            LOG.info("registered " + userName);
            return true;
        }
        return false;
    }

    private static boolean isEmpty(String s) {
        return s == null || "".equals(s);
    }

}