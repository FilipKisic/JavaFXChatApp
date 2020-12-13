package hr.algebra.model;

public class UserProvider {

    private Contact user;
    private final static UserProvider INSTANCE = new UserProvider();

    private UserProvider(){}

    public static UserProvider getInstance(){
        return INSTANCE;
    }

    public void setUser(Contact user){
        this.user = user;
    }

    public Contact getUser(){
        return this.user;
    }
}
