package artan.fitnessapp;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * This class we used to create an RealmObject for all users's details(such as steps and distance), so we can add in our Realm database
 */
public class UserDetails extends RealmObject {


    UserDetails(String name, int steps, int distanceFeet, long timeAdded) {
        this.name = name;
        this.steps = steps;
        this.distanceFeet = distanceFeet;
        this.timeAdded = timeAdded;
    }

    private String name;
    private int steps;
    private int distanceFeet;
    @PrimaryKey
    private long timeAdded;

    public UserDetails() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getDistanceFeet() {
        return distanceFeet;
    }

    public void setDistanceFeet(int distanceFeet) {
        this.distanceFeet = distanceFeet;
    }

    public long getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(long timeAdded) {
        this.timeAdded = timeAdded;
    }
}
