package at.ac.tuwien.sepm.assignment.individual.main.service;

import at.ac.tuwien.sepm.assignment.individual.main.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.main.entities.BookingVehicle;
import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.main.persistence.BookingVehicleDAO;
import at.ac.tuwien.sepm.assignment.individual.main.persistence.BookingVehicleDAOImp;

import java.sql.SQLException;
import java.util.List;

public class BookingVehicleServiceImp implements BookingVehicleService {

    private BookingVehicleDAO bookingVehicleDAO;

    public BookingVehicleServiceImp() throws SQLException {
        this.bookingVehicleDAO = new BookingVehicleDAOImp();
    }
    @Override
    public BookingVehicle create(BookingVehicle bookingVehicle) throws SQLException {
        return bookingVehicleDAO.create(bookingVehicle);
    }

    @Override
    public List<BookingVehicle> getBookingVehicleByBooking(Booking booking) throws DAOException {
        List<BookingVehicle> bookingVehicleList = null;
        try {
            bookingVehicleList = bookingVehicleDAO.getBookingVehicleByBooking(booking);

        }catch (NullPointerException e){

        }
        return bookingVehicleList;
    }
}
