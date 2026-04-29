import database.DatabaseMethods;
import utilities.User;
import utilities.passwordHashing;

import java.util.Scanner;

public class Main {

    Scanner sc = new Scanner(System.in);
    passwordHashing hash = new passwordHashing();
    DatabaseMethods DBmethod = new DatabaseMethods();
    int userChoice = -1;

    void main() {


        //Menu (Login/Register)
        do {


            while (true) {

                System.out.print("""
                    1. Login
                    2. Register new user
                    \s
                    Input:\s"""
                );

                if(sc.hasNextInt()){
                    userChoice = sc.nextInt();
                    sc.nextLine();
                    break;
                }else {
                    System.out.println("Invalid input, please enter a number.");
                    sc.nextLine();
                }
            }


            switch (userChoice) {
                case 1:
                    loginMenu();
                    break;
                case 2:
                    registerMenu();
                    break;
                default:
                    System.out.println("Something went wrong, try again.");
            }

        } while (true);

    }

    public void loginMenu() {

        User loggedInUser;
        String username;
        String password;

        //Menu (Login)
        do {

            System.out.println("Welcome, please put in credentials to login.");

            username = getUsername();
            password = getPassword();

            //Calls DB to check if user exists
            loggedInUser = DBmethod.verifyUserLogin(username, password);

            //If there is no role, return. (Only User and Admin allowed!)
            if (loggedInUser.getRole() != null) {
                if (loggedInUser.getRole().equals("USER") || loggedInUser.getRole().equals("ADMIN")) {
                    new LoggedInPanel().loggedInPanel(loggedInUser);
                } else {
                    System.out.println("Role did not match (USER / ADMIN)");
                    return;
                }
            } else {
                return; //Role was Null
            }

        } while (true);
    }

    public void registerMenu() {

        User createdUser;
        String username;
        String password;

        System.out.println("Welcome, please put in credentials to register new user.");

        username = getUsername();
        password = getPassword();

        createdUser = new DatabaseMethods().registerNewUser(username, password);

        if (createdUser.getRole() == null) {
            System.out.println("Something went wrong, try again.");
            return;
        }

        System.out.println(createdUser.getUsername());

    }

    public String getUsername() {
        System.out.print("Username: ");
        return sc.nextLine();
    }

    public String getPassword() {
        System.out.print("Password: ");
        return hash.hashPassword(sc.nextLine());
    }
}
