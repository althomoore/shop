package shopcart.servlets.util;

import shopcart.servlets.model.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class H2Messages extends H2Base implements AutoCloseable {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(H2Messages.class);

    public static final String MEM_DB = "jdbc:h2:mem:shop";
    public static final String FILE_DB = "jdbc:h2:~/shop";

    public H2Messages(String dbFile) {
        super(dbFile);
        try {
            loadResource("/db/messages.sql");
        } catch (Exception e) {
            LOG.error("Can't find database driver: " + e.getMessage());
            throw new H2MessageException(e);
        }
    }

    public List<Message> messagesFor(String userId) {
        List<Message> out = new ArrayList<>();
        try (PreparedStatement ps = prepare("SELECT userId, message, msg_date, reply, reply_date FROM messages WHERE userId=? ORDER BY msg_date DESC")) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message msg = new Message()
                        .setUserId(rs.getString(1))
                        .setMessage(rs.getString(2))
                        .setMessageDate(rs.getTimestamp(3))
                        .setReply(rs.getString(4))
                        .setReplyDate(rs.getTimestamp(5));
                out.add(msg);
            }
        } catch (SQLException e) {
            throw new H2MessageException(e);
        }
        return out;
    }

    public void addMessage(String userId, String message) {
        long now = new java.util.Date().getTime();

        try (PreparedStatement ps = prepare("INSERT INTO messages (userId, message, msg_date, reply, reply_date) VALUES (?,?,?,?,?)")) {
            ps.setString(1, userId);
            ps.setString(2, message);
            ps.setTimestamp(3, new Timestamp(now));
            ps.setString(4, "Thank you for your message.  We will reply as soon as possible.");
            ps.setTimestamp(5, new Timestamp(now + 1000L));
            ps.execute();
        } catch (SQLException e) {
            throw new H2MessageException(e);
        }
    }

    private void loadResource(String name) {
        try {
            String cmd = new Scanner(getClass().getResource(name).openStream()).useDelimiter("\\Z").next();
            try (PreparedStatement ps = prepare(cmd)) {
                ps.execute();
            } catch (Exception e) {
                throw new H2MessageException(e);
            }
        } catch (IOException e) {
            throw new H2MessageException("Can't open " + name + " to load db commands: " + e.getMessage());
        }
    }

    public class H2MessageException extends RuntimeException {
        H2MessageException(Exception e) {
            super(e);
        }

        H2MessageException(String s) {
            super(s);
        }
    }
}