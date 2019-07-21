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

import org.eclipse.paho.client.mqttv3.MqttClient;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import ir.yektasmart.smarthome.Fragments.DeviceFragment;
import ir.yektasmart.smarthome.Fragments.EditModuleFragment;
import ir.yektasmart.smarthome.Fragments.GroupFragment;
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
        EditModuleFragment.OnFragmentInteractionListener
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

    RelativeLayout mainLayout;
    DrawerLayout drawer;

    @SuppressLint("StaticFieldLeak")
    public static DataBase mDB ;
    public static currentView currPage = currentView.devicePage;
    public static String ARG_BaseID = "arg_baseId";
    public static String ARG_BaseName= "arg_baseName";

    private Context context;

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

        BottomNavigationView navView = findViewById(R.id.nav_bottom_view);
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
                }
//            else if (g.getType().equals(ModuleType.INTERNET.toString())) {
//
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                InternetModuleFragment internetModuleFragment = InternetModuleFragment.newInstance(g.getId(),g.getName());
//                ft.replace(R.id.contentContainer, internetModuleFragment);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                ft.addToBackStack(null);
//                ft.commit();
//
//            }else {
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
}
