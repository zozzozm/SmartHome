package ir.yektasmart.smarthome.Model;

/**
 * Created by YektaCo on 17/01/2017.
 */

public class Group {
    int id;
    String name;
    int index;
    int onOff;
    int color;
    int dimmer;
    int whiteOn;
    int whiteValue;
    int homeId;

    boolean selected;
    int counter;

    public Group()
    {
        id = -1;
        name = "";
        index = -1;
        onOff = 0;
        color = 0;
        dimmer = 0;
        whiteOn = 0;
        whiteValue = 0;
        homeId = -1;

        selected = false;
        counter = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getOnOff() {
        return onOff;
    }

    public void setOnOff(int onOff) {
        this.onOff = onOff;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getDimmer() {
        return dimmer;
    }

    public void setDimmer(int dimmer) {
        this.dimmer = dimmer;
    }

    public int getWhiteOn() {
        return whiteOn;
    }

    public void setWhiteOn(int whiteOn) {
        this.whiteOn = whiteOn;
    }

    public int getWhiteValue() {
        return whiteValue;
    }

    public void setWhiteValue(int whiteValue) {
        this.whiteValue = whiteValue;
    }

    public int getHomeId() {
        return homeId;
    }

    public void setHomeId(int homeId) {
        this.homeId = homeId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
