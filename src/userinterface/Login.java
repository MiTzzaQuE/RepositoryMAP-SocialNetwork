package userinterface;

import domain.Message;
import domain.User;
import domain.validation.ValidationException;
import service.ServiceMessage;
import service.ServiceUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * UI for the login
 * ID- the ID of the user logged in
 * servMessage - service of messages
 * servUser - service of users
 */
public class Login {
    ServiceMessage servMessage;
    ServiceUser servUser;
    UI ui;
    User currentUser;

    /**
     * Constructor
     * @param serviceMessage - serviceMessage
     * @param serviceUser - serviceUser
     */
    public Login(ServiceMessage serviceMessage, ServiceUser serviceUser, UI ui) {
        this.servMessage = serviceMessage;
        this.servUser = serviceUser;
        this.ui = ui;
    }

    /**
     * Function which run the application with its all menus
     */
    public void run(){
        Scanner in = new Scanner(System.in);
        boolean running = true;
        while(running){
            showUserOptions();
            System.out.println("Enter your option: ");
            String option = in.nextLine();

            switch (option){
                case "1" -> {
                    System.out.println("\nRegister down below. Please provide: first name, last name\n");
                    ui.adduser();
                }
                case "2" ->
                    //for log in use
                    userLogin();
                case "3" ->
                    ui.show();
                case "x" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("wrong command");
            }
        }
    }

    private void userLogin() {

        currentUser = login();
        if (currentUser == null )
            return;

        System.out.println("\nLogin approved!\n");

        boolean logged = true;
        showMenu();
        while(logged){
            System.out.println("Enter your option:");
            Scanner loggedUserInput = new Scanner(System.in);
            String userOption = loggedUserInput.nextLine();

            switch (userOption) {
                case "1" -> {
                    //do the sending friendship stuff
                }
                case "2" -> {
                    //show the friendship requests for the logged user
                }
                case "3" ->
                    //add a message
                    addMessage();
                case "4" ->
                    //reply to a message
                    addReply();
                case "5" ->
                    //show conversation
                    showPrivateChat();
                case "x" ->
                    //logout
                    logged = false;
                default -> System.out.println("wrong command");
            }
        }
    }

    private void showMenu() {
        System.out.println("Current logged user: "  + currentUser.getFirstName() + " " + currentUser.getLastName());
        System.out.println("""
        1.Send a friendship request
        2.Show my friendship requests
        3.Sent a message
        4.Reply to a message
        5.Show conversation with another user
        x.Logout
        """);
    }

    /**
     *
     */
    private void showUserOptions(){
        System.out.println("""
        1.Register
        2.Login
        3.Menu
        x.Exit""");
    }

    /**
     * Function that login a user to the application
     * @return the user if it exists on database
     */
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
     * Read the data and try to add a message
     */
    private void addMessage(){

        Scanner scanner = new Scanner(System.in);

        List<Long> to = new ArrayList<>();
        System.out.println("Message:");
        String msg = scanner.nextLine();
        String cmd;
        while(true){
            System.out.println("Choose a user id\nX-STOP");
            cmd = scanner.nextLine();
            if(Objects.equals(cmd,"x"))
                break;
            try{
                long id = Long.parseLong(cmd);
                servUser.findOne(id);
                if(currentUser.getId() == id)
                    throw new ValidationException("You cannot send a message to yourself");
                to.add(id);
            }
            catch (NumberFormatException exception){
                System.out.println(cmd + "is not a valid id");
            }
            catch (ValidationException exception){
                System.out.println(exception.getMessage());
            }
        }
        try{
            servMessage.save(currentUser.getId(),to,msg);
            System.out.println("Message sent!");
        }
        catch (ValidationException | IllegalArgumentException exception){
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Read the data and try to save a reply message
     */
    private void addReply(){
        Scanner scanner = new Scanner(System.in);
        String cmd;
        long idReply;
        System.out.println("Give id of the message");
        cmd = scanner.nextLine();
        try{
            idReply = Long.parseLong(cmd);
            System.out.println("Message:");
            String msg = scanner.nextLine();
            servMessage.saveReply(currentUser.getId(),msg,idReply);
        }
        catch (NumberFormatException e){
            System.out.println(cmd + " is not a valid id!");
        }
    }

    private void showPrivateChat(){

        Scanner scanner = new Scanner(System.in);
        String id = "";

        try{
            System.out.println("Id of second user:");
            id = scanner.nextLine();
            long id2 = Long.parseLong(id);
            servUser.findOne(id2);
            List<Message> conversation = servMessage.PrivateChat(currentUser.getId(),id2);
            conversation.forEach(System.out::println);
        }
        catch (NumberFormatException e){
            System.out.println(id + " is not a valid id!");
        }
        catch (ValidationException exception){
            System.out.println(exception.getMessage());
        }
    }
}
