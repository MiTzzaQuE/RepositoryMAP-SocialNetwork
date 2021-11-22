package repository.database;

import domain.Message;
import domain.User;
import domain.validation.Validator;
import repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
    public Message findOne( Long id ) {

        if(id == null)
            throw new IllegalArgumentException("Id must not be null!");

        Message msg;

        try(Connection connection = DriverManager.getConnection(url,username,password);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from messages where messages.id = ?")){
            preparedStatement.setInt(1,Math.toIntExact(id));
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                Long idm = resultSet.getLong("id");
                LocalDateTime date = LocalDateTime.ofInstant(resultSet.getTimestamp("datem").toInstant(), ZoneOffset.ofHours(0));
                Long fromId = resultSet.getLong("fromm");
                User from = findOneUser(fromId);
                String to1 = resultSet.getString("tom");
                List<String> toId = new ArrayList<String>(Arrays.asList(to1.split(" ")));
                toId.remove(0); // lose the first element because it's a space there from the stream reduce :(

                List<User> to = new ArrayList<>();
                List<Long> idList = toId.stream()
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
                idList.forEach( x -> to.add(findOneUser(x)) );
                String message = resultSet.getString("messagem");
                Long idReply = resultSet.getLong("replym");

                Message reply = this.findOne(idReply);
                msg = new Message(from,to,message);
                msg.setId(idm);
                msg.setDate(date);
                msg.setRepliedTo(reply);
                return msg;
            }
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Message> findAll() {

        Set<Message> messages = new HashSet<>();
        Message msg = null;

        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from messages order by datem")){
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Long idm = resultSet.getLong("id");
                LocalDateTime date = LocalDateTime.ofInstant(resultSet.getTimestamp("datem").toInstant(), ZoneOffset.ofHours(0));
                Long fromId = resultSet.getLong("fromm");
                User from = findOneUser(fromId);
                String to1 = resultSet.getString("tom");
                List<String> toId = new ArrayList<String>(Arrays.asList(to1.split(" ")));
                toId.remove(0); // lose the first element because it's a space there from the stream reduce :(

                List<User> to = new ArrayList<>();
                List<Long> idList = toId.stream()
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
                idList.forEach( x -> to.add(findOneUser(x)) );
                String message = resultSet.getString("messagem");
                Long idReply = resultSet.getLong("replym");

                Message reply = this.findOne(idReply);
                msg = new Message(from,to,message);
                msg.setId(idm);
                msg.setDate(date);
                msg.setRepliedTo(reply);
                messages.add(msg);
            }
            return messages;
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return messages;
    }

    @Override
    public Message save( Message entity ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm");

        if(entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }
        validator.validate(entity);
        Message message = null;

        String sql = "insert into messages (fromm, tom, messagem, datem, replym ) values (?,?,?,'"+entity.getDate().format(formatter)+"',?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            message = this.findOne(entity.getId());
            if(message != null)
                return message;

            ps.setLong(1, entity.getFrom().getId());
            String ss = String.valueOf(entity.getTo()
                    .stream()
                    .map(x -> x.getId().toString())
                    .reduce("",(x,y) -> x+" "+y));

            ps.setString(2,ss);
            ps.setString(3,entity.getMessage());
            if(entity.getRepliedTo() != null)
                ps.setLong(4,entity.getRepliedTo().getId());
            else
                ps.setLong(4,-1L);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message delete( Long id ) {
        if(id == null)
            throw new IllegalArgumentException("ID must not be null!");
        Message messageRemoved = null;

        try(Connection connection = DriverManager.getConnection(url,username,password)) {
            PreparedStatement ps = connection.prepareStatement("delete from messages where id = ?");

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
    public Message update( Message entity ) {
        return null;
    }

    /**
     * Function that search one user with a given id
     * @param fromId - integer, id of user
     * @return the User with the given id
     */
    private User findOneUser( Long fromId ) {

        if(fromId == null)
            throw new IllegalArgumentException("Id must not be null");

        User user;

        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users where users.id = ?")){
            statement.setInt(1, Math.toIntExact(fromId));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                user = new User(firstName,lastName);
                user.setId(fromId);
                return user;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Find the friends of one user
     * @param id - integer, user id
     * @return - list of users
     */
    private List<User> FindFriends (Long id){

        List<User> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select id, first_name, last_name, date\n" +
                     "from users u inner join friendships f on u.id = f.id1 or u.id=f.id2\n" +
                     "where (f.id1= ? or f.id2 = ? )and u.id!= ?")){
            statement.setLong(1, id);
            statement.setLong(2, id);
            statement.setLong(3, id);
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    Long idNew = resultSet.getLong("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");

                    User user = new User(firstName,lastName);
                    user.setId(idNew);

                    users.add(user);
                }
            }
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return null;
    }
}
