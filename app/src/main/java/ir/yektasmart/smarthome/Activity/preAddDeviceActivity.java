package ir.yektasmart.smarthome.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.UnsupportedEncodingException;

import ir.yektasmart.smarthome.DelayThread;
import ir.yektasmart.smarthome.Model.HandlerMessage;
import ir.yektasmart.smarthome.R;
import ir.yektasmart.smarthome.SendTcpAsync;

public class preAddDeviceActivity extends AppCompatActivity implements TextWatcher {


    EditText passcode;
    TextView passCodeDesc;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_add_device);

        passcode = (EditText) findViewById(R.id.passCodeBox);
        passCodeDesc = (TextView) findViewById(R.id.passcodeDesc);
        passcode.addTextChangedListener(this);

//        Intent intent = new Intent(getApplicationContext(), addDevice.class);
//        intent.putExtra("from", "preAddDevice");
//        intent.putExtra("type", "1202");
//        intent.putExtra("mac", "1a1aaaaaaaaa");
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_qr_code,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_qrcode:
                //new IntentIntegrator(this).initiateScan();

                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan qr-code on device box.");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setBeepEnabled(true);
                integrator.setOrientationLocked(false);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();

                break;
        }

        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String txt = passcode.getText().toString();
        if (txt.length() == 16 ){

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
                            Toast.makeText(preAddDeviceActivity.this, getResources().getString(R.string.failedToConnect), Toast.LENGTH_SHORT).show();
                            break;
                        case CON_TIMEOUT:
                            Toast.makeText(preAddDeviceActivity.this,getResources().getString(R.string.connectionTimeout), Toast.LENGTH_SHORT).show();
                            break;
                        case SER_TIMEOUT:
                            Toast.makeText(preAddDeviceActivity.this,getResources().getString(R.string.serverNotRespond), Toast.LENGTH_SHORT).show();
                            break;
                        case ERR:
                            Toast.makeText(preAddDeviceActivity.this,getResources().getString(R.string.unknowingMessage), Toast.LENGTH_SHORT).show();
                            break;
                        case CON_SUCCESSFUL:

                            try {
                                String str = new String(hm.getMessage(), "UTF-8");
                                if (str.equals("!92472!")) {

                                    Toast.makeText(preAddDeviceActivity.this, getResources().getString(R.string.connectSeccessfully), Toast.LENGTH_SHORT).show();
                                    Handler handler = new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {

                                            Intent intent = new Intent(getApplicationContext(), addDevice.class);
                                            intent.putExtra("from", "preAddDevice");
                                            intent.putExtra("type", passcode.getText().subSequence(0, 4).toString());
                                            intent.putExtra("mac", passcode.getText().subSequence(4, 16).toString());
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                    };
                                    DelayThread t = new DelayThread(handler, 1000);
                                    t.execute();
                                }
                                else {
                                    Toast.makeText(preAddDeviceActivity.this, getResources().getString(R.string.serverSendWrongData), Toast.LENGTH_SHORT).show();
                                }
                            }catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            break;
                        default:
                            Toast.makeText(preAddDeviceActivity.this,getResources().getString(R.string.unknowingMessage), Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };

            SendTcpAsync sta = new SendTcpAsync(myHandler,passcode.getText().toString().toLowerCase().getBytes(),passcode.getText().length());
            sta.execute();

        }
    }

    //////////////////////////added to qr-Code reader
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                //Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                passcode.setText(result.getContents());
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
