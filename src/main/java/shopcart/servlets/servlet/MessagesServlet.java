package shopcart.servlets.servlet;

import shopcart.servlets.util.H2Messages;
import shopcart.servlets.model.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MessagesServlet extends BaseServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(MessagesServlet.class);
    private static final long serialVersionUID = 7681636400975681333L;

    private static final String MESSAGES_TEMPLATE = "messages.mustache";

    private final H2Messages h2Messages;

    public MessagesServlet(H2Messages h2Messages) {
        this.h2Messages = h2Messages;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = user(request);
        List<Message> messages = h2Messages.messagesFor(user);
        Map<String,Object> map = baseMap(request);
        map.put("messages", messages);
        showView(response, MESSAGES_TEMPLATE, map);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = user(request);
        String message = request.getParameter("message");
        h2Messages.addMessage(user, message);
        response.sendRedirect(response.encodeRedirectURL("/messages"));
    }

}