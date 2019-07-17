package ir.yektasmart.smarthome.Model;

/**
 * Created by YektaCo on 17/01/2017.
 */

public class ExportDeviceModel {

    int id;
    String title;
    int permission = -1;
    int ownPermission = -1;
    boolean selected;
    boolean enable = true;
    boolean highLight = false;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isHighLight() {
        return highLight;
    }

    public void setHighLight(boolean highLight) {
        this.highLight = highLight;
    }

    public int getOwnPermission() {
        return ownPermission;
    }

    public void setOwnPermission(int ownPermission) {
        this.ownPermission = ownPermission;
    }
}
