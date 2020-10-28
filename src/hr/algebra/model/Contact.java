package hr.algebra.model;

import javafx.scene.image.Image;

public class Contact {
    private final int idContact;
    private final String fullName;
    private final Image profileImage;

    public Contact(int idContact, String fullName, Image profileImage) {
        this.idContact = idContact;
        this.fullName = fullName;
        this.profileImage = profileImage;
    }

    public int getIdContact() {
        return idContact;
    }

    public String getFullName() {
        return fullName;
    }

    public Image getProfileImage() {
        return profileImage;
    }

    @Override
    public String toString() {
        return fullName;
    }
}
