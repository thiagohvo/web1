import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnection {

    public static Connection createconnection() {
        Connection connection = null;
        try {
             connection =
                    DriverManager.getConnection(
                            "jdbc:mysql://localhost:3307/your_db",
                            "root",
                            "2404");
            Statement statement = connection.createStatement(

        );

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static Connection createConnection() {

        return null;
    }
}
