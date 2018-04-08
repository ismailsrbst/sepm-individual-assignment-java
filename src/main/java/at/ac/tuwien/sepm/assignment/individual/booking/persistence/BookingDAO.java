package at.ac.tuwien.sepm.assignment.individual.booking.persistence;

import at.ac.tuwien.sepm.assignment.individual.booking.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.booking.exception.DAOException;

import java.util.List;

public interface BookingDAO {

    /**create new Booking
     * @param booking
     * @return booking with generated number
     * @throws DAOException
     */
    Booking create(Booking booking) throws DAOException;

    /**delete booking
     * @param booking
     * @return booking
     * @throws DAOException
     */
    Booking delete(Booking booking) throws DAOException;

    /**update Booking: set status = cancel
     * @param booking
     * @return created booking
     * @throws DAOException
     */
    Booking cancel(Booking booking) throws DAOException;

    /**update Booking: set status = completed
     * @param booking
     * @return created booking
     * @throws DAOException
     */
    Booking completed(Booking booking) throws DAOException;

    /**loading list of booking
     * @return all booking from database
     * @throws DAOException
     */
    List<Booking> getAllBooking() throws DAOException;

    /**
     * get list of booking by id
     * @param id
     * @return
     * @throws DAOException
     */
    Booking getBookingByBid(int id) throws DAOException;

}
