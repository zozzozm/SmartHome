package ir.yektasmart.smarthome.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.yektasmart.smarthome.Adapter.ExportDeviceAdapter;
import ir.yektasmart.smarthome.Const;
import ir.yektasmart.smarthome.MainActivity;
import ir.yektasmart.smarthome.Model.Access;
import ir.yektasmart.smarthome.Model.BaseDevice;
import ir.yektasmart.smarthome.Model.ExportDeviceModel;
import ir.yektasmart.smarthome.Model.RgbDevice;
import ir.yektasmart.smarthome.Model.User;
import ir.yektasmart.smarthome.R;
import ir.yektasmart.smarthome.TcpClient;

public class ExportDeviceDevSel extends AppCompatActivity  implements View.OnLongClickListener, TcpClient.OnMessageReceived {

    private static final String TAG = "ExportDeviceDevSel";
    ExportDeviceAdapter exportDeviceAdapter;
    ArrayList<BaseDevice> baseModules = new ArrayList<>();
    String cyperString = "";
    String jsonStr = "";
    int selectedUID = -1;

    TcpClient tcpClient;
    EditText selUser;
    ListView selectedDevice_lv;

    TextView wait;
    TextView dots;
    TextView msg;
    private ArrayList<User> dbUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_device_dev_sel);

        selUser = findViewById(R.id.ExportDeviceDevSel_user);
        selectedDevice_lv = findViewById(R.id.ExportDeviceDevSel_LV);

        wait = findViewById(R.id.ExportDeviceDevSel_waiting);
        dots = findViewById(R.id.ExportDeviceDevSel_Dots);
        msg = findViewById(R.id.ExportDeviceDevSel_Message);


        selUser.setText(ExportDeviceUserCon.SELECTED_USER.getName());
        selUser.setOnLongClickListener(this);
        baseModules = MainActivity.mDB.getBaseModulesForExport();
        dbUsers = MainActivity.mDB.getUsers(ExportDeviceUserCon.SELECTED_USER.getUuid());

        if(dbUsers.size() > 0) {
            ArrayList<Access> accesses = new ArrayList<>();
            accesses = MainActivity.mDB.getAccessForUid(dbUsers.get(0).getId());
            for (int i = 0; i < baseModules.size(); i++) {
                for (int j = 0; j < accesses.size(); j++) {
                    if (baseModules.get(i).getId() == accesses.get(j).getMid())
                        baseModules.get(i).setSelected(true);
                }
            }
            selectedUID = dbUsers.get(0).getId();
        }else{
            selectedUID = MainActivity.mDB.addUser(ExportDeviceUserCon.SELECTED_USER.getUuid(),selUser.getText().toString(),R.drawable.user,"No Desc","No cell");
        }

        ArrayList<ExportDeviceModel> expDevice = new ArrayList<>();
        for (int i = 0; i < baseModules.size(); i++) {
            ExportDeviceModel item = new ExportDeviceModel();
            item.setId(baseModules.get(i).getId());
            item.setPermission(1);
            item.setOwnPermission(baseModules.get(i).getPermission());
            if(baseModules.get(i).isSelected())
                item.setTitle(baseModules.get(i).getName() + " (Sent)");
            else
                item.setTitle(baseModules.get(i).getName());
            expDevice.add(item);
        }

        exportDeviceAdapter = new ExportDeviceAdapter(getApplication(),R.layout.row_export_device,expDevice);
        selectedDevice_lv.setAdapter(exportDeviceAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_save:

                int count = 0;
                int uid = -1;
                try {
                    // TODO: 8/15/2017 remove this  try block if export make a crash
                    tcpClient.stopClient();
                }catch (Exception e){
                    e.printStackTrace();
                }

                if(!ExportDeviceUserCon.SELECTED_USER.getName().equals(selUser.getText().toString())){
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.mDB.updateUser(selectedUID,selUser.getText().toString());
                        }
                    });
                    thread.start();
                }
                if(dbUsers.size()==0) {
                    uid = MainActivity.mDB.addUser(ExportDeviceUserCon.SELECTED_USER.getUuid(),
                            ExportDeviceUserCon.SELECTED_USER.getName(), 0, "desc", "noCell");
                }
                for (int i = 0; i < baseModules.size(); i++) {

                    if (exportDeviceAdapter.getItem(i).isSelected()) {
                        count++;
                        MainActivity.mDB.addAccess(baseModules.get(i).getId(), selectedUID, exportDeviceAdapter.getItem(i).getPermission(),baseModules.get(i).getPermission()==0?0:1);
                        if (baseModules.get(i).getPermission() == 0) {
                            baseModules.get(i).setUid(selectedUID);
                        }
                    }
                }
                JSONArray jsonBaseArray = getBaseDevJson(baseModules);

                JSONObject jasonAll = new JSONObject();
                try {
                    jasonAll.put("DEVICE",jsonBaseArray);
                    jsonStr = jasonAll.toString();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e(TAG, "selectedDevice: " + jsonStr );
                tcpClient = new TcpClient(jsonStr+"\n" ,ExportDeviceUserCon.SELECTED_USER.getDescription(), Const.TCP_PORT, this);
                tcpClient.start();
                msg.setText("Sent " + count + " devices to " + ExportDeviceUserCon.SELECTED_USER.getName());
                break;
        }
        return true;
    }

    private JSONArray getRgbDevJson(ArrayList<RgbDevice> rgbDevices) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < rgbDevices.size(); i++) {

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rgb_id", rgbDevices.get(i).getId());
                jsonObject.put("rgb_pid", rgbDevices.get(i).getPid());

                jsonArray.put(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {

            return jsonArray;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONArray getBaseDevJson(ArrayList<BaseDevice> baseModules) {
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < baseModules.size(); i++) {

            try {
                if(exportDeviceAdapter.getItem(i).isSelected()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("_id", baseModules.get(i).getId());
                    jsonObject.put("_name", baseModules.get(i).getName());
                    jsonObject.put("_pass", baseModules.get(i).getPass());
                    jsonObject.put("_mac", baseModules.get(i).getMac());
                    jsonObject.put("_tid", baseModules.get(i).getTypeId());
                    jsonObject.put("_avt", baseModules.get(i).getAvetar());
                    jsonObject.put("_lPrt", baseModules.get(i).getLatchPort());
                    jsonObject.put("_perm", exportDeviceAdapter.getItem(i).getPermission());
                    jsonObject.put("_port", baseModules.get(i).getPort());
                    jsonObject.put("_onOff", baseModules.get(i).getOnOff());
                    jsonObject.put("_uid", baseModules.get(i).getUid());

                    jsonArray.put(jsonObject);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {

            return jsonArray;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onLongClick(View v) {

        return false;
    }

    @Override
    public void TcpMessageReceived(String message) {

    }

    @Override
    public void ConnectionFailed(int status, String onIp) {

    }
}
