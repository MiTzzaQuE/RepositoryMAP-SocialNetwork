package domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Message extends Entity<Long> {

    private User from;
    private List<User> to;
    private String message;
    private LocalDateTime date;
    private Message repliedTo;

    public Message(User from, List<User> to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.repliedTo=null;
    }

    public Message(User from, List<User> to, String message, Message reply) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.repliedTo = reply;
    }


    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public List<User> getTo() {
        return to;
    }

    public void setTo(List<User> to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Message getRepliedTo() {
        return repliedTo;
    }

    public void setRepliedTo(Message repliedTo) {
        this.repliedTo = repliedTo;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from=" + from +
                ", to=" + to +
                ", message='" + message + '\'' +
                ", date=" + date +
                ", repliedTo=" + repliedTo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return from.equals(message1.from) && to.equals(message1.to) && message.equals(message1.message) && date.equals(message1.date) && repliedTo.equals(message1.repliedTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, message, date, repliedTo);
    }

    /*
    public String listToString(){
        String listString = "";
        for(User user : to){
            listString = listString + user.getId().toString() + ",";
        }
        return listString;
    }
    */
}
