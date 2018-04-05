package at.ac.tuwien.sepm.assignment.individual.main.service;

import at.ac.tuwien.sepm.assignment.individual.main.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.main.persistence.BookingDAO;
import at.ac.tuwien.sepm.assignment.individual.main.persistence.BookingDAOImp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingServiceImp implements BookingService {

    private BookingDAO bookingDAO;

    public BookingServiceImp() throws SQLException {
        this.bookingDAO = new BookingDAOImp();
    }

    @Override
    public Booking create(Booking booking) throws DAOException {
        return bookingDAO.create(booking);
    }

    @Override
    public Booking delete(Booking booking) {
        return bookingDAO.delete(booking);
    }

    @Override
    public Booking cancel(Booking booking) throws DAOException {
        return bookingDAO.cancel(booking);
    }

    @Override
    public Booking completed(Booking booking) throws DAOException {
        return bookingDAO.completed(booking);
    }

    @Override
    public List<Booking> getAllBooking() throws DAOException {
        List<Booking> bookingList = new ArrayList<Booking>();
        for (Booking booking : bookingDAO.getAllBooking()){
            bookingList.add(booking);
        }
        return bookingList;
    }
}
