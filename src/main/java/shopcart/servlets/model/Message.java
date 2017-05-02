package shopcart.servlets.model;

import lombok.Data;
import lombok.experimental.Accessors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;


@Data
@Accessors(chain=true)

public class Message {

    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(Message.class);

    private String userId;
    private String message;
    private Timestamp messageDate;
    private String reply;
    private Timestamp replyDate;

    public Message() {

    }
}