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

    //Returns a connection
    public static Connection getConnection() throws IOException, SQLException {
        return DriverManager.getConnection(prop.getProperty("sqlurl"), prop.getProperty("username"), prop.getProperty("password"));
    }
}
