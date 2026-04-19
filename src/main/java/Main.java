import database.DatabaseMethods;
import utilities.User;
import utilities.passwordHashing;

import java.util.Scanner;

public class Main {

    Scanner sc = new Scanner(System.in);
    passwordHashing hash = new passwordHashing();
    DatabaseMethods DBmethod = new DatabaseMethods();

    void main() {


        System.out.println(DBmethod.verifyUserLogin("Hanna123","123"));

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

        String username;
        String password;

        //Menu (Login)
        do{

            System.out.println("Welcome, please put in credentials to login.");

            username = getUsername();
            password = getPassword();

            //Calls DB to check if user exists
            String userRole = DBmethod.verifyUserLogin(username, password);

            //If there is no role, return. (Only User and Admin allowed!)
            if (userRole.isEmpty()) {
                return;
            }

            //Creates a user object
            User loggedInUser = new User(username, userRole);

            System.out.println(loggedInUser.getRole() +" : "+loggedInUser.getUsername());

            //TODO - Create a if/switch for admin/user menu.

        }while (true);
    }

    public void registerMenu() {

        String username;
        String password;

        System.out.println("Welcome, please put in credentials to register new user.");

        username = getUsername();
        password = getPassword();

        String role = new DatabaseMethods().registerNewUser(username, password);

        if (role.equals("ERROR")) {
            System.out.println("Something went wrong, try again.");
            return;
        }

        User test1 = new User(username, role);

        System.out.println(test1);

        //TODO - NOt sure, but something is missing.

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
