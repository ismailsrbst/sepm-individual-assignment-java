package at.ac.tuwien.sepm.assignment.individual.universe.service;

import at.ac.tuwien.sepm.assignment.individual.universe.persistence.DAOException;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Vehicle;

import java.util.List;

public interface VehicleService {

    Vehicle create(Vehicle vehicle) throws DAOException;

    Vehicle delete(Vehicle vehicle) throws DAOException;

    List<Vehicle> getAllVehicleList() throws DAOException;
}
