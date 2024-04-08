package pizzapp.pizzappbackend.models;

import java.util.ArrayList;
import java.util.List;

public class User extends UserCredentials{
    private int id;
    private String role;
    private boolean banned;

    public User(int id, String email, String password, String role, boolean banned) {
        super(email,password);
        this.id=id;
        this.role = role;
        this.banned = banned;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }
}