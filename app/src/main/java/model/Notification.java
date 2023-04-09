package model;

import androidx.annotation.NonNull;

import util.Util;

public class Notification{
    private String id;
    private String dogOwnerId;
    private String dogWalkerName;
    private String dogName;
    private String description;
    private String status;
    private String requestStatus;

    public Notification(String id, String dogWalkerName, String dogName, String dogOwnerId, String status, String requestStatus) {
        this.id = id;
        this.dogOwnerId = dogOwnerId;
        this.dogWalkerName = dogWalkerName;
        this.dogName = dogName;
        this.status = status;
        this.requestStatus = requestStatus;


    }

    public Notification(String dogWalkerName, String dogName, String requestStatus) {
        this.dogWalkerName = dogWalkerName;
        this.dogName = dogName;
        this.requestStatus = requestStatus;

    }


    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDogOwnerId() {
        return dogOwnerId;
    }

    public void setDogOwnerId(String dogOwnerId) {
        this.dogOwnerId = dogOwnerId;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDogWalkerName() {
        return dogWalkerName;
    }

    public void setDogWalkerName(String dogWalkerName) {
        this.dogWalkerName = dogWalkerName;
    }

    public String getDogName() {
        return dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    public String getDescription() {
        return this.toString();
    }


    public void setDescription(String description) {
        this.description = this.toString();
    }

    @NonNull
    @Override
    public String toString() {
        if (getRequestStatus().contains(Util.requestStatus.Rejected.toString()))
            return "We really sorry, but " + this.getDogWalkerName() + " " + Util.requestStatus.Rejected.toString().toLowerCase() +
                     " to walk " + this.getDogName();
        else
            return "Heads up! " + this.getDogWalkerName() + " " + Util.requestStatus.Accepted.toString().toLowerCase() +
                    " to walk " + this.getDogName();

    }


}
