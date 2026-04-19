package utilities;

import java.util.ArrayList;

public class User {

    private String username;
    private String role;
    private ArrayList<Notes> userNotes;

    public User(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public ArrayList<Notes> getUserNotes() {
        return userNotes;
    }
    public void setUserNotes(ArrayList<Notes> userNotes) {
        this.userNotes = userNotes;
    }
}
