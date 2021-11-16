package repository.database;

import domain.Friendship;
import domain.Tuple;
import domain.validation.Validator;
import repository.Repository;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

/**
 * DataBase friendship repository made for sql use
 * implements the base interface Repository
 * contains objects of type Tuple of (Long,Long) and Friendship
 */

public class FriendshipDbRepository implements Repository<Tuple<Long,Long>,Friendship> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Friendship> validator;

    /**
     * Public constructor for the FriendshipDataBase Repository
     * @param url - String
     * @param username - String
     * @param password - String
     * @param validator - Validator
     */
    public FriendshipDbRepository(String url, String username, String password, Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    private Friendship extractFriendship(ResultSet resultSet) throws SQLException{
        Friendship friendship;
        if(resultSet.next()){
            Long id1 = resultSet.getLong("id1");
            Long id2 = resultSet.getLong("id2");
            LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();

            friendship = new Friendship();
            friendship.setId(new Tuple(id1,id2));
            friendship.setDate(date);
            return friendship;
        }
        return null;
    }

    @Override
    public Friendship findOne(Tuple<Long, Long> id) {
        if(id == null)
            throw new IllegalArgumentException("Id must not be null");
        Friendship friendship;
        String sql = "SELECT * FROM friendships WHERE id1 = ? AND id2 = ?";
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, Math.toIntExact(id.getLeft()));
            statement.setInt(2, Math.toIntExact(id.getRight()));
            ResultSet resultSet = statement.executeQuery();
            friendship = this.extractFriendship(resultSet);
            if(friendship != null)
                return friendship;

            statement.setInt(2, Math.toIntExact(id.getLeft()));
            statement.setInt(1, Math.toIntExact(id.getRight()));
            resultSet = statement.executeQuery();
            friendship = this.extractFriendship(resultSet);
            if(friendship != null)
                return friendship;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();

                Friendship friendship = new Friendship();
                friendship.setId(new Tuple(id1,id2));
                friendship.setDate(date);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    private void executeStatement(Friendship friendship, String sql){
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, Math.toIntExact(friendship.getId().getLeft()));
            statement.setInt(2, Math.toIntExact(friendship.getId().getRight()));
            statement.executeUpdate();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Friendship save(Friendship friendship) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm");

        if(friendship == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }
        validator.validate(friendship);
        String sql = "INSERT INTO Friendships(id1,id2,date) VALUES (?,?,'"+friendship.getDate().format(formatter)+"')";
        this.executeStatement(friendship,sql);
        return null;
    }

    @Override
    public Friendship delete(Tuple<Long, Long> id) {
        if(id == null || id.getLeft() == null || id.getRight() == null)
            throw new IllegalArgumentException("Id must not be null");

        Friendship friendship = this.findOne(id);
        if(friendship != null){
            String sql = "DELETE FROM friendships WHERE id1 = ? and id2 = ?";
            this.executeStatement(friendship,sql);
        }
        return friendship;
    }

    @Override
    public Friendship update(Friendship friendship) {
        return null;
    }
}
