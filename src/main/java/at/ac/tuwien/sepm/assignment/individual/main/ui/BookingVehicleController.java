package at.ac.tuwien.sepm.assignment.individual.main.ui;

import at.ac.tuwien.sepm.assignment.individual.main.entities.*;
import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.main.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.main.exception.UIException;
import at.ac.tuwien.sepm.assignment.individual.main.persistence.BookingDAOImp;
import at.ac.tuwien.sepm.assignment.individual.main.service.BookingService;
import at.ac.tuwien.sepm.assignment.individual.main.service.BookingServiceImp;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class BookingVehicleController implements Initializable {

    @FXML
    private TextField tf_customerName;

    @FXML
    private TextField tf_payNumber;

    @FXML
    private Label lb_startDate;

    @FXML
    private Label lb_endDate;

    @FXML
    private TableView<Vehicle> tv_bookingVehicle;

    @FXML
    private TableColumn<Vehicle, String> tc_modelForBooking;

    @FXML
    private TableColumn<Vehicle, String> tc_licenseForBooking;

    @FXML
    private TableColumn<Vehicle, Integer> tc_basePriceForBooking;

    private final static Logger logger = LoggerFactory.getLogger(BookingVehicleController.class);

    private List<Vehicle> vehicleList;
    private MainWindowController controller;
    private BookingService bookingService;
    private Timestamp beginDate;
    private Timestamp endDate;
    private List<BookingVehicle> bookingVehicleList;
    private BookingVehicleService bookingVehicleService;
    private Booking booking;
    private int totalPrice = 0;

    public BookingVehicleController(MainWindowController controller){
        this.controller = controller;
    }

    public BookingVehicleController(MainWindowController controller, List<Vehicle> vehicleList, Timestamp beginDate, Timestamp endDate) {
        this.controller = controller;
        this.vehicleList = vehicleList;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.bookingVehicleList = new ArrayList<>();
        this.booking = new Booking();
        try {
            this.bookingService = new BookingServiceImp();
            this.bookingVehicleService = new BookingVehicleServiceImp();
        } catch (SQLException e){
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, "BookingService or BookingVehicleService not created"+e, ButtonType.OK);
            alertError.showAndWait();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTableview();
        fillBookingVehicle();
    }

    public void loadTableview(){
        ObservableList<Vehicle> vehicles = FXCollections.observableArrayList(vehicleList);
        tc_modelForBooking.setCellValueFactory(new PropertyValueFactory<>("model"));
        tc_licenseForBooking.setCellValueFactory(new PropertyValueFactory<>("driverLicense"));
        tc_basePriceForBooking.setCellValueFactory(new PropertyValueFactory<>("basePrice"));

        tv_bookingVehicle.setItems(vehicles);
        tv_bookingVehicle.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void check() throws UIException {
        IBANValidator ibanValidator = new IBANValidator();
        CreditCardValidator creditCardValidator = new CreditCardValidator();

        if (tf_customerName.getText() == null || tf_customerName.getText().isEmpty()){
            throw new UIException("Customer name can not be empty.");
        } else if (!ibanValidator.isValid(tf_payNumber.getText()) && !creditCardValidator.isValid(tf_payNumber.getText())) {
            throw new UIException("Pay number is not valid.");
        }
    }

    public boolean bookingAlert(){
        if (vehicleList != null) {
            for (Vehicle vehicle : vehicleList) {
                for (BookingVehicle bookingVehicle : bookingVehicleList) {
                    if (bookingVehicle != null) {
                        if (vehicle.getVid() == bookingVehicle.getVehicle().getVid()) {
                            /*if ((vehicle.getDriverLicense().contains("A") || vehicle.getDriverLicense().contains("C")) && (bookingVehicle1.getLicenseNumber() == null || bookingVehicle1.getLicenseCreateDate() == null)) {
                                return false;
                            }*/
                            if (!vehicle.getDriverLicense().equals("") && bookingVehicle.getLicenseNumber().equals("") && bookingVehicle.getLicenseCreateDate() ==null){
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    public void fillBookingVehicle(){
        if (vehicleList != null) {
            for (Vehicle vehicle : vehicleList) {
                totalPrice += (int) Math.ceil((double) getDifferenzTwoDateInHour(beginDate, endDate)) * vehicle.getBasePrice();
                BookingVehicle bookingVehicle = new BookingVehicle();
                bookingVehicle.setVehicle(vehicle);
                bookingVehicle.setLicenseNumber("");
                bookingVehicle.setLicenseCreateDate(null);
                bookingVehicleList.add(bookingVehicle);
            }
        }
    }

    @FXML
    void finishBookingButtonClicked(ActionEvent event) {
        //System.out.println(ibanValidator.isValid("AT611904300234573201"));
        if (bookingAlert()) {
            try {
                Booking booking = new Booking();
                check();

                booking.setCustomerName(tf_customerName.getText());
                booking.setPayNumber(tf_payNumber.getText());
                booking.setStatus(Status.OPEN);
                booking.setBeginnDate(beginDate);
                booking.setEndDate(endDate);
                booking.setTotalPrice(totalPrice);
                booking.setInvoiceDate(null);
                booking.setInvoiceNumber(0);
                bookingService.create(booking);
                for (BookingVehicle bookingVehicle : bookingVehicleList) {
                    bookingVehicle.setBid(booking.getBid());
                    bookingVehicleService.create(bookingVehicle);
                }
                controller.exit();
            } catch (UIException e) {
                logger.error(e.getMessage());
                Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alertError.showAndWait();
            } catch (ServiceException e) {
                logger.error(e.getMessage());
                Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alertError.showAndWait();
            }
        } else {
            Alert alertError = new Alert(Alert.AlertType.ERROR, "you should fill license number and license date", ButtonType.OK);
            alertError.showAndWait();
        }
    }

    @FXML
    void addLicenseNumberAndDateButtonCliked(ActionEvent event) {
        Vehicle vehicle = tv_bookingVehicle.getSelectionModel().getSelectedItem();
        System.out.println(bookingVehicleList.size());

        int i = 0;
        for (BookingVehicle bookingVehicle1 : bookingVehicleList){
            if (vehicle.getVid() == bookingVehicle1.getVehicle().getVid()){
                break;
            }
            i++;
        }
        BookingVehicle bookingVehicle = bookingVehicleList.get(i);
        if (vehicle != null && !vehicle.getDriverLicense().equals("") && bookingVehicle != null && bookingVehicle.getLicenseCreateDate() == null && bookingVehicle.getLicenseNumber().equals("") ) {
            try {
                Stage stage = new Stage();
                LicenseController licenseController = new LicenseController(controller, bookingVehicleList.get(i), vehicle, stage);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/licenseNumberAndDate.fxml"));
                fxmlLoader.setControllerFactory(param -> param.isInstance(licenseController) ? licenseController : null);
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                stage.setTitle("Booking Vehicle " + vehicle.getModel());
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                //logger.error(e.getMessage());
                e.printStackTrace();
            }

        }
    }

    private int getDifferenzTwoDateInHour(Timestamp after, Timestamp before){
        long milliseconds = Math.abs(after.getTime() - before.getTime());
        long seconds = milliseconds / 1000;
        int hours = (int)seconds / 3600;
        return hours;
    }

    private int getDifferenzTwoDateInDay(Timestamp after, Timestamp before){
        long milliseconds = Math.abs(after.getTime() - before.getTime());
        long seconds = milliseconds / 1000;
        int hours = (int)seconds / 3600;
        int days = hours / 24;
        return days;
    }

}
