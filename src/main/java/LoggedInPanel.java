import database.DatabaseMethods;
import utilities.Notes;
import utilities.User;
import utilities.passwordHashing;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoggedInPanel {

    Scanner sc = new Scanner(System.in);
    private List<Notes> notes = new ArrayList<>();
    boolean userMenuLoop = true;
    int menuChoice;


    public void loggedInPanel(User currentUser) {

        /*TODO
         *  1. (DONE) Create DB method to GET User Notes OR admin notes (All notes).
         *  2. (DONE) Make DB sp for storing notes again, overwriting the old one.
         *  3. (DONE) Make DB SP to delete Notes.
         *  4. (DONE) Make DB Method for replacing current password
         *  5. (WiP) Make a DB method for users to create a NEW Note
         * */

        switch (currentUser.getRole()) {
            case "USER":
                userMenu(currentUser);
                break;
            case "ADMIN":
                adminMenu(currentUser);
                break;
            default:
                System.out.println("Something went wrong in : loggedInPanel()");
                break;
        }

    }

    private void userMenu(User currentUser) {
        System.out.println("User menu");

        do {

            notes = new DatabaseMethods().getUserNotes(currentUser.getId()); //Grabs only the Users notes.

            System.out.println("""
                    1: New Note
                    2: Check Note
                    3: Edit Notes
                    4: Delete Notes
                    5: Change Password
                    6: Exit
                    
                    --------------NOTES--------------"""
            );
            printNotesTitles(currentUser.getRole(), notes); //Print all users notes
            System.out.println("--------------INPUT--------------");

            System.out.print("Input: ");
            if (sc.hasNextInt()) {

                menuChoice = sc.nextInt();
                sc.nextLine(); //Clear the row

                switch (menuChoice) {
                    case 1: //New Note
                        createNewNote(currentUser.getId());
                        break;
                    case 2: //Check Note
                        System.out.print("What note to open: ");
                        if (sc.hasNextInt()) {
                            checkUserNotes(currentUser.getRole(), sc.nextInt());
                        } else {
                            System.out.println("Invalid input");
                        }
                        break;
                    case 3: //Edit Note
                        System.out.print("What note to edit: ");
                        int editNoteChoice = sc.nextInt();
                        sc.nextLine(); //Clears scanner

                        editUserNotes(editNoteChoice, currentUser.getId());

                        break;
                    case 4: //Delete Note
                        System.out.print("What note to Delete: ");
                        if (sc.hasNextInt()) {
                            int noteChoice = sc.nextInt();
                            if (noteChoice > notes.size() - 1 || noteChoice < 0) {
                                IO.println("Invalid choice");
                                break;
                            }
                            new DatabaseMethods().deleteNote(
                                    currentUser.getId(),
                                    currentUser.getRole(),
                                    notes.get(noteChoice).getId()
                            );
                        } else {
                            System.out.println("Invalid input");
                        }
                        break;
                    case 5: //Change Password
                        System.out.print("New password: ");
                        new DatabaseMethods().changeUserPassword(currentUser.getUsername(), new passwordHashing().hashPassword(sc.nextLine()));
                        break;
                    case 6://Exit
                        return;
                    default:
                        System.out.println("Invalid input, try again.");
                        break;
                }
            }

        } while (userMenuLoop);
    }

    private void adminMenu(User currentUser) {
        System.out.println("\nAdmin menu");

        do {

            notes = new DatabaseMethods().getAdminNotes(); //Grabs ALL notes for admin use.

            System.out.println("""
                    1: Check user notes
                    2: Remove user notes
                    3: Change Password
                    4: Exit
                    
                    --------------NOTES--------------"""
            );
            printNotesTitles(currentUser.getRole(), notes); //Prints all Notes
            System.out.println("--------------INPUT--------------");

            System.out.print("Input: ");
            if (sc.hasNextInt()) {

                menuChoice = sc.nextInt();
                sc.nextLine(); //Clear the row

                switch (menuChoice) {
                    case 1: //Check Note
                        System.out.print("What note to open: ");
                        if (sc.hasNextInt()) {
                            checkUserNotes(currentUser.getRole(), sc.nextInt());
                        } else {
                            System.out.println("Invalid input");
                        }

                        break;
                    case 2: //Delete Note
                        //Remove note **SQL & Java dependent
                        System.out.print("What note to Delete: ");
                        if (sc.hasNextInt()) {
                            int noteChoice = sc.nextInt();
                            if (noteChoice > notes.size() - 1 || noteChoice < 0) {
                                IO.println("Invalid choice");
                                break;
                            }
                            new DatabaseMethods().deleteNote(
                                    currentUser.getId(),
                                    currentUser.getRole(),
                                    notes.get(noteChoice).getId()
                            );
                        } else {
                            System.out.println("Invalid input");
                        }
                        break;
                    case 3: //Change Password
                        System.out.print("New password: ");
                        new DatabaseMethods().changeUserPassword(currentUser.getUsername(), new passwordHashing().hashPassword(sc.nextLine()));
                        break;
                    case 4: //Exit
                        return;
                    default:
                        System.out.println("Invalid input, try again.");
                        break;
                }
            }

        } while (userMenuLoop);
    }

    private void printNotesTitles(String role, List<Notes> notes) {

        if (role.equals("ADMIN")) {
            int i = 0;
            for (Notes note : notes) {
                System.out.println(i + ": [ID " + note.getId() + "] Owner: " + note.getNoteOwner() + " | " + note.getTitle());
                i++;
            }
        } else {
            int i = 0;
            for (Notes note : notes) {
                System.out.println(i + ": " + note.getTitle());
                i++;
            }
        }
    }

    private void checkUserNotes(String role, int noteListId) {
        IO.println("--------------NOTES--------------");
        if (role.equals("ADMIN")) {
            System.out.println("[ID " + notes.get(noteListId).getId() + "] Title: " + notes.get(noteListId).getTitle() + "\n" + notes.get(noteListId).getNoteData());
        } else {
            System.out.println("Title: " + notes.get(noteListId).getTitle() + "\n" + notes.get(noteListId).getNoteData());
        }
        IO.println("--------------NOTES--------------");
    }

    private void editUserNotes(int chosenNoteNr, int userID) {

        IO.println("----------------Title----------------");
        System.out.println(notes.get(chosenNoteNr).getTitle());

        IO.println("--------------Note Body--------------");
        System.out.println(notes.get(chosenNoteNr).getNoteData());

        IO.println("--------------Edit Body--------------");
        System.out.print("""
                Leave Title blank to not change.
                Title:\s""");
        String titleReplace = sc.nextLine().trim();

        System.out.print("""
                Leave note body blank to not change.
                Note Body:\s""");
        String bodyReplace = sc.nextLine().trim();

        //Nothing changed
        if (titleReplace.isEmpty() && bodyReplace.isEmpty()) {
            return;
        }
        //Title changed
        if (!titleReplace.isEmpty()) {
            notes.get(chosenNoteNr).setTitle(titleReplace);
        }
        //Body Changed
        if (!bodyReplace.isEmpty()) {
            notes.get(chosenNoteNr).setNoteData(bodyReplace);
        }

        //Sends the updated Note to the DB
        new DatabaseMethods().updateUserNote(
                userID,
                notes.get(chosenNoteNr).getId(),
                notes.get(chosenNoteNr).getTitle(),
                notes.get(chosenNoteNr).getNoteData()
        );

    }

    private void createNewNote(int userID) {

        IO.println("----------------Title----------------");
        String title = sc.nextLine().trim();
        IO.println("--------------Note Body--------------");
        String body = sc.nextLine().trim();

        new DatabaseMethods().createUserNote(userID, title, body);

    }
}
