package ir.yektasmart.smarthome.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ir.yektasmart.smarthome.DelayThread;
import ir.yektasmart.smarthome.Fragments.EditModuleFragment;
import ir.yektasmart.smarthome.MainActivity;
import ir.yektasmart.smarthome.Model.BaseDevice;
import ir.yektasmart.smarthome.Model.User;
import ir.yektasmart.smarthome.Model.Value;
import ir.yektasmart.smarthome.Protocol.Command;
import ir.yektasmart.smarthome.Protocol.UtilFunc;
import ir.yektasmart.smarthome.R;

public class UserAdapter extends ArrayAdapter<User> {

    private  static String TAG = "UserAdapter";
    ProgressDialog progressDialog1 ;
    Context context;
    private BaseDevice baseDevice;

    public UserAdapter(Context context, int res, ArrayList<User> ar ) {

        super(context,res,ar);
        this.context = context;

    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public User getItem(int i) {
        return super.getItem(i);
    }

    @Override
    public long getItemId(int i) {
        return super.getItemId(i);
    }

    public void setBaseDevice(BaseDevice baseDevice) {
        this.baseDevice = baseDevice;
    }

    private class ViewHolder {
        TextView tv;
        CheckBox cb;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final ViewHolder holder;

        if(convertView == null)
        {
            //LayoutInflater mInflater = (LayoutInflater) context.getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE);
            convertView = LayoutInflater.from(context).inflate(R.layout.row_user,null);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.rowUserTitle);
            holder.cb = (CheckBox) convertView.findViewById(R.id.rowUserCheckBox);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final User rowItem = getItem(position);

        holder.tv.setText(rowItem.getName());
        holder.cb.setOnCheckedChangeListener(null);

        if(rowItem.getIsActive() == 1)
            holder.cb.setChecked(true);
        else
            holder.cb.setChecked(false);

        if(baseDevice.getPermission() != 0){
            holder.cb.setEnabled(false);
        }else{
            holder.cb.setEnabled(true);
        }

        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Value vv = new Value();
                vv.setDimmer(rowItem.getId());//is user id

                UtilFunc utilFunc = new UtilFunc(context);
                utilFunc.CommunicationProtocol(baseDevice,holder.cb.isChecked()? Command.UserPermissionSet:Command.UserPermissionReset,R.id.rightArr,vv);
                //MainActivity.CommunicationProtocol(baseDevice,holder.cb.isChecked()?Command.UserPermissionSet:Command.UserPermissionReset,R.id.rightArr,vv);
                progressDialog1 = new ProgressDialog(context);
                progressDialog1 = ProgressDialog.show(context,"Permission","Wait until permission modification completed.",false);

                if(holder.cb.isChecked())
                    EditModuleFragment.SET_OR_RESET = 1 ;
                else
                    EditModuleFragment.SET_OR_RESET = 0 ;

                EditModuleFragment.MAC_ADDRESS = baseDevice.getMac();

                EditModuleFragment.USER_ID = rowItem.getId();

                Handler time = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        progressDialog1.dismiss();
                        if(EditModuleFragment.RESULT_STATUS){
                            holder.cb.setError(null);
                            Toast.makeText(context, "Successfully allowed permission.", Toast.LENGTH_SHORT).show();
                            EditModuleFragment.RESULT_STATUS = false;
                            EditModuleFragment.USER_ID = -1;
                            EditModuleFragment.MAC_ADDRESS = "-";
                        }else {
                            holder.cb.setChecked(EditModuleFragment.SET_OR_RESET==1?false:true);
                            holder.cb.setError("Failed");
                            Toast.makeText(context, "Failed to allow permission", Toast.LENGTH_SHORT).show();
                            EditModuleFragment.USER_ID = -1;
                            EditModuleFragment.MAC_ADDRESS = "-";
                        }

                        MainActivity.mDB.updateAccess(baseDevice.getId(),rowItem.getId(),holder.cb.isChecked()?1:0);

                    }
                };

                DelayThread t = new DelayThread(time, 5000);
                t.execute();
            }
        });

        return convertView;
    }

}
