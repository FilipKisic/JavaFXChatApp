package hr.algebra.dal;

import hr.algebra.model.Contact;

public class ContactHolderSingleton {

    private static Contact contact;
    private final static ContactHolderSingleton INSTANCE = new ContactHolderSingleton();

    private ContactHolderSingleton(){}

    public static ContactHolderSingleton getInstance(){
        return INSTANCE;
    }

    public void setContact(Contact contact){
        ContactHolderSingleton.contact = contact;
    }

    public Contact getContact(){
        return contact;
    }
}
