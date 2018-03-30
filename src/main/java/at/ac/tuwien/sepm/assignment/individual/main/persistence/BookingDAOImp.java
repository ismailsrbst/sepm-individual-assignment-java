package at.ac.tuwien.sepm.assignment.individual.main.persistence;

import at.ac.tuwien.sepm.assignment.individual.main.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.main.entities.BookingVehicle;
import at.ac.tuwien.sepm.assignment.individual.main.entities.PayTyp;
import at.ac.tuwien.sepm.assignment.individual.main.entities.Status;
import at.ac.tuwien.sepm.assignment.individual.main.util.DBUtil;

import java.sql.*;

public class BookingDAOImp implements BookingDAO {

    private Connection connection = null;
    private String sqlQuery = "";

    public BookingDAOImp() throws SQLException {
        this.connection = DBUtil.getConnection();
    }

    @Override
    public Booking create(Booking booking) {
        this.sqlQuery = "INSERT INTO Booking(customerName,payNumber,beginnDate,endDate,createDate,totalPrice," +
            "editDate,status) Values (?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement psmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);

            psmt.setString(1, booking.getCustomerName());
            psmt.setString(2, booking.getPayNumber());
            psmt.setTimestamp(3, booking.getBeginnDate());
            psmt.setTimestamp(4, booking.getEndDate());
            booking.setCreateDate(new Timestamp(System.currentTimeMillis()));
            psmt.setTimestamp(5, booking.getCreateDate());
            psmt.setInt(6, booking.getTotalPrice());
            psmt.setTimestamp(7, booking.getEditDate());
            psmt.setString(8, booking.getStatus() == Status.OPEN ? "OPEN" : booking.getStatus() == Status.CANCELED ? "CANCELED" : "COMPLETED");
            psmt.executeUpdate();

            ResultSet generatedKeys = psmt.getGeneratedKeys();
            generatedKeys.next();
            booking.setBid(generatedKeys.getInt(1));

        }catch (SQLException e){

        }

        return booking;
    }
}
