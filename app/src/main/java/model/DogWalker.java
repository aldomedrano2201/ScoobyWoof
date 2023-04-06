package model;

public class DogWalker extends UserInfo{

    private String id;
    private String name;
    private double locationXOnMap;
    private double locationYOnMap;
    private boolean isActive;
    private Integer ranking;
    private String description;
    private String phoneNumber;
    private double rate;





    public DogWalker(String id, double locationXOnMap, double locationYOnMap, Boolean active, Integer ranking, String description,
                     String phoneNumber, String email, Double rate, String firstName, String lastName) {
        super(firstName,lastName,email);
        this.id = id;
        this.name = super.toString();
        this.locationXOnMap = locationXOnMap;
        this.locationYOnMap = locationYOnMap;
        this.isActive = active;
        this.ranking = ranking;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.rate = rate;
    }

    public DogWalker(double locationXOnMap, double locationYOnMap, Boolean active, Integer ranking, String phoneNumber,
                     String description, Double rate) {

        this.locationXOnMap = locationXOnMap;
        this.locationYOnMap = locationYOnMap;
        this.isActive = active;
        this.ranking = ranking;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.rate = rate;
    }




    public String getId() {
        return id;
    }

    public double getLocationXOnMap() {
        return locationXOnMap;
    }

    public void setLocationXOnMap(double locationXOnMap) {
        this.locationXOnMap = locationXOnMap;
    }

    public double getLocationYOnMap() {
        return locationYOnMap;
    }

    public void setLocationYOnMap(double locationYOnMap) {
        this.locationYOnMap = locationYOnMap;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }






}
