package artan.fitnessapp;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * This class we used to create an RealmObject for all users (Login or SignUp), so we can add in our Realm database
 */
public class User extends RealmObject {

    @PrimaryKey
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
