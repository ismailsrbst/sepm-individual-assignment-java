package at.ac.tuwien.sepm.assignment.individual.main.entities;

import java.sql.Timestamp;

public class BookingVehicle {

    private Vehicle vehicle;
    private Integer bid, basePrice;
    private String licenseNumber, model;
    private Timestamp licenseCreateDate;

    public Integer getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Integer basePrice) {
        this.basePrice = basePrice;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Timestamp getLicenseCreateDate() {
        return licenseCreateDate;
    }

    public void setLicenseCreateDate(Timestamp licenseCreateDate) {
        this.licenseCreateDate = licenseCreateDate;
    }
}
