package class_Customer;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.BitSet;

/**
 * Created by TanNguyen on 03/04/2017.
 */

public class Customer implements Serializable{
    String name, addressStart, address, district, city;
    double weight;
    long phoneNumber;
    public  Bitmap image;
    int id;

    public Customer() {
    }

    public Customer(String name, String addressStart, String address, String district, String city, double weight, long phoneNumber,Bitmap image,int id) {
        this.name = name;
        this.addressStart = addressStart;
        this.address = address;
        this.district = district;
        this.city = city;
        this.weight = weight;
        this.phoneNumber = phoneNumber;
        this.image=image;
        this.id=id;
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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}