package com.api.data;

public class UserPOJOClass {

//    Use private access modifiers for variables so that we cannot access them outside this Java class.
//    And we have set of setters and getters to assign and call value to these variables.

    public UserPOJOClass(){

    }
    private String id;
    private String name;
    private String email;
    private String status;
    private String gender;

    public UserPOJOClass(String name, String email, String status, String gender) {
        this.name = name;
        this.email = email;
        this.status = status;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "UserPOJOClass : "+"\n"+"{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }

//    ToString() - When we try to print the object of the user class (ie. UserPOJOClass) , it will print in above format rather than giving the memory address
//     @Override - It is coming from the user class (ie. UserPOJOClass)

}
