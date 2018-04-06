package at.ac.tuwien.sepm.assignment.individual.application;

import at.ac.tuwien.sepm.assignment.individual.main.entities.BookingVehicle;
import at.ac.tuwien.sepm.assignment.individual.main.service.*;
import at.ac.tuwien.sepm.assignment.individual.main.ui.BookingVehicleController;
import at.ac.tuwien.sepm.assignment.individual.main.ui.MainWindowController;
import at.ac.tuwien.sepm.assignment.individual.main.util.DBUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.SQLException;

public final class MainApplication extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void start(Stage primaryStage) throws Exception {
        // setup application
        primaryStage.setTitle("Rent a Vehicle");
        //primaryStage.setWidth(1000);
        //primaryStage.setHeight(155);
        primaryStage.centerOnScreen();
        primaryStage.setOnCloseRequest(event -> {
            try {
                LOG.debug("Application shutdown initiated");
                DBUtil.closeConnection();
            }catch (SQLException ex){
                Alert alert= new Alert(Alert.AlertType.ERROR,"Failed to close Connection");
                alert.showAndWait();
            }
        });

        // initiate service and controller
        VehicleService vehicleService = new VehicleServiceImp();
        BookingService bookingService = new BookingServiceImp();
        BookingVehicleService bookingVehicleService = new BookingVehicleServiceImp();
        MainWindowController mainWindowController = new MainWindowController(vehicleService, bookingService, bookingVehicleService);
        //BookingVehicleController bookingVehicleController = new BookingVehicleController(mainWindowController);

        // prepare fxml loader to inject controller
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/mainWindow.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(mainWindowController) ? mainWindowController : null);
        primaryStage.setScene(new Scene(fxmlLoader.load()));

        // show application
        primaryStage.show();
        primaryStage.toFront();
        LOG.debug("Application startup complete");
    }

    public static void main(String[] args) {
        LOG.debug("Application starting with arguments={}", (Object) args);
        Application.launch(MainApplication.class, args);
    }

}
