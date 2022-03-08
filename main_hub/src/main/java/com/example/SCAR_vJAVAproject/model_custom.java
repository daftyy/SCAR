package com.example.SCAR_vJAVAproject;

public class model_custom {

    private String description;
    private String event;
    private String date_custom;



    public model_custom() {
    }

    public model_custom(String description, String event, String date_custom) {
        this.description = description;
        this.event = event;
        this.date_custom = date_custom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDate_custom() {
        return date_custom;
    }

    public void setDate_custom(String date_custom) {
        this.date_custom = date_custom;
    }
}
