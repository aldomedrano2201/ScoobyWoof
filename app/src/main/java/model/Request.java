package model;

import java.io.Serializable;

public class Request implements Serializable {

    private String id;
    private String dogId;
    protected String dogOwnerId;
    private String dogWalkerId;
    private String status;
    private String dateTime;

    public Request(String id, String dogId, String dogOwnerId, String dogWalkerId, String status, String dateTime) {
        this.id = id;
        this.dogId = dogId;
        this.dogOwnerId = dogOwnerId;
        this.dogWalkerId = dogWalkerId;
        this.status = status;
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDogId() {
        return dogId;
    }

    public void setDogId(String dogId) {
        this.dogId = dogId;
    }

    public String getDogOwnerId() {
        return dogOwnerId;
    }

    public void setDogOwnerId(String dogOwnerId) {
        this.dogOwnerId = dogOwnerId;
    }

    public String getDogWalkerId() {
        return dogWalkerId;
    }

    public void setDogWalkerId(String dogWalkerId) {
        this.dogWalkerId = dogWalkerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
