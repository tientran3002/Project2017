package class_Customer;

import java.io.Serializable;

/**
 * Created by TanNguyen on 03/04/2017.
 */

public class Customer implements Serializable{
    String name, addressStart, address, district, city;
    double weight;
    long phoneNumber;

    public Customer() {
    }

    public Customer(String name, String addressStart, String address, String district, String city, double weight, long phoneNumber) {
        this.name = name;
        this.addressStart = addressStart;
        this.address = address;
        this.district = district;
        this.city = city;
        this.weight = weight;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressStart() {
        return addressStart;
    }

    public void setAddressStart(String addressStart) {
        this.addressStart = addressStart;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}