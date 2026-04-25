package utilities;

import java.util.ArrayList;

public class User {

    private String username;
    private String role;
    private int id;

    public User(String username, String role, int id) {
        this.username = username;
        this.role = role;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public int getId() {
        return id;
    }

}
