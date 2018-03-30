package at.ac.tuwien.sepm.assignment.individual.main.service;

import at.ac.tuwien.sepm.assignment.individual.main.entities.BookingVehicle;
import at.ac.tuwien.sepm.assignment.individual.main.persistence.BookingVehicleDAO;
import at.ac.tuwien.sepm.assignment.individual.main.persistence.BookingVehicleDAOImp;

import java.sql.SQLException;

public class BookingVehicleServiceImp implements BookingVehicleService {

    private BookingVehicleDAO bookingVehicleDAO;

    public BookingVehicleServiceImp() throws SQLException {
        this.bookingVehicleDAO = new BookingVehicleDAOImp();
    }
    @Override
    public BookingVehicle create(BookingVehicle bookingVehicle) throws SQLException {
        return bookingVehicleDAO.create(bookingVehicle);
    }
}
