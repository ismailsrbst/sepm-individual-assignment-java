package at.ac.tuwien.sepm.assignment.individual.main.persistence;

import at.ac.tuwien.sepm.assignment.individual.main.entities.BookingVehicle;
import at.ac.tuwien.sepm.assignment.individual.main.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookingVehicleDAOImp implements BookingVehicleDAO {

    private Connection connection = null;
    private String sqlQuery = "";

    public BookingVehicleDAOImp() throws SQLException {
        this.connection = DBUtil.getConnection();
    }

    @Override
    public BookingVehicle create(BookingVehicle bookingVehicle) throws SQLException {
        sqlQuery = "INSERT INTO BookingVehicle(vid,bid,licenseNumber,licenseCreateDate," +
            "model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit,power," +
            "basePrice,imageUrl,editDate) Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement ps = connection.prepareStatement(sqlQuery);
        ps.setInt(1, bookingVehicle.getVehicle().getVid());
        ps.setInt(2, bookingVehicle.getBid());
        ps.setString(3, bookingVehicle.getLicenseNumber());
        ps.setTimestamp(4, bookingVehicle.getLicenseCreateDate());
        ps.setString(5, bookingVehicle.getVehicle().getModel());
        ps.setInt(6, bookingVehicle.getVehicle().getConstructionYear());
        ps.setString(7, bookingVehicle.getVehicle().getDescription());
        ps.setInt(8, bookingVehicle.getVehicle().getSeating());
        ps.setString(9, bookingVehicle.getVehicle().getPlateNumber());
        ps.setString(10, bookingVehicle.getVehicle().getDriverLicense());
        ps.setString(11, bookingVehicle.getVehicle().getPowerUnit());
        ps.setInt(12, bookingVehicle.getVehicle().getPower());
        ps.setInt(13, bookingVehicle.getVehicle().getBasePrice());
        ps.setString(14, bookingVehicle.getVehicle().getImageUrl());
        ps.setTimestamp(15, bookingVehicle.getVehicle().getEditDate());
        ps.executeUpdate();
        return bookingVehicle;
    }
}
