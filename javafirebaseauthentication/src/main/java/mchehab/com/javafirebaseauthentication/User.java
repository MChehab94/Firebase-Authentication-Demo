package mchehab.com.javafirebaseauthentication;

public class User {

    private String uid;
    private String name;
    private String dateOfBirth;
    private String email;

    public User(String uid, String name, String dateOfBirth, String email) {
        this.uid = uid;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
    }

    public User() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
