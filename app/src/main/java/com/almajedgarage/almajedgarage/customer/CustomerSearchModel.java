package com.almajedgarage.almajedgarage.customer;

/**
 * Created by mg on 12/4/2016.
 */
public class CustomerSearchModel {
    String status;
    String imageone;
    String description;
    String contact;
    String customer;
    String carno;
    String charges;
    String imagetwo;

    public String getImageone() {
        return imageone;
    }

    public void setImageone(String imageone) {
        this.imageone = imageone;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCarno() {
        return carno;
    }

    public void setCarno(String carno) {
        this.carno = carno;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getImagetwo() {
        return imagetwo;
    }

    public void setImagetwo(String imagetwo) {
        this.imagetwo = imagetwo;
    }

    public String getImagethree() {
        return imagethree;
    }

    public void setImagethree(String imagethree) {
        this.imagethree = imagethree;
    }

    String imagethree;
    int id;
    public CustomerSearchModel(int id, String contact, String customer, String carno, String charges, String comments, String status, String imageone, String imagetwo, String imagethree) {

        this.id = id;
        this.status = status;
        this.imageone = imageone;
        this.imagetwo = imagetwo;
        this.imagethree = imagethree;
        this.description = comments;
        this.contact = contact;
        this.customer = customer;
        this.carno = carno;
        this.charges = charges;
    }

    public CustomerSearchModel() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return imageone;
    }

    public void setImage(String image) {
        this.imageone = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
