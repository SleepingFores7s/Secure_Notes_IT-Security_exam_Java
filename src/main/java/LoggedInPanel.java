import utilities.Notes;
import utilities.User;

import java.util.ArrayList;
import java.util.List;

public class LoggedInPanel {

    private List<Notes> notes = new ArrayList<>();

    public void loggedInPanel(User loggedInUser) {

        /*TODO
        *  1. Create DB method to GET User Notes OR admin notes (All notes).
        *  2. Make DB sp for storing notes again, overwriting the old one.
        *  3. Make DB SP to delete Notes.
        *  4. Make DB Method for replacing current password
        * */

        System.out.println("User/Admin Panel");

        System.out.println("""
                1: Check Notes
                2: Edit Notes
                3: Delete Notes
                4: Change Password""");
    }
}
