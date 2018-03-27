package at.ac.tuwien.sepm.assignment.individual.main.persistence;

import at.ac.tuwien.sepm.assignment.individual.main.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.main.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAOImp implements VehicleDAO {

    private Connection connection = null;
    private String sqlQuery = "";

    public VehicleDAOImp(Connection connection){
        this.connection = connection;
    }

    public VehicleDAOImp() throws SQLException {
        this.connection = DBUtil.getConnection();
    }

    @Override
    public Vehicle create(Vehicle vehicle) throws DAOException {

        this.sqlQuery = "INSERT INTO Vehicle(model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit," +
            "power,basePrice,createDate,imageUrl,editDate,isDeleted) Values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement psmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            psmt.setString(1, vehicle.getModel());
            psmt.setInt(2, vehicle.getConstructionYear());
            psmt.setString(3, vehicle.getDescription());
            psmt.setInt(4, vehicle.getSeating());
            psmt.setString(5, vehicle.getPlateNumber());
            psmt.setString(6, vehicle.getDriverLicense());
            psmt.setString(7, vehicle.getPowerUnit());
            psmt.setInt(8, vehicle.getPower());
            psmt.setInt(9, vehicle.getBasePrice());
            psmt.setTimestamp(10, vehicle.getCreateDate());
            psmt.setString(11, vehicle.getImageUrl());
            psmt.setTimestamp(12, vehicle.getEditDate());
            vehicle.setisDeleted(false);
            psmt.setBoolean(13, vehicle.getisDeleted());
            psmt.executeUpdate();

            ResultSet generatedKeys = psmt.getGeneratedKeys();
            generatedKeys.next();
            vehicle.setVid(generatedKeys.getInt(1));

        }catch (SQLException ex){
            throw new DAOException("Vehicle not created." + ex);
        }

        return vehicle;
    }

    public Vehicle update(Vehicle vehicle) throws DAOException{
        this.sqlQuery = "UPDATE Vehicle SET model=?, constructionYear=?, description=?, " +
            "seating=?, plateNumber=?, driverLicense=?, powerUnit=?, power=?, basePrice=?, " +
            "imageUrl=?, editDate=? WHERE vid = " + vehicle.getVid();

        try {
            PreparedStatement psmt = connection.prepareStatement(this.sqlQuery);
            psmt.setString(1, vehicle.getModel());
            psmt.setInt(2, vehicle.getConstructionYear());
            psmt.setString(3, vehicle.getDescription());
            psmt.setInt(4, vehicle.getSeating());
            psmt.setString(5, vehicle.getPlateNumber());
            psmt.setString(6, vehicle.getDriverLicense());
            psmt.setString(7, vehicle.getPowerUnit());
            psmt.setInt(8, vehicle.getPower());
            psmt.setInt(9, vehicle.getBasePrice());
            psmt.setString(10, vehicle.getImageUrl());
            psmt.setTimestamp(11, vehicle.getEditDate());
            psmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Vehicle not updated. " + e);
        }
        return vehicle;
    }

    @Override
    public Vehicle delete(Vehicle vehicle) throws DAOException {
        this.sqlQuery = "UPDATE Vehicle SET isDeleted = ? WHERE vid = " + vehicle.getVid();

        try {
            PreparedStatement psmt = connection.prepareStatement(this.sqlQuery);

            vehicle.setisDeleted(true);
            psmt.setBoolean(1, true);
            psmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Vehicle not deleted.\n" + e);
        }
        return vehicle;
    }

    @Override
    public List<Vehicle> getAllVehichle() throws DAOException {
        List<Vehicle> vehicleList = new ArrayList<Vehicle>();
        this.sqlQuery = "SELECT * FROM Vehicle WHERE isDeleted = FALSE";

        try {
            PreparedStatement psmt = connection.prepareStatement(sqlQuery);
            ResultSet rs = psmt.executeQuery();

            while (rs.next()){

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
                vehicle.setCreateDate(rs.getTimestamp("createDate"));
                vehicle.setImageUrl(rs.getString("imageUrl"));
                vehicle.setEditDate(rs.getTimestamp("editDate"));
                vehicle.setisDeleted(rs.getBoolean("isDeleted"));

                vehicleList.add(vehicle);
            }
        } catch (SQLException ex){
            throw new DAOException("can't load vehicle");
        }

        return vehicleList;
    }
}
