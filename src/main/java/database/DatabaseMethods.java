package database;

import com.mysql.cj.protocol.Resultset;
import utilities.Notes;
import utilities.User;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class DatabaseMethods {

    public User verifyUserLogin(String inputUsername, String inputPassword) {

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL LoginUser(?,?,?,?,?)}")) {

            stmt.setString(1, inputUsername);
            stmt.setString(2, inputPassword);


            //P_Role
            stmt.registerOutParameter(3, Types.VARCHAR);
            //P_User_ID
            stmt.registerOutParameter(4, Types.INTEGER);
            //P_Status_Message
            stmt.registerOutParameter(5, Types.VARCHAR);

            stmt.execute();


            String returnedRole = stmt.getString(3);
            int userID = stmt.getInt(4);
            String returnedStatusMessage = stmt.getString(5);

            System.out.println(returnedStatusMessage);

            return new User(inputUsername, returnedRole, userID);

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }


    }

    public User registerNewUser(String username, String password) {

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL CreateNewUser(?,?,?,?,?)}")) {

            //P_User_Username , P_User_Password
            stmt.setString(1, username);
            stmt.setString(2, password);

            //P_User_ID
            stmt.registerOutParameter(3, Types.INTEGER);
            //P_User_Role
            stmt.registerOutParameter(4, Types.VARCHAR);
            //P_Status_Message
            stmt.registerOutParameter(5, Types.VARCHAR);

            stmt.execute();

            int userID = stmt.getInt(3);
            String role = stmt.getString(4);
            String statusMessage = stmt.getString(5);

            System.out.println(statusMessage);

            return new User(username, role, userID);
            /*TODO - Create a loginResult class for easier management of retuned values etc.'
            *   Ex. loginResult(boolean success, String role, String DBMessage)
            *   Ex. LoginResult(Boolean success, User userObject, String DBMessage)
            * */

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Notes> getUserNotes(int id) {

        try(Connection connection = DatabaseConnection.getConnection();
        CallableStatement stmt = connection.prepareCall("{CALL GetNotesForUser(?)}")) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            List<Notes> retrivedNotes = new ArrayList<>();

            while(rs.next()) {
                Notes note = new Notes(
                        rs.getInt("ID"),
                        rs.getString("Title"),
                        rs.getString("Stored_Data"),
                        rs.getString("owner_name")//Note owner
                );
                retrivedNotes.add(note);
            }

            return retrivedNotes;

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Notes> getAdminNotes() {
        try(Connection connection = DatabaseConnection.getConnection();
            CallableStatement stmt = connection.prepareCall("{CALL GetNotesForAdmin()}")) {

            ResultSet rs = stmt.executeQuery();

            List<Notes> retrivedNotes = new ArrayList<>();

            while(rs.next()) {
                Notes note = new Notes(
                        rs.getInt("ID"),
                        rs.getString("Title"),
                        rs.getString("Stored_Data"),
                        rs.getString("owner_name")//Note owner
                );
                retrivedNotes.add(note);
            }

            return retrivedNotes;

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteNote(int userID, String userRole, int noteID) {
        try(Connection connection = DatabaseConnection.getConnection();
        CallableStatement stmt = connection.prepareCall("{CALL DeleteNote(?,?,?,?)}")) {

            stmt.setInt(1, userID);
            stmt.setString(2, userRole);
            stmt.setInt(3, noteID);

            stmt.registerOutParameter(4, Types.BOOLEAN); //Was_Deleted True/False

            stmt.execute();

            IO.println("--------------OUTCOME--------------");
            if (stmt.getBoolean(4)) {
                System.out.println("Deletion was successful.");
            }else {
                System.out.println("Something went wrong, try again.");
            }
            IO.println("--------------OUTCOME--------------");

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void changeUserPassword(String username, String newPassword) {

        try(Connection connection = DatabaseConnection.getConnection();
        CallableStatement stmt = connection.prepareCall("{CALL ChangePassword(?,?,?)}")) {

            stmt.setString(1, username);
            stmt.setString(2, newPassword);

            stmt.registerOutParameter(3, Types.BOOLEAN);

            stmt.execute();

            if (stmt.getBoolean(3)) {
                IO.println("Password change was successful.");
            } else {
                IO.println("Password change failed.");
            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void createUserNote(int userID, String title, String body) {

        try(Connection connection = DatabaseConnection.getConnection();
        CallableStatement stmt = connection.prepareCall("{CALL CreateNewNote(?,?,?,?)}")) {

            stmt.setInt(1, userID);
            stmt.setString(2, title);
            stmt.setString(3, body);

            stmt.registerOutParameter(4, Types.BOOLEAN);

            stmt.execute();

            if (stmt.getBoolean(4)) {
                IO.println("Note created successfully.");
            }else {
                IO.println("Note creating failed.");
            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateUserNote(int userID, int noteID, String titleReplacer, String noteReplacer) {

        try(Connection connection = DatabaseConnection.getConnection();
        CallableStatement stmt = connection.prepareCall("{CALL EditNote(?,?,?,?,?)}")) {

            stmt.setInt(1, userID);
            stmt.setInt(2, noteID);
            stmt.setString(3, titleReplacer);
            stmt.setString(4, noteReplacer);

            stmt.registerOutParameter(5, Types.BOOLEAN);

            stmt.execute();

            if (stmt.getBoolean(5)) {
                IO.println("Note uploaded successfully.");
            } else {
                IO.println("Note upload failed.");
            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

    }

}
