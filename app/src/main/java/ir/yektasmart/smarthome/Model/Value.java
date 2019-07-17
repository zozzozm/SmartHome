package ir.yektasmart.smarthome.Model;

import java.util.ArrayList;

/**
 * Created by macbookpro on 10/25/16 AD.
 */
public class Value {
    private static final String TAG = "Value";

    Boolean isPending = false;
    yColor color;
    ArrayList<yColor> colors;
    int Dimmer;
    boolean OnOff;
    boolean play;
    int effect;
    int musicalMode;
    int count = 0;

    public Value() {}

    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }
    public yColor getColor() {
        return color;
    }

    public void setColor(yColor color) {
        this.color = color;
    }

    public ArrayList<yColor> getColors() {
        return colors;
    }

    public void setColors(ArrayList<yColor> colors) {
        this.colors = colors;
    }
    public void addColors(yColor a) { this.colors.add(a); }
    public yColor getColors(int position) {
        if(position< colors.size() ) {
            return  colors.get(position);
        }
        return null;
    }

    public int getEffect() {
        return effect;
    }

    public void setEffect(int effect) {
        this.effect = effect;
    }

    public int getDimmer() {
        return Dimmer;
    }

    public void setDimmer(int dimmer) {
        Dimmer = dimmer;
    }

    public boolean isOnOff() {
        return OnOff;
    }

    public void setOnOff(boolean onOff) {
        OnOff = onOff;
    }

    public void setColors(RgbDevice rgbDevice) {

        colors = new ArrayList<>();
        yColor c = new yColor(rgbDevice.fav01);
        this.colors.add(c);
        c = new yColor(rgbDevice.fav02);
        this.colors.add(c);
        c = new yColor(rgbDevice.fav03);
        this.colors.add(c);
        c = new yColor(rgbDevice.fav04);
        this.colors.add(c);
        c = new yColor(rgbDevice.fav05);
        this.colors.add(c);
        c = new yColor(rgbDevice.fav06);
        this.colors.add(c);
        c = new yColor(rgbDevice.fav07);
        this.colors.add(c);
        c = new yColor(rgbDevice.fav08);
        this.colors.add(c);
        c = new yColor(rgbDevice.fav09);
        this.colors.add(c);
        c = new yColor(rgbDevice.fav10);
        this.colors.add(c);

    }

    public Boolean isPending() {
        return isPending;
    }

    public void setPending(Boolean pending) {
        isPending = pending;
    }
    public int getMusicalMode() {
        return musicalMode;
    }

    public void setMusicalMode(int musicalMode) {
        this.musicalMode = musicalMode;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
