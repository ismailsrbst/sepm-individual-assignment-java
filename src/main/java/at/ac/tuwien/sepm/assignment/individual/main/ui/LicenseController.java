package at.ac.tuwien.sepm.assignment.individual.main.ui;

import at.ac.tuwien.sepm.assignment.individual.main.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.main.entities.BookingVehicle;
import at.ac.tuwien.sepm.assignment.individual.main.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.main.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.main.exception.UIException;
import at.ac.tuwien.sepm.assignment.individual.main.persistence.BookingDAOImp;
import at.ac.tuwien.sepm.assignment.individual.main.service.BookingService;
import at.ac.tuwien.sepm.assignment.individual.main.service.BookingVehicleService;
import at.ac.tuwien.sepm.assignment.individual.main.service.BookingVehicleServiceImp;
import at.ac.tuwien.sepm.assignment.individual.main.service.VehicleService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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

    private final static Logger logger = LoggerFactory.getLogger(LicenseController.class);

    private MainWindowController controller;
    private BookingVehicleService service;
    private Vehicle vehicle;
    private Booking booking;
    private Stage stage;
    private List<BookingVehicle> bookingVehicleList;
    private BookingVehicle bookingVehicle;

    private BookingService bookingService;

    /*public LicenseController(MainWindowController controller, Vehicle vehicle, Stage stage){
        this.controller = controller;
        this.vehicle = vehicle;
        this.stage = stage;
        this.bookingVehicleList = new ArrayList<>();
        try {
            this.service = new BookingVehicleServiceImp();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    public LicenseController(MainWindowController controller, BookingVehicle bookingVehicle, Vehicle vehicle, Stage stage){
        this.controller = controller;
        this.bookingVehicle = bookingVehicle;
        this.vehicle = vehicle;
        this.stage = stage;
    }

    public LicenseController(MainWindowController controller, Vehicle vehicle, Booking booking, Stage stage, List<BookingVehicle> bookingVehicleList, BookingService bookingService){
        this.controller = controller;
        this.vehicle = vehicle;
        this.stage = stage;
        this.booking = booking;
        this.bookingVehicleList = bookingVehicleList;
        this.bookingService = bookingService;
        try {
            this.service = new BookingVehicleServiceImp();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, "BookingVehicleService can not created.\n"+e, ButtonType.OK);
            alertError.showAndWait();
        }
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

    @FXML
    void finishButtonCliked(ActionEvent event) {

        try {
            check();
            Timestamp licenseDate = new Timestamp(Date.from(dp_licenseCreateDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
            bookingVehicle.setVehicle(vehicle);
            bookingVehicle.setLicenseNumber(tf_licenseNumber.getText());
            bookingVehicle.setLicenseCreateDate(licenseDate);
            stage.close();

        } catch (UIException e){
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        } /*catch (ServiceException e) {
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        }*/

    }

    public void check() throws UIException {

        if (tf_licenseNumber.getText() == null || tf_licenseNumber.getText().isEmpty()){
            throw new UIException("License number can not be empty.");
        } else if (dp_licenseCreateDate.getValue() == null){
            throw new UIException("Enter date correctly.");
        }

        Timestamp licenseDate = new Timestamp(Date.from(dp_licenseCreateDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        int days = controller.getDifferenzTwoDateInHour(currentTime, licenseDate) / 24;
        if (currentTime.before(licenseDate)){
            throw new UIException("Enter date correctly.");
        } else if ((cb_a.isSelected() || cb_c.isSelected()) && days < 1095){
            throw new UIException("your license date should before 3 year");
        } else if (!cb_a.isSelected() && !cb_b.isSelected() && !cb_c.isSelected()){
            throw new UIException("you must select a license.");
        }
    }
}
