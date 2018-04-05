package at.ac.tuwien.sepm.assignment.individual.main.ui;

import at.ac.tuwien.sepm.assignment.individual.main.entities.*;
import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.main.exception.UIException;
import at.ac.tuwien.sepm.assignment.individual.main.service.BookingService;
import at.ac.tuwien.sepm.assignment.individual.main.service.BookingVehicleService;
import at.ac.tuwien.sepm.assignment.individual.main.service.VehicleService;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.validator.routines.CreditCardValidator;
import org.apache.commons.validator.routines.IBANValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class MainWindowController implements Initializable{

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final double VISIBLE = 1.0;
    private static final double INVISIBLE = 0.0;

    private final FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1));
    private final FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(3));

    @FXML
    private TableView<Vehicle> tv_vehicle;

    @FXML
    private TableColumn<Vehicle, String> tc_model;

    @FXML
    private TableColumn<Vehicle, String> tc_license;

    @FXML
    private TableColumn<Vehicle, String> tc_powerUnit;

    @FXML
    private TableColumn<Vehicle, Integer> tc_basePrice;

    @FXML
    private TextField tf_description;

    @FXML
    private TextField tf_seating;

    @FXML
    private TextField tf_basePriceMin;

    @FXML
    private TextField tf_basePriceMax;

    @FXML
    private DatePicker dp_beginnBooking;

    @FXML
    private DatePicker dp_endBooking;

    @FXML
    private RadioButton rb_motorized;

    @FXML
    private RadioButton rb_brawn;


    @FXML
    private CheckBox cb_a;

    @FXML
    private CheckBox cb_c;

    @FXML
    private CheckBox cb_b;

    @FXML
    private TableView<Booking> tv_booking;

    @FXML
    private TableColumn<Booking, String> tc_customerName;

    @FXML
    private TableColumn<Booking, Timestamp> tc_beginnDate;

    @FXML
    private TableColumn<Booking, Timestamp> tc_endDate;

    @FXML
    private TableColumn<Booking, String> tc_status;

    @FXML
    private TableColumn<Booking, Integer> tc_totalPrice;

    @FXML
    private ChoiceBox<Integer> chb_beginBookingHour;

    @FXML
    private ChoiceBox<Integer> chb_endBookingMinute;

    @FXML
    private ChoiceBox<Integer> chb_endBookingHour;

    @FXML
    private ChoiceBox<Integer> chb_beginBookingMinute;

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

    @FXML
    private TextField tf_customerName;

    @FXML
    private TextField tf_payNumber;

    @FXML
    private TextField tf_licenseNumber;

    @FXML
    private DatePicker dp_licenseCreateDate;

    @FXML
    private Label lb_licenseNumber;

    @FXML
    private Label lb_LicenseCreateDate;

    @FXML
    private Label lb_driverLicense;

    private final VehicleService vehicleService;
    private final BookingService bookingService;
    private final BookingVehicleService bookingVehicleService;

    private static final AtomicInteger count = new AtomicInteger(1000);
    private static int invoiceNumber ;
    private int totalPrice = 0;

    private List<Vehicle> listVehicle = new ArrayList<>();
    private List<BookingVehicle> bookingVehicle = null;

    private Stage stage;


    public MainWindowController(VehicleService vehicleService, BookingService bookingService, BookingVehicleService bookingVehicleService) {
        this.vehicleService = vehicleService;
        this.bookingService = bookingService;
        this.bookingVehicleService = bookingVehicleService;
        // some transitions for a visually appealing effect
        fadeOutTransition.setFromValue(VISIBLE);
        fadeOutTransition.setToValue(INVISIBLE);
        fadeInTransition.setFromValue(INVISIBLE);
        fadeInTransition.setToValue(VISIBLE);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadTable();
            loadBooking();
        } catch (DAOException e) {
            e.printStackTrace();
        }

        checkNumberFormat(tf_basePriceMax);
        checkNumberFormat(tf_basePriceMin);
        checkNumberFormat(tf_seating);

        Integer hour[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23};
        Integer minute[] = {00,15,30,45};
        ObservableList<Integer> hours = FXCollections.observableArrayList(hour);
        ObservableList<Integer> minutes = FXCollections.observableArrayList(minute);

        chb_beginBookingHour.setItems(hours);
        chb_endBookingHour.setItems(hours);
        chb_beginBookingMinute.setItems(minutes);
        chb_endBookingMinute.setItems(minutes);

        chb_beginBookingHour.setValue(0);
        chb_endBookingHour.setValue(0);
        chb_beginBookingMinute.setValue(00);
        chb_endBookingMinute.setValue(00);
    }

    public void checkNumberFormat(TextField textField){
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.isEmpty()){
                    int value = 0;
                }else {
                    if (newValue.matches("((\\d*)|(\\d+\\.\\d*))")) {
                        int value = Integer.parseInt(newValue);
                    } else {
                        textField.setText(oldValue);
                    }
                }
            }
        });
    }

    public void loadBooking() throws DAOException{
        ObservableList<Booking> bookings = FXCollections.observableArrayList(bookingService.getAllBooking());
        tc_customerName.setCellValueFactory(new PropertyValueFactory<Booking, String>("customerName"));
        tc_beginnDate.setCellValueFactory(new PropertyValueFactory<Booking, Timestamp>("beginnDate"));
        tc_endDate.setCellValueFactory(new PropertyValueFactory<Booking, Timestamp>("endDate"));
        tc_status.setCellValueFactory(new PropertyValueFactory<Booking, String>("status"));
        tc_totalPrice.setCellValueFactory(new PropertyValueFactory<Booking, Integer>("totalPrice"));

        tv_booking.setItems(bookings);
        tv_booking.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void loadTable() throws DAOException {
        ObservableList<Vehicle> vehicles = FXCollections.observableArrayList(vehicleService.getAllVehicleList());
        tc_model.setCellValueFactory(new PropertyValueFactory<Vehicle,String>("model"));
        tc_license.setCellValueFactory(new PropertyValueFactory<Vehicle,String>("driverLicense"));
        tc_powerUnit.setCellValueFactory(new PropertyValueFactory<Vehicle,String>("powerUnit"));
        tc_basePrice.setCellValueFactory(new PropertyValueFactory<Vehicle,Integer>("basePrice"));

        tv_vehicle.setItems(vehicles);
        tv_vehicle.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public static int getInvoiceNumber() {
        return invoiceNumber++;
    }

    private String handleOption(){
        String str = "";
        if (cb_a.isSelected()){
            str += "A";
        }
        if (cb_b.isSelected()){
            str += "B";
        }
        if (cb_c.isSelected()){
            str += "C";
        }
        return str;
    }

    @FXML
    void addButtonCliked(ActionEvent event) throws IOException {

        AddAndEditVehicleController addAndEditVehicleController = new AddAndEditVehicleController(this, vehicleService);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/addAndEditVehicle.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(addAndEditVehicleController) ? addAndEditVehicleController : null);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setTitle("Add Vehicle");
        addAndEditVehicleController.getBt_edit().setVisible(false);
        addAndEditVehicleController.getBt_booking().setVisible(false);
        addAndEditVehicleController.getBt_delete().setVisible(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void deleteButtonClicked(ActionEvent event) throws DAOException {
        List<Vehicle> vehicleList = tv_vehicle.getSelectionModel().getSelectedItems();
        if (!vehicleList.isEmpty()){
            for (Vehicle vehicle : vehicleList){
                if (vehicle != null){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete "+vehicle.getModel()+" ?");
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get() == ButtonType.OK) {
                        vehicleService.delete(vehicle);
                    }
                }
            }
            this.loadTable();
        }
    }

    @FXML
    void editButtonClicked(ActionEvent event) throws IOException {

        List<Vehicle> vehicleList = new ArrayList<Vehicle>();
        vehicleList = tv_vehicle.getSelectionModel().getSelectedItems();

        //Vehicle vehicle = tv_vehicle.getSelectionModel().getSelectedItem();

        if (vehicleList.size() == 1){
            for (Vehicle vehicle : vehicleList) {
                AddAndEditVehicleController addAndEditVehicleController = new AddAndEditVehicleController(this, vehicleService, vehicle);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/addAndEditVehicle.fxml"));
                fxmlLoader.setControllerFactory(param -> param.isInstance(addAndEditVehicleController) ? addAndEditVehicleController : null);
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                stage = new Stage();
                stage.setTitle("Edit Vehicle");
                stage.setScene(scene);
                addAndEditVehicleController.getBt_add().setVisible(false);
                stage.show();

            }
            //TODO else kismina popup koy
        }
    }

    private void checkDate() throws UIException {

        if (dp_beginnBooking.getValue() != null ) {
            Timestamp beginnDate = new Timestamp(Date.from(dp_beginnBooking.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            if (currentTime.after(beginnDate)) {
                throw new UIException("Enter date correctly.");
            }
        } else if (dp_endBooking.getValue() != null && dp_beginnBooking.getValue().isAfter(dp_endBooking.getValue())) {
            throw new UIException("Date can not empty");
        } else if (dp_beginnBooking.getValue() == null || dp_endBooking.getValue() == null){
            throw new UIException("Date can not empty");
        }
    }

    @FXML
    void searchButtonCliked(ActionEvent event) {

        try {
            checkDate();
            SearchFilter searchFilter = new SearchFilter();
            searchFilter.setModel(tf_description.getText());
            searchFilter.setPower(rb_motorized.isSelected() ? "Motorized" : rb_brawn.isSelected() ? "Brawn" : "");
            searchFilter.setMinPrice(tf_basePriceMin.getText().isEmpty() ? 0 : Integer.parseInt(tf_basePriceMin.getText()));
            searchFilter.setMaxPrice(tf_basePriceMax.getText().isEmpty() ? 999 : Integer.parseInt(tf_basePriceMax.getText()));
            searchFilter.setSeating(tf_seating.getText().isEmpty() ? 0 :Integer.parseInt(tf_seating.getText()));
            searchFilter.setDriverLicense(handleOption());
           /* if (dp_beginnBooking.getValue() == null && dp_endBooking.getValue() != null){
                Timestamp endDate = new Timestamp(Date.from(dp_endBooking.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
                endDate.setTime(endDate.getTime() + (3600000*chb_endBookingHour.getValue()) + (6000*chb_endBookingMinute.getValue()));
                searchFilter.setEndDate(endDate);
                searchFilter.setStartDate(null);
            } else if (dp_endBooking.getValue() == null && dp_beginnBooking.getValue() != null){
                Timestamp beginDate = new Timestamp(Date.from(dp_beginnBooking.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
                beginDate.setTime(beginDate.getTime() + (3600000*chb_beginBookingHour.getValue()) + (60000*chb_beginBookingMinute.getValue()));
                searchFilter.setStartDate(beginDate);
                searchFilter.setEndDate(null);
            } else if (dp_endBooking.getValue() == null || dp_beginnBooking.getValue() == null){
                searchFilter.setStartDate(null);
                searchFilter.setEndDate(null);
            } else {
                Timestamp beginDate = new Timestamp(Date.from(dp_beginnBooking.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
                Timestamp endDate = new Timestamp(Date.from(dp_endBooking.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
                beginDate.setTime(beginDate.getTime() + (3600000*chb_beginBookingHour.getValue()) + (60000*chb_beginBookingMinute.getValue()));
                endDate.setTime(endDate.getTime() + (3600000*chb_endBookingHour.getValue()) + (6000*chb_endBookingMinute.getValue()));

                searchFilter.setStartDate(beginDate);
                searchFilter.setEndDate(endDate);
            }*/
            if (dp_beginnBooking.getValue() != null && dp_endBooking.getValue() != null) {
                Timestamp beginDate = new Timestamp(Date.from(dp_beginnBooking.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
                Timestamp endDate = new Timestamp(Date.from(dp_endBooking.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
                beginDate.setTime(beginDate.getTime() + (3600000*chb_beginBookingHour.getValue()) + (60000*chb_beginBookingMinute.getValue()));
                endDate.setTime(endDate.getTime() + (3600000*chb_endBookingHour.getValue()) + (6000*chb_endBookingMinute.getValue()));

                searchFilter.setStartDate(beginDate);
                searchFilter.setEndDate(endDate);
            } else if (dp_beginnBooking.getValue() != null && dp_endBooking.getValue() == null){
                Timestamp beginDate = new Timestamp(Date.from(dp_beginnBooking.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
                beginDate.setTime(beginDate.getTime() + (3600000*chb_beginBookingHour.getValue()) + (60000*chb_beginBookingMinute.getValue()));
                searchFilter.setStartDate(beginDate);
            } else if (dp_beginnBooking.getValue() == null && dp_endBooking.getValue() != null){
                Timestamp endDate = new Timestamp(Date.from(dp_endBooking.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
                endDate.setTime(endDate.getTime() + (3600000*chb_endBookingHour.getValue()) + (6000*chb_endBookingMinute.getValue()));
                searchFilter.setEndDate(endDate);
            }


            ObservableList<Vehicle> list = FXCollections.observableArrayList(vehicleService.search(searchFilter));
            tc_model.setCellValueFactory(new PropertyValueFactory<Vehicle,String>("model"));
            tc_license.setCellValueFactory(new PropertyValueFactory<Vehicle,String>("driverLicense"));
            tc_powerUnit.setCellValueFactory(new PropertyValueFactory<Vehicle,String>("powerUnit"));
            tc_basePrice.setCellValueFactory(new PropertyValueFactory<Vehicle,Integer>("basePrice"));

            tv_vehicle.setItems(list);
            tv_vehicle.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        } catch (UIException e){
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        }

    }

    @FXML
    void viewDetailsButtonClicked(ActionEvent event) {
        List<Vehicle> vehicleList = new ArrayList<Vehicle>();
        vehicleList = tv_vehicle.getSelectionModel().getSelectedItems();

        if (vehicleList.size() == 1){
            for (Vehicle vehicle : vehicleList) {
                try {
                    AddAndEditVehicleController addAndEditVehicleController = new AddAndEditVehicleController(this, vehicleService, vehicle);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/addAndEditVehicle.fxml"));
                    fxmlLoader.setControllerFactory(param -> param.isInstance(addAndEditVehicleController) ? addAndEditVehicleController : null);
                    Parent root = fxmlLoader.load();
                    Scene scene = new Scene(root);
                    stage = new Stage();
                    stage.setTitle("Detail Vehicle");
                    stage.setScene(scene);
                    addAndEditVehicleController.getBt_add().setVisible(false);
                    addAndEditVehicleController.getTf_model().setDisable(true);
                    addAndEditVehicleController.getTf_basePrice().setDisable(true);
                    addAndEditVehicleController.getTf_constructionYear().setDisable(true);
                    addAndEditVehicleController.getTf_description().setDisable(true);
                    addAndEditVehicleController.getTf_plateNumber().setDisable(true);
                    addAndEditVehicleController.getTf_power().setDisable(true);
                    addAndEditVehicleController.getTf_seating().setDisable(true);
                    addAndEditVehicleController.getRb_brawn().setDisable(true);
                    addAndEditVehicleController.getRb_motorized().setDisable(true);
                    addAndEditVehicleController.getCb_licenseA().setDisable(true);
                    addAndEditVehicleController.getCb_licenseB().setDisable(true);
                    addAndEditVehicleController.getCb_licenseC().setDisable(true);
                    addAndEditVehicleController.getBt_selectImage().setDisable(true);
                    addAndEditVehicleController.getLb_createDate().setText("Create Date: " + vehicle.getCreateDate());
                    addAndEditVehicleController.getLb_edtDate().setText("Edit Date: " + vehicle.getEditDate());
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    void bookingButtonClicked(ActionEvent event) throws IOException {
        /*List<Vehicle> vehicleList = new ArrayList<Vehicle>();
        vehicleList = tv_vehicle.getSelectionModel().getSelectedItems();

        if (!vehicleList.isEmpty()){
            BookingVehicleController bookingVehicleController = new BookingVehicleController(this, vehicleList, bookingService);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/bookingVehicle.fxml"));
            fxmlLoader.setControllerFactory(param -> param.isInstance(bookingVehicleController) ? bookingVehicleController : null);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.setTitle("Booking Vehicle");
            stage.setScene(scene);
            stage.show();
        }*/

        try {
            List<Vehicle> tmp = tv_vehicle.getSelectionModel().getSelectedItems();
            checkDate();
            Timestamp beginDate = new Timestamp(Date.from(dp_beginnBooking.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
            Timestamp endDate = new Timestamp(Date.from(dp_endBooking.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
            beginDate.setTime(beginDate.getTime() + (3600000*chb_beginBookingHour.getValue()) + (60000*chb_beginBookingMinute.getValue()));
            endDate.setTime(endDate.getTime() + (3600000*chb_endBookingHour.getValue()) + (6000*chb_endBookingMinute.getValue()));
            lb_startDate.setText(beginDate.toString());
            lb_endDate.setText(endDate.toString());
            if (tmp != null){
                for (Vehicle vehicle : tmp){
                    if (vehicle != null){
                        totalPrice += (int) Math.ceil((double) getDifferenzTwoDateInHour(beginDate, endDate)) * vehicle.getBasePrice();
                        listVehicle.add(vehicle);
                    }
                }
            }
            if (listVehicle != null){
                ObservableList<Vehicle> vehicles = FXCollections.observableArrayList(listVehicle);
                tc_modelForBooking.setCellValueFactory(new PropertyValueFactory<Vehicle,String>("model"));
                tc_licenseForBooking.setCellValueFactory(new PropertyValueFactory<Vehicle,String>("driverLicense"));
                tc_basePriceForBooking.setCellValueFactory(new PropertyValueFactory<Vehicle,Integer>("basePrice"));

                tv_bookingVehicle.setItems(vehicles);
                tv_bookingVehicle.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            }
        } catch (UIException e) {

        }

    }

    @FXML
    void cancelButtonCliked(ActionEvent event) throws DAOException {
        Booking booking = tv_booking.getSelectionModel().getSelectedItem();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        if (booking != null && booking.getStatus().name().equals("OPEN") && booking.getBeginnDate().after(currentTime)){
            int hour = getDifferenzTwoDateInHour(currentTime, booking.getBeginnDate());
            if ( hour < 168 && hour >= 72){
                booking.setTotalPrice((booking.getTotalPrice()*40)/100);
                bookingStatusUpdate(booking, currentTime);
            } else if (hour < 72 && hour <= 24){
                booking.setTotalPrice((booking.getTotalPrice()*75)/100);
                bookingStatusUpdate(booking, currentTime);
            } else if (hour < 24){
                Alert alert = new Alert(Alert.AlertType.WARNING,"you can not cancel. you must pay full rental.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "are you sure delete this booking");
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get() == ButtonType.OK) {
                    System.out.println(booking.getBid());
                    bookingService.delete(booking);
                }
            }
            loadBooking();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING,"can only be canceled if they are open or before the start date");
            alert.showAndWait();
        }

    }

    public void bookingStatusUpdate(Booking booking, Timestamp currentTime) throws DAOException {
        booking.setInvoiceDate(currentTime);
        invoiceNumber = count.incrementAndGet();
        booking.setInvoiceNumber(invoiceNumber);
        bookingService.cancel(booking);
    }

    private int getDifferenzTwoDateInHour(Timestamp after, Timestamp before){
        long milliseconds = Math.abs(after.getTime() - before.getTime());
        long seconds = milliseconds / 1000;
        int hours = (int)seconds / 3600;
        return hours;
    }

    @FXML
    void completedButtonCliked(ActionEvent event) throws DAOException {
        Booking booking = tv_booking.getSelectionModel().getSelectedItem();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        if (booking != null && booking.getStatus().name().equals("OPEN") && booking.getBeginnDate().before(currentTime)){
            booking.setInvoiceDate(currentTime);
            invoiceNumber = count.incrementAndGet();
            booking.setInvoiceNumber(invoiceNumber);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "can not be canceled for free");
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get() == ButtonType.OK) {
                bookingService.completed(booking);
                loadBooking();
            }

        } else {
            Alert alert=new Alert(Alert.AlertType.WARNING,"can only be canceled if they are open or after the start date");
            alert.showAndWait();
        }
    }

    @FXML
    void detailViewButtonCliked(ActionEvent event) throws DAOException {
        Booking booking = tv_booking.getSelectionModel().getSelectedItem();
        List<BookingVehicle> bookingVehicle = bookingVehicleService.getBookingVehicleByBooking(booking);
        if (booking != null && !booking.getStatus().name().equals("OPEN")){
            try {
                InvoiceDetailsController invoiceDetailsController = new InvoiceDetailsController(this, booking, bookingVehicle, bookingVehicleService);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/invoiceDetails.fxml"));
                fxmlLoader.setControllerFactory(param -> param.isInstance(invoiceDetailsController) ? invoiceDetailsController : null);
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                stage = new Stage();
                stage.setTitle("Detail Booking");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Alert alert=new Alert(Alert.AlertType.WARNING,"there is no detailed view of the open ones");
            alert.showAndWait();
        }
    }

    @FXML
    void editBookingButtonCliked(ActionEvent event) {

    }

    @FXML
    void finishBookingButtonClicked(ActionEvent event)  {
        //List<Vehicle> vehicleList = tv_bookingVehicle.getSelectionModel().getSelectedItems();

        Booking booking = null;
        try {
            check();
            booking = new Booking();
            Timestamp beginDate = new Timestamp(Date.from(dp_beginnBooking.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
            Timestamp endDate = new Timestamp(Date.from(dp_endBooking.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
            beginDate.setTime(beginDate.getTime() + (3600000*chb_beginBookingHour.getValue()) + (60000*chb_beginBookingMinute.getValue()));
            endDate.setTime(endDate.getTime() + (3600000*chb_endBookingHour.getValue()) + (6000*chb_endBookingMinute.getValue()));


            booking.setCustomerName(tf_customerName.getText());
            booking.setPayNumber(tf_payNumber.getText());
            booking.setStatus(Status.OPEN);
            booking.setBeginnDate(beginDate);
            booking.setEndDate(endDate);
            booking.setTotalPrice(totalPrice);
            booking.setInvoiceDate(null);
            booking.setInvoiceNumber(0);

            if (getDifferenzTwoDateInHour(beginDate, new Timestamp(System.currentTimeMillis())) <= 168){
                //bookingAlert();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "can not be canceled for free");
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get() == ButtonType.OK) {
                    bookingCreate(booking);
                }
            }else {
                //bookingAlert();
                bookingCreate(booking);
            }

        } catch (UIException e) {
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        }
    }

    public void bookingCreate(Booking booking){
        try {
            if (bookingVehicle != null) {
                for (Vehicle vehicle : listVehicle) {
                    if (vehicle.getDriverLicense().equals("") || vehicle.getDriverLicense().equals("B")) {
                        BookingVehicle bookingVehicle1 = new BookingVehicle();
                        bookingVehicle1.setVehicle(vehicle);
                        bookingVehicle1.setLicenseCreateDate(null);
                        bookingVehicle1.setLicenseNumber(null);
                        setBookingVehicle(bookingVehicle1);
                    }
                }
            } else {
                bookingVehicle = new ArrayList<>();
                for (Vehicle vehicle : listVehicle) {
                    if (vehicle.getDriverLicense().equals("") || vehicle.getDriverLicense().equals("B")) {
                        BookingVehicle bookingVehicle1 = new BookingVehicle();
                        bookingVehicle1.setVehicle(vehicle);
                        bookingVehicle1.setLicenseCreateDate(null);
                        bookingVehicle1.setLicenseNumber(null);
                        setBookingVehicle(bookingVehicle1);
                    }
                }
            }
            if (bookingVehicle != null && bookingVehicle.size() == listVehicle.size()) {
                bookingService.create(booking);
                for (BookingVehicle bookingVehicle1 : bookingVehicle) {
                    bookingVehicle1.setBid(booking.getBid());
                    bookingVehicleService.create(bookingVehicle1);
                }
                tf_customerName.setText("");
                tf_payNumber.setText("");
                tv_bookingVehicle.setItems(null);
                loadBooking();
            } else {
                Alert alertError = new Alert(Alert.AlertType.ERROR, "you forgot write license Number and date", ButtonType.OK);
                alertError.showAndWait();
            }
        } catch (DAOException e){
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        } catch (SQLException e) {
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        }
    }

    public void bookingAlert(){
        for (Vehicle vehicle : listVehicle){
            if (bookingVehicle != null) {
                for (BookingVehicle bookingVehicle1 : bookingVehicle) {
                    if (vehicle.getVid() == bookingVehicle1.getVehicle().getVid()) {
                        if ((vehicle.getDriverLicense().contains("A") || vehicle.getDriverLicense().contains("C")) && (bookingVehicle1.getLicenseNumber() == null || bookingVehicle1.getLicenseCreateDate() == null)) {
                            Alert alert = new Alert(Alert.AlertType.WARNING, "License number can not be empty.");
                            alert.showAndWait();
                        }
                    }
                }
            }
        }
    }


    public void setLicenseInfo(boolean bool){
        lb_driverLicense.setVisible(bool);
        lb_LicenseCreateDate.setVisible(bool);
        lb_licenseNumber.setVisible(bool);
        tf_licenseNumber.setVisible(bool);
        dp_licenseCreateDate.setVisible(bool);
        cb_a.setVisible(bool);
        cb_b.setVisible(bool);
        cb_c.setVisible(bool);
    }

    public void checkLicenseInfo() throws UIException {
        if (tf_licenseNumber.getText() == null || tf_licenseNumber.getText().isEmpty()){
            throw new UIException("License number can not be empty.");
        } else if (dp_licenseCreateDate.getValue() == null){
            throw new UIException("Enter date correctly.");
        }
        Timestamp licenseDate = new Timestamp(Date.from(dp_licenseCreateDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        long milliseconds = (currentTime.getTime() - licenseDate.getTime());
        long seconds = milliseconds / 1000;
        int hours = (int)seconds / 3600;
        int days = hours / 24;

        if (currentTime.before(licenseDate)){
            throw new UIException("Enter date correctly.");
        } else if ((cb_a.isSelected() || cb_c.isSelected()) && days < 1095){
            throw new UIException("your license date should before 3 year");
        }
    }

    @FXML
    void tv_bookingVehicleOnMouseCliked(MouseEvent event) {
        Vehicle vehicle = tv_bookingVehicle.getSelectionModel().getSelectedItem();
        if (bookingVehicle != null) {
            if (bookingVehicle.isEmpty()){
                if ((vehicle.getDriverLicense().contains("A") || vehicle.getDriverLicense().contains("C"))){
                    setLicenseInfo(true);
                    if (!vehicle.getDriverLicense().contains("A")){
                        cb_a.setVisible(false);
                    }
                    if (!vehicle.getDriverLicense().contains("B")){
                        cb_b.setVisible(false);
                    }
                    if (!vehicle.getDriverLicense().contains("C")){
                        cb_c.setVisible(false);
                    }
                } else {
                    setLicenseInfo(false);
                }
            }else {
                boolean b = true;
                for (BookingVehicle bookingVehicle1 : bookingVehicle) {
                    if (bookingVehicle1 != null && vehicle.getVid() == bookingVehicle1.getVehicle().getVid()) {
                        b = false;
                        setLicenseInfo(false);
                        Alert alert = new Alert(Alert.AlertType.WARNING, "you have already  license number and date.");
                        alert.showAndWait();
                        break;
                    }
                }
                if (b && (vehicle.getDriverLicense().contains("A") || vehicle.getDriverLicense().contains("C"))) {
                    setLicenseInfo(true);
                    if (!vehicle.getDriverLicense().contains("A")){
                        cb_a.setVisible(false);
                    }
                    if (!vehicle.getDriverLicense().contains("B")){
                        cb_b.setVisible(false);
                    }
                    if (!vehicle.getDriverLicense().contains("C")){
                        cb_c.setVisible(false);
                    }
                } else {
                    setLicenseInfo(false);
                }
            }
        } else {
            bookingVehicle = new ArrayList<>();
            if ((vehicle.getDriverLicense().contains("A") || vehicle.getDriverLicense().contains("C"))){
                setLicenseInfo(true);
                if (!vehicle.getDriverLicense().contains("A")){
                    cb_a.setVisible(false);
                }
                if (!vehicle.getDriverLicense().contains("B")){
                    cb_b.setVisible(false);
                }
                if (!vehicle.getDriverLicense().contains("C")){
                    cb_c.setVisible(false);
                }
            } else {
                setLicenseInfo(false);
            }
        }
    }

    @FXML
    void addLicenseNumberAndDateButtonCliked(ActionEvent event) {

        Vehicle vehicle = tv_bookingVehicle.getSelectionModel().getSelectedItem();
        if (tf_licenseNumber.isVisible() && dp_licenseCreateDate.isVisible()) {
            BookingVehicle bookingVehicle1 = new BookingVehicle();
            if (!cb_b.isSelected()) {
                try {
                    checkLicenseInfo();
                    bookingVehicle1.setVehicle(vehicle);
                    bookingVehicle1.setLicenseNumber(tf_licenseNumber.getText());
                    Timestamp licenseCreateDate = new Timestamp(Date.from(dp_licenseCreateDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
                    bookingVehicle1.setLicenseCreateDate(licenseCreateDate);

                } catch (UIException e) {
                    Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                    alertError.showAndWait();
                }
                setBookingVehicle(bookingVehicle1);
            } else {
                bookingVehicle1.setVehicle(vehicle);
                bookingVehicle1.setLicenseNumber(null);
                bookingVehicle1.setLicenseCreateDate(null);

                setBookingVehicle(bookingVehicle1);
            }
        } else {
            Alert alert=new Alert(Alert.AlertType.WARNING,"you dont need to write license number or date.");
            alert.showAndWait();

        }
        setLicenseInfo(false);

        /*if (bookingVehicle != null){
            boolean b = true;
            for (BookingVehicle bookingVehicle1 : bookingVehicle){
                if ((vehicle.getDriverLicense().contains("A") || vehicle.getDriverLicense().contains("C")) && (bookingVehicle1.getLicenseNumber() ==null || bookingVehicle1.getLicenseCreateDate() != null)){
                    b = false;
                }
            }
            if (!b || ((vehicle.getDriverLicense().contains("A") || vehicle.getDriverLicense().contains("C")&& b))){
                Stage stage = new Stage();
                LicenseController licenseController = new LicenseController(this, vehicle, stage);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/licenseNumberAndDate.fxml"));
                fxmlLoader.setControllerFactory(param -> param.isInstance(licenseController) ? licenseController : null);
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

                BookingVehicle bookingVehicle1 = new BookingVehicle();
                bookingVehicle1.setVehicle(vehicle);
                bookingVehicle1.setLicenseNumber(licenseController.getTf_licenseNumber().getText());
                Timestamp licenseCreateDate = new Timestamp(Date.from(licenseController.getDp_licenseCreateDate().getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
                bookingVehicle1.setLicenseCreateDate(licenseCreateDate);
                setBookingVehicle(bookingVehicle1);
            } else {
                Alert alert=new Alert(Alert.AlertType.WARNING,"you dont need to write license number or date.");
                alert.showAndWait();
            }
        }*/
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

    public List<BookingVehicle> getBookingVehicle() {
        return bookingVehicle;
    }
    public void setBookingVehicle(BookingVehicle bookingVehicle){
        this.bookingVehicle.add(bookingVehicle);
    }

    public void exit(){
        this.stage.close();
    }
}
