package at.ac.tuwien.sepm.assignment.individual.universe.ui;

import at.ac.tuwien.sepm.assignment.individual.universe.persistence.DAOException;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.universe.service.VehicleService;
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

    private final VehicleService vehicleService;


    public MainWindowController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
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
        tc_license.setCellValueFactory(new PropertyValueFactory<Vehicle,String>("marking"));
        tc_powerUnit.setCellValueFactory(new PropertyValueFactory<Vehicle,String>("powerUnit"));
        tc_basePrice.setCellValueFactory(new PropertyValueFactory<Vehicle,Integer>("basePrice"));

        tv_vehicle.setItems(vehicles);
        tv_vehicle.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    void addButtonCliked(ActionEvent event) throws IOException {

        AddVehicleController addVehicleController = new AddVehicleController(this, vehicleService);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/addVehicle.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(addVehicleController) ? addVehicleController : null);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Add Vehicle");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void deleteButtonClicked(ActionEvent event) throws DAOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this vehicle?");

        List<Vehicle> vehicleList = new ArrayList<Vehicle>();
        vehicleList = tv_vehicle.getSelectionModel().getSelectedItems();
        //Vehicle vehicle = tv_vehicle.getSelectionModel().getSelectedItem();
        if (!vehicleList.isEmpty()){
            for (Vehicle vehicle : vehicleList){
                if (vehicle != null){
                    vehicleService.delete(vehicle);
                }
            }
            loadTable();
        }
    }

    @FXML
    void editButtonClicked(ActionEvent event) throws IOException {

        List<Vehicle> vehicleList = new ArrayList<Vehicle>();
        vehicleList = tv_vehicle.getSelectionModel().getSelectedItems();

        if (vehicleList.size() == 1){
            AddVehicleController addVehicleController = new AddVehicleController(this, vehicleService);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/addVehicle.fxml"));
            fxmlLoader.setControllerFactory(param -> param.isInstance(addVehicleController) ? addVehicleController : null);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Add Vehicle");
            stage.setScene(scene);
            stage.show();
        }
    }
}
