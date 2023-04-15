package model;

public class Review {

    private String message;
    private String dogWalkerId;
    private String id;

    public Review(String message, String dogWalkerId, String rateReview) {
        this.message = message;
        this.dogWalkerId = dogWalkerId;
        this.id = rateReview;

    }

    public Review(String message, String rateReview) {
        this.message = message;
        this.id = rateReview;

    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDogWalkerId() {
        return dogWalkerId;
    }

    public void setDogWalkerId(String dogWalkerId) {
        this.dogWalkerId = dogWalkerId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
