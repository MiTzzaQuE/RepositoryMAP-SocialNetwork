import domain.Friendship;
import domain.Message;
import domain.Tuple;
import domain.User;
import domain.validation.FriendshipValidator;
import domain.validation.MessageValidator;
import domain.validation.UserValidator;
import repository.Repository;
import repository.database.FriendshipDbRepository;
import repository.database.MessageDbRepository;
import repository.database.UserDbRepository;
import service.ServiceFriendship;
import service.ServiceMessage;
import service.ServiceUser;
import userinterface.UI;

/**
 * Where all the magic starts
 */
public class Main {
    /**
     * main function of the program
     * @param args list of arguments from the line command
     */
    public static void main(String[] args)
    {
        Repository<Long,User> repo =
                new UserDbRepository("jdbc:postgresql://localhost:5432/socialnetwork","postgres","1234",new UserValidator());
        Repository<Tuple<Long,Long>, Friendship> repofriends =
                new FriendshipDbRepository("jdbc:postgresql://localhost:5432/socialnetwork","postgres","1234", new FriendshipValidator());
        Repository<Long, Message> repoMessage =
                new MessageDbRepository("jdbc:postgresql://localhost:5432/socialnetwork","postgres","1234", new MessageValidator());

        ServiceUser serv = new ServiceUser(repo,repofriends);
        ServiceFriendship servFr = new ServiceFriendship(repo,repofriends);
        ServiceMessage servMsg = new ServiceMessage(repo,repoMessage);

        UI userinterface = new UI(serv,servFr,servMsg);
        userinterface.run();
    }
}
