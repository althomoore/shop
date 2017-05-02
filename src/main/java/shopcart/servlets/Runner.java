package shopcart.servlets;

import shopcart.servlets.util.H2Messages;
import shopcart.servlets.login.MemUser;
import shopcart.servlets.servlet.LoginServlet;
import shopcart.servlets.servlet.MessagesServlet;
import shopcart.servlets.servlet.filters.LoginFilter;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class Runner {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(Runner.class);

    private static final int PORT = 9000;

    private final MemUser loginService;
    private final H2Messages h2Messages;

    private Runner() {
        loginService = new MemUser();
        h2Messages = new H2Messages(H2Messages.MEM_DB);
    }

    private void start() throws Exception {
        Server server = new Server(PORT);

        ServletContextHandler handler = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        handler.setInitParameter("org.eclipse.jetty.servlet.Default." + "resourceBase", "src/main/resources/webApp");

        //if filter matches a particular URI the ...servlet.filters.LoginFilter.doFilter() method is called
        LoginFilter filter = new LoginFilter();
        handler.addFilter(new FilterHolder(filter), "/messages", EnumSet.allOf(DispatcherType.class));

        LoginServlet login = new LoginServlet(loginService);
        handler.addServlet(new ServletHolder(login), "/login");

        MessagesServlet messages = new MessagesServlet(h2Messages);
        handler.addServlet(new ServletHolder(messages), "/messages");

        DefaultServlet ds = new DefaultServlet();
        handler.addServlet(new ServletHolder(ds), "/");

        server.start();
        LOG.info("Server started, will run until terminated");
        server.join();
    }

    public static void main(String[] args) {
        try {
            LOG.info("starting");
            new Runner().start();
        } catch (Exception e) {
            LOG.error("Unexpected error running shop: " + e.getMessage());
        }
    }
}
