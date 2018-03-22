package at.ac.tuwien.sepm.assignment.individual.universe.ui;

import at.ac.tuwien.sepm.assignment.individual.universe.persistence.DAOException;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.universe.service.VehicleService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.file.Files;

public class AddVehicleController {
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

    private MainWindowController controller;
    private VehicleService service;
    private String imageName = "";

    public AddVehicleController(MainWindowController controller, VehicleService service){
        this.controller = controller;
        this.service = service;
    }

    @FXML
    void addButtonCliked(ActionEvent event) throws DAOException {
        Vehicle vehicle = new Vehicle();

        vehicle.setModel(tf_model.getText());
        vehicle.setConstructionYear(Integer.parseInt(tf_constructionYear.getText()));
        vehicle.setDescription(tf_description.getText());
        vehicle.setSeating(Integer.parseInt(tf_seating.getText()));
        vehicle.setMarking(handleOptions(cb_licenseA, cb_licenseB, cb_licenseC));
        vehicle.setPowerUnit(rb_brawn.isSelected() ? "Brawn" : "Motorized");
        vehicle.setPower(Integer.parseInt(tf_power.getText()));
        vehicle.setBasePrice(Integer.parseInt(tf_basePrice.getText()));
        //tf_image.setText("C:\\Users\\Ismail\\Desktop\\SEPM\\sepm-individual-assignment-java\\src\\main\\resources\\Images\\noImageCar.png");

        if(tf_image.getText().isEmpty()){
            vehicle.setImageUrl("src\\main\\resources\\Images\\noImageCar.jpeg");
        } else {
            try {
                copyFile(new File(tf_image.getText()), new File("src\\main\\resources\\Images\\" + imageName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            vehicle.setImageUrl("src\\main\\resources\\Images\\" + imageName);
        }

        service.create(vehicle);
        controller.loadTable();
    }

    private String handleOptions(CheckBox a, CheckBox b, CheckBox c){
        String mark = "";
        if (a.isSelected()){
            mark += "License A\n";
        }
        if (b.isSelected()){
            mark += "License B\n";
        }
        if (c.isSelected()){
            mark += "License C";
        }

        if (mark.equals("")){
            return "No License";
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
        File file2 = new File("src\\main\\resources\\Images");
        fileChooser.setInitialDirectory(file2);
        File file = fileChooser.showOpenDialog(null);

        if (file!=null){
            tf_image.setText(file.getPath());
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
            }

            //image= new Image("src\\main\\resources\\Images\\" + tf_image.getText());
            //iv_image.setImage(image);
        }

    }

    private static void copyFile(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }

    @FXML
    void cancelButtonCliked(ActionEvent event) {



    }

}
