package ir.yektasmart.smarthome.Model;

/**
 * Created by YektaCo on 17/01/2017.
 */

public class User {
    int id;
    String uuid;
    String name;
    String cell;
    byte[] pic;
    String description;
    int isActive;

    boolean selected;
    //int wait;

    public User(){
        id = -1 ;
        uuid = "";
        name = "";
        cell = "";

        description = "";
        isActive = 0;
        selected = true;
       // wait = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}