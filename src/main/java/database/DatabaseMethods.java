package database;

import com.mysql.cj.protocol.Resultset;

import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class DatabaseMethods {

    public String verifyUserLogin(String inputUsername, String inputPassword) {

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL LoginUser(?,?,?,?,?)}")) {

            stmt.setString(1, inputUsername);
            stmt.setString(2, inputPassword);

            //P_User_Found
            stmt.registerOutParameter(3, Types.BOOLEAN);
            //P_Role
            stmt.registerOutParameter(4, Types.VARCHAR);
            //P_Status_Message
            stmt.registerOutParameter(5, Types.VARCHAR);

            stmt.execute();

            boolean userFound = stmt.getBoolean(3);
            String returnedRole = stmt.getString(4);
            String returnedStatusMessage = stmt.getString(5);

            if (userFound) {
                return returnedRole;
            } else {
                System.out.println(returnedStatusMessage);
                return "";
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public String registerNewUser(String username, String password) {

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL CreateNewUser(?,?,?,?,?)}")) {

            //P_User_Username , P_User_Password
            stmt.setString(1, username);
            stmt.setString(2, password);

            //P_User_Created
            stmt.registerOutParameter(3, Types.BOOLEAN);
            //P_User_Role
            stmt.registerOutParameter(4, Types.VARCHAR);
            //P_Status_Message
            stmt.registerOutParameter(5, Types.VARCHAR);


            stmt.execute();

            boolean userCreated = stmt.getBoolean(3);
            String role = stmt.getString(4);
            String statusMessage = stmt.getString(5);

            /*TODO - Create a loginResult class for easier management of retuned values etc.'
            *   Ex. loginResult(boolean success, String role, String DBMessage)
            *   Ex. LoginResult(Boolean success, User userObject, String DBMessage)
            * */

            System.out.println(statusMessage);
            if (userCreated) {
                return role;
            } else {
                return "ERROR";
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
