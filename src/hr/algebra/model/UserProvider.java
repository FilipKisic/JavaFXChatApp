package hr.algebra.model;

public class UserProvider {

    private AppUser user;
    private final static UserProvider INSTANCE = new UserProvider();

    private UserProvider(){}

    public static UserProvider getInstance(){
        return INSTANCE;
    }

    public void setUser(AppUser user){
        this.user = user;
    }

    public AppUser getUser(){
        return this.user;
    }
}
