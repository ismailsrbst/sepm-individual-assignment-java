package at.ac.tuwien.sepm.assignment.individual.main.ui;

import at.ac.tuwien.sepm.assignment.individual.main.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.main.entities.BookingVehicle;
import at.ac.tuwien.sepm.assignment.individual.main.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.main.service.BookingService;
import at.ac.tuwien.sepm.assignment.individual.main.service.BookingVehicleService;
import at.ac.tuwien.sepm.assignment.individual.main.service.BookingVehicleServiceImp;
import at.ac.tuwien.sepm.assignment.individual.main.service.VehicleService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class LicenseController {

    @FXML
    private TextField tf_licenseNumber;

    @FXML
    private DatePicker dp_licenseCreateDate;

    private MainWindowController controller;
    private BookingVehicleService service;
    private Vehicle vehicle;
    private Booking booking;
    private Stage stage;
    private BookingVehicle bookingVehicle;

    public LicenseController(MainWindowController controller, Vehicle vehicle, Booking booking, Stage stage, BookingVehicle bookingVehicle){
        this.controller = controller;
        this.vehicle = vehicle;
        this.stage = stage;
        this.booking = booking;
        this.bookingVehicle = bookingVehicle;
        try {
            this.service = new BookingVehicleServiceImp();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void nextButtonCliked(ActionEvent event) {

        bookingVehicle.setBid(booking.getBid());
        bookingVehicle.setVehicle(vehicle);
        if (tf_licenseNumber.getText() == null || tf_licenseNumber.getText().isEmpty()){
            Alert alert=new Alert(Alert.AlertType.WARNING,"License number can not be empty..");
            alert.showAndWait();
        }
        bookingVehicle.setLicenseNumber(tf_licenseNumber.getText());

        Timestamp licenseDate = new Timestamp(Date.from(dp_licenseCreateDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
        bookingVehicle.setLicenseCreateDate(licenseDate);
        try {
            service.create(bookingVehicle);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //booking.addBookingVehicles(bookingVehicle);
        stage.close();
    }

    @FXML
    void licenceCreateDateSelected(ActionEvent event) {
        Timestamp licenseDate = new Timestamp(Date.from(dp_licenseCreateDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        long milliseconds = (currentTime.getTime() - licenseDate.getTime());
        long seconds = milliseconds / 1000;
        int hours = (int)seconds / 3600;
        int days = hours / 24;
        if (dp_licenseCreateDate.getValue() == null || currentTime.before(licenseDate) || ((vehicle.getDriverLicense().contains("A") || vehicle.getDriverLicense().contains("C")) && days < 1095)){
            //Alert alertError = new Alert(Alert.AlertType.ERROR, "enter in the correct format ", ButtonType.OK);
            //alertError.showAndWait();
            Alert alert=new Alert(Alert.AlertType.WARNING,"your license date should before 3 year");
            alert.showAndWait();
        }
    }

    public BookingVehicle getBookingVehicle() {
        return bookingVehicle;
    }
}
