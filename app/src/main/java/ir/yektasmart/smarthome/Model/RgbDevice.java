package ir.yektasmart.smarthome.Model;

/**
 * Created by YektaCo on 15/01/2017.
 */

public class RgbDevice {

    BaseDevice baseDevice;
    int pid;
    int id;
    int color;
    int whiteOn;
    int whiteValue;
    int animate;
    int mode;
    int fav01;
    int fav02;
    int fav03;
    int fav04;
    int fav05;
    int fav06;
    int fav07;
    int fav08;
    int fav09;
    int fav10;

    public RgbDevice(BaseDevice baseDevice) {
        this.baseDevice = baseDevice;
    }
    public RgbDevice(){
        baseDevice = null;
    }
    public BaseDevice getBaseDevice() {
        return baseDevice;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
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

    public int getAnimate() {
        return animate;
    }

    public void setAnimate(int animate) {
        this.animate = animate;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getFav01() {
        return fav01;
    }

    public void setFav01(int fav01) {
        this.fav01 = fav01;
    }

    public int getFav02() {
        return fav02;
    }

    public void setFav02(int fav02) {
        this.fav02 = fav02;
    }

    public int getFav03() {
        return fav03;
    }

    public void setFav03(int fav03) {
        this.fav03 = fav03;
    }

    public int getFav04() {
        return fav04;
    }

    public void setFav04(int fav04) {
        this.fav04 = fav04;
    }

    public int getFav05() {
        return fav05;
    }

    public void setFav05(int fav05) {
        this.fav05 = fav05;
    }

    public int getFav06() {
        return fav06;
    }

    public void setFav06(int fav06) {
        this.fav06 = fav06;
    }

    public int getFav07() {
        return fav07;
    }

    public void setFav07(int fav07) {
        this.fav07 = fav07;
    }

    public int getFav08() {
        return fav08;
    }

    public void setFav08(int fav08) {
        this.fav08 = fav08;
    }

    public int getFav09() {
        return fav09;
    }

    public void setFav09(int fav09) {
        this.fav09 = fav09;
    }

    public int getFav10() {
        return fav10;
    }

    public void setFav10(int fav10) {
        this.fav10 = fav10;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
