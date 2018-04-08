package at.ac.tuwien.sepm.assignment.individual.booking.ui;

import at.ac.tuwien.sepm.assignment.individual.booking.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.booking.entities.BookingVehicle;
import at.ac.tuwien.sepm.assignment.individual.booking.entities.SearchFilter;
import at.ac.tuwien.sepm.assignment.individual.booking.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.booking.service.BookingService;
import at.ac.tuwien.sepm.assignment.individual.booking.service.BookingVehicleService;
import at.ac.tuwien.sepm.assignment.individual.booking.service.VehicleService;
import at.ac.tuwien.sepm.assignment.individual.booking.entities.*;
import at.ac.tuwien.sepm.assignment.individual.booking.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.booking.exception.InputException;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

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

    //Statistic
    @FXML
    private CheckBox cb_Astatistic;

    @FXML
    private CheckBox cb_Cstatistic;

    @FXML
    private CheckBox cb_Bstatistic;

    @FXML
    private CheckBox cb_noneStatistic;

    @FXML
    private DatePicker dp_startDateStatistic;

    @FXML
    private DatePicker dp_endDateStatistic;

    @FXML
    private BarChart<String, Integer> barChartId;

    private final static Logger logger = LoggerFactory.getLogger(MainWindowController.class);
    private final VehicleService vehicleService;
    private final BookingService bookingService;
    private final BookingVehicleService bookingVehicleService;

    private boolean checkDate;

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
            logger.info("initialize, loading vehicle and booking to tableview.");
            loadTable();
            loadBooking();
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
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

    public void loadBooking() throws ServiceException{
        ObservableList<Booking> bookings = FXCollections.observableArrayList(bookingService.getAllBooking());
        tc_customerName.setCellValueFactory(new PropertyValueFactory<Booking, String>("customerName"));
        tc_beginnDate.setCellValueFactory(new PropertyValueFactory<Booking, Timestamp>("beginnDate"));
        tc_endDate.setCellValueFactory(new PropertyValueFactory<Booking, Timestamp>("endDate"));
        tc_status.setCellValueFactory(new PropertyValueFactory<Booking, String>("status"));
        tc_totalPrice.setCellValueFactory(new PropertyValueFactory<Booking, Integer>("totalPrice"));

        tv_booking.setItems(bookings);
        tv_booking.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void loadTable() throws ServiceException {
        ObservableList<Vehicle> vehicles = FXCollections.observableArrayList(vehicleService.getAllVehicleList());
        tc_model.setCellValueFactory(new PropertyValueFactory<Vehicle,String>("model"));
        tc_license.setCellValueFactory(new PropertyValueFactory<Vehicle,String>("driverLicense"));
        tc_powerUnit.setCellValueFactory(new PropertyValueFactory<Vehicle,String>("powerUnit"));
        tc_basePrice.setCellValueFactory(new PropertyValueFactory<Vehicle,Integer>("basePrice"));

        tv_vehicle.setItems(vehicles);
        tv_vehicle.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
    void addButtonCliked(ActionEvent event) {

        try {
            logger.info("initialize, loading vehicle to tableview.");
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
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e){
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, "can not load fxmlfile.\n"+e, ButtonType.OK);
            alertError.showAndWait();
        }

    }

    @FXML
    void deleteButtonClicked(ActionEvent event) {
        try {
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
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "You must select a vehicle.");
                alert.showAndWait();
            }
        } catch (ServiceException e){
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        }

    }

    @FXML
    void editButtonClicked(ActionEvent event) {
        List<Vehicle> vehicleList = tv_vehicle.getSelectionModel().getSelectedItems();
        Vehicle vehicle = tv_vehicle.getSelectionModel().getSelectedItem();
        if (vehicleList.size() == 1) {
            if (vehicle != null) {
                try {
                    AddAndEditVehicleController addAndEditVehicleController = new AddAndEditVehicleController(this, vehicleService, vehicle);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/addAndEditVehicle.fxml"));
                    fxmlLoader.setControllerFactory(param -> param.isInstance(addAndEditVehicleController) ? addAndEditVehicleController : null);
                    Parent root = fxmlLoader.load();
                    Scene scene = new Scene(root);
                    stage = new Stage();
                    stage.setTitle("Edit Vehicle");
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    addAndEditVehicleController.getBt_add().setVisible(false);
                    addAndEditVehicleController.getCb_licenseA().setDisable(false);
                    addAndEditVehicleController.getCb_licenseB().setDisable(false);
                    addAndEditVehicleController.getCb_licenseC().setDisable(false);
                    stage.show();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    Alert alertError = new Alert(Alert.AlertType.ERROR, "can not load fxmlfile.\n" + e, ButtonType.OK);
                    alertError.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "You must select a vehicle.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You must select one vehicle.");
            alert.showAndWait();
        }
    }

    private void checkDate() throws InputException {
        if (dp_beginnBooking.getValue() != null ) {
            Timestamp beginnDate = new Timestamp(Date.from(dp_beginnBooking.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            if (currentTime.after(beginnDate)) {
                throw new InputException("Start date can not be before current date.");
            }
        }
        if (dp_endBooking.getValue() != null && dp_beginnBooking.getValue() != null) {
            Timestamp beginDate = new Timestamp(Date.from(dp_beginnBooking.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
            Timestamp endDate = new Timestamp(Date.from(dp_endBooking.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
            beginDate.setTime(beginDate.getTime() + (3600000 * chb_beginBookingHour.getValue()) + (60000 * chb_beginBookingMinute.getValue()));
            endDate.setTime(endDate.getTime() + (3600000 * chb_endBookingHour.getValue()) + (6000 * chb_endBookingMinute.getValue()));

            if (endDate.before(beginDate) || endDate.equals(beginDate)){
                throw new InputException("Start date can not be after or equal end date");
            }

        } else if (dp_beginnBooking.getValue() == null && dp_endBooking.getValue() == null){
            throw new InputException("Date can not empty");
        }
    }

    @FXML
    void searchButtonCliked(ActionEvent event) {
        logger.info("search button clicked.");
        try {
            //checkDate();
            SearchFilter searchFilter = new SearchFilter();
            searchFilter.setModel(tf_description.getText());
            searchFilter.setPower(rb_motorized.isSelected() ? "Motorized" : rb_brawn.isSelected() ? "Brawn" : "");
            searchFilter.setMinPrice(tf_basePriceMin.getText().isEmpty() ? 0 : Integer.parseInt(tf_basePriceMin.getText()));
            searchFilter.setMaxPrice(tf_basePriceMax.getText().isEmpty() ? 999 : Integer.parseInt(tf_basePriceMax.getText()));
            searchFilter.setSeating(tf_seating.getText().isEmpty() ? 0 :Integer.parseInt(tf_seating.getText()));
            searchFilter.setDriverLicense(handleOption());
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
            checkDate = true;
        } catch (ServiceException e){
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        }

    }

    @FXML
    void viewDetailsButtonClicked(ActionEvent event) {
        logger.info("detail view button clicked for vehicle.");
        List<Vehicle> vehicleList = tv_vehicle.getSelectionModel().getSelectedItems();
        Vehicle vehicle = tv_vehicle.getSelectionModel().getSelectedItem();
        if (vehicleList.size() == 1) {
            if (vehicle != null) {
                try {
                    AddAndEditVehicleController addAndEditVehicleController = new AddAndEditVehicleController(this, vehicleService, vehicle);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/addAndEditVehicle.fxml"));
                    fxmlLoader.setControllerFactory(param -> param.isInstance(addAndEditVehicleController) ? addAndEditVehicleController : null);
                    Parent root = fxmlLoader.load();
                    Scene scene = new Scene(root);
                    stage = new Stage();
                    stage.setTitle("Detail Vehicle");
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
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
                    addAndEditVehicleController.getLb_editDate().setText("Edit Date: " + vehicle.getEditDate());
                    addAndEditVehicleController.getLb_createDate().setText("Create Date: " + vehicle.getCreateDate());
                    stage.show();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    Alert alertError = new Alert(Alert.AlertType.ERROR, "can not load fxmlfile.\n" + e, ButtonType.OK);
                    alertError.showAndWait();
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "You must select a vehicle.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You must select one vehicle.");
            alert.showAndWait();
        }
    }

    @FXML
    void bookingButtonClicked(ActionEvent event) {
        logger.info("booking button clicked.");
         try {
            List<Vehicle> vehicleList = tv_vehicle.getSelectionModel().getSelectedItems();
            if (!vehicleList.isEmpty()) {
                 checkDate();
               if (dp_beginnBooking.getValue() != null && dp_endBooking.getValue() != null && checkDate) {
                   checkDate = false;

                    Timestamp beginDate = new Timestamp(Date.from(dp_beginnBooking.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
                    Timestamp endDate = new Timestamp(Date.from(dp_endBooking.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
                    beginDate.setTime(beginDate.getTime() + (3600000 * chb_beginBookingHour.getValue()) + (60000 * chb_beginBookingMinute.getValue()));
                    endDate.setTime(endDate.getTime() + (3600000 * chb_endBookingHour.getValue()) + (6000 * chb_endBookingMinute.getValue()));

                    BookingVehicleController bookingVehicleController = new BookingVehicleController(this, vehicleList, beginDate, endDate);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/bookingVehicle.fxml"));
                    fxmlLoader.setControllerFactory(param -> param.isInstance(bookingVehicleController) ? bookingVehicleController : null);
                    Parent root = fxmlLoader.load();
                    Scene scene = new Scene(root);
                    stage = new Stage();
                    stage.setTitle("Booking Vehicle");
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    bookingVehicleController.getLb_startDate().setText("" + beginDate);
                    bookingVehicleController.getLb_endDate().setText("" + endDate);
                    stage.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING,"You must entry beginn and end date.");
                    alert.showAndWait();
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "You must select a vehicle.");
                alert.showAndWait();
            }
        } catch (InputException e) {
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        } catch (IOException e) {
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, "can not load fxmlfile.\n"+e, ButtonType.OK);
            alertError.showAndWait();
        }
    }

    @FXML
    void cancelButtonCliked(ActionEvent event) {
        logger.info("cancel button clicked.");
        try {
            Booking booking = tv_booking.getSelectionModel().getSelectedItem();
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            if (booking != null && booking.getStatus().name().equals("OPEN") && booking.getBeginnDate().after(currentTime)){
                int hour = getDifferenzTwoDateInHour(booking.getBeginnDate(), currentTime);
                if ( hour < 168 && hour >= 72){
                    booking.setTotalPrice((booking.getTotalPrice()*40)/100);
                    bookingStatusUpdate(booking, currentTime);
                } else if (hour < 72 && hour >= 24){
                    booking.setTotalPrice((booking.getTotalPrice()*75)/100);
                    bookingStatusUpdate(booking, currentTime);
                } else if (hour < 24){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "you can not cancel. you must pay full rental.");
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get() == ButtonType.OK) {
                        System.out.println(booking.getBid());
                        bookingService.delete(booking);
                    }
                } else if (hour >= 168){
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
        } catch (ServiceException e){
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        }
    }

    public void bookingStatusUpdate(Booking booking, Timestamp currentTime) {
        try {
            booking.setInvoiceDate(currentTime);
            booking.setInvoiceNumber(booking.hashCode());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "can not be canceled for free");
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get() == ButtonType.OK) {
                bookingService.cancel(booking);
                loadBooking();
            }
        } catch (ServiceException e){
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        }

    }

    public int getDifferenzTwoDateInHour(Timestamp after, Timestamp before){
        long milliseconds = Math.abs(after.getTime() - before.getTime());
        long seconds = milliseconds / 1000;
        int hours = (int)seconds / 3600;
        return hours;
    }

    @FXML
    void completedButtonCliked(ActionEvent event) {
        logger.info("completed button clicked.");
        try {
            Booking booking = tv_booking.getSelectionModel().getSelectedItem();
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            if (booking != null && booking.getStatus().name().equals("OPEN") && booking.getBeginnDate().before(currentTime)){
                booking.setInvoiceDate(currentTime);
                booking.setInvoiceNumber(booking.hashCode());
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "can not be canceled for free");
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get() == ButtonType.OK) {
                    bookingService.completed(booking);
                    loadBooking();
                }
            } else {
                Alert alert=new Alert(Alert.AlertType.WARNING,"can only be completed if they are open and after the start date");
                alert.showAndWait();
            }
        } catch (ServiceException e){
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        }
    }

    @FXML
    void detailViewButtonCliked(ActionEvent event) {
        logger.info("detail view button clicked for booking.");
        Booking booking = tv_booking.getSelectionModel().getSelectedItem();
        try {
            List<BookingVehicle> bookingVehicle = bookingVehicleService.getBookingVehicleByBooking(booking);
        } catch (ServiceException e){
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        }
        if (booking != null && !booking.getStatus().name().equals("OPEN")){
            try {
                InvoiceDetailsController invoiceDetailsController = new InvoiceDetailsController(this, booking, bookingVehicle, bookingVehicleService);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/editBooking.fxml"));
                fxmlLoader.setControllerFactory(param -> param.isInstance(invoiceDetailsController) ? invoiceDetailsController : null);
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                stage = new Stage();
                stage.setTitle("Detail Reservation");
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                invoiceDetailsController.getTf_customerName().setDisable(true);
                invoiceDetailsController.getTf_payNember().setDisable(true);
                invoiceDetailsController.getTf_invoiceDate().setVisible(true);
                invoiceDetailsController.getTf_invoiceDate().setDisable(true);
                invoiceDetailsController.getTf_invoiceNumber().setVisible(true);
                invoiceDetailsController.getTf_invoiceNumber().setDisable(true);
                invoiceDetailsController.getLb_invoiceDate().setVisible(true);
                invoiceDetailsController.getLb_invoiceNumber().setVisible(true);
                invoiceDetailsController.getBt_edit().setVisible(false);
                invoiceDetailsController.getBt_deleteVehicle().setVisible(false);
                stage.show();
            } catch (IOException e) {
                logger.error(e.getMessage());
                Alert alertError = new Alert(Alert.AlertType.ERROR, "can not load fxmlfile.\n"+e, ButtonType.OK);
                alertError.showAndWait();            }
        }else {
            InvoiceDetailsController invoiceDetailsController = new InvoiceDetailsController(this, booking, bookingVehicle, bookingVehicleService);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/editBooking.fxml"));
            fxmlLoader.setControllerFactory(param -> param.isInstance(invoiceDetailsController) ? invoiceDetailsController : null);
            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                logger.error(e.getMessage());
                Alert alertError = new Alert(Alert.AlertType.ERROR, "can not load fxmlfile.\n"+e, ButtonType.OK);
                alertError.showAndWait();            }
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.setTitle("Detail Booking");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            invoiceDetailsController.getBt_edit().setVisible(false);
            invoiceDetailsController.getBt_deleteVehicle().setVisible(false);
            invoiceDetailsController.getTf_customerName().setDisable(true);
            invoiceDetailsController.getTf_payNember().setDisable(true);
            stage.show();
        }
    }

    @FXML
    void editBookingButtonCliked(ActionEvent event) {
        logger.info("edit button clicked for booking.");
        try {
            Booking booking = tv_booking.getSelectionModel().getSelectedItem();
            List<BookingVehicle> bookingVehicle = bookingVehicleService.getBookingVehicleByBooking(booking);
            if (booking != null && booking.getStatus().name().equals("OPEN")){
                try {
                    InvoiceDetailsController invoiceDetailsController = new InvoiceDetailsController(this, booking, bookingVehicle, bookingVehicleService);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/editBooking.fxml"));
                    fxmlLoader.setControllerFactory(param -> param.isInstance(invoiceDetailsController) ? invoiceDetailsController : null);
                    Parent root = fxmlLoader.load();
                    Scene scene = new Scene(root);
                    stage = new Stage();
                    stage.setTitle("Edit Booking");
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.show();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    Alert alertError = new Alert(Alert.AlertType.ERROR, "can not load fxmlfile.\n"+e, ButtonType.OK);
                    alertError.showAndWait();                }
            } else {
                Alert alertError = new Alert(Alert.AlertType.ERROR, "you can edit only open booking", ButtonType.OK);
                alertError.showAndWait();
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        }
    }


    private void checkLicense() throws InputException {
        if (!cb_Astatistic.isSelected() && !cb_noneStatistic.isSelected() && !cb_Cstatistic.isSelected() && ! cb_Bstatistic.isSelected()){
            throw new InputException("you must select license.");
        }

    }

    @FXML
    void getStatisticMetotClicked(ActionEvent event) {

        if (dp_startDateStatistic.getValue() != null && dp_endDateStatistic.getValue() != null && dp_startDateStatistic.getValue().isBefore(dp_endDateStatistic.getValue())) {
            Timestamp beginDate = new Timestamp(Date.from(dp_startDateStatistic.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
            Timestamp endDate = new Timestamp(Date.from(dp_endDateStatistic.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());

            ObservableList<XYChart.Series<String, Integer>> list = FXCollections.observableArrayList();
            barChartId.getData().clear();
            barChartId.setAnimated(false);
            try {
                checkLicense();
                if (cb_Astatistic.isSelected()) {
                    list.add(getBarChart(beginDate, endDate, 'A'));
                }
                if (cb_Bstatistic.isSelected()) {
                    list.add(getBarChart(beginDate, endDate, 'B'));
                }
                if (cb_Cstatistic.isSelected()) {
                    list.add(getBarChart(beginDate, endDate, 'C'));

                }
                if (cb_noneStatistic.isSelected()) {
                    list.add(getBarChart(beginDate, endDate, (char)0));
                }
                barChartId.getData().addAll(list);
            } catch (InputException e) {
                Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alertError.showAndWait();
            }
        } else {
            Alert alertError = new Alert(Alert.AlertType.ERROR, "you must entry start and end date.", ButtonType.OK);
            alertError.showAndWait();
        }
    }

    private XYChart.Series<String, Integer> getBarChart(Timestamp start, Timestamp end, char license){
        XYChart.Series<String, Integer> chart = new XYChart.Series<>();

        try {

            List<BookingVehicle> bookingVehicleList = bookingVehicleService.getBookingVehicle(start, end, license);
            List<Booking> bookingList = new ArrayList<>();
            if (bookingVehicleList != null || !bookingVehicleList.isEmpty()){
                for (BookingVehicle bookingVehicle : bookingVehicleList){
                    if (!bookingList.isEmpty()){
                        if (!bookingList.contains(bookingVehicle.getBid())){
                            bookingList.add(bookingService.getBookingByBid(bookingVehicle.getBid()));
                        }
                    } else {
                        bookingList.add(bookingService.getBookingByBid(bookingVehicle.getBid()));
                    }
                }
            }

            int countMON = 0, countTUE = 0, countWED = 0, countTHU = 0, countFRI = 0, countSAT = 0, countSUN = 0;

            LocalDateTime startLocalDate = start.toLocalDateTime();
            LocalDateTime endLocalDate = end.toLocalDateTime();
            while (!startLocalDate.isEqual(endLocalDate.plusDays(1))){
                for (BookingVehicle bookingVehicle : bookingVehicleList){
                    for (Booking booking : bookingList){
                        if (booking.getBeginnDate().toLocalDateTime().isBefore(startLocalDate) || booking.getBeginnDate().toLocalDateTime().isEqual(startLocalDate)){
                            DayOfWeek day = startLocalDate.getDayOfWeek();
                            if (day == DayOfWeek.MONDAY){
                                countMON++;
                            } else if (day == DayOfWeek.TUESDAY){
                                countTUE++;
                            } else if (day == DayOfWeek.WEDNESDAY){
                                countWED++;
                            } else if (day == DayOfWeek.THURSDAY){
                                countTHU++;
                            } else if (day == DayOfWeek.FRIDAY){
                                countFRI++;
                            } else if (day == DayOfWeek.SATURDAY){
                                countSAT++;
                            } else if (day == DayOfWeek.SUNDAY){
                                countSUN++;
                            }
                        }
                    }
                }
                startLocalDate = startLocalDate.plusDays(1);
            }
            chart.getData().add(new XYChart.Data<>("MONDAY", countMON));
            chart.getData().add(new XYChart.Data<>("TUESDAY", countTUE));
            chart.getData().add(new XYChart.Data<>("WEDNESDAY", countWED));
            chart.getData().add(new XYChart.Data<>("THURSDAY", countTHU));
            chart.getData().add(new XYChart.Data<>("FRIDAY", countFRI));
            chart.getData().add(new XYChart.Data<>("SATURDAY", countSAT));
            chart.getData().add(new XYChart.Data<>("SUNDAY", countSUN));

        } catch (ServiceException e) {
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        }
        return chart;
    }

    public void exit(){
        this.stage.close();
    }
}

//AT611904300234573201