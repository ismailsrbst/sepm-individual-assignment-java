package at.ac.tuwien.sepm.assignment.individual.booking.ui;

import at.ac.tuwien.sepm.assignment.individual.booking.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.booking.entities.BookingVehicle;
import at.ac.tuwien.sepm.assignment.individual.booking.entities.Status;
import at.ac.tuwien.sepm.assignment.individual.booking.service.BookingVehicleService;
import at.ac.tuwien.sepm.assignment.individual.booking.exception.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class InvoiceDetailsController implements Initializable {

    @FXML
    private TextField tf_customerName;

    @FXML
    private TextField tf_startDate;

    @FXML
    private TextField tf_payNember;

    @FXML
    private TextField tf_totalPrice;

    @FXML
    private TextField tf_endDate;

    @FXML
    private TextField tf_createDate;

    @FXML
    private TableView<BookingVehicle> tv_editBooking;

    @FXML
    private TableColumn<BookingVehicle, String> tc_model;

    @FXML
    private TableColumn<BookingVehicle, Integer> tc_price;

    @FXML
    private Label lb_invoiceNumber;

    @FXML
    private Label lb_invoiceDate;

    @FXML
    private TextField tf_invoiceDate;

    @FXML
    private TextField tf_invoiceNumber;

    @FXML
    private Button bt_deleteVehicle;

    @FXML
    private Button bt_edit;

    private final static Logger logger = LoggerFactory.getLogger(InvoiceDetailsController.class);

    private MainWindowController controller;
    private BookingVehicleService bookingVehicleService;
    private List<BookingVehicle> bookingVehicle;
    private Booking booking;

    public InvoiceDetailsController(MainWindowController controller, Booking booking, List<BookingVehicle> bookingVehicle, BookingVehicleService bookingVehicleService){
        this.controller = controller;
        this.bookingVehicle = bookingVehicle;
        this.bookingVehicleService = bookingVehicleService;
        this.booking = booking;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            logger.info("initialize, loading booking vehicle to tableview.");
            ObservableList<BookingVehicle> bookingVehicles = FXCollections.observableArrayList(bookingVehicleService.getBookingVehicleByBooking(booking));

            tc_model.setCellValueFactory(new PropertyValueFactory<BookingVehicle, String >("model"));
            tc_price.setCellValueFactory(new PropertyValueFactory<BookingVehicle, Integer>("basePrice"));

            tv_editBooking.setItems(bookingVehicles);
            tv_editBooking.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

            tf_customerName.setText(booking.getCustomerName());
            tf_payNember.setText(booking.getPayNumber());
            tf_startDate.setText(booking.getBeginnDate().toString());
            tf_endDate.setText(booking.getEndDate().toString());
            tf_totalPrice.setText(booking.getTotalPrice()+"");
            tf_createDate.setText(booking.getCreateDate().toString());
            if (booking.getStatus() != Status.OPEN) {
                tf_invoiceDate.setText(booking.getInvoiceDate().toString());
                tf_invoiceNumber.setText(booking.getInvoiceNumber() + "");
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        }
    }

    @FXML
    void deleteVehicleButtonCliked(ActionEvent event) {

    }

    @FXML
    void editButtonCliked(ActionEvent event) {

    }

    public TextField getTf_customerName() {
        return tf_customerName;
    }

    public TextField getTf_payNember() {
        return tf_payNember;
    }

    public TextField getTf_invoiceDate() {
        return tf_invoiceDate;
    }

    public TextField getTf_invoiceNumber() {
        return tf_invoiceNumber;
    }

    public Label getLb_invoiceDate() {
        return lb_invoiceDate;
    }

    public Label getLb_invoiceNumber() {
        return lb_invoiceNumber;
    }

    public Button getBt_edit() {
        return bt_edit;
    }

    public Button getBt_deleteVehicle() {
        return bt_deleteVehicle;
    }
}
