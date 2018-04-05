package at.ac.tuwien.sepm.assignment.individual.main.service;

import at.ac.tuwien.sepm.assignment.individual.main.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.main.entities.BookingVehicle;
import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;

import java.sql.SQLException;
import java.util.List;

public interface BookingVehicleService {

    BookingVehicle create(BookingVehicle bookingVehicle) throws SQLException;

    List<BookingVehicle> getBookingVehicleByBooking(Booking booking) throws DAOException;
}
