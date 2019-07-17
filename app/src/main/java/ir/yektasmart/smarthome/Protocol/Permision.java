package ir.yektasmart.smarthome.Protocol;

/**
 * Created by YektaCo on 16/01/2017.
 */

public enum Permision {
    Admin,     // can use device and send device to other's with unique id for each one (User/Guest)
    User,      // can use device and send to other's with it's own id
    Guest;     // can use device until his (parent's) id is valid


    @Override
    public String toString() {
        return super.toString();
    }
}
