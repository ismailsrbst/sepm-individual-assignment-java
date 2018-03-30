package at.ac.tuwien.sepm.assignment.individual.main.ui;

import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.main.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.main.service.BookingService;
import at.ac.tuwien.sepm.assignment.individual.main.service.BookingVehicleService;
import at.ac.tuwien.sepm.assignment.individual.main.service.VehicleService;
import javafx.animation.FadeTransition;
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
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

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
    private TextField tf_powerUnit;

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

    private final VehicleService vehicleService;
    private final BookingService bookingService;
    private final BookingVehicleService bookingVehicleService;


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
        } catch (DAOException e) {
            e.printStackTrace();
        }
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


        List<Vehicle> vehicleList = new ArrayList<Vehicle>();
        vehicleList = tv_vehicle.getSelectionModel().getSelectedItems();
        //Vehicle vehicle = tv_vehicle.getSelectionModel().getSelectedItem();
        if (!vehicleList.isEmpty()){
            for (Vehicle vehicle : vehicleList){
                if (vehicle != null){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this vehicle?");
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get() == ButtonType.OK) {
                        vehicleService.delete(vehicle);
                    }
                }
            }
            loadTable();
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


    @FXML
    void searchButtonCliked(ActionEvent event) {

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
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    void bookingButtonClicked(ActionEvent event) throws IOException {
        List<Vehicle> vehicleList = new ArrayList<Vehicle>();
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
        }
    }

    public void exit(){
        this.stage.close();
    }
}
