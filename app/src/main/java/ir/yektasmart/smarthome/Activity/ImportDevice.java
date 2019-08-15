package ir.yektasmart.smarthome.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.MultiplePulse;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;
import java.util.ArrayList;

import ir.yektasmart.smarthome.AsyncServerThread;
import ir.yektasmart.smarthome.Const;
import ir.yektasmart.smarthome.DataBase;
import ir.yektasmart.smarthome.MainActivity;
import ir.yektasmart.smarthome.Model.BaseDevice;
import ir.yektasmart.smarthome.Model.RgbDevice;
import ir.yektasmart.smarthome.R;
import ir.yektasmart.smarthome.SendUdp;

public class ImportDevice extends AppCompatActivity implements AsyncServerThread.yektaAsyncServer {


    private static final String TAG = "ImportDevice";
    //UdpServer udpServer;
    //TcpClient tcpClient;
    AsyncServerThread tcpServer;
    int connectionCounter = 0;
    //ArrayList<String> ipList = new ArrayList<>();
    ArrayList<BaseDevice> baseDevices = new ArrayList<>();
    //ArrayList<RgbDevice> rgbDevices = new ArrayList<>();
    SendUdp sendUdp;
    ProgressBar progressBar;
    MultiplePulse doubleBounce;
    TextView stateTV;
    TextView messageTV;

    private ImageView qrGen;
    String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_device);

        progressBar = findViewById(R.id.importDeviceProgressBar);
        stateTV = findViewById(R.id.importDeviceWaiting);
        messageTV = findViewById(R.id.importDeviceMessage);
        qrGen = findViewById(R.id.qrGen);

        tcpServer = new AsyncServerThread(Const.TCP_PORT,this);
        tcpServer.setDaemon(false);
        tcpServer.start();
        try {
            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

            String[] parts = ip.toString().split("\\.");

            int p1 = Integer.parseInt(parts[0]);
            int p2 = Integer.parseInt(parts[1]);
            int p3 = Integer.parseInt(parts[2]);
            int p4 = Integer.parseInt(parts[3]);

            ip = String.format("%03d.%03d.%03d.%03d", p1, p2, p3, p4);
            //Log.e(TAG, "ip cor: " + ip);
        }catch (Exception e){
            e.printStackTrace();
        }

        String sendMessage = Const.UDP_HANDSHAKE_MSG  + Const.USER_NAME + Const.UUID  ;//20+??+50
        sendUdp = new SendUdp(sendMessage.getBytes(), Const.BROADCAST_IP,Const.UDP_PORT);
        sendUdp.start();

        doubleBounce = new MultiplePulse();
        progressBar.setIndeterminateDrawable(doubleBounce);
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

                try {
                    doubleBounce.setAlpha(0);
                    doubleBounce.stop();
                    doubleBounce.setVisible(false,false);
                    messageTV.setText("Scan this code with other phone.");

                    QRCodeWriter writer = new QRCodeWriter();
                    String sendMessage = Const.UDP_HANDSHAKE_MSG  + Const.USER_NAME + Const.UUID + ip ;//20+??+50
                    try {
                        BitMatrix bitMatrix = writer.encode(sendMessage, BarcodeFormat.QR_CODE,256, 256);
                        int width = bitMatrix.getWidth();
                        int height = bitMatrix.getHeight();
                        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                        for (int x = 0; x < width; x++) {
                            for (int y = 0; y < height; y++) {
                                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                            }
                        }
                        qrGen.setImageBitmap(bmp);

                    } catch (WriterException e) {
                        e.printStackTrace();

                        doubleBounce.setColor(Color.RED);
                        doubleBounce.start();
                        doubleBounce.setAlpha(255);
                        messageTV.setText("Error on creating qr-code.");
                    }



                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }

        return true;
    }

    private ArrayList<RgbDevice> parseJsonToRgbDev(String cyperString) {
        ArrayList<RgbDevice> rgbDevices = new ArrayList<>();
        try {

            JSONObject jo=new JSONObject(cyperString);

            JSONArray jsonArray=jo.getJSONArray("RGB");

            for (int i = 0; i <jsonArray.length() ; i++) {

                RgbDevice bd = new RgbDevice();

                JSONObject jsoni = jsonArray.getJSONObject(i);
                bd.setId(jsoni.getInt("rgb_id"));
                bd.setPid(jsoni.getInt("rgb_pid"));
                rgbDevices.add(bd);
            }

            return rgbDevices;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rgbDevices;
    }

    private ArrayList<BaseDevice> parseJsonToBaseDev(String msg) {

        ArrayList<BaseDevice> baseDevices = new ArrayList<>();
        try {

            JSONObject jo=new JSONObject(msg);

            JSONArray jsonArray=jo.getJSONArray("DEVICE");
            for (int i = 0; i <jsonArray.length() ; i++) {

                BaseDevice bd = new BaseDevice();

                JSONObject jsoni = jsonArray.getJSONObject(i);
                bd.setId(jsoni.getInt("_id"));
                bd.setName(jsoni.getString("_name"));
                bd.setPass(jsoni.getString("_pass"));
                bd.setMac(jsoni.getString("_mac"));
                bd.setTypeId(jsoni.getInt("_tid"));
                //bd.setAvetar(jsoni.getInt("_avt"));
                bd.setLatchPort(jsoni.getInt("_lPrt"));
                bd.setPermission(jsoni.getInt("_perm"));
                bd.setPort(jsoni.getInt("_port"));
                bd.setOnOff(jsoni.getInt("_onOff"));
                bd.setUid(jsoni.getInt("_uid"));
                if(bd.getPermission() > 2 || bd.getPermission() < 1 ){
                    //bd.setUid(2);
                    bd.setPermission(2);
                }
                if (bd.getUid() != 0) {
                    baseDevices.add(bd);
                    Log.e(TAG, "parseJson: " + i + ":" + bd.getName() + " cant be admin.");
                }
            }

            return baseDevices;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return baseDevices;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sendUdp != null) {
            sendUdp.Runing(false);
            sendUdp = null;
        }
        if (tcpServer != null) {
            tcpServer.Running(false);
            tcpServer = null;
        }
        //tcpClient.stopClient();
    }

    @Override
    public void onTcpReceived(Socket socket, String string) {
        Log.e(TAG, "onTcpReceived: " + string );
        //try {
        baseDevices = parseJsonToBaseDev(string);
        Log.e(TAG, "onTcpReceived: sixe: " + baseDevices.size() );
        for (int i=0 ; i<baseDevices.size();i++){
            MainActivity.mDB.addDevice(baseDevices.get(i).getMac(),
                    baseDevices.get(i).getName(),
                    baseDevices.get(i).getPass(),
                    baseDevices.get(i).getTypeId(),
                    baseDevices.get(i).getPermission(),
                    baseDevices.get(i).getPort(),
                    baseDevices.get(i).getLatchPort(),
                    baseDevices.get(i).getUid(),
                    DataBase.Confilict.abort);
            Log.e(TAG, "insert:  " + baseDevices.get(i).getName() );
            //String _mac,String _name ,String _pass, int _tid , int _permission ,int _port,int _latchPort)
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //doubleBounce.reset();
                doubleBounce.setColor(Color.BLUE);
                doubleBounce.setAlpha(255);
                messageTV.setText("received:" + baseDevices.size() + " device");
            }
        });
    }
}
