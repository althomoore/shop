package shopcart.servlets.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class H2Base implements AutoCloseable {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(H2Base.class);

    private Connection connection;

    private static Connection getConnection(String file) throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");   // the driver class must be loaded so that DriverManager can find the loaded class
        return DriverManager.getConnection(file, "sa", "");
    }

    protected H2Base(String file)  {
        try {
            connection = getConnection(file);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void close() {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected PreparedStatement prepare(String statement) throws SQLException {
        return connection.prepareStatement(statement);
    }
}