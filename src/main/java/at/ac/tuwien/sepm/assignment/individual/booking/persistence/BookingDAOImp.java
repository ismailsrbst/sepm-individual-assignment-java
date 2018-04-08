package at.ac.tuwien.sepm.assignment.individual.booking.persistence;

import at.ac.tuwien.sepm.assignment.individual.booking.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.booking.util.DBUtil;
import at.ac.tuwien.sepm.assignment.individual.booking.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.booking.entities.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImp implements BookingDAO {

    private final static Logger logger = LoggerFactory.getLogger(BookingDAOImp.class);
    private Connection connection = null;
    private String sqlQuery = "";

    public BookingDAOImp() throws SQLException {
        this.connection = DBUtil.getConnection();
    }

    @Override
    public Booking create(Booking booking) throws DAOException {
        this.sqlQuery = "INSERT INTO Booking(customerName,payNumber,beginnDate,endDate,createDate,totalPrice," +
            "editDate,invoiceDate,invoiceNumber,status) Values (?,?,?,?,?,?,?,?,?,?)";
        logger.debug("creating new booking");
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
            psmt.executeUpdate();

            ResultSet generatedKeys = psmt.getGeneratedKeys();
            generatedKeys.next();
            booking.setBid(generatedKeys.getInt(1));

        }catch (SQLException e){
            logger.error(e.getMessage());
            throw new DAOException("Booking not created.\n"+e);
        }
        return booking;
    }

    @Override
    public Booking delete(Booking booking) throws DAOException {
        this.sqlQuery = "DELETE FROM BookingVehicle WHERE bid IN (SELECT bid FROM Booking WHERE bid = " + booking.getBid() + ")";

        logger.debug("deleting bookingVehicle");
        try {
            PreparedStatement psmt = connection.prepareStatement(this.sqlQuery);
            psmt.executeUpdate();
        } catch (SQLException e){
            logger.error(e.getMessage());

            throw new DAOException("BookingVehicle not deleted.\n"+e);
        }
        this.sqlQuery = "DELETE FROM Booking WHERE bid = " + booking.getBid();
        logger.debug("deleting booking");
        try {
            PreparedStatement psmt = connection.prepareStatement(this.sqlQuery);
            psmt.executeUpdate();
        } catch (SQLException e){
            logger.error(e.getMessage());
            throw new DAOException("Booking not deleted.\n"+e);

        }
        return booking;
    }

    @Override
    public Booking cancel(Booking booking) throws DAOException {
        this.sqlQuery = "UPDATE Booking SET status = ?, totalPrice = ?, invoiceDate = ?, invoiceNumber = ? WHERE bid = " + booking.getBid();

        logger.debug("updating booking: status = CANCEL");
        try {
            PreparedStatement psmt = connection.prepareStatement(this.sqlQuery);
            psmt.setString(1, "CANCELED");
            psmt.setInt(2, booking.getTotalPrice());
            psmt.setTimestamp(3, booking.getInvoiceDate());
            psmt.setInt(4, booking.getInvoiceNumber());
            psmt.executeUpdate();

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DAOException("Booking not canceled.\n" + e);
        }
        return booking;
    }

    @Override
    public Booking completed(Booking booking) throws DAOException {
        this.sqlQuery = "UPDATE Booking SET status = ?, invoiceDate = ?, invoiceNumber = ? WHERE bid = " + booking.getBid();
        logger.debug("updating booking status = COPMLETED");

        try {
            PreparedStatement psmt = connection.prepareStatement(this.sqlQuery);
            psmt.setString(1, "COMPLETED");
            psmt.setTimestamp(2, booking.getInvoiceDate());
            psmt.setInt(3, booking.getInvoiceNumber());
            psmt.executeUpdate();

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DAOException("Booking not completed.\n" + e);
        }
        return booking;
    }

    @Override
    public List<Booking> getAllBooking() throws DAOException {
        List<Booking> bookingList = new ArrayList<Booking>();
        this.sqlQuery = "SELECT * FROM Booking ORDER BY beginnDate DESC";

        logger.debug("loading booking");

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
            logger.error(e.getMessage());
            throw new DAOException("can't load booking."+e);
        }
        return bookingList;
    }

    @Override
    public Booking getBookingByBid(int id) throws DAOException {
        Booking booking = new Booking();
        this.sqlQuery = "SELECT * FROM Booking WHERE bid = ?";
        logger.debug("loading booking by Date");

        try {
            PreparedStatement psmt = connection.prepareStatement(sqlQuery);
            psmt.setInt(1, id);
            ResultSet rs = psmt.executeQuery();

            while (rs.next()) {
                booking = (getBookingResultSet(rs));
            }

        } catch (SQLException e){
            logger.error(e.getMessage());
            throw new DAOException("can't load booking."+e);
        }
        return booking;
    }

    private Booking getBookingResultSet(ResultSet rs) throws DAOException{
        Booking booking = new Booking();
        try {
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
        } catch (SQLException e){
            throw new DAOException("booking can not created from resultset.\n"+e);
        }


        return booking;
    }

}
