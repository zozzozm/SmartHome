package ir.yektasmart.smarthome.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import ir.yektasmart.smarthome.MainActivity;
import ir.yektasmart.smarthome.Model.BaseDevice;
import ir.yektasmart.smarthome.Model.Value;
import ir.yektasmart.smarthome.Protocol.Command;
import ir.yektasmart.smarthome.Protocol.ModuleType;
import ir.yektasmart.smarthome.R;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * Created by macbookpro on 10/19/16 AD.
 */
public class BaseDeviceAdapter extends ArrayAdapter<BaseDevice> {

        Context context;

        public BaseDeviceAdapter(Context context, int res, ArrayList<BaseDevice> ar ) {

            super(context,res,ar);
            this.context = context;

        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        public BaseDevice getItem(int i) {
            return super.getItem(i);
        }

        @Override
        public long getItemId(int i) {
            return super.getItemId(i);
        }

    public boolean macIsExist(BaseDevice module) {
        for (int i = 0; i < getCount(); i++) {
            if(getItem(i).getMac().equals(module.getMac()))
                return true;
        }
        return false;
    }

    public synchronized boolean macIsExist(String module) {
        
        for (int i = 0; i < getCount(); i++) {
            if(getItem(i).getMac().equals(module))
                return true;
        }
        return false;
    }

    private class ViewHolder {
            TextView tv;
            Switch sw;
            ImageView iv;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            final ViewHolder holder;

            if(convertView == null)
            {
                //LayoutInflater mInflater = (LayoutInflater) context.getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE);
                convertView = LayoutInflater.from(context).inflate(R.layout.row_module,null);
                holder = new ViewHolder();
                holder.tv = convertView.findViewById(R.id.rowModuleName);
                holder.iv = convertView.findViewById(R.id.rowModuleImage);
                holder.sw = convertView.findViewById(R.id.rowModuleSwitch);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            final BaseDevice rowItem = getItem(position);

            holder.tv.setText(rowItem.getName());

            if(rowItem.getType().equals(ModuleType.RF.toString()) ||
                    rowItem.getType().equals(ModuleType.LATCH002.toString())||
                    rowItem.getType().equals(ModuleType.IR.toString())||
                    rowItem.getType().equals(ModuleType.INTERNET.toString()))
            {
                holder.sw.setEnabled(false);
                holder.sw.setAlpha(0);
            }
            else
            {
                holder.sw.setEnabled(true);
                holder.sw.setAlpha(1);
            }
            //todo image avatar should be depend on text format not int
            holder.iv.setImageResource(rowItem.getAvetar());

           holder.sw.setOnCheckedChangeListener(null);
            if(rowItem.getOnOff() == 0)
                holder.sw.setChecked(false);
            else
                holder.sw.setChecked(true);
            holder.sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    try {

                        MainActivity.mDB.updateOnOff(rowItem.getId(), isChecked?1:0);

                        if (!isChecked)
                            MainActivity.mDB.updateWhiteOn(getItem(position).getId(), false);

                        if (isChecked) getItem(position).setOnOff(1);
                        else getItem(position).setOnOff(0);

                        Value v = new Value();
                        v.setOnOff(isChecked);

                        SendCommand(getItem(position), isChecked? Command.TurnOn:Command.TurnOff, holder.sw.getId(), v);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });
            return convertView;
        }

    private void SendCommand(BaseDevice baseDevice, Command cmd, int id, Value v) {

        Log.e(TAG, "SendCommand: " + cmd + " to " + baseDevice.getName() + " value: " + v);

    }

}
