package hr.algebra.model;

public class ContactProvider {

    private static Contact contact;
    private final static ContactProvider INSTANCE = new ContactProvider();

    private ContactProvider(){}

    public static ContactProvider getInstance(){
        return INSTANCE;
    }

    public void setContact(Contact contact){
        ContactProvider.contact = contact;
    }

    public Contact getContact(){
        return contact;
    }
}
