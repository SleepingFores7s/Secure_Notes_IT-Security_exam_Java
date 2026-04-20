import database.DatabaseMethods;
import utilities.User;
import utilities.passwordHashing;

import java.util.Scanner;

public class Main {

    Scanner sc = new Scanner(System.in);
    passwordHashing hash = new passwordHashing();
    DatabaseMethods DBmethod = new DatabaseMethods();

    void main() {


        //Menu (Login/Register)
        do {
            System.out.print("""
                   1. Login
                   2. Register new user
                   \s
                   Input:\s""");

            int userChoice = sc.nextInt();
            sc.nextLine();

            switch(userChoice) {
                case 1: loginMenu(); break;
                case 2: registerMenu(); break;
                default:
                    System.out.println("Something went wrong, try again.");
            }

        }while (true);

    }

    public void loginMenu() {

        User loggedInUser;
        String username;
        String password;

        //Menu (Login)
        do{

            System.out.println("Welcome, please put in credentials to login.");

            username = getUsername();
            password = getPassword();

            //Calls DB to check if user exists
            loggedInUser = DBmethod.verifyUserLogin(username, password);

            //If there is no role, return. (Only User and Admin allowed!)
            if (loggedInUser.getRole() != null) {
                if (loggedInUser.getRole().equals("USER")) {


                    System.out.println(loggedInUser.getRole() + " : " + loggedInUser.getUsername());

                    new UserMenu().UserUI(loggedInUser);

                } else if (loggedInUser.getRole().equals("ADMIN")) {
                    // Call to admin menu UI
                } else {
                    return; //Role did not match any known variables
                }
            }else {
                return; //Role was Null
            }

        }while (true);
    }

    public void registerMenu() {

        User createdUser;
        String username;
        String password;

        System.out.println("Welcome, please put in credentials to register new user.");

        username = getUsername();
        password = getPassword();


        //TODO Fix method so it returns a user object instead with an ID
        createdUser = new DatabaseMethods().registerNewUser(username, password);

        if (createdUser.getRole() == null) {
            System.out.println("Something went wrong, try again.");
            return;
        }

        System.out.println(createdUser.getUsername());

        //TODO - NOt sure, but something feels like it is missing.

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
