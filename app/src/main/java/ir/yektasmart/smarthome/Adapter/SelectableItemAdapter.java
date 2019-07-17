package ir.yektasmart.smarthome.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import ir.yektasmart.smarthome.Model.SelectableItem;
import ir.yektasmart.smarthome.R;

/**
 * Created by YektaCo on 17/01/2017.
 */

public class SelectableItemAdapter extends ArrayAdapter<SelectableItem> {

    Context context;

    public SelectableItemAdapter(Context context, int res, ArrayList<SelectableItem> ar ) {

        super(context,res,ar);
        this.context = context;

    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public SelectableItem getItem(int i) {
        return super.getItem(i);
    }

    @Override
    public long getItemId(int i) {
        return super.getItemId(i);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item,null);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.rowItemTitle);
            holder.cb = (CheckBox) convertView.findViewById(R.id.rowItemCheckBox);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SelectableItem rowItem = getItem(position);


        holder.tv.setText(rowItem.getTitle());

        holder.cb.setOnCheckedChangeListener(null);
        if(rowItem.isSelected())
            holder.cb.setChecked(true);
        else
            holder.cb.setChecked(false);

        holder.cb.setEnabled(rowItem.isEnable());


        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                getItem(position).setSelected(isChecked);
            }
        });

        return convertView;
    }

}
