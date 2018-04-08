package at.ac.tuwien.sepm.assignment.individual.booking.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Booking {

    private Integer bid, totalPrice, invoiceNumber;
    private String customerName, payNumber;
    private Timestamp beginnDate, endDate, createDate, editDate, invoiceDate;
    private PayTyp payTyp;
    private Status status;
    private List<BookingVehicle> bookingVehicles;

    public Booking(){
        this.bookingVehicles = new ArrayList<BookingVehicle>();
    }

    public List<BookingVehicle> getBookingVehicles() {
        return bookingVehicles;
    }

    public void setBookingVehicles(List<BookingVehicle> bookingVehicles) {
        this.bookingVehicles = bookingVehicles;
    }

    public void addBookingVehicles(BookingVehicle bookingVehicle){
        this.bookingVehicles.add(bookingVehicle);
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPayNumber() {
        return payNumber;
    }

    public void setPayNumber(String payNumber) {
        this.payNumber = payNumber;
    }

    public Timestamp getBeginnDate() {
        return beginnDate;
    }

    public void setBeginnDate(Timestamp beginnDate) {
        this.beginnDate = beginnDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getEditDate() {
        return editDate;
    }

    public void setEditDate(Timestamp editDate) {
        this.editDate = editDate;
    }

    public PayTyp getPayTyp() {
        return payTyp;
    }

    public void setPayTyp(PayTyp payTyp) {
        this.payTyp = payTyp;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Timestamp getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Timestamp invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
}
