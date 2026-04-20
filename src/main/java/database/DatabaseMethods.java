package database;

import utilities.User;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
