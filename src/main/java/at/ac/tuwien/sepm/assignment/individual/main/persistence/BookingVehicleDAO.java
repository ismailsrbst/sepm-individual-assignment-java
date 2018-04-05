package at.ac.tuwien.sepm.assignment.individual.main.persistence;

import at.ac.tuwien.sepm.assignment.individual.main.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.main.entities.BookingVehicle;
import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;

import java.sql.SQLException;
import java.util.List;

public interface BookingVehicleDAO {

    BookingVehicle create(BookingVehicle bookingVehicle) throws SQLException;

    List<BookingVehicle> getBookingVehicleByBooking(Booking booking) throws DAOException;
}
