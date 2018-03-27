package at.ac.tuwien.sepm.assignment.individual.main.ui;

import at.ac.tuwien.sepm.assignment.individual.main.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.main.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.main.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.main.exception.UIException;
import at.ac.tuwien.sepm.assignment.individual.main.service.VehicleService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class AddVehicleController implements Initializable{
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
    private Button bt_edit;

    @FXML
    private Button bt_add;


    private MainWindowController controller;
    private VehicleService service;
    //private String imageName = "";
    private Vehicle vehicle = null;
    private File file;

    public AddVehicleController(MainWindowController controller, VehicleService service){
        this.controller = controller;
        this.service = service;
        //vehicle = new Vehicle();
    }
    public AddVehicleController(MainWindowController controller, VehicleService service, Vehicle vehicle){
        this.controller = controller;
        this.service = service;
        this.vehicle = vehicle;

    }

    public void setEditTable(Vehicle vehicle){
        tf_model.setText(vehicle.getModel());
        tf_constructionYear.setText(vehicle.getConstructionYear() + "");
        tf_description.setText(vehicle.getDescription());
        tf_seating.setText(vehicle.getSeating() + "");
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
            e.printStackTrace();
        }
    }

    public Button getBt_edit(){
        return bt_edit;
    }

    public Button getBt_add(){
        return bt_add;
    }


    @FXML
    void addButtonCliked(ActionEvent event) throws DAOException {

        try {
            vehicle = new Vehicle();
            vehicle.setModel(tf_model.getText());
            vehicle.setConstructionYear(Integer.parseInt(tf_constructionYear.getText()));
            vehicle.setDescription(tf_description.getText());
            vehicle.setSeating((Integer.parseInt(tf_seating.getText().isEmpty() ? "0" : tf_seating.getText())));
            vehicle.setPowerUnit(rb_brawn.isSelected() ? "Brawn" : "Motorized");
            vehicle.setDriverLicense(handleOptions(cb_licenseA, cb_licenseB, cb_licenseC));
            vehicle.setPlateNumber(tf_plateNumber.getText());
            vehicle.setPower(Integer.parseInt(tf_power.getText().isEmpty() ? "0" : tf_power.getText()));
            vehicle.setBasePrice(Integer.parseInt(tf_basePrice.getText()));
            if (file == null){
                vehicle.setImageUrl("src\\main\\resources\\Images\\noImageCar.jpeg");
            }else {
                vehicle.setImageUrl(file.getAbsolutePath());
            }

            check();
            service.create(vehicle);

            /*if(tf_image.getText().isEmpty()){
                vehicle.setImageUrl("src\\main\\resources\\Images\\noImageCar.jpeg");
            } else {
                try {
                    copyFile(new File(tf_image.getText()), new File("src\\main\\resources\\Images\\" + imageName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                vehicle.setImageUrl("src\\main\\resources\\Images\\" + imageName);
            }*/

        } catch (ServiceException e){
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        } catch (NumberFormatException e){
            Alert alertError = new Alert(Alert.AlertType.ERROR, "enter in the correct format " + e, ButtonType.OK);
            alertError.showAndWait();
        }catch (UIException e){
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        }
        //tf_image.setText("C:\\Users\\Ismail\\Desktop\\SEPM\\sepm-individual-assignment-java\\src\\main\\resources\\Images\\noImageCar.png");

        controller.loadTable();
    }

    public void check() throws UIException {
        if (tf_model.getText().isEmpty()){
            throw new UIException("Model cannot be empty.");
        }else if(tf_constructionYear.getText().isEmpty()){
            throw new UIException("Construction Year cannot be empty.");
        }else if((cb_licenseA.isSelected() || cb_licenseB.isSelected() || cb_licenseC.isSelected()) && tf_plateNumber.getText().isEmpty()){
            throw new UIException("Plate Number cannot be empty.");
        }else if(!rb_motorized.isSelected() && !rb_brawn.isSelected()){
            throw new UIException("You should select either motorized or brawn.");
        }else if (tf_basePrice.getText().isEmpty()){
            throw new UIException("Base Price cannot be empty.");
        }
    }

    private String handleOptions(CheckBox a, CheckBox b, CheckBox c){
        String mark = "";

        if (a.isSelected()){
            mark += 'A';
        }
        if (b.isSelected()){
            mark += 'B';
        }
        if (c.isSelected()){
            mark += 'C';
        }
        return mark;
    }

    @FXML
    void selectImageButtonCliked(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        //FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Images", "*.JPEG", "*.PNG");
        //FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Image", ".jpeg", ".png");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images", "*.JPEG", "*.PNG"));
        fileChooser.setTitle("Select a photo for the article");
        //File file2 = new File("src\\main\\resources\\Images");
        fileChooser.setInitialDirectory(new File("src\\main\\resources\\Images"));
        file = fileChooser.showOpenDialog(null);

        if (file != null){
            //vehicle.setImageUrl(file.getAbsolutePath());
            InputStream is = null;
            try {
                //is = new FileInputStream(vehicle.getImageUrl());
                is = new FileInputStream(file.getAbsolutePath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Image image= new Image(is);
            //File file = new File(vehicle.getImageUrl());
            long filesizeMB= file.length() / 1024 / 1024;
            String url = "";
            if(image.getHeight()<500 || image.getWidth()<500){
                Alert alert=new Alert(Alert.AlertType.WARNING,"Image is too small. \nIt must be at least 500x500.");
                alert.showAndWait();
                url = "src\\main\\resources\\Images\\noImageCar.jpeg";
                //vehicle.setImageUrl("src\\main\\resources\\Images\\noImageCar.jpeg");
                //tf_imageUrl.setText("No-Image-Placeholder.jpg");
            }
            if(filesizeMB >= 5){
                Alert alert=new Alert(Alert.AlertType.WARNING,"Image is too big. Maximum is 5MB.");
                alert.showAndWait();
                url = "src\\main\\resources\\Images\\noImageCar.jpeg";
                //vehicle.setImageUrl("src\\main\\resources\\Images\\noImageCar.jpeg");
                //tf_imageUrl.setText("No-Image-Placeholder.jpg");
            }
            try {
                if (url.equals("")) {
                    iv_image.setImage(new Image(new FileInputStream(file.getAbsolutePath())));
                } else {
                    iv_image.setImage(new Image(new FileInputStream(url)));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Files.copy(file.toPath(), new File("src\\main\\resources\\Images\\" + file.getName()).toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*tf_image.setText(file.getPath());
            imageName = file.getName();
            InputStream is = null;
            try {
                is = new FileInputStream(tf_image.getText());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Image image = new Image("src\\main\\resources\\Images\\" + tf_image.getText());
            Image image = new Image(is);
            long imagelength = (file.length()/1024)/1024;

            if(image.getHeight()<500 || image.getWidth()<500){
                Alert alert=new Alert(Alert.AlertType.WARNING,"Image is too small. \nIt must be at least 500x500.");
                tf_image.setText("");
                alert.showAndWait();
            }
            if(imagelength>=5){
                Alert alert=new Alert(Alert.AlertType.WARNING,"Image is too big. Maximum is 5MB.");
                tf_image.setText("");
                alert.showAndWait();
            }*/

            //image= new Image("src\\main\\resources\\Images\\" + tf_image.getText());
            //iv_image.setImage(image);
        }

    }

    /*private static void copyFile(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }*/

    @FXML
    void editButtonCliked(ActionEvent event) throws DAOException {

        try {
            vehicle.setModel(tf_model.getText());
            vehicle.setConstructionYear(Integer.parseInt(tf_constructionYear.getText()));
            vehicle.setDescription(tf_description.getText());
            vehicle.setSeating((Integer.parseInt(tf_seating.getText().isEmpty() ? "0" : tf_seating.getText())));
            vehicle.setPowerUnit(rb_brawn.isSelected() ? "Brawn" : "Motorized");
            vehicle.setDriverLicense(handleOptions(cb_licenseA, cb_licenseB, cb_licenseC));
            vehicle.setPlateNumber(tf_plateNumber.getText());
            vehicle.setPower(Integer.parseInt(tf_power.getText().isEmpty() ? "0" : tf_power.getText()));
            vehicle.setBasePrice(Integer.parseInt(tf_basePrice.getText()));
            if (file == null){
                vehicle.setImageUrl("src\\main\\resources\\Images\\noImageCar.jpeg");
            }else {
                vehicle.setImageUrl(file.getAbsolutePath());
            }

            check();
            service.update(vehicle);

            /*if(tf_image.getText().isEmpty()){
                vehicle.setImageUrl("src\\main\\resources\\Images\\noImageCar.jpeg");
            } else {
                try {
                    copyFile(new File(tf_image.getText()), new File("src\\main\\resources\\Images\\" + imageName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                vehicle.setImageUrl("src\\main\\resources\\Images\\" + imageName);
            }*/

        } catch (NumberFormatException e){
            Alert alertError = new Alert(Alert.AlertType.ERROR, "enter in the correct format " + e, ButtonType.OK);
            alertError.showAndWait();
        }catch (UIException e){
            Alert alertError = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alertError.showAndWait();
        } catch (DAOException e) {
            e.printStackTrace();
        }
        //tf_image.setText("C:\\Users\\Ismail\\Desktop\\SEPM\\sepm-individual-assignment-java\\src\\main\\resources\\Images\\noImageCar.png");

        controller.loadTable();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (vehicle != null) {
            setEditTable(vehicle);
        }
    }
}
