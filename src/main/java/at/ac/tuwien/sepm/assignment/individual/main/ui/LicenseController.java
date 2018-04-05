package at.ac.tuwien.sepm.assignment.individual.main.ui;

import at.ac.tuwien.sepm.assignment.individual.main.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.main.entities.BookingVehicle;
import at.ac.tuwien.sepm.assignment.individual.main.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.main.exception.UIException;
import at.ac.tuwien.sepm.assignment.individual.main.service.BookingService;
import at.ac.tuwien.sepm.assignment.individual.main.service.BookingVehicleService;
import at.ac.tuwien.sepm.assignment.individual.main.service.BookingVehicleServiceImp;
import at.ac.tuwien.sepm.assignment.individual.main.service.VehicleService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;

public class LicenseController implements Initializable{

    @FXML
    private TextField tf_licenseNumber;

    @FXML
    private DatePicker dp_licenseCreateDate;

    @FXML
    private CheckBox cb_a;

    @FXML
    private CheckBox cb_c;

    @FXML
    private CheckBox cb_b;

    private MainWindowController controller;
    private BookingVehicleService service;
    private Vehicle vehicle;
    private Booking booking;
    private Stage stage;
    private BookingVehicle bookingVehicle;

    public LicenseController(MainWindowController controller, Vehicle vehicle, Stage stage){
        this.controller = controller;
        this.vehicle = vehicle;
        this.stage = stage;
        this.bookingVehicle = new BookingVehicle();
        try {
            this.service = new BookingVehicleServiceImp();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
        if (!vehicle.getDriverLicense().contains("A")){
            cb_a.setVisible(false);
        }
        if (!vehicle.getDriverLicense().contains("B")){
            cb_b.setVisible(false);
        }
        if (!vehicle.getDriverLicense().contains("C")){
            cb_c.setVisible(false);
        }
    }

    @FXML
    void finishButtonCliked(ActionEvent event) throws SQLException {

        try {
            if (cb_b.isSelected()){
                bookingVehicle.setVehicle(vehicle);
                bookingVehicle.setLicenseNumber(null);
                bookingVehicle.setLicenseCreateDate(null);
                //service.create(bookingVehicle);
                stage.close();
            } else {
                check();
                Timestamp licenseDate = new Timestamp(Date.from(dp_licenseCreateDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
                //bookingVehicle.setBid(booking.getBid());
                bookingVehicle.setVehicle(vehicle);
                bookingVehicle.setLicenseNumber(tf_licenseNumber.getText());
                bookingVehicle.setLicenseCreateDate(licenseDate);
                //service.create(bookingVehicle);
                stage.close();
            }

        } catch (UIException e){
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        } /*catch (NullPointerException e){
            Alert alertError = new Alert(Alert.AlertType.ERROR, "Enter date correctly. " + e, ButtonType.OK);
            alertError.showAndWait();
        }*/

    }

    public void check() throws UIException {
        Timestamp licenseDate = new Timestamp(Date.from(dp_licenseCreateDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        long milliseconds = (currentTime.getTime() - licenseDate.getTime());
        long seconds = milliseconds / 1000;
        int hours = (int)seconds / 3600;
        int days = hours / 24;

        if (tf_licenseNumber.getText() == null || tf_licenseNumber.getText().isEmpty()){
            throw new UIException("License number can not be empty.");
        } else if (dp_licenseCreateDate.getValue() == null){
            throw new UIException("Enter date correctly.");
        } else if (currentTime.before(licenseDate)){
            throw new UIException("Enter date correctly.");
        } else if ((cb_a.isSelected() || cb_c.isSelected()) && days < 1095){
            throw new UIException("your license date should before 3 year");
        }
    }

    public TextField getTf_licenseNumber() {
        return tf_licenseNumber;
    }

    public DatePicker getDp_licenseCreateDate() {
        return dp_licenseCreateDate;
    }

    public BookingVehicle getBookingVehicle() {
        return bookingVehicle;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (!vehicle.getDriverLicense().contains("A")){
            cb_a.setVisible(false);
        }
        if (!vehicle.getDriverLicense().contains("B")){
            cb_b.setVisible(false);
        }
        if (!vehicle.getDriverLicense().contains("C")){
            cb_c.setVisible(false);
        }
    }
}
