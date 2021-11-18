package domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ReplyMessage extends Message{

    private Message replyMessage;


    public ReplyMessage(User from, List<User> to, String message, LocalDateTime date, Message replyMessage) {
        super(from, to, message, date);
        this.replyMessage = replyMessage;
    }

    public Message getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(Message replyMessage) {
        this.replyMessage = replyMessage;
    }

    @Override
    public String toString() {
        return "ReplyMessage{" +
                "replyMessage=" + replyMessage +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ReplyMessage that = (ReplyMessage) o;
        return replyMessage.equals(that.replyMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), replyMessage);
    }
}
