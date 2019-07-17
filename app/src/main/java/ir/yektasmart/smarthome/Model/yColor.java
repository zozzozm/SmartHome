package ir.yektasmart.smarthome.Model;

import android.graphics.Color;

/**
 * Created by macbookpro on 10/25/16 AD.
 */
public class yColor {
    int Red;
    int Green;
    int Blue;
    int White;

    public yColor(int red, int green, int blue, int white) {
        Red = red;
        Green = green;
        Blue = blue;
        White = white;
    }

    public yColor(int red, int green, int blue) {
        Red = red;
        Green = green;
        Blue = blue;
    }
    public  yColor(int color){
        Red   = Color.red(color);
        Green = Color.green(color);
        Blue  = Color.blue(color);
    }
    public int getRed() {
        return Red;
    }

    public void setRed(int red) {
        Red = red;
    }

    public int getGreen() {
        return Green;
    }

    public void setGreen(int green) {
        Green = green;
    }

    public int getBlue() {
        return Blue;
    }

    public void setBlue(int blue) {
        Blue = blue;
    }

    public int getWhite() {
        return White;
    }

    public void setWhite(int white) {
        White = white;
    }
}
