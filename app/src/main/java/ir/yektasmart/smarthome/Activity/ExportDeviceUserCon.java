package ir.yektasmart.smarthome.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.net.DatagramSocket;
import java.util.ArrayList;

import ir.yektasmart.smarthome.Adapter.UserRowAdapter;
import ir.yektasmart.smarthome.Const;
import ir.yektasmart.smarthome.Model.User;
import ir.yektasmart.smarthome.R;
import ir.yektasmart.smarthome.UdpServer;

public class ExportDeviceUserCon extends AppCompatActivity  implements  AdapterView.OnItemClickListener,UdpServer.OnMessageReceived {

    private static final String TAG = "ExportDeviceUserCon";
    public static User SELECTED_USER ;
    public static DatagramSocket socket;
    //SendUdp sendUdp;
    //AsyncServerThread tcpServer;

    UdpServer udpServer;

    ArrayList<String> ipList;
    // ArrayList<Socket> sockets = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>();
    UserRowAdapter userUserRowAdapter;

    public static boolean sendingFlag = true;
    Toast toast;
    ListView conUserLV;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;
        setContentView(R.layout.activity_export_device_user_con);

        conUserLV = findViewById(R.id.ExportDeviceUserCon_LV);
        tv = findViewById(R.id.connectedUsersTitle);

        ipList = new ArrayList<>();

        users = new ArrayList<>();

        userUserRowAdapter = new UserRowAdapter(getApplication(), R.layout.row_connected_user, users);
        conUserLV.setAdapter(userUserRowAdapter);

        conUserLV.setItemsCanFocus(false);

        //tcpServer = new AsyncServerThread(Const.TCP_PORT,this);
        //tcpServer.setDaemon(false);
        //tcpServer.start();

        //sendUdp = new SendUdp(Const.UDP_HANDSHAKE_MSG.getBytes(), Const.BROADCAST_IP,Const.UDP_PORT);
        //sendUdp.start();

        udpServer = new UdpServer(Const.UDP_PORT, getApplicationContext(), this);
        udpServer.start();
        conUserLV.setOnItemClickListener(this);

        //conUserLV.setOnClickListener(this);
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
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan qr-code on other device.");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setBeepEnabled(true);
                integrator.setOrientationLocked(false);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
                break;
        }

        return true;
    }

    public void showToast(String msg) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        udpServer.kill();
        udpServer.close();

    }


    private boolean recMessageIsValid(String message) {
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(this,ExportDeviceDevSel.class);
        //intent.putExtra(,userUserRowAdapter.getItem(position).getName())
        ExportDeviceUserCon.SELECTED_USER = userUserRowAdapter.getItem(position);
        startActivity(intent);
    }

    @Override
    public void UdpMessageReceived(final String message, final String ip) {
        if(ipList.indexOf(ip) == -1 && UdpMessageIsValid(message)) {

            //tcpClient = new TcpClient(Const.USER_NAME + Const.UUID ,ip, Const.TCP_PORT, this);
            //tcpClient.start();

            ipList.add(ip);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {

                        showToast("Received from: " + ip);
                        yektasmart.com.geniusremote.yektaMessage ym = new yektasmart.com.geniusremote.yektaMessage(message);
                        User u = new User();
                        u.setName(ym.getName());
                        u.setUuid(ym.getUUID());
                        u.setDescription(ip);
                        userUserRowAdapter.add(u);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        }
    }

    private boolean UdpMessageIsValid(String message) {
        //Log.e(TAG, "UdpMessageIsValid: " + message);
        if (message.length() > 70) {
            if (message.substring(0, 20).equals(Const.UDP_HANDSHAKE_MSG))
                return true;
            else
                return false;
        }
        return false;
    }



    //////////////////////////added to qr-Code reader
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String qr = result.getContents();
                if(UdpMessageIsValid(qr)) {
                    yektasmart.com.geniusremote.yektaMessage ym = new yektasmart.com.geniusremote.yektaMessage(qr);
                    User u = new User();
                    u.setName(ym.getNameViaQr());
                    u.setUuid(ym.getUUIDViaQr());
                    u.setDescription(ym.getIP());
                    Log.e(TAG, "getIP: " + ym.getIP() );
                    ExportDeviceUserCon.SELECTED_USER = u;
                    startActivity(new Intent(this,ExportDeviceDevSel.class));
                }else {
                    Toast.makeText(this, "Wrong qr-code", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
