package database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static final Properties prop = new Properties();

    //Runs only one time total, so the properties file is not opened thousands of times.
    static {
        try (FileInputStream fis = new FileInputStream("src/main/resources/mySQLCreedentials.properties")) {
            prop.load(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Credentials (Change values if you do not use a .properties file) src = src/main/resources/mySQLCreedentials.properties
    static String username = prop.getProperty("username");
    static String password = prop.getProperty("password");
    static String mySQLurl = prop.getProperty("sqlurl");



    //Returns a connection
    public static Connection getConnection() throws IOException, SQLException {
        return DriverManager.getConnection(mySQLurl, username, password);
    }
}
