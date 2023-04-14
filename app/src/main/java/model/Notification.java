package model;

import androidx.annotation.NonNull;

import util.Util;

public class Notification{
    private String dogWalkerId;
    private String dogOwnerId;
    private String dogWalkerName;
    private String dogName;
    private String description;
    private String status;
    private String requestStatus;

    public Notification(String dogWalkerId, String dogWalkerName, String dogName, String dogOwnerId, String status, String requestStatus) {
        this.dogWalkerId = dogWalkerId;
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

    public Notification(String dogWalkerId, String dogWalkerName, String dogName, String requestStatus) {
        this.dogWalkerId = dogWalkerId;
        this.dogWalkerName = dogWalkerName;
        this.dogName = dogName;
        this.requestStatus = requestStatus;
    }

    public String getDogWalkerId() {
        return dogWalkerId;
    }

    public void setDogWalkerId(String dogWalkerId) {
        this.dogWalkerId = dogWalkerId;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
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
            return "We really sorry, but " + this.getDogWalkerName() + " is unable" +
                     " to walk " + this.getDogName();
        else
            return "Heads up! " + this.getDogWalkerName() + " " + Util.requestStatus.Accepted.toString().toLowerCase() +
                    " to walk " + this.getDogName();

    }


}
