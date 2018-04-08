package at.ac.tuwien.sepm.assignment.individual.booking.persistence;

import at.ac.tuwien.sepm.assignment.individual.booking.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.booking.entities.SearchFilter;
import at.ac.tuwien.sepm.assignment.individual.booking.entities.Vehicle;

import java.util.List;

public interface VehicleDAO {

    /**create new vehicle
     * @param vehicle
     * @return vehicle with generated number
     * @throws DAOException
     */
    Vehicle create(Vehicle vehicle) throws DAOException;

    /**loading list of vehicle
     * @return all vehicle from database
     * @throws DAOException
     */
    List<Vehicle> getAllVehichle() throws DAOException;

    /**update vehicle: set isDeleted=true and create new vehicle
     * @param vehicle
     * @return vehicle
     * @throws DAOException
     */
    Vehicle delete(Vehicle vehicle) throws DAOException;

    /**returns filtered list of vehicle
     * @param searchFilter
     * @return
     * @throws DAOException
     */
    List<Vehicle> search(SearchFilter searchFilter) throws DAOException;

    /**update vehicle
     * @param vehicle
     * @return returns created vehicle
     * @throws DAOException
     */
    Vehicle update(Vehicle vehicle) throws DAOException;
}
