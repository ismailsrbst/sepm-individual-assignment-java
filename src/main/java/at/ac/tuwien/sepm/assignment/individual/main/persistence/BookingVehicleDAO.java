package at.ac.tuwien.sepm.assignment.individual.main.persistence;

import at.ac.tuwien.sepm.assignment.individual.main.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.main.entities.BookingVehicle;
import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;

import java.sql.SQLException;
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
}
