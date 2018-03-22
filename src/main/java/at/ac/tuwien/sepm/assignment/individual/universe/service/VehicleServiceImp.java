package at.ac.tuwien.sepm.assignment.individual.universe.service;

import at.ac.tuwien.sepm.assignment.individual.universe.persistence.DAOException;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.universe.persistence.VehicleDAO;
import at.ac.tuwien.sepm.assignment.individual.universe.persistence.VehicleDAOImp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleServiceImp implements VehicleService {

    private VehicleDAO vehicleDAO;

    public VehicleServiceImp() throws SQLException {
        this.vehicleDAO = new VehicleDAOImp();
    }

    @Override
    public Vehicle create(Vehicle vehicle) throws DAOException {
        return vehicleDAO.create(vehicle);
    }

    @Override
    public Vehicle delete(Vehicle vehicle) throws DAOException {
        return vehicleDAO.delete(vehicle);
    }

    @Override
    public List<Vehicle> getAllVehicleList() throws DAOException {
        List<Vehicle> vehicleList = new ArrayList<Vehicle>();

        for (Vehicle vehicle : vehicleDAO.getAllVehichle()){
            vehicleList.add(vehicle);
        }
        return vehicleList;
    }

}
