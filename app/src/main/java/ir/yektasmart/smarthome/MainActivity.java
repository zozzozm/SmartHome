package ir.yektasmart.smarthome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutorService;

import ir.yektasmart.smarthome.Fragments.DeviceFragment;
import ir.yektasmart.smarthome.Fragments.EditModuleFragment;
import ir.yektasmart.smarthome.Fragments.GroupFragment;
import ir.yektasmart.smarthome.Fragments.InternetModuleFragment;
import ir.yektasmart.smarthome.Fragments.Latch001ModuleFragment;
import ir.yektasmart.smarthome.Fragments.Latch002ModuleFragment;
import ir.yektasmart.smarthome.Fragments.RfModuleFragment;
import ir.yektasmart.smarthome.Fragments.RgbMusicalModuleFragment;
import ir.yektasmart.smarthome.Fragments.SettingFragment;
import ir.yektasmart.smarthome.Model.BaseDevice;
import ir.yektasmart.smarthome.Protocol.ModuleType;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DeviceFragment.OnFragmentInteractionListener,
        GroupFragment.OnFragmentInteractionListener,
        RgbMusicalModuleFragment.OnFragmentInteractionListener,
        SettingFragment.OnFragmentInteractionListener,
        Latch001ModuleFragment.OnFragmentInteractionListener,
        Latch002ModuleFragment.OnFragmentInteractionListener,
        RfModuleFragment.OnFragmentInteractionListener,
        EditModuleFragment.OnFragmentInteractionListener,
        InternetModuleFragment.OnFragmentInteractionListener,
        SettingFragment.AwayModeChangeListener,
        MqttCallback
{

    private static final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static int PERMISSIONS = 0;
    public static String UUID = "-1";
    private static String  macAddress = "200000000000";
    Toast toast;

    public static Boolean  searching_flag = false;
    public static int scrollyDev=0;
    public static sharedPrefrence shP;
    public static ExecutorService pool;

    public static Vibrator vib;
    public static int vib_delay = 50;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    public static String ARG_GroupID = "arg_groupId";
    public static String ARG_GroupName = "arg_groupName";
    public static String ARG_BaseTypeId = "arg_baseTypeId";

    public static ArrayList<BaseDevice> InternetDevices;
    private MqttClient mqttClient;

    DhcpInfo d;
    WifiManager wifii;

    BottomNavigationView navView;
    RelativeLayout mainLayout;
    DrawerLayout drawer;

    @SuppressLint("StaticFieldLeak")
    public static DataBase mDB ;
    public static currentView currPage = currentView.devicePage;
    public static String ARG_BaseID = "arg_baseId";
    public static String ARG_BaseName= "arg_baseName";

    private Context context;
    private MainActivity thisCon = this;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_devices:
                    replaceDevFragment();
                    return true;
                case R.id.navigation_groups:
                    GroupFragment groupFragment = new GroupFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentContainer, groupFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                    return true;
                case R.id.navigation_settings:
                    SettingFragment settingFragment = new SettingFragment();
                    settingFragment.setAwayModeListener(thisCon);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentContainer, settingFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                    return true;
            }
            return false;
        }

    };


    private void replaceDevFragment() {
        DeviceFragment devFragment = new DeviceFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentContainer, devFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navView = findViewById(R.id.nav_bottom_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        context = this;
        mDB = new DataBase(this);


        mDB.addDevice("a020a618765d","MusicalStrip","1234",5,0,10000,0,0 , DataBase.Confilict.replace);
        mDB.addDevice("a020a618765e","switch001","1234",8,0,10000,0,0 , DataBase.Confilict.replace);
        mDB.addDevice("a020a618765f","latch001","1234",9,0,10000,0,0 , DataBase.Confilict.replace);
        mDB.addDevice("a020a618765g","latch002","1234",10,0,10000,0,0 , DataBase.Confilict.replace);
        mDB.addDevice("a020a618765h","rf","1234",1,0,10000,0,0 , DataBase.Confilict.replace);
        mDB.addDevice("a020a618765i","internet","1234",0,0,10000,0,0 , DataBase.Confilict.replace);

        //        mDB.addUser("UUID1: User Unique Identifier to distinguish users", "Ali" ,1 ,"this is Description.","cell phone" );
        mDB.seeTypes();
        mDB.seeModules();
        mDB.seeRgbModules();
        mDB.seeUsers();
        mDB.seeAccess();
        mDB.seeGroups2();

        replaceDevFragment();

        shP = new sharedPrefrence(this);
        UUID = shP.loadExtraString(Const.SP_UUID);
        if (UUID.isEmpty()) {
            UUID = generateUUID();//Log.e(TAG, "uuid len: " + UUID.length());
            shP.saveExtraString(Const.SP_UUID, UUID);
        }
        Const.UUID = UUID;
        Const.USER_NAME = shP.loadExtraString(Const.SP_LoginUserName);
        Const.isVibrateMode = shP.loadExtraBool(Const.SP_VibrateMode);
        vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        macAddress = UUID.substring(0,12);
    }

    private String generateUUID() {
        String _UUID = "";
        try {
            String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

            Calendar cc = Calendar.getInstance();
            int year=cc.get(Calendar.YEAR);
            int month=cc.get(Calendar.MONTH);
            int mDay = cc.get(Calendar.DAY_OF_MONTH);
            int mHour = cc.get(Calendar.HOUR_OF_DAY);
            int mMinute = cc.get(Calendar.MINUTE);
            int mSecond = cc.get(Calendar.SECOND);
            int mmSecond = cc.get(Calendar.MILLISECOND);
            int mWeek = cc.get(Calendar.WEEK_OF_YEAR);

            int max = 99999;
            int min = 0;

            Random r = new Random();
            int rand = r.nextInt(max - min) + min;
            int rand1 = r.nextInt(max - min) + min;
            int rand2 = r.nextInt(max - min) + min;
            int rand3 = r.nextInt(max - min) + min;
            int rand4 = r.nextInt(max - min) + min;
            int rand5 = r.nextInt(max - min) + min;
            //Log.e(TAG, "rand: " + rand );
            String date = String.format("%1$04d%2$02d%3$02d%4$02d%5$02d%6$02d%7$03d%8$02d%9$05d".toLowerCase(Locale.US),
                    year,month,mDay,mHour,mMinute,mSecond,mmSecond,mWeek,rand);

            //get user name and password
            String name = shP.loadExtraString(Const.SP_LoginUserName);

            String secRand = String.format("%1$05d%2$05d%3$05d%4$05d%5$05d".toLowerCase(Locale.US),rand1,rand2,rand3,rand4,rand5);

            _UUID = android_id + name + date + secRand;

            if(_UUID.length()<51)
            {
                _UUID += secRand;
                _UUID += secRand;
            }
            //Log.e(TAG, "UUID: " + _UUID );

        }catch (Exception e){
            e.printStackTrace();
        }
        return _UUID.substring(0,_UUID.length()-(_UUID.length()-50));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_new_device) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.exit) {
            System.exit(1);
            return true;

        } else if (id == R.id.feedback) {

        } else if (id == R.id.about_us) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClickDev(BaseDevice g) {
        try {
//            if (g.getType().equals(ModuleType.RGBW_LAMP.toString())
//                    || g.getType().equals(ModuleType.RGBW_STRIP.toString())) {
//
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                RgbModuleFragment rgbModuleFragment = RgbModuleFragment.newInstance(g.getId(),g.getName());
//                ft.replace(R.id.contentContainer, rgbModuleFragment);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                ft.addToBackStack(null);
//                ft.commit();
//            }
//            else
            if (g.getType().equals(ModuleType.RF.toString())) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                RfModuleFragment rfModuleFragment = RfModuleFragment.newInstance(g.getId(),g.getName());
                ft.replace(R.id.contentContainer, rfModuleFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
            } else if (g.getType().equals(ModuleType.LATCH001.toString()) ) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Latch001ModuleFragment latch001ModuleFragment = Latch001ModuleFragment.newInstance(g.getId(),g.getName());
                ft.replace(R.id.contentContainer, latch001ModuleFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
            } else if (g.getType().equals(ModuleType.LATCH002.toString())) {//is latch 002

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Latch002ModuleFragment latch002ModuleFragment = Latch002ModuleFragment.newInstance(g.getId(),g.getName());
                ft.replace(R.id.contentContainer, latch002ModuleFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
            } else if (g.getType().equals(ModuleType.MUSICAL_STRIP.toString())) {
                 FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                 RgbMusicalModuleFragment rgbMusicalModuleFragment = RgbMusicalModuleFragment.newInstance(g.getId(), g.getName());
                 ft.replace(R.id.contentContainer, rgbMusicalModuleFragment);
                 ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                 ft.addToBackStack(null);
                 ft.commit();
                } else if (g.getType().equals(ModuleType.INTERNET.toString())) {

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                InternetModuleFragment internetModuleFragment = InternetModuleFragment.newInstance(g.getId(),g.getName());
                ft.replace(R.id.contentContainer, internetModuleFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();

            }
//            else {
//
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                UnknowModuleFragment unknowModuleFragment = UnknowModuleFragment.newInstance(g.getId(),g.getName());
//                ft.replace(R.id.contentContainer, unknowModuleFragment);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                ft.addToBackStack(null);
//                ft.commit();
//
//            }

        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onFragmentInteraction(Uri uri) {


    }

    @Override
    public void connectionLost(Throwable throwable) {
        Toast.makeText(getBaseContext(), "Mqtt Connection lost.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

        Log.e(TAG, "messageArrived: " + s + " - " + mqttMessage  );
        if(s.contains(".UpTime")) {

            String str = String.valueOf(mqttMessage);
            int milis = Integer.parseInt(str.substring(8, str.length()));
            milis /= 1000;
            //Log.e(TAG, "messageArrived: " + "Up time: " + "D:" + milis / 86400 + " H:" + milis / 3600 + " M:" + milis / 60);

            final int d = milis/86400;milis%=86400;
            final int h = milis/3600;milis%=3600;
            final int m = milis/60;milis%=60;
            final int sec = milis;
            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(context, "Up time: "  + d + ":" + h + ":" + m + ":" + sec, Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(context, "The command executed successfully", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        Toast.makeText(getBaseContext(), "deliveryComplete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void awayModeChanged(Boolean isAway) {

        Const.isAwayMode = isAway;
        InternetDevices = mDB.getInternetDevice();

        if(isAway) {

            if (InternetDevices.size() > 0) {
                try {
                    // TODO: should update on network state change. maybe first change to away mode then connect to  wii or LTE 8/14/2017

                    mqttClient = new MqttClient(Const.BROKER, Const.UUID.substring(1, 15), null);
                    MqttConnectOptions options = new MqttConnectOptions();
                    options.setCleanSession(true);
                    options.setKeepAliveInterval(60);
                    options.setConnectionTimeout(30);
                    mqttClient.connect(options);
                    mqttClient.setCallback(this);
                    mqttClient.subscribe("0001" + InternetDevices.get(InternetDevices.size() - 1).getMac() + ".UpTime");
                    mqttClient.subscribe("0001" + InternetDevices.get(InternetDevices.size() - 1).getMac() + ".DevRsp");

                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }
        else {

            if (InternetDevices.size() > 0) {
                try {
                    // TODO: should update on network state change. maybe first change to away mode then connect to  wii or LTE 8/14/2017

                    if (mqttClient != null && mqttClient.isConnected()) {
                        mqttClient.setCallback(null);
                        mqttClient.unsubscribe("0001" + InternetDevices.get(InternetDevices.size() - 1).getMac() + ".UpTime");
                        mqttClient.unsubscribe("0001" + InternetDevices.get(InternetDevices.size() - 1).getMac() + ".DevRsp");
                        mqttClient.disconnect();
                    }

                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
