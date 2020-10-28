package hr.algebra.model;

public class AppUser {
    private int idAppUser;
    private final String username;
    private final String password;
    private String fullname;

    public AppUser(int idAppUser, String username, String password, String fullname) {
        this.idAppUser = idAppUser;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
    }

    public AppUser(String username, String password, String fullname) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
    }

    public int getIdAppUser() { return idAppUser; }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
