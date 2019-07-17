package ir.yektasmart.smarthome.Model;

/**
 * Created by YektaCo on 17/01/2017.
 */

public class Access {
    int mid;
    int  uid;
    int  permission;

    public Access(){
        mid = -1 ;
        uid = -1;
        permission = -1;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }
}