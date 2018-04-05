package at.ac.tuwien.sepm.assignment.individual.main.persistence;

import at.ac.tuwien.sepm.assignment.individual.main.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.main.entities.BookingVehicle;
import at.ac.tuwien.sepm.assignment.individual.main.entities.PayTyp;
import at.ac.tuwien.sepm.assignment.individual.main.entities.Status;
import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.main.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImp implements BookingDAO {

    private Connection connection = null;
    private String sqlQuery = "";

    public BookingDAOImp() throws SQLException {
        this.connection = DBUtil.getConnection();
    }

    @Override
    public Booking create(Booking booking) throws DAOException {
        this.sqlQuery = "INSERT INTO Booking(customerName,payNumber,beginnDate,endDate,createDate,totalPrice," +
            "editDate,invoiceDate,invoiceNumber,status) Values (?,?,?,?,?,?,?,?,?,?)";

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
            psmt.setTimestamp(8, booking.getInvoiceDate());
            psmt.setInt(9, booking.getInvoiceNumber());
            psmt.setString(10,"OPEN");
            //psmt.setString(10, booking.getStatus() == Status.OPEN ? "OPEN" : booking.getStatus() == Status.CANCELED ? "CANCELED" : "COMPLETED");
            psmt.executeUpdate();

            ResultSet generatedKeys = psmt.getGeneratedKeys();
            generatedKeys.next();
            booking.setBid(generatedKeys.getInt(1));

        }catch (SQLException e){
            throw new DAOException("");
        }

        return booking;
    }

    @Override
    public Booking delete(Booking booking) {
        this.sqlQuery = "DELETE FROM BookingVehicle WHERE bid IN (SELECT bid FROM Booking WHERE bid = " + booking.getBid() + ")";

        try {
            PreparedStatement psmt = connection.prepareStatement(this.sqlQuery);
            psmt.executeUpdate();
        } catch (SQLException e){

        }
        this.sqlQuery = "DELETE FROM Booking WHERE bid = " + booking.getBid();
        try {
            PreparedStatement psmt = connection.prepareStatement(this.sqlQuery);
            psmt.executeUpdate();
        } catch (SQLException e){

        }
        return booking;
    }

    @Override
    public Booking cancel(Booking booking) throws DAOException {
        this.sqlQuery = "UPDATE Booking SET status = ?, totalPrice = ? WHERE bid = " + booking.getBid();
        try {
            PreparedStatement psmt = connection.prepareStatement(this.sqlQuery);
            psmt.setString(1, "CANCELED");
            psmt.setInt(2, booking.getTotalPrice());
            psmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Booking not canceled.\n" + e);
        }
        return booking;
    }

    @Override
    public Booking completed(Booking booking) throws DAOException {
        this.sqlQuery = "UPDATE Booking SET status = ?, invoiceDate = ?, invoiceNumber = ? WHERE bid = " + booking.getBid();
        try {
            PreparedStatement psmt = connection.prepareStatement(this.sqlQuery);
            psmt.setString(1, "COMPLETED");
            psmt.setTimestamp(2, booking.getInvoiceDate());
            psmt.setInt(3, booking.getInvoiceNumber());
            psmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Booking not completed.\n" + e);
        }
        return booking;
    }

    @Override
    public List<Booking> getAllBooking() throws DAOException {
        List<Booking> bookingList = new ArrayList<Booking>();
        this.sqlQuery = "SELECT * FROM Booking ORDER BY beginnDate DESC";

        try {
            PreparedStatement psmt = connection.prepareStatement(sqlQuery);
            ResultSet rs = psmt.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBid(rs.getInt("bid"));
                booking.setCustomerName(rs.getString("customerName"));
                booking.setPayNumber(rs.getString("payNumber"));
                booking.setBeginnDate(rs.getTimestamp("beginnDate"));
                booking.setEndDate(rs.getTimestamp("endDate"));
                booking.setCreateDate(rs.getTimestamp("createDate"));
                booking.setTotalPrice(rs.getInt("totalPrice"));
                booking.setEditDate(rs.getTimestamp("editDate"));
                booking.setInvoiceDate(rs.getTimestamp("invoiceDate"));
                booking.setInvoiceNumber(rs.getInt("invoiceNumber"));
                booking.setStatus(Status.valueOf(rs.getString("status")));

                bookingList.add(booking);
            }

        } catch (SQLException e){
            throw new DAOException("can't load booking.");
        }
        return bookingList;
    }

}
