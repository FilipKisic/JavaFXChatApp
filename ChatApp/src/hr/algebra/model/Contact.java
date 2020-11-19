package hr.algebra.model;

import javafx.scene.image.Image;

public class Contact {
    private int idContact;
    private String username;
    private String password;
    private String fullName;
    private final Image profileImage;

    public Contact(int idContact, String username, String password, String fullName, Image profileImage) {
        this.idContact = idContact;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.profileImage = profileImage;
    }

    public Contact(String username, String password, String fullName, Image profileImage) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.profileImage = profileImage;
    }

    public Contact(int idContact, String fullName, Image profileImage) {
        this.idContact = idContact;
        this.fullName = fullName;
        this.profileImage = profileImage;
    }

    public int getIdContact() {
        return idContact;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Image getProfileImage() {
        return profileImage;
    }

    @Override
    public String toString() {
        return fullName;
    }
}
