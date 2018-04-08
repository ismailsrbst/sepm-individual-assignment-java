package at.ac.tuwien.sepm.assignment.individual.booking.service;

import at.ac.tuwien.sepm.assignment.individual.booking.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.booking.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.booking.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.booking.persistence.BookingDAO;
import at.ac.tuwien.sepm.assignment.individual.booking.persistence.BookingDAOImp;

import java.sql.SQLException;
import java.util.List;

public class BookingServiceImp implements BookingService {

    private BookingDAO bookingDAO;

    public BookingServiceImp() throws SQLException {
        this.bookingDAO = new BookingDAOImp();
    }

    public void validation(Booking booking) throws ServiceException {
        if (booking.getCustomerName().trim().length() >= 50){
            throw new ServiceException("Customer Name can not be bigger than 50");
        }
    }

    @Override
    public Booking create(Booking booking) throws ServiceException {
        try {
            validation(booking);
            return bookingDAO.create(booking);
        } catch (DAOException e){
            throw new ServiceException("Booking not created.\n"+e);
        }
    }

    @Override
    public Booking delete(Booking booking) throws ServiceException{
        try {
            validation(booking);
            return bookingDAO.delete(booking);
        } catch (DAOException e){
            throw new ServiceException("Booking not deleted."+e);
        }
    }

    @Override
    public Booking cancel(Booking booking) throws ServiceException {
        try {
            validation(booking);
            return bookingDAO.cancel(booking);
        } catch (DAOException e){
            throw new ServiceException("Booking not canceled.\n" + e);
        }
    }

    @Override
    public Booking completed(Booking booking) throws ServiceException {
        try {
            validation(booking);
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

    @Override
    public Booking getBookingByBid(int id) throws ServiceException {
        try {
            return bookingDAO.getBookingByBid(id);
        } catch (DAOException e){
            throw new ServiceException("can't load booking."+e);
        }
    }
}
