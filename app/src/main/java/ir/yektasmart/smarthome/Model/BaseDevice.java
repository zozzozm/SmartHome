package ir.yektasmart.smarthome.Model;

import ir.yektasmart.smarthome.R;

/**
 * Created by YektaCo on 15/01/2017.
 */

public class BaseDevice {

    int id;
    String name;
    String mac = "noMacAddress";
    String pass;
    int avetar;
    int index;
    int onOff;
    int permission;
    int port;
    int latchPort;
    int typeId;
    String type;
    int homeId;
    int groupId;
    int uid;

    boolean selected;

    public BaseDevice() {
        id = -1;
        name = "No Name";
        mac = "No Mac";
        pass = "No Pass";
        avetar = R.drawable.question;
        index = -1;
        onOff = 0;
        permission = 0;
        port = 0;
        latchPort = -1;
        typeId = -1;
        type = "unknow";
        homeId = -1;
        groupId = -1;

        selected = false;
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

    public String getMac() {
          return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getAvetar() {
        return avetar;
    }

    public void setAvetar(int avetar) {
        this.avetar = avetar;
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

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getLatchPort() {
        return latchPort;
    }

    public void setLatchPort(int latchPort) {
        this.latchPort = latchPort;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getHomeId() {
        return homeId;
    }

    public void setHomeId(int homeId) {
        this.homeId = homeId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
