package at.ac.tuwien.sepm.assignment.individual.universe.persistence;

import at.ac.tuwien.sepm.assignment.individual.universe.entities.Vehicle;

import java.util.List;

public interface VehicleDAO {

    Vehicle create(Vehicle vehicle) throws DAOException;

    List<Vehicle> getAllVehichle() throws DAOException;

    Vehicle delete(Vehicle vehicle) throws DAOException;
}
