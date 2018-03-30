package at.ac.tuwien.sepm.assignment.individual.main.service;

import at.ac.tuwien.sepm.assignment.individual.main.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.main.persistence.BookingDAO;
import at.ac.tuwien.sepm.assignment.individual.main.persistence.BookingDAOImp;

import java.sql.SQLException;

public class BookingServiceImp implements BookingService {

    private BookingDAO bookingDAO;

    public BookingServiceImp() throws SQLException {
        this.bookingDAO = new BookingDAOImp();
    }

    @Override
    public Booking create(Booking booking) {
        return bookingDAO.create(booking);
    }
}
