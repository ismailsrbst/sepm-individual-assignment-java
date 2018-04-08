package at.ac.tuwien.sepm.assignment.individual.booking.service;

import at.ac.tuwien.sepm.assignment.individual.booking.entities.SearchFilter;
import at.ac.tuwien.sepm.assignment.individual.booking.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.booking.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.booking.exception.ServiceException;

import java.util.List;

public interface VehicleService {

    /**creating new vehicle
     * @param vehicle
     * @return vehicle
     * @throws ServiceException
     */
    Vehicle create(Vehicle vehicle) throws ServiceException;

    /**delete vehicle
     * @param vehicle
     * @return vehicle
     * @throws ServiceException
     */
    Vehicle delete(Vehicle vehicle) throws ServiceException;

    List<Vehicle> getAllVehicleList() throws ServiceException;

    /**returns filtered list of vehicle
     * @param searchFilter
     * @return
     * @throws DAOException
     */
    List<Vehicle> search(SearchFilter searchFilter) throws ServiceException;

    /**updating vehicle
     * @param vehicle
     * @return vehicle
     * @throws ServiceException
     */
    Vehicle update(Vehicle vehicle) throws ServiceException;
}
