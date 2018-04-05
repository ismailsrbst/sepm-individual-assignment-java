package at.ac.tuwien.sepm.assignment.individual.main.service;

import at.ac.tuwien.sepm.assignment.individual.main.entities.SearchFilter;
import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.main.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.main.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.main.persistence.VehicleDAO;
import at.ac.tuwien.sepm.assignment.individual.main.persistence.VehicleDAOImp;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class VehicleServiceImp implements VehicleService {

    private VehicleDAO vehicleDAO;

    public VehicleServiceImp() throws SQLException {
        this.vehicleDAO = new VehicleDAOImp();
    }

    public Timestamp currentTime(){
        return new Timestamp(System.currentTimeMillis());
    }

    @Override
    public Vehicle create(Vehicle vehicle) throws DAOException, ServiceException {
        vehicle.setCreateDate(currentTime());
        return vehicleDAO.create(vehicle);
    }

    @Override
    public Vehicle update(Vehicle vehicle) throws DAOException{
        vehicle.setEditDate(currentTime());
        return vehicleDAO.update(vehicle);
    }

    @Override
    public List<Vehicle> search(SearchFilter searchFilter){
        return vehicleDAO.search(searchFilter);
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
