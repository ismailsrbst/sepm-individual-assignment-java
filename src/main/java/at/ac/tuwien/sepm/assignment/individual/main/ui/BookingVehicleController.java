package at.ac.tuwien.sepm.assignment.individual.main.ui;

import at.ac.tuwien.sepm.assignment.individual.main.entities.*;
import at.ac.tuwien.sepm.assignment.individual.main.service.BookingService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.CreditCardValidator;
import org.apache.commons.validator.routines.IBANValidator;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.List;

public class BookingVehicleController {

    @FXML
    private TextField tf_customerName;

    @FXML
    private TextField tf_payNumber;

    @FXML
    private DatePicker dp_startDate;

    @FXML
    private DatePicker dp_endDate;

    @FXML
    private RadioButton rb_open;

    @FXML
    private RadioButton rb_canceled;

    @FXML
    private RadioButton rb_completed;

    private List<Vehicle> vehicleList;
    private MainWindowController controller;
    private BookingService bookingService;
    //private Stage stage;

    public BookingVehicleController(MainWindowController controller, List<Vehicle> vehicleList, BookingService bookingService){
        this.controller = controller;
        this.vehicleList = vehicleList;
        this.bookingService = bookingService;
    }

    @FXML
    void bookingButtonClicked(ActionEvent event) {

        IBANValidator ibanValidator = new IBANValidator();
        CreditCardValidator creditCardValidator = new CreditCardValidator();
        //System.out.println(ibanValidator.isValid("AT611904300234573201"));

        Booking booking = new Booking();
        BookingVehicle bookingVehicle = new BookingVehicle();

        if (tf_customerName.getText() != null || !tf_customerName.getText().isEmpty()){
            booking.setCustomerName(tf_customerName.getText());
        } else {
            Alert alert=new Alert(Alert.AlertType.WARNING,"Customer name can not be empty.");
            alert.showAndWait();
        }

        //booking.setPayTyp(rb_iban.isSelected() ? PayTyp.IBAN : PayTyp.CREDITCARD);
        String payNumber = tf_payNumber.getText();
        if (ibanValidator.isValid(payNumber) || creditCardValidator.isValid(payNumber)) {
            booking.setPayNumber(payNumber);
        } else {
            Alert alert=new Alert(Alert.AlertType.WARNING,"Pay number is not valid.");
            alert.showAndWait();
        }

        Timestamp beginnDate = new Timestamp(Date.from(dp_startDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
        Timestamp endDate = new Timestamp(Date.from(dp_endDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());

        if (beginnDate.after(endDate)){
            Alert alert=new Alert(Alert.AlertType.WARNING,"Enter date correctly.");
            alert.showAndWait();
        }else {
            booking.setBeginnDate(beginnDate);
            booking.setEndDate(endDate);
        }
        booking.setStatus(rb_open.isSelected() ? Status.OPEN : rb_canceled.isSelected() ? Status.CANCELED : Status.COMPLETED); //TODO

        int totalPrice = 0;
        if (!vehicleList.isEmpty()) {
            for (Vehicle vehicle : vehicleList) {
                totalPrice += vehicle.getBasePrice();
            }
        }
        booking.setTotalPrice(totalPrice);
        bookingService.create(booking);
        controller.exit();

        try {
            if (!vehicleList.isEmpty()) {
                for (Vehicle vehicle : vehicleList) {

                    //bookingVehicle.setVehicle(vehicle);
                    Stage stage = new Stage();
                    LicenseController licenseController = new LicenseController(controller, vehicle, booking, stage, bookingVehicle);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/licenseNumberAndDate.fxml"));
                    fxmlLoader.setControllerFactory(param -> param.isInstance(licenseController) ? licenseController : null);
                    Parent root = fxmlLoader.load();
                    Scene scene = new Scene(root);
                    stage.setTitle("Booking Vehicle " + vehicle.getModel());
                    stage.setScene(scene);
                    stage.show();
                    //booking.addBookingVehicles(bookingVehicle);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
