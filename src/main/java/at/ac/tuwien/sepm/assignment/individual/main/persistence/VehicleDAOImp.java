package at.ac.tuwien.sepm.assignment.individual.main.persistence;

import at.ac.tuwien.sepm.assignment.individual.main.entities.SearchFilter;
import at.ac.tuwien.sepm.assignment.individual.main.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.main.util.DBUtil;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAOImp implements VehicleDAO {

    private final static Logger logger = LoggerFactory.getLogger(VehicleDAOImp.class);
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

        if (vehicle == null){
            throw new IllegalArgumentException();
        }

        this.sqlQuery = "INSERT INTO Vehicle(model,constructionYear,description,seating,plateNumber,driverLicense,powerUnit," +
            "power,basePrice,createDate,imageUrl,editDate,isDeleted) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

        logger.debug("creating new Vehicle");
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
            logger.error(ex.getMessage());
            throw new DAOException("Vehicle not created.\n" + ex);
        }
        return vehicle;
    }

    public Vehicle update(Vehicle vehicle) throws DAOException{
        this.sqlQuery = "UPDATE Vehicle SET model=?, constructionYear=?, description=?, " +
            "seating=?, plateNumber=?, driverLicense=?, powerUnit=?, power=?, basePrice=?, " +
            "imageUrl=?, editDate=? WHERE vid = " + vehicle.getVid();

        logger.debug("updating Vehicle");
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
            logger.error(e.getMessage());
            throw new DAOException("Vehicle not updated.\n" + e);
        }
        return vehicle;
    }

    @Override
    public Vehicle delete(Vehicle vehicle) throws DAOException {
        this.sqlQuery = "UPDATE Vehicle SET isDeleted = ? WHERE vid = " + vehicle.getVid();

        logger.debug("deleting Vehicle");
        try {
            PreparedStatement psmt = connection.prepareStatement(this.sqlQuery);

            vehicle.setisDeleted(true);
            psmt.setBoolean(1, true);
            psmt.executeUpdate();

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DAOException("Vehicle not deleted.\n" + e);
        }
        return vehicle;
    }

    @Override
    public List<Vehicle> search(SearchFilter searchFilter) throws DAOException {
        List<Vehicle> vehicleList = new ArrayList<>();
        logger.debug("loading filtered Vehicle");
        try {
            System.out.println(getSqlQueryForFilter(searchFilter));
            PreparedStatement psmt = connection.prepareStatement(getSqlQueryForFilter(searchFilter));
            ResultSet rs = psmt.executeQuery();
            while (rs.next()){
                vehicleList.add(getVehicleResultSet(rs));
            }
        } catch (SQLException e){
            logger.error(e.getMessage());
            throw new DAOException("can't load filtered vehicle.\n" + e);
        }
        return vehicleList;
    }

    @Override
    public List<Vehicle> getAllVehichle() throws DAOException {
        List<Vehicle> vehicleList = new ArrayList<Vehicle>();
        this.sqlQuery = "SELECT * FROM Vehicle WHERE isDeleted = FALSE";

        logger.debug("loading all Vehicle");
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
            logger.error(ex.getMessage());
            throw new DAOException("can't load vehicle.\n" + ex);
        }
        return vehicleList;
    }

    private Vehicle getVehicleResultSet(ResultSet rs) throws DAOException {
        Vehicle vehicle = new Vehicle();
        logger.debug("creating new Vehicle from resultset");
        try {
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
        } catch (SQLException e){
            logger.error(e.getMessage());
            throw new DAOException("vehicle can not created from resultset.\n"+e);
        }
        return vehicle;
    }

    private String getSqlQueryForFilter(SearchFilter searchFilter){
        String query = "SELECT * FROM Vehicle WHERE isDeleted = FALSE";
        List<String> querys = new ArrayList<>();
        String tmp = "";

        if (!searchFilter.getModel().equals("") && searchFilter.getModel() != null){
            querys.add("UPPER(model) LIKE UPPER('%" + searchFilter.getModel() + "%')");
        }
        if (searchFilter.getPower() != null && !searchFilter.getPower().equals("")){
            querys.add("powerUnit = '" + searchFilter.getPower() + "'");
        }
        if (searchFilter.getSeating() != null && searchFilter.getSeating() != 0){
            querys.add("seating = " + searchFilter.getSeating() + " ");
        }
        if (searchFilter.getMinPrice() != null && searchFilter.getMaxPrice() != null && searchFilter.getMinPrice() != 0 && searchFilter.getMaxPrice() != 999){
            querys.add("basePrice >= " + searchFilter.getMinPrice() + " AND basePrice <= " + searchFilter.getMaxPrice());
        }
        if (!searchFilter.getDriverLicense().equals("") && searchFilter.getDriverLicense() != null){
            querys.add("driverLicense LIKE '%" + searchFilter.getDriverLicense() + "%'");
        }
        if (searchFilter.getStartDate() != null && searchFilter.getEndDate() != null){
            /*tmp += " vid NOT IN (SELECT vid FROM BookingVehicle WHERE EXISTS " +
                                "(SELECT bid FROM Booking WHERE BookingVehicle.bid = Booking.bid AND " +
                "(beginnDate >= '" +searchFilter.getStartDate() + "' AND beginnDate <= '"+searchFilter.getEndDate()+ "') " +
                "OR (endDate >= '"+ searchFilter.getStartDate()+ "' AND endDate <= '" +searchFilter.getEndDate() +"') " +
                "OR (beginnDate <= '"+searchFilter.getStartDate()+"' AND endDate >= '"+searchFilter.getEndDate()+"'))) ";
            */
            tmp += " vid NOT IN (SELECT vid FROM BookingVehicle LEFT JOIN Booking ON BookingVehicle.bid = Booking.bid WHERE " +
                "((beginnDate >= '" +searchFilter.getStartDate() + "' AND beginnDate <= '"+searchFilter.getEndDate()+ "') " +
                "OR (endDate >= '"+ searchFilter.getStartDate()+ "' AND endDate <= '" +searchFilter.getEndDate() +"') " +
                "OR (beginnDate <= '"+searchFilter.getStartDate()+"' AND endDate >= '"+searchFilter.getEndDate()+"')) AND status = 'OPEN') ";
            querys.add(tmp);
        } else if (searchFilter.getEndDate() == null && searchFilter.getStartDate() != null){
            tmp += " vid NOT IN (SELECT vid FROM BookingVehicle LEFT JOIN Booking ON BookingVehicle.bid = Booking.bid WHERE " +
                "beginnDate >= '" + searchFilter.getStartDate() + "')";
            querys.add(tmp);
        } else if (searchFilter.getStartDate() == null && searchFilter.getEndDate() != null){
            tmp += tmp += " vid NOT IN (SELECT vid FROM BookingVehicle LEFT JOIN Booking ON BookingVehicle.bid = Booking.bid WHERE " +
                "endDate <= '" + searchFilter.getEndDate() + "')";
            querys.add(tmp);
        }
        if (!querys.isEmpty()){
            for (String s : querys) {
                query += " AND " + s;
            }
            querys.clear();
        }

        return query;
    }
}
