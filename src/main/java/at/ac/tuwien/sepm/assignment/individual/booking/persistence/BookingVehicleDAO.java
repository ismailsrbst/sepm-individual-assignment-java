package at.ac.tuwien.sepm.assignment.individual.booking.persistence;

import at.ac.tuwien.sepm.assignment.individual.booking.entities.BookingVehicle;
import at.ac.tuwien.sepm.assignment.individual.booking.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.booking.entities.Booking;

import java.sql.Timestamp;
import java.util.List;

public interface BookingVehicleDAO {

    /**create new bookingVehicle
     * @param bookingVehicle
     * @return bookingVehicle with generated number
     * @throws DAOException
     */
    BookingVehicle create(BookingVehicle bookingVehicle) throws DAOException;
    /**loading list of bookingVehicle
     * @param booking
     * @return all bookingVehicle same bookingId from database
     * @throws DAOException
     */
    List<BookingVehicle> getBookingVehicleByBooking(Booking booking) throws DAOException;

    /**
     * loading list of bookingVehicle
     * @param start
     * @param end
     * @param license
     * @return
     * @throws DAOException
     */
    List<BookingVehicle> getBookingVehicle(Timestamp start, Timestamp end, char license) throws DAOException;
}
