package ir.yektasmart.smarthome;

import android.content.Context;
import android.content.SharedPreferences;

public class sharedPrefrence {

    SharedPreferences sharedPreferences;
    Context context;

    String myModule="YektaSmartGroup";


    public sharedPrefrence(Context context) {
        this.context=context;
    }

    public void saveExtraString(String key,String value) {
        sharedPreferences = context.getSharedPreferences(this.myModule,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public String loadExtraString(String key) {
        sharedPreferences = context.getSharedPreferences(this.myModule,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public void saveExtraInt(String key,int dig) {
        sharedPreferences = context.getSharedPreferences(this.myModule,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(key, dig);
        editor.apply();
    }
    public int loadExtraInt(String key){
        sharedPreferences = context.getSharedPreferences(this.myModule,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,-1);
    }

    public void saveExtraBool(String key,boolean flag) {
        sharedPreferences = context.getSharedPreferences(this.myModule,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(key, flag);
        editor.apply();
    }
    public boolean loadExtraBool(String key){
        sharedPreferences = context.getSharedPreferences(this.myModule,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,false);
    }
}
