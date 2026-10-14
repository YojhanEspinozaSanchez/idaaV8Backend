package com.idaav8.app.dto;

public class ProfileResponseDTO {


    private String name;
    private String lastName;
    private String email;
    private String area;
    private String userName;
    


    public ProfileResponseDTO() {
    }

    public ProfileResponseDTO(String name, String lastName, String email, String area, String userName) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.area = area;
        this.userName = userName;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    
}
