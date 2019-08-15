package ir.yektasmart.smarthome.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.yektasmart.smarthome.Model.User;
import ir.yektasmart.smarthome.R;

public class UserRowAdapter  extends ArrayAdapter<User> {

    private  static String TAG = "UserRowAdapter";
    Context context;

    public UserRowAdapter(Context context, int res, ArrayList<User> ar ) {

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

    private class ViewHolder {
        TextView tv;
        ImageView iv;
        TextView des;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final ViewHolder holder;

        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_connected_user,null);
            holder = new ViewHolder();
            holder.tv = convertView.findViewById(R.id.row_conUserTitle);
            holder.iv = convertView.findViewById(R.id.row_conUserPic);
            holder.des = convertView.findViewById(R.id.row_conUserDesc);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final User rowItem = getItem(position);

        holder.tv.setText(rowItem.getName());
        holder.des.setText(rowItem.getDescription());
        return convertView;
    }

}