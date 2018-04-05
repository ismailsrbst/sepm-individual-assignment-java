package at.ac.tuwien.sepm.assignment.individual.main.ui;

import at.ac.tuwien.sepm.assignment.individual.main.entities.*;
import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.main.exception.UIException;
import at.ac.tuwien.sepm.assignment.individual.main.service.BookingService;
import at.ac.tuwien.sepm.assignment.individual.main.service.BookingVehicleService;
import at.ac.tuwien.sepm.assignment.individual.main.service.BookingVehicleServiceImp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.validator.routines.CreditCardValidator;
import org.apache.commons.validator.routines.IBANValidator;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class BookingVehicleController implements Initializable {

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

    @FXML
    private TableView<Vehicle> tv_bookingVehicle;

    @FXML
    private TableColumn<Vehicle, String> tc_model;

    @FXML
    private TableColumn<Vehicle, String> tc_license;

    @FXML
    private TableColumn<Vehicle, Integer> tc_basePrice;

    private List<Vehicle> vehicleList;
    private MainWindowController controller;
    private BookingService bookingService;
    //private Stage stage;

    public BookingVehicleController(MainWindowController controller, List<Vehicle> vehicleList, BookingService bookingService){
        this.controller = controller;
        this.vehicleList = vehicleList;
        this.bookingService = bookingService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Vehicle> vehicles = FXCollections.observableArrayList(vehicleList);
        tc_model.setCellValueFactory(new PropertyValueFactory<Vehicle,String>("model"));
        tc_license.setCellValueFactory(new PropertyValueFactory<Vehicle,String>("driverLicense"));
        tc_basePrice.setCellValueFactory(new PropertyValueFactory<Vehicle,Integer>("basePrice"));

        tv_bookingVehicle.setItems(vehicles);
        tv_bookingVehicle.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void check() throws UIException {
        IBANValidator ibanValidator = new IBANValidator();
        CreditCardValidator creditCardValidator = new CreditCardValidator();
        Timestamp beginnDate = new Timestamp(Date.from(dp_startDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        if (tf_customerName.getText() == null || tf_customerName.getText().isEmpty()){
            throw new UIException("Customer name can not be empty.");
        } else if (!ibanValidator.isValid(tf_payNumber.getText()) && !creditCardValidator.isValid(tf_payNumber.getText())) {
            throw new UIException("Pay number is not valid.");
        } else if (currentTime.after(beginnDate)){
            throw new UIException("Enter date correctly.");
        } else if (dp_startDate.getValue().isAfter(dp_endDate.getValue()) || dp_endDate.getValue() == null){
            throw new UIException("Enter date correctly.");
        } else if (dp_startDate.getValue() == null || dp_endDate.getValue() == null){
            throw new UIException("Enter date correctly.");
        }
    }

    @FXML
    void bookingButtonClicked(ActionEvent event) {
        //System.out.println(ibanValidator.isValid("AT611904300234573201"));
        Booking booking = null;
        BookingVehicle bookingVehicle = null;
        try {
            booking = new Booking();
            bookingVehicle = new BookingVehicle();
            check();
            Timestamp beginnDate = new Timestamp(Date.from(dp_startDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
            Timestamp endDate = new Timestamp(Date.from(dp_endDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
            beginnDate.setTime(beginnDate.getTime() + 3600000);
            endDate.setTime(endDate.getTime() + (3600000*3));

            booking.setCustomerName(tf_customerName.getText());
            booking.setPayNumber(tf_payNumber.getText());
            booking.setStatus(Status.OPEN);
            //TODO Saat Ekle
            booking.setBeginnDate(beginnDate);
            booking.setEndDate(endDate);


            int totalPrice = 0;
            if (!vehicleList.isEmpty()) {
                for (Vehicle vehicle : vehicleList) {
                    totalPrice += vehicle.getBasePrice();
                }
            }
            //TODO Saate göre Hesapla
            booking.setTotalPrice(totalPrice);



            //TODO buralara bak cancel ve complete metotlarina tasi
            booking.setInvoiceDate(null);
            booking.setInvoiceNumber(0);


            if (getDifferenzTwoDateInDay(beginnDate, new Timestamp(System.currentTimeMillis())) <= 7){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "can not be canceled for free");
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get() == ButtonType.OK) {
                    bookingService.create(booking);
                    controller.exit();
                }
            }else {
                bookingService.create(booking);
                controller.exit();
            }
        } catch (UIException e){
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        } /*catch (NullPointerException e){
            Alert alertError = new Alert(Alert.AlertType.ERROR, "Enter date correctly. " + e, ButtonType.OK);
            alertError.showAndWait();
        }*/ catch (DAOException e) {
            e.printStackTrace();
        }

        if (booking.getBid() != null) {
            BookingVehicleService bookingVehicleService = null;
            try {
                bookingVehicleService = new BookingVehicleServiceImp();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (!vehicleList.isEmpty()) {
                    for (Vehicle vehicle : vehicleList) {
                        //bookingVehicle.setVehicle(vehicle);
                        if (!vehicle.getDriverLicense().equals("")) {
                            Stage stage = new Stage(StageStyle.UNDECORATED);
                            LicenseController licenseController = new LicenseController(controller, vehicle, booking, stage, bookingVehicle);
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/licenseNumberAndDate.fxml"));
                            fxmlLoader.setControllerFactory(param -> param.isInstance(licenseController) ? licenseController : null);
                            Parent root = fxmlLoader.load();
                            Scene scene = new Scene(root);
                            stage.setTitle("Booking Vehicle " + vehicle.getModel());
                            stage.setScene(scene);
                            stage.show();
                        } else {
                            bookingVehicle.setBid(booking.getBid());
                            bookingVehicle.setVehicle(vehicle);
                            try {
                                bookingVehicleService.create(bookingVehicle);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        }
                        //booking.addBookingVehicles(bookingVehicle);
                    }
                }
                controller.loadBooking();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DAOException e) {
                e.printStackTrace();
            }
        }
    }

    private int getDifferenzTwoDateInDay(Timestamp after, Timestamp before){
        long milliseconds = Math.abs(after.getTime() - before.getTime());
        long seconds = milliseconds / 1000;
        int hours = (int)seconds / 3600;
        int days = hours / 24;
        return days;
    }

}
