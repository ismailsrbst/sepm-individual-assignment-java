package at.ac.tuwien.sepm.assignment.individual.main.ui;

import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.main.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.main.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.main.exception.UIException;
import at.ac.tuwien.sepm.assignment.individual.main.persistence.BookingDAOImp;
import at.ac.tuwien.sepm.assignment.individual.main.service.VehicleService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddAndEditVehicleController implements Initializable{

    @FXML
    private TextField tf_model;

    @FXML
    private TextField tf_constructionYear;

    @FXML
    private TextField tf_description;

    @FXML
    private TextField tf_seating;

    @FXML
    private TextField tf_basePrice;

    @FXML
    private TextField tf_power;

    @FXML
    private TextField tf_plateNumber;

    @FXML
    private RadioButton rb_motorized;

    @FXML
    private RadioButton rb_brawn;

    @FXML
    private CheckBox cb_licenseA;

    @FXML
    private CheckBox cb_licenseB;

    @FXML
    private CheckBox cb_licenseC;

    @FXML
    private TextField tf_image;

    @FXML
    private ImageView iv_image;

    @FXML
    private Label lb_plateNumber;

    @FXML
    private Label lb_driverLicense;

    @FXML
    private Label lb_power;

    @FXML
    private Label lb_createDate;

    @FXML
    private Label lb_edtDate;

    @FXML
    private Button bt_edit;

    @FXML
    private Button bt_add;

    @FXML
    private Button bt_booking;

    @FXML
    private Button bt_delete;

    @FXML
    private Button bt_selectImage;

    private final static Logger logger = LoggerFactory.getLogger(AddAndEditVehicleController.class);

    private MainWindowController controller;
    private VehicleService service;
    //private String imageName = "";
    private Vehicle vehicle = null;
    private File file;

    public AddAndEditVehicleController(MainWindowController controller, VehicleService service){
        this.controller = controller;
        this.service = service;
        //vehicle = new Vehicle();
    }
    public AddAndEditVehicleController(MainWindowController controller, VehicleService service, Vehicle vehicle){
        this.controller = controller;
        this.service = service;
        this.vehicle = vehicle;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (vehicle != null) {
            setEditTable(vehicle);
            cb_licenseA.setDisable(true);
            cb_licenseB.setDisable(true);
            cb_licenseC.setDisable(true);
        }

        controller.checkNumberFormat(tf_constructionYear);
        controller.checkNumberFormat(tf_seating);
        controller.checkNumberFormat(tf_power);
        controller.checkNumberFormat(tf_basePrice);
    }

    public void check() throws UIException {
        if (tf_model.getText().isEmpty()){
            throw new UIException("Model cannot be empty.");
        }else if(tf_constructionYear.getText().isEmpty()){
            throw new UIException("Construction Year cannot be empty.");
        }else if(Integer.parseInt(tf_constructionYear.getText()) < 1930 || Integer.parseInt(tf_constructionYear.getText()) > 2018){
            throw new UIException("Construction Year 1930-2018");
        }else if((cb_licenseA.isSelected() || cb_licenseB.isSelected() || cb_licenseC.isSelected()) && tf_plateNumber.getText().isEmpty()){
            throw new UIException("Plate Number cannot be empty.");
        }else if(rb_motorized.isSelected() && tf_power.getText().isEmpty()){
            throw new UIException("Power Number cannot be empty.");
        }else if (tf_basePrice.getText().isEmpty()){
            throw new UIException("Base Price cannot be empty.");
        }
    }


    private String handleOptions(){
        String mark = "";
        if (cb_licenseA.isSelected()){
            mark += 'A';
        }
        if (cb_licenseB.isSelected()){
            mark += 'B';
        }
        if (cb_licenseC.isSelected()){
            mark += 'C';
        }
        return mark;
    }

    @FXML
    void addButtonCliked(ActionEvent event) {
        try {
            check();
            vehicle = new Vehicle();
            vehicle.setModel(tf_model.getText());
            vehicle.setConstructionYear(Integer.parseInt(tf_constructionYear.getText()));
            vehicle.setDescription(tf_description.getText());
            vehicle.setSeating((Integer.parseInt(tf_seating.getText().isEmpty() ? "0" : tf_seating.getText())));
            vehicle.setPowerUnit(rb_brawn.isSelected() ? "Brawn" : "Motorized");
            vehicle.setDriverLicense(handleOptions());
            vehicle.setPlateNumber(tf_plateNumber.getText());
            vehicle.setPower(Integer.parseInt(tf_power.getText().isEmpty() ? "0" : tf_power.getText()));
            vehicle.setBasePrice(Integer.parseInt(tf_basePrice.getText()));
            if (file == null){
                vehicle.setImageUrl("src\\main\\resources\\Images\\noImageCar.jpeg");
            }else {
                vehicle.setImageUrl(file.getAbsolutePath());
            }
            service.create(vehicle);
            controller.loadTable();
        } catch (ServiceException e){
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        } catch (UIException e){
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        }
    }

    @FXML
    void selectImageButtonCliked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images", "*.JPEG", "*.PNG"));
        fileChooser.setTitle("Select a photo for the article");
        fileChooser.setInitialDirectory(new File("src\\main\\resources\\Images"));
        file = fileChooser.showOpenDialog(null);

        if (file != null){
            InputStream is = null;
            try {
                is = new FileInputStream(file.getAbsolutePath());
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage());
                Alert alertError = new Alert(Alert.AlertType.ERROR, "File can not open.\n"+e, ButtonType.OK);
                alertError.showAndWait();            }
            if (is != null){
                Image image= new Image(is);
                long filesizeMB = file.length() / 1024 / 1024;
                String url = "";
                if(image.getHeight()<500 || image.getWidth()<500){
                    Alert alert=new Alert(Alert.AlertType.WARNING,"Image is too small. \nIt must be at least 500x500.");
                    alert.showAndWait();
                    url = "src\\main\\resources\\Images\\noImageCar.jpeg";
                }
                if(filesizeMB >= 5){
                    Alert alert=new Alert(Alert.AlertType.WARNING,"Image is too big. Maximum is 5MB.");
                    alert.showAndWait();
                    url = "src\\main\\resources\\Images\\noImageCar.jpeg";
                }
                try {
                    if (url.equals("")) {
                        iv_image.setImage(new Image(new FileInputStream(file.getAbsolutePath())));
                    } else {
                        iv_image.setImage(new Image(new FileInputStream(url)));
                    }
                } catch (FileNotFoundException e) {
                    logger.error(e.getMessage());
                    Alert alertError = new Alert(Alert.AlertType.ERROR, "Image not shown.\n"+e, ButtonType.OK);
                    alertError.showAndWait();
                }
                try {
                    File file2 = new File("src\\main\\resources\\Images\\" + file.getName());
                    if (file.getName().compareTo(file2.getName()) != 0) {
                        Files.copy(file.toPath(), file2.toPath());
                    }
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    Alert alertError = new Alert(Alert.AlertType.ERROR, "can not copy file.\n"+e, ButtonType.OK);
                    alertError.showAndWait();                }
            }
        }

    }

    @FXML
    void editButtonCliked(ActionEvent event) throws DAOException {

        try {
            check();
            vehicle.setModel(tf_model.getText());
            vehicle.setConstructionYear(Integer.parseInt(tf_constructionYear.getText()));
            vehicle.setDescription(tf_description.getText());
            vehicle.setSeating((Integer.parseInt(tf_seating.getText().isEmpty() ? "0" : tf_seating.getText())));
            vehicle.setPowerUnit(rb_brawn.isSelected() ? "Brawn" : "Motorized");
            vehicle.setDriverLicense(handleOptions());
            vehicle.setPlateNumber(tf_plateNumber.getText());
            vehicle.setPower(Integer.parseInt(tf_power.getText().isEmpty() ? "0" : tf_power.getText()));
            vehicle.setBasePrice(Integer.parseInt(tf_basePrice.getText()));
            if (file == null){
                vehicle.setImageUrl("src\\main\\resources\\Images\\noImageCar.jpeg");
            }else {
                vehicle.setImageUrl(file.getAbsolutePath());
            }
            service.update(vehicle);
            controller.loadTable();
        } catch (UIException e){
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        }
    }

    @FXML
    void bookingButtonCliked(ActionEvent event) {

    }

    @FXML
    void deleteButtonCliked(ActionEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this vehicle?");
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get() == ButtonType.OK) {
                service.delete(vehicle);
                controller.loadTable();
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        }
    }

    public void setEditTable(Vehicle vehicle){
        tf_model.setText(vehicle.getModel());
        tf_constructionYear.setText(vehicle.getConstructionYear() + "");
        tf_description.setText(vehicle.getDescription());
        tf_seating.setText(vehicle.getSeating() == 0 ? "" : vehicle.getSeating() + "");
        if (vehicle.getPowerUnit().equals("Motorized")){
            rb_motorized.setSelected(true);
            rb_brawn.setSelected(false);
        }else {
            rb_motorized.setSelected(false);
            rb_brawn.setSelected(true);
        }
        if (vehicle.getDriverLicense().contains("A")){
            cb_licenseA.setSelected(true);
        }
        if (vehicle.getDriverLicense().contains("B")){
            cb_licenseB.setSelected(true);
        }
        if (vehicle.getDriverLicense().contains("C")){
            cb_licenseC.setSelected(true);
        }
        tf_plateNumber.setText(vehicle.getPlateNumber());
        tf_power.setText(vehicle.getPower() + "");
        tf_basePrice.setText(vehicle.getBasePrice() + "");

        try {
            iv_image.setImage(new Image(new FileInputStream(vehicle.getImageUrl())));
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            Alert alertError = new Alert(Alert.AlertType.ERROR, "Image not shown.\n"+e, ButtonType.OK);
            alertError.showAndWait();
        }
    }

    public Button getBt_edit(){
        return bt_edit;
    }

    public Button getBt_add(){
        return bt_add;
    }

    public Button getBt_booking() {
        return bt_booking;
    }

    public Button getBt_delete() {
        return bt_delete;
    }

    public Button getBt_selectImage(){
        return bt_selectImage;
    }

    public TextField getTf_model() {
        return tf_model;
    }

    public TextField getTf_constructionYear() {
        return tf_constructionYear;
    }

    public TextField getTf_description() {
        return tf_description;
    }

    public TextField getTf_seating() {
        return tf_seating;
    }

    public TextField getTf_basePrice() {
        return tf_basePrice;
    }

    public TextField getTf_power() {
        return tf_power;
    }

    public TextField getTf_plateNumber() {
        return tf_plateNumber;
    }

    public RadioButton getRb_motorized() {
        return rb_motorized;
    }

    public RadioButton getRb_brawn() {
        return rb_brawn;
    }

    public CheckBox getCb_licenseA() {
        return cb_licenseA;
    }

    public CheckBox getCb_licenseB() {
        return cb_licenseB;
    }

    public CheckBox getCb_licenseC() {
        return cb_licenseC;
    }

    public Label getLb_createDate() {
        return lb_createDate;
    }

    public Label getLb_edtDate(){
        return lb_edtDate;
    }
}
