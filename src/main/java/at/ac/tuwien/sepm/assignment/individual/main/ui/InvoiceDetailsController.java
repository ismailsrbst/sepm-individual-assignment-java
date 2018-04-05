package  at.ac.tuwien.sepm.assignment.individual.main.ui;

import at.ac.tuwien.sepm.assignment.individual.main.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.main.entities.BookingVehicle;
import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.main.service.BookingService;
import at.ac.tuwien.sepm.assignment.individual.main.service.BookingVehicleService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.TimeStringConverter;

import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

public class InvoiceDetailsController implements Initializable {

    @FXML
    private TableView<BookingVehicle> tv_invoiceDetails;

    @FXML
    private Label lb_totalPrice;

    @FXML
    private Label lb_invoiceNumber;

    @FXML
    private Label lb_status;

    @FXML
    private Label lb_invoiceDate;

    @FXML
    private Label lb_startDate;

    @FXML
    private Label lb_endDate;

    @FXML
    private TableColumn<BookingVehicle, String> tc_vehicle;

    @FXML
    private TableColumn<BookingVehicle, Integer> tc_price;

    private MainWindowController controller;
    private BookingVehicleService bookingVehicleService;
    private List<BookingVehicle> bookingVehicle;
    private  Booking booking;

    public InvoiceDetailsController(MainWindowController controller, Booking booking, List<BookingVehicle> bookingVehicle, BookingVehicleService bookingVehicleService){
        this.controller = controller;
        this.bookingVehicle = bookingVehicle;
        this.bookingVehicleService = bookingVehicleService;
        this.booking = booking;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ObservableList<BookingVehicle> bookingVehicles = FXCollections.observableArrayList(bookingVehicleService.getBookingVehicleByBooking(booking));
            /*tc_invoiceNumber.setCellValueFactory(new PropertyValueFactory<Booking, Integer>("invoiceNumber"));
            tc_status.setCellValueFactory(new PropertyValueFactory<Booking, String>("status"));
            tc_startDate.setCellValueFactory(new PropertyValueFactory<Booking, Timestamp>("beginnDate"));
            tc_endDate.setCellValueFactory(new PropertyValueFactory<Booking, Timestamp>("endDate"));
            tc_invoiceTime.setCellValueFactory(new PropertyValueFactory<Booking, Timestamp>("invoiceDate"));*/
            tc_vehicle.setCellValueFactory(new PropertyValueFactory<BookingVehicle, String >("model"));
            tc_price.setCellValueFactory(new PropertyValueFactory<BookingVehicle, Integer>("basePrice"));

            tv_invoiceDetails.setItems(bookingVehicles);

            lb_invoiceNumber.setText("Invoice Number: " + booking.getInvoiceNumber());
            lb_status.setText("Status: " + booking.getStatus());
            lb_startDate.setText("Start Date: " + booking.getBeginnDate());
            lb_endDate.setText("End Date: " + booking.getEndDate());
            lb_invoiceDate.setText("Invoice Date: " + booking.getInvoiceDate());
            lb_totalPrice.setText("Total Price: " + booking.getTotalPrice());


        } catch (DAOException e) {
            e.printStackTrace();
        }

    }
}
