package at.ac.tuwien.sepm.assignment.individual.main.persistence;

import at.ac.tuwien.sepm.assignment.individual.main.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;

import java.util.List;

public interface VehicleDAO {

    Vehicle create(Vehicle vehicle) throws DAOException;

    List<Vehicle> getAllVehichle() throws DAOException;

    Vehicle delete(Vehicle vehicle) throws DAOException;

    public Vehicle update(Vehicle vehicle) throws DAOException;
}
