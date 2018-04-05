package at.ac.tuwien.sepm.assignment.individual.main.persistence;

import at.ac.tuwien.sepm.assignment.individual.main.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;

import java.util.List;

public interface BookingDAO {

    Booking create(Booking booking) throws DAOException;

    Booking delete(Booking booking);

    Booking cancel(Booking booking) throws DAOException;

    Booking completed(Booking booking) throws DAOException;

    List<Booking> getAllBooking() throws DAOException;

}
