package at.ac.tuwien.sepm.assignment.individual.main.service;

import at.ac.tuwien.sepm.assignment.individual.main.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.main.entities.BookingVehicle;
import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.main.exception.ServiceException;

import java.sql.SQLException;
import java.util.List;

public interface BookingVehicleService {

    /**create new bookingVehicle
     * @param bookingVehicle
     * @return bookingVehicle
     * @throws DAOException
     */
    BookingVehicle create(BookingVehicle bookingVehicle) throws ServiceException;

    /**loading list of bookingVehicle
     * @param booking
     * @return all bookingVehicle same bookingId from database
     * @throws DAOException
     */
    List<BookingVehicle> getBookingVehicleByBooking(Booking booking) throws ServiceException;
}
