package at.ac.tuwien.sepm.assignment.individual.booking.service;

import at.ac.tuwien.sepm.assignment.individual.booking.entities.BookingVehicle;
import at.ac.tuwien.sepm.assignment.individual.booking.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.booking.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.booking.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.booking.persistence.BookingVehicleDAO;
import at.ac.tuwien.sepm.assignment.individual.booking.persistence.BookingVehicleDAOImp;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class BookingVehicleServiceImp implements BookingVehicleService {

    private BookingVehicleDAO bookingVehicleDAO;

    public BookingVehicleServiceImp() throws SQLException {
        this.bookingVehicleDAO = new BookingVehicleDAOImp();
    }

    @Override
    public BookingVehicle create(BookingVehicle bookingVehicle) throws ServiceException {
        try {
            return bookingVehicleDAO.create(bookingVehicle);
        } catch (DAOException e){
            throw new ServiceException("BookingVehicle not created.\n"+e);
        }

    }

    @Override
    public List<BookingVehicle> getBookingVehicleByBooking(Booking booking) throws ServiceException {
        try {
            return bookingVehicleDAO.getBookingVehicleByBooking(booking);
        }catch (DAOException e){
            throw new ServiceException("can't load bookingVehicle.\n"+e);
        }
    }

    @Override
    public List<BookingVehicle> getBookingVehicle(Timestamp start, Timestamp end, char license) throws ServiceException {
        try {
            return bookingVehicleDAO.getBookingVehicle(start, end, license);
        } catch (DAOException e){
            throw new ServiceException("can't load bookingVehicle.\n"+e);
        }
    }
}
