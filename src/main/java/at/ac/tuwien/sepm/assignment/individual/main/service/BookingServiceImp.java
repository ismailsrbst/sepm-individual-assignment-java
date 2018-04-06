package at.ac.tuwien.sepm.assignment.individual.main.service;

import at.ac.tuwien.sepm.assignment.individual.main.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.main.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.main.persistence.BookingDAO;
import at.ac.tuwien.sepm.assignment.individual.main.persistence.BookingDAOImp;

import java.sql.DataTruncation;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingServiceImp implements BookingService {

    private BookingDAO bookingDAO;

    public BookingServiceImp() throws SQLException {
        this.bookingDAO = new BookingDAOImp();
    }

    @Override
    public Booking create(Booking booking) throws ServiceException {
        try {
            return bookingDAO.create(booking);
        } catch (DAOException e){
            throw new ServiceException("Booking not created.\n"+e);
        }
    }

    @Override
    public Booking delete(Booking booking) throws ServiceException{
        try {
            return bookingDAO.delete(booking);
        } catch (DAOException e){
            throw new ServiceException("Booking not deleted."+e);
        }
    }

    @Override
    public Booking cancel(Booking booking) throws ServiceException {
        try {
            return bookingDAO.cancel(booking);
        } catch (DAOException e){
            throw new ServiceException("Booking not canceled.\n" + e);
        }
    }

    @Override
    public Booking completed(Booking booking) throws ServiceException {
        try {
            return bookingDAO.completed(booking);
        } catch (DAOException e){
            throw new ServiceException("Booking not completed.\n" + e);
        }
    }

    @Override
    public List<Booking> getAllBooking() throws ServiceException {
        try {
            return bookingDAO.getAllBooking();
        } catch (DAOException e){
            throw new ServiceException("can't load booking."+e);
        }
    }
}
