package com.example.SCAR_vJAVAproject;

public class model {

    private String amount;
    private String company;
    private String date;

    public model() {
    }

    public model(String amount, String company, String date) {
        this.amount = amount;
        this.company = company;
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
