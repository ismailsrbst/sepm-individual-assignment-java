package at.ac.tuwien.sepm.assignment.individual.main.persistence;

import at.ac.tuwien.sepm.assignment.individual.main.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.main.entities.BookingVehicle;
import at.ac.tuwien.sepm.assignment.individual.main.entities.Status;
import at.ac.tuwien.sepm.assignment.individual.main.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.main.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingVehicleDAOImp implements BookingVehicleDAO {

    private final static Logger logger = LoggerFactory.getLogger(BookingVehicleDAOImp.class);
    private Connection connection = null;
    private String sqlQuery = "";

    public BookingVehicleDAOImp() throws SQLException {
        this.connection = DBUtil.getConnection();
    }

    @Override
    public BookingVehicle create(BookingVehicle bookingVehicle) throws DAOException {

        logger.debug("creating new bookingVehicle");
        try {
            sqlQuery = "INSERT INTO BookingVehicle(vid,bid,licenseNumber,licenseCreateDate," +
                "model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power," +
                "basePrice,imageUrl,editDate) Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement ps = connection.prepareStatement(sqlQuery);
            ps.setInt(1, bookingVehicle.getVehicle().getVid());
            ps.setInt(2, bookingVehicle.getBid());
            ps.setString(3, bookingVehicle.getLicenseNumber());
            ps.setTimestamp(4, bookingVehicle.getLicenseCreateDate());
            ps.setString(5, bookingVehicle.getVehicle().getModel());
            bookingVehicle.setModel(bookingVehicle.getVehicle().getModel());
            ps.setInt(6, bookingVehicle.getVehicle().getConstructionYear());
            ps.setString(7, bookingVehicle.getVehicle().getDescription());
            ps.setInt(8, bookingVehicle.getVehicle().getSeating());
            ps.setString(9, bookingVehicle.getVehicle().getPlateNumber());
            ps.setString(10, bookingVehicle.getVehicle().getDriverLicense());
            ps.setString(11, bookingVehicle.getVehicle().getPowerUnit());
            ps.setInt(12, bookingVehicle.getVehicle().getPower());
            ps.setInt(13, bookingVehicle.getVehicle().getBasePrice());
            bookingVehicle.setBasePrice(bookingVehicle.getVehicle().getBasePrice());
            ps.setString(14, bookingVehicle.getVehicle().getImageUrl());
            ps.setTimestamp(15, bookingVehicle.getVehicle().getEditDate());
            ps.executeUpdate();
        } catch (SQLException e){
            logger.error(e.getMessage());
            throw new DAOException("BookingVehicle not created.\n"+e);
        }
        return bookingVehicle;
    }

    @Override
    public List<BookingVehicle> getBookingVehicleByBooking(Booking booking) throws DAOException {
        this.sqlQuery = "SELECT * FROM BookingVehicle WHERE bid IN (SELECT bid FROM Booking WHERE bid = " + booking.getBid() +")";
        List<BookingVehicle> bookingVehicleList = new ArrayList<>();
        logger.debug("loading bookingVehicle by bookingId");
        try {
            PreparedStatement psmt = connection.prepareStatement(sqlQuery);
            ResultSet rs = psmt.executeQuery();

            while (rs.next()) {
                BookingVehicle bookingVehicle = new BookingVehicle();
                Vehicle vehicle = new Vehicle();
                vehicle.setVid(rs.getInt("vid"));
                vehicle.setModel(rs.getString("model"));
                vehicle.setConstructionYear(rs.getInt("constructionYear"));
                vehicle.setDescription(rs.getString("description"));
                vehicle.setSeating(rs.getInt("seating"));
                vehicle.setPlateNumber(rs.getString("plateNumber"));
                vehicle.setDriverLicense(rs.getString("driverLicense"));
                vehicle.setPowerUnit(rs.getString("powerUnit"));
                vehicle.setPower(rs.getInt("power"));
                vehicle.setBasePrice(rs.getInt("basePrice"));
                vehicle.setImageUrl(rs.getString("imageUrl"));
                vehicle.setEditDate(rs.getTimestamp("editDate"));

                bookingVehicle.setBid(rs.getInt("bid"));
                bookingVehicle.setVehicle(vehicle);
                bookingVehicle.setLicenseNumber(rs.getString("licenseNumber"));
                bookingVehicle.setLicenseCreateDate(rs.getTimestamp("licenseCreateDate"));
                bookingVehicle.setBasePrice(vehicle.getBasePrice());
                bookingVehicle.setModel(vehicle.getModel());

                bookingVehicleList.add(bookingVehicle);

            }

        } catch (SQLException e){
            logger.error(e.getMessage());
            throw new DAOException("can't load bookingVehicle.\n"+e);
        }
        return bookingVehicleList;
    }
}
