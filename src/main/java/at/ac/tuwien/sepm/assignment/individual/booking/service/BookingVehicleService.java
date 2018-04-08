package at.ac.tuwien.sepm.assignment.individual.booking.service;

import at.ac.tuwien.sepm.assignment.individual.booking.entities.BookingVehicle;
import at.ac.tuwien.sepm.assignment.individual.booking.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.booking.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.booking.exception.ServiceException;

import java.sql.Timestamp;
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

    List<BookingVehicle> getBookingVehicle(Timestamp start, Timestamp end, char license) throws ServiceException;


}
