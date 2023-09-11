package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customer {

    public int customerID, zip, addressID;
    public String customer, address, address2, city, country, phone;
    public byte active;

    public Customer() {

    }

    public Customer(int customerID, String customer, String address, String address2, int addressID, String city, int zip, String country, String phone) {
        this.customerID = customerID;
        this.customer = customer;
        this.address = address;
        this.address2 = address2;
        this.addressID = addressID;
        this.city = city;
        this.country = country;
        this.zip = zip;
        this.phone = phone;

    }

    public int getCustomerID() {
        return this.customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomer() {
        return this.customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return this.address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getZip() {
        return this.zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public byte getActive() {
        return this.active;
    }

    public void setActive(byte active) {
        this.active = active;
    }

    public int getAddressID() {
        return this.addressID;
    }

    public void setAddressID(int ID) {
        this.addressID = ID;
    }

}
