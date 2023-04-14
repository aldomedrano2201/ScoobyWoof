package model;

public class Review {
    private String message;
    private String dogWalkerId;
    private float rateReview;

    public Review(String message, String dogWalkerId, float rateReview) {
        this.message = message;
        this.dogWalkerId = dogWalkerId;
        this.rateReview = rateReview;
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

    public float getRateReview() {
        return rateReview;
    }

    public void setRateReview(float rateReview) {
        this.rateReview = rateReview;
    }
}
