package ankit.applespace.carbuddies.Model;

/**
 * Created by ankit on 28/12/17.
 */

public class Name {
    String userName;
    String deviceID;

    public Name(String userName, String deviceID) {
        this.userName = userName;
        this.deviceID = deviceID;
    }

    public Name() {

    }

    public String getUserName() {
        return userName;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
}
