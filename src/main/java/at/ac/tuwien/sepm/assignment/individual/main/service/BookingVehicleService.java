package at.ac.tuwien.sepm.assignment.individual.main.service;

import at.ac.tuwien.sepm.assignment.individual.main.entities.BookingVehicle;

import java.sql.SQLException;

public interface BookingVehicleService {
    BookingVehicle create(BookingVehicle bookingVehicle) throws SQLException;
}
