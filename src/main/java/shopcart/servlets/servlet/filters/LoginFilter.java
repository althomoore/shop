package shopcart.servlets.servlet.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

public class LoginFilter implements Filter {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(LoginFilter.class);

    public LoginFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String uri = request.getRequestURI();

        //try to get the user session attribute
        //we need to be logged in to access this URL
        //method line 49
        String user = user(request);
        //if there is none, new user
        if (user == null) {
            HttpServletResponse response = (HttpServletResponse) resp;
            //method line 57
            redirect(request, uri);
            response.sendRedirect("/login");
            LOG.info("LOGIN filter redirect to /login, redirect to " + uri);
            //don't call any more filters
            return;
        }
        //are there any more filters, if not, call servlet
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
    }

    protected String user(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (String) session.getAttribute("user");
    }

    //sets the re-direct attribute in the session
    protected void redirect(HttpServletRequest request, String uri) {
        HttpSession session = request.getSession(true);
        session.setAttribute("redirect", uri);
    }
}