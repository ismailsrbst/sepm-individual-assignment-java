package at.ac.tuwien.sepm.assignment.individual.booking.service;

import at.ac.tuwien.sepm.assignment.individual.booking.entities.SearchFilter;
import at.ac.tuwien.sepm.assignment.individual.booking.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.booking.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.booking.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.booking.persistence.VehicleDAO;
import at.ac.tuwien.sepm.assignment.individual.booking.persistence.VehicleDAOImp;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class VehicleServiceImp implements VehicleService {

    private VehicleDAO vehicleDAO;

    public VehicleServiceImp() throws SQLException {
        this.vehicleDAO = new VehicleDAOImp();
    }

    private void validation(Vehicle vehicle){
        if (vehicle.getModel().trim().length() >= 30){
            throw new IllegalArgumentException("Model length can not be bigger then 30");
        } else if (vehicle.getSeating() > 35){
            throw new IllegalArgumentException("Seating can not be bigger then 35.");
        } else if (vehicle.getBasePrice() > 400){
            throw new IllegalArgumentException("Base price can not be bigger then 400.");
        } else if (vehicle.getPlateNumber().trim().length() > 10){
            throw new IllegalArgumentException("Plate number can not be bigger then 10.");
        } else if (vehicle.getDescription().trim().length() >= 30){
            throw new IllegalArgumentException("Description length can not be bigger then 30");
        } else if (vehicle.getPower() > 1000){
            throw new IllegalArgumentException("Power can not be bigger then 1000.");
        }
    }

    public Timestamp currentTime(){
        return new Timestamp(System.currentTimeMillis());
    }

    @Override
    public Vehicle create(Vehicle vehicle) throws  ServiceException {
        try {
            validation(vehicle);
            vehicle.setCreateDate(currentTime());
            return vehicleDAO.create(vehicle);
        } catch (DAOException e){
            throw new ServiceException("Vehicle not created.\n" + e);
        }

    }

    @Override
    public Vehicle update(Vehicle vehicle) throws ServiceException{
        try {
            validation(vehicle);
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
            validation(vehicle);
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
