package at.ac.tuwien.sepm.assignment.individual.booking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static Connection con = null;

    private static Connection openConnection() throws SQLException{
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection("jdbc:h2:~/resources;" +
                "INIT=runscript from 'classpath:sqlData.sql'", "sa", "");
        } catch (SQLException e) {
            throw new SQLException("Connection to Database failed.");
        }

        return connection;
    }

    public static Connection getConnection() throws SQLException {
        if (con == null) {
            con = openConnection();
        }
        return con;
    }

    public static void closeConnection() throws SQLException {
        try {
            if(con!=null) {
                con.close();
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}
