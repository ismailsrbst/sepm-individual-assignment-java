package at.ac.tuwien.sepm.assignment.individual.main.service;

import at.ac.tuwien.sepm.assignment.individual.main.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.main.entities.BookingVehicle;
import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.main.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.main.persistence.BookingVehicleDAO;
import at.ac.tuwien.sepm.assignment.individual.main.persistence.BookingVehicleDAOImp;

import java.sql.SQLException;
import java.util.Date;
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
}
