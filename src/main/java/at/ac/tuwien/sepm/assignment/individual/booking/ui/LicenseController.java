package at.ac.tuwien.sepm.assignment.individual.booking.ui;

import at.ac.tuwien.sepm.assignment.individual.booking.entities.BookingVehicle;
import at.ac.tuwien.sepm.assignment.individual.booking.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.booking.exception.InputException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZoneId;
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
    private Vehicle vehicle;
    private Stage stage;
    private BookingVehicle bookingVehicle;


    public LicenseController(MainWindowController controller, BookingVehicle bookingVehicle, Vehicle vehicle, Stage stage){
        this.controller = controller;
        this.bookingVehicle = bookingVehicle;
        this.vehicle = vehicle;
        this.stage = stage;
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

        logger.info("finish button clicked for license info.");
        try {
            check();
            Timestamp licenseDate = new Timestamp(Date.from(dp_licenseCreateDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
            bookingVehicle.setVehicle(vehicle);
            bookingVehicle.setLicenseNumber(tf_licenseNumber.getText());
            bookingVehicle.setLicenseCreateDate(licenseDate);
            stage.close();

        } catch (InputException e){
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        }

    }

    public void check() throws InputException {

        if (tf_licenseNumber.getText() == null || tf_licenseNumber.getText().isEmpty()){
            throw new InputException("License number can not be empty.");
        } else if (dp_licenseCreateDate.getValue() == null){
            throw new InputException("Enter date correctly.");
        }

        Timestamp licenseDate = new Timestamp(Date.from(dp_licenseCreateDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        int days = controller.getDifferenzTwoDateInHour(currentTime, licenseDate) / 24;
        if (currentTime.before(licenseDate)){
            throw new InputException("Enter date correctly.");
        } else if ((cb_a.isSelected() || cb_c.isSelected()) && days < 1095){
            throw new InputException("your license date should before 3 year");
        } else if (!cb_a.isSelected() && !cb_b.isSelected() && !cb_c.isSelected()){
            throw new InputException("you must select a license.");
        }
    }
}
