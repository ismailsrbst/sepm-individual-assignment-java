package at.ac.tuwien.sepm.assignment.individual.main.persistence;

import at.ac.tuwien.sepm.assignment.individual.main.entities.BookingVehicle;

import java.sql.SQLException;

public interface BookingVehicleDAO {

    BookingVehicle create(BookingVehicle bookingVehicle) throws SQLException;
}
