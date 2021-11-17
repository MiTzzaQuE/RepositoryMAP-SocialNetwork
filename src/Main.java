import domain.Friendship;
import domain.Tuple;
import domain.User;
import domain.validation.FriendshipValidator;
import domain.validation.UserValidator;
import repository.Repository;
import repository.database.FriendshipDbRepository;
import repository.database.UserDbRepository;
import repository.file.FriendshipFile;
import repository.file.UserFile;
import service.ServiceFriendship;
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
                new UserDbRepository("jdbc:postgresql://localhost:5432/socialnetwork","postgres","222cafe592",new UserValidator());
        Repository<Tuple<Long,Long>, Friendship> repofriends =
                new FriendshipDbRepository("jdbc:postgresql://localhost:5432/socialnetwork","postgres","222cafe592", new FriendshipValidator());

        ServiceUser serv = new ServiceUser(repo,repofriends);
        ServiceFriendship servFr = new ServiceFriendship(repo,repofriends);
        UI userinterface = new UI(serv,servFr);
        userinterface.show();
    }
}
