package at.ac.tuwien.sepm.assignment.individual.main.service;

import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.main.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.main.exception.ServiceException;

import java.util.List;

public interface VehicleService {

    Vehicle create(Vehicle vehicle) throws DAOException, ServiceException;

    Vehicle delete(Vehicle vehicle) throws DAOException;

    List<Vehicle> getAllVehicleList() throws DAOException;

    public Vehicle update(Vehicle vehicle) throws DAOException;
}
