package userinterface;

import domain.Entity;
import domain.User;
import domain.UserFriendDTO;
import service.Network;
import domain.validation.ValidationException;
import service.ServiceFriendship;
import service.ServiceUser;
import utils.Months;

import java.util.Objects;
import java.util.Scanner;

/**
 * class UI for the user interface of the project
 * servUser-Service
 */
public class UI {
    ServiceUser servUser;
    ServiceFriendship servFriendship;
    private User currentUser = null;  //for log in use

    /**
     * constructor for the UI
     * @param userService the service of the application
     */
    public UI(ServiceUser userService, ServiceFriendship friendshipService) {
        this.servUser = userService;
        this.servFriendship = friendshipService;
    }

    public void run(){
        Scanner in = new Scanner(System.in);
        boolean running = true;
        while(running){
            showUserOptions();
            System.out.println("Enter your option: ");
            String option = in.nextLine();

            switch (option){
                case "1" -> {
                    System.out.println();
                    System.out.println("Register down below. Please provide: first name, last name");
                    System.out.println();

                    adduser();
                }
                case "2" -> {
                    while(true){
                        User loggedUser = login();
                        if(loggedUser != null){
                            currentUser = loggedUser;
                            break;
                        }
                        System.out.println("User does not exist!");
                    }
                    System.out.println();
                    System.out.println("Login approved!");
                    System.out.println();

                    boolean logged = true;
                    while(logged){
                        System.out.println("Current logged user: " + currentUser.getId());
                        System.out.println();
                        System.out.println("1.Send a friendship request");
                        System.out.println("2.Show my friendship request");
                        System.out.println("3.Logout");
                        System.out.println("\nEnter your option: ");

                        Scanner loggedUserInput = new Scanner(System.in);
                        String userOption = loggedUserInput.nextLine();

                        if(userOption.equals("1")){
                            //do the sending friendship stuff
                        }
                        if(userOption.equals("2")){
                            //show the friendship requests for the logged user
                        }
                        if(userOption.equals("3")){
                            //logout
                            logged = false;
                        }
                    }
                }
                case "3" -> {
                    show();
                }
                case "4" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("wrong command");
            }
        }
    }

    private void showUserOptions(){
        System.out.println();
        System.out.println("1.Register");
        System.out.println("2.Login");
        System.out.println("3.Menu");
        System.out.println("4.Exit");
        System.out.println();
    }

    private User login(){
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Enter ID: ");
            Long id = Long.parseLong(scanner.nextLine());
            return servUser.findOne(id);
        }catch (NumberFormatException ex){
            ex.printStackTrace();
        }catch (ValidationException ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }

    /**
     * show the menu and keep track of the command
     */
    public void show() {
        String cmd;
        Scanner scanner = new Scanner(System.in);
        show_menu();

        while (true) {
            System.out.println("\nYour input: ");
            cmd = scanner.nextLine();
            if (Objects.equals(cmd, "x")) {
                System.out.println("Sayonara Suka!");
                break;
            }
            menu(cmd);
        }
    }

    private void show_menu() {
        System.out.println("""
                --------------------
                █▀▄▀█ █▀▀ █▄░█ █░█
                █░▀░█ ██▄ █░▀█ █▄█
                --------------------""");
        System.out.println("""
                (0)-Menu
                (1)-Add user
                (2)-Update user
                (3)-Delete user
                (4)-Add a friendship
                (5)-Delete a friendship
                (6)-Print the number of communities
                (7)-Biggest community
                (8)-Print users
                (9)-Print friendships
                (10)-Print user friends
                (11)-Print user friends made on a specific month
                (x)-Exit""");
    }

    /**
     * select the command chosen
     * @param cmd-String
     */
    public void menu(String cmd) {
        switch (cmd) {
            case "0" -> show_menu();
            case "1" -> adduser();
            case "2" -> updateuser();
            case "3" -> deleteuser();
            case "4" -> addfriendship();
            case "5" -> deletefriendship();
            case "6" -> communities();
            case "7" -> biggest_community();
            case "8" -> printUsers();
            case "9" -> printFriendships();
            case "10" -> getUserFriendsUI();
            case "11" -> getUserFriendsByMonthUI();
            default -> System.out.println("wrong command");
        }
    }

    /**
     * print all the Friendships
     */
    private void printFriendships() {
        System.out.println("""

                █▀▀ █▀█ █ █▀▀ █▄░█ █▀▄ █▀ █░█ █ █▀█ █▀
                █▀░ █▀▄ █ ██▄ █░▀█ █▄▀ ▄█ █▀█ █ █▀▀ ▄█:""");
        servFriendship.printFr().forEach(x-> System.out.println(x.toString() + x.getDate().getMonth()));
    }

    /**
     * print all the Users
     */
    private void printUsers() {
        System.out.println("""

                █░█ █▀ █▀▀ █▀█ █▀
                █▄█ ▄█ ██▄ █▀▄ ▄█""");
        servUser.printUs().forEach(x-> System.out.println(x.toString()));
    }


    private void getUserFriendsByMonthUI(){
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Id of user you want to show its friends:");
            Long id = Long.parseLong(scanner.nextLine());
            System.out.println("Give the month when friendship was made: (CAPS LOCK ONLY!)");
            String month = (scanner.nextLine());
            if(servUser.findFriendshipsByMonth(id,month) == null)
                System.out.println("User does not have friendships made on this month!");
            else
                for(UserFriendDTO friendDTO : servUser.findFriendshipsByMonth(id,month)){
                    System.out.println(friendDTO);
                }
        } catch (IllegalArgumentException ex){
            System.out.println("Id must be an integer number!");
        } catch (ValidationException ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * read an id and if is valid display all friends of the user with that id
     * otherwise, catch the exception
     */
    private void getUserFriendsUI(){
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Id of user you want to show its friends:");
            Long id = Long.parseLong(scanner.nextLine());
            for(UserFriendDTO friendDTO : servUser.getFriendsForUser(id)){
                System.out.println(friendDTO);
            }
        } catch (IllegalArgumentException ex){
            System.out.println("Id must be an integer number!");
        } catch (ValidationException ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * create a Network and print the number of connected Components
     */
    private void communities(){
        Network nw=new Network(Math.toIntExact(servUser.get_size()));
        nw.addUsers(servUser.printUs());
        nw.addFriendships(servFriendship.printFr());
        System.out.println(nw.connectedComponents());
    }
    /**
     * create a Network and print the biggest Component
     */
    private void biggest_community() {

        Network nw = new Network(Math.toIntExact(servUser.get_size()));
        nw.addUsers(servUser.printUs());
        nw.addFriendships(servFriendship.printFr());
        System.out.println(nw.biggestComponent());
        servUser.printUs().forEach(x -> nw.biggestComponent().forEach(y ->{
            if (Objects.equals(Long.parseLong(String.valueOf(x.getId())), y))
                System.out.println(x.getFirstName()+ " " + x.getLastName());
        }));
    }

    /**
     * read two ids and delete friendship if it s valid
     * otherwise, catch the exception
     */
    private void deletefriendship() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Id of first user:");
        String id = scanner.nextLine();
        long nr1;
        long nr2;
        try {
            nr1 = Long.parseLong(id);
            System.out.println("Id of second user:");
            id = scanner.nextLine();
            nr2 = Long.parseLong(id);
            servFriendship.deleteFriend(nr1,nr2);
            System.out.println("Friendship deleted successfully!");
        }
        catch (NumberFormatException e) {
            System.out.println("Id must be an integer number");
        }
        catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * read two ids and add friendship if it s valid
     * otherwise, catch the exception
     */
    private void addfriendship() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Id of first user:");
        String id = scanner.nextLine();
        long nr1;
        long nr2;
        try {
            nr1 = Long.parseLong(id);
            System.out.println("Id of second user:");
            id = scanner.nextLine();
            nr2 = Long.parseLong(id);
            servFriendship.addFriend(nr1,nr2);
            System.out.println("Friendship added successfully!");
        }
        catch (NumberFormatException e) {
            System.out.println("Id must be an integer number");
        }
        catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * read two strings and if it s valid save the new user
     * otherwise, catch the exception
     */
    private void adduser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Write the first name:");
        String first = scanner.nextLine();
        System.out.println("Write the last name:");
        String last = scanner.nextLine();
        try {
            servUser.save( first, last);
            System.out.println("User added successfully!");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * read one id and two strings and if it s valid update the user
     * otherwise, catch the exception
     */
    private void updateuser() {
        long nr;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Id of user you want to update:");
        String id = scanner.nextLine();
        try {
            nr = Long.parseLong(id);
            System.out.println("Write the new first name:");
            String first = scanner.nextLine();
            System.out.println("Write the new last name:");
            String last = scanner.nextLine();
            servUser.update(nr, first, last);
            System.out.println("User updated successfully!");
        }
        catch (NumberFormatException e) {
            System.out.println("Id must be an integer number");
        }
        catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * read one id and if it s valid delete the user
     * otherwise, catch the exception
     */
    private void deleteuser() {
        long nr;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Id of user you want to delete:");
        String id = scanner.nextLine();
        try {
            nr = Long.parseLong(id);
            Entity deletedUser=servUser.delete(nr);
            deletedUser.toString();
            System.out.println("User " + deletedUser + " deleted successfully!");
        }
        catch (NumberFormatException e) {
            System.out.println("Id must be an integer number");
        }
        catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }
}
