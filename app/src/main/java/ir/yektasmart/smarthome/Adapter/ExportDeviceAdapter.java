package ir.yektasmart.smarthome.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ir.yektasmart.smarthome.Model.ExportDeviceModel;
import ir.yektasmart.smarthome.R;

public class ExportDeviceAdapter  extends ArrayAdapter<ExportDeviceModel> {

    Context context;

    public ExportDeviceAdapter(Context context, int res, ArrayList<ExportDeviceModel> ar ) {

        super(context,res,ar);
        this.context = context;

    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public ExportDeviceModel getItem(int i) {
        return super.getItem(i);
    }

    @Override
    public long getItemId(int i) {
        return super.getItemId(i);
    }

    private class ViewHolder {
        ImageView iv;
        TextView tv;
        CheckBox cb;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final ViewHolder holder;

        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_export_device,null);
            holder = new ViewHolder();
            holder.iv = convertView.findViewById(R.id.row_expDev_pic);
            holder.tv = convertView.findViewById(R.id.row_expDev_title);
            holder.cb = convertView.findViewById(R.id.row_expDev_box);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ExportDeviceModel rowItem = getItem(position);


        holder.tv.setText(rowItem.getTitle());

        holder.cb.setOnCheckedChangeListener(null);
        if(rowItem.isSelected()) {
            holder.cb.setChecked(true);
            switch (rowItem.getPermission()%3){
                case 0:
                    holder.iv.setImageResource(R.drawable.key);
                    break;

                case 1:
                    holder.iv.setImageResource(R.drawable.unlock);
                    break;

                case 2:
                    holder.iv.setImageResource(R.drawable.lock);
                    break;
            }

        }
        else {
            holder.cb.setChecked(false);
            holder.iv.setImageResource(R.drawable.ballun);
        }

        holder.cb.setEnabled(rowItem.isEnable());


        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.cb.isChecked()) {
                    rowItem.setPermission( (rowItem.getPermission() + 1) % 3);
                    if(rowItem.getPermission() <= rowItem.getOwnPermission()) {
                        rowItem.setPermission( (rowItem.getPermission() + 1) % 3);
                    }
                    switch (rowItem.getPermission() % 3) {
                        case 0:
                            holder.iv.setImageResource(R.drawable.key);
                            break;

                        case 1:
                            holder.iv.setImageResource(R.drawable.unlock);
                            break;

                        case 2:
                            holder.iv.setImageResource(R.drawable.lock);
                            break;
                    }
                }else {
                    Toast.makeText(context, "First select device.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                getItem(position).setSelected(isChecked);
                if(!isChecked)
                    holder.iv.setImageResource(R.drawable.ballun);
                else {
                    rowItem.setPermission(1);
                    switch (rowItem.getPermission() % 3) {
                        case 0:
                            holder.iv.setImageResource(R.drawable.key);
                            break;

                        case 1:
                            holder.iv.setImageResource(R.drawable.unlock);
                            break;

                        case 2:
                            holder.iv.setImageResource(R.drawable.lock);
                            break;
                    }
                }

            }
        });

        return convertView;
    }

}