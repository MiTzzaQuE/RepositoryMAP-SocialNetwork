package repository.database;

import domain.Message;
import domain.validation.Validator;
import repository.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

/**
 * DataBase user repository made for sql use
 * implements the base interface Repository
 * contains objects of type Long and Message
 */
public class MessageDbRepository implements Repository<Long, Message> {

    private final String url;
    private final String username;
    private final String password;
    private final Validator<Message> validator;

    /**
     * Public constructor for the UserDataBase Repository
     * @param url - String
     * @param username - String
     * @param password - String
     * @param validator - Validator
     */
    public MessageDbRepository(String url, String username, String password, Validator<Message> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }


    @Override
    public Message findOne(Long id) {
        return null;
    }

    @Override
    public Iterable<Message> findAll() {
        return null;
    }

    @Override
    public Message save(Message entity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm");

        if(entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }
        validator.validate(entity);

        String sql = "insert into messages (from, to, message, date, replied_to ) values (?,?,?,'"+entity.getDate().format(formatter)+"',?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getFrom().getId());
            //ps.setString(2, entity.getTo().toString());  aici nu stiu cum facem trebe sa ne gandim la ceva fc
            ps.setString(3,entity.getMessage());
            ps.setLong(4,entity.getRepliedTo().getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message delete(Long id) {
        if(id == null)
            throw new IllegalArgumentException("ID must not be null!");
        Message messageRemoved = null;
        String sql = "delete from messages where id = ?";
        try(Connection connection = DriverManager.getConnection(url,username,password)) {
            PreparedStatement ps = connection.prepareStatement(sql);

            messageRemoved = this.findOne(id);
            if(messageRemoved == null)
                return null;

            ps.setLong(1,id);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return messageRemoved;
    }

    @Override
    public Message update(Message entity) {
        return null;
    }
}
