package hr.algebra.dal;

import hr.algebra.model.AppUser;

public class UserHolderSingleton {

    private AppUser user;
    private final static UserHolderSingleton INSTANCE = new UserHolderSingleton();

    private UserHolderSingleton(){}

    public static UserHolderSingleton getInstance(){
        return INSTANCE;
    }

    public void setUser(AppUser user){
        this.user = user;
    }

    public AppUser getUser(){
        return this.user;
    }
}
