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
    public Vehicle create(Vehicle vehicle) throws  ServiceException {
        try {
            vehicle.setCreateDate(currentTime());
            return vehicleDAO.create(vehicle);
        } catch (DAOException e){
            throw new ServiceException("Vehicle not created.\n" + e);
        }

    }

    @Override
    public Vehicle update(Vehicle vehicle) throws ServiceException{
        try {
            vehicle.setEditDate(currentTime());
            return vehicleDAO.update(vehicle);
        } catch (DAOException e){
            throw new ServiceException("Vehicle not updated.\n" + e);
        }
    }

    @Override
    public List<Vehicle> search(SearchFilter searchFilter) throws ServiceException{
        try {
            return vehicleDAO.search(searchFilter);
        } catch (DAOException e){
            throw new ServiceException("can't load filtered vehicle.\n" + e);
        }

    }

    @Override
    public Vehicle delete(Vehicle vehicle) throws ServiceException {
        try {
            return vehicleDAO.delete(vehicle);
        } catch (DAOException e){
            throw new ServiceException("Vehicle not deleted.\n" + e);
        }

    }

    @Override
    public List<Vehicle> getAllVehicleList() throws ServiceException {
        try {
            return vehicleDAO.getAllVehichle();
        } catch (DAOException e){
            throw new ServiceException("can't load vehicle.\n" + e);
        }
    }

}
