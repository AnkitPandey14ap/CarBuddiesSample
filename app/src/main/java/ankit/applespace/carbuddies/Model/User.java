package ankit.applespace.carbuddies.Model;

/**
 * Created by ankit on 26/11/17.
 */

public class User {
    String name;
    double lat;
    double lng;

    String ID;

    public User(String name, double lat, double lng, String ID) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.ID = ID;
    }

    //blank activity
    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
