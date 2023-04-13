package model;

public class DogOwner extends UserInfo{


    private String address;
    private String phoneNumber;
    private String description;

    public DogOwner(String address, String phoneNumber, String description) {


        this.address = address;
        this.phoneNumber = phoneNumber;
        this.description = description;

    }

    public DogOwner(String firstName, String lastname, String email, String phoneNumber, String address) {
        super(firstName, lastname, email);
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }




    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




}
