package com.rollins.tennis;

/**
 * Represents a tennis player with biographical information and UTR rating.
 */
public class Player {
    private String id;
    private String firstName;
    private String lastName;
    private String classYear;
    private String nationality;
    private double utr;
    private String image; // player image path
    
    public Player() {}
    
    public Player(String id, String firstName, String lastName, String classYear, 
                  String nationality, double utr) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.classYear = classYear;
        this.nationality = nationality;
        this.utr = utr;
    }
    
    public Player(String id, String firstName, String lastName, String classYear, 
                  String nationality, double utr, String image) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.classYear = classYear;
        this.nationality = nationality;
        this.utr = utr;
        this.image = image;
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getClassYear() { return classYear; }
    public void setClassYear(String classYear) { this.classYear = classYear; }
    
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    
    public double getUtr() { return utr; }
    public void setUtr(double utr) { this.utr = utr; }
    
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    @Override
    public String toString() {
        return getFullName() + " (" + classYear + ") - UTR: " + utr;
    }
}

