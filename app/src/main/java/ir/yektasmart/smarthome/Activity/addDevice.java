package ir.yektasmart.smarthome.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import ir.yektasmart.smarthome.DataBase;
import ir.yektasmart.smarthome.DelayThread;
import ir.yektasmart.smarthome.MainActivity;
import ir.yektasmart.smarthome.Model.HandlerMessage;
import ir.yektasmart.smarthome.R;
import ir.yektasmart.smarthome.SendTcpAsync;

public class addDevice extends AppCompatActivity {

    EditText ssid;
    EditText netPass;
    EditText personalPass;
    EditText port;
    EditText devName;

    String _mac = "";
    int _type = -1;

    ProgressDialog progressDialog;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        ssid = findViewById(R.id.ssid);
        netPass = findViewById(R.id.pass);
        personalPass = findViewById(R.id.ppass);
        port =  findViewById(R.id.port);
        devName = findViewById(R.id.devName);
        ssid.clearFocus();

        Bundle bundle = getIntent().getExtras();
        try {
            _type = Integer.parseInt(bundle.getString("type").toString());
            _mac = bundle.getString("mac").toString();


        }catch (Exception e){e.printStackTrace();}

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle(getResources().getString(R.string.addNewDev));

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case  android.R.id.home:
                finish();
/*                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                upIntent.putExtra(MainActivity.from,"addDev");
                setResult(RESULT_OK, upIntent);
                finish();
                return true;*/
                return true;

            case R.id.action_save:


                if (checkEnteredConfig())
                {
                    View view = this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    byte[] msg = new byte[128];

                    int _port = Integer.parseInt(port.getText().toString());
                    String _perPass  = personalPass.getText().toString();
                    String _ssid = ssid.getText().toString();
                    String _netPass = netPass.getText().toString();
                    String _name = devName.getText().toString();


                    int k = 0;
                    msg[k++] = (byte)(_port & 0xFF);
                    msg[k++] = (byte)((_port & 0xFF00)>>8);

                    for (int i = 0; i < 4; i++) {
                        msg[i+k] = (byte) _perPass.charAt(i);

                    }
                    k+=4;

                    msg[k++] = (byte) _ssid.length();
                    msg[k++] = (byte) _netPass.length();
                    msg[k++] = (byte) _name.length();

                    Log.e("AddDevice", "onOptionsItemSelected:000 "+ _netPass.length() );
                    for (int i = 0; i < _ssid.length(); i++) {
                        msg[i+k] = (byte)_ssid.charAt(i);
                    }
                    k+=_ssid.length();

                    for (int i = 0; i < _netPass.length(); i++) {
                        msg[i+k] = (byte)_netPass.charAt(i);
                    }
                    k+=_netPass.length();
                    for (int i = 0; i < _name.length(); i++) {
                        msg[i+k] = (byte)_name.charAt(i);
                    }
                    k +=_name.length();

                    sendConfig(msg,k);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean checkEnteredConfig() {

        boolean flag=true;

        if ((ssid.length() == 0) || ssid.length() > 32 )
        {
            ssid.setError(getString(R.string.ssidError));
            flag = false;
        }

        if ((netPass.length() <8 ) || netPass.length() > 32 )
        {
            netPass.setError(getString(R.string.netPassError));
            flag = false;
        }

        if ((personalPass.length() < 4 ) || personalPass.length() >= 5 )
        {
            personalPass.setError(getString(R.string.perPassError));
            flag = false;
        }

        if (port.getText().length() == 0 ||
                (Integer.parseInt(port.getText().toString()) < 1024)  ||
                (Integer.parseInt(port.getText().toString()) > 65500)  )
        {

            port.setError(getString(R.string.portError));
            flag = false;
        }

        if ((devName.length() < 1 ) || devName.length() > 8)
        {
            devName.setError(getString(R.string.devNameError));
            flag = false;
        }

        return flag;
    }

    public void sendConfig(final byte[] str, final int len)
    {

        //Toast.makeText(this, passcode.getText().subSequence(0,4)+","+passcode.getText().subSequence(4,16), Toast.LENGTH_SHORT).show();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(getResources().getString(R.string.pleaseWait));
        progressDialog.setMessage(getResources().getString(R.string.communicatingWithServer));
        progressDialog.show();

        Handler myHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                HandlerMessage hm = (HandlerMessage) msg.obj;
                switch (hm.getState()) {

                    case CON_FAILED:
                        Toast.makeText(addDevice.this, getResources().getString(R.string.failedToConnect), Toast.LENGTH_SHORT).show();
                        break;
                    case CON_TIMEOUT:
                        Toast.makeText(addDevice.this,getResources().getString(R.string.connectionTimeout), Toast.LENGTH_SHORT).show();
                        break;
                    case SER_TIMEOUT:
                        Toast.makeText(addDevice.this,getResources().getString(R.string.serverNotRespond), Toast.LENGTH_SHORT).show();
                        break;
                    case ERR:
                        Toast.makeText(addDevice.this,getResources().getString(R.string.unknowingMessage), Toast.LENGTH_SHORT).show();
                        break;
                    case CON_SUCCESSFUL:

                        try {
                            boolean equalityFlag = true;
                            byte[] rec = ((HandlerMessage) msg.obj).getMessage();
                            for (int i = 0; i < len; i++) {
                                if(str[i]!= rec[i])
                                    equalityFlag = false;
                            }
                            if (equalityFlag){

                                Toast.makeText(addDevice.this, getResources().getString(R.string.connectSeccessfully), Toast.LENGTH_SHORT).show();

                                Handler handler = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        Handler mh = new Handler(){
                                            @Override
                                            public void handleMessage(Message msg) {
                                                super.handleMessage(msg);
                                            }
                                        };

                                        SendTcpAsync sta = new SendTcpAsync(mh,"!92472!".getBytes(),"!92472!".length());
                                        sta.execute();

                                        int tid = -1;
                                        switch (_type){

                                            case 1401:
                                                tid = 1;
                                                break;

                                            case 1103:
                                                tid = 3;
                                                break;

                                            case 1201:
                                                tid = 4;
                                                break;

                                            case 1202:
                                                tid = 5;
                                                break;

                                            case 1102:
                                                tid = 6;
                                                break;

                                            case 1101:
                                                tid = 8;
                                                break;

                                            case 1001:
                                                tid = 9;
                                                break;

                                            case 1002:
                                                tid = 10;
                                                break;

                                            case 0001:
                                                tid = 0;
                                                break;

                                            default:
                                                tid = 7;
                                                break;
                                        }

                                        MainActivity.mDB.addDevice(_mac,devName.getText().toString(),
                                                personalPass.getText().toString(),tid,0,
                                                Integer.parseInt(port.getText().toString()),0 , 0, DataBase.Confilict.replace);

                                        Intent upIntent = NavUtils.getParentActivityIntent(addDevice.this);
                                        upIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        setResult(RESULT_OK, upIntent);

                                        onKeyMetric(_mac,devName.getText().toString(),tid+"",port.getText().toString());
                                        finish();
                                    }
                                };
                                DelayThread t = new DelayThread(handler, 1000);
                                t.execute();
                            }
                            else {
                                Toast.makeText(addDevice.this, getResources().getString(R.string.serverSendWrongData), Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        break;
                    default:
                        Toast.makeText(addDevice.this,getResources().getString(R.string.unknowingMessage), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        SendTcpAsync sta = new SendTcpAsync(myHandler,str,len);
        sta.execute();

    }

    public void showToast(String msg){
        if( toast != null ) {
            toast.cancel() ;
            toast = null ;
        }
        toast = Toast.makeText(getApplicationContext() , msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onKeyMetric(String newDeviceMac,String newDeviceType,String name , String port) {
    }
}
