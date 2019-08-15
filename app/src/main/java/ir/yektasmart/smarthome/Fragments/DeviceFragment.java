package ir.yektasmart.smarthome.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import ir.yektasmart.smarthome.Activity.ExportDeviceUserCon;
import ir.yektasmart.smarthome.Activity.ImportDevice;
import ir.yektasmart.smarthome.Activity.addDevice;
import ir.yektasmart.smarthome.Activity.preAddDeviceActivity;
import ir.yektasmart.smarthome.Adapter.BaseDeviceAdapter;
import ir.yektasmart.smarthome.MainActivity;
import ir.yektasmart.smarthome.Model.BaseDevice;
import ir.yektasmart.smarthome.R;


public class DeviceFragment extends Fragment implements AdapterView.OnItemClickListener {


    ArrayList<BaseDevice> baseDeviceArrayList;
    ListView lv_Device;
    BaseDeviceAdapter modules;

    private OnFragmentInteractionListener mListener;

    public DeviceFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_device, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        try {

            super.onStart();

            baseDeviceArrayList = MainActivity.mDB.getBaseModules();

            View view = getView();

            if (view != null) {
                //container = (LinearLayout) view.findViewById(R.id.deviceLinearLayout);
                lv_Device = (ListView) view.findViewById(R.id.lv_dev);
                lv_Device.setItemsCanFocus(false);
                modules = new BaseDeviceAdapter(getActivity(), R.layout.row_module, this.baseDeviceArrayList);
                lv_Device.setAdapter(modules);
                lv_Device.setOnItemClickListener(this);

            }
        }catch (Exception e){e.printStackTrace();}

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        try {
            mListener.onClickDev(baseDeviceArrayList.get(position));
            Animation moveRight = AnimationUtils.loadAnimation(getActivity(), R.anim.row_select_effect);
            view.startAnimation(moveRight);
        }catch (Exception e){e.printStackTrace();}
    }

    public interface OnFragmentInteractionListener {
        void onClickDev(BaseDevice g);
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_add_new_device:
                Intent intent = new Intent(getActivity(), preAddDeviceActivity.class);
                intent.putExtra("from","Main");
                startActivity(intent);
                return true;

            case R.id.action_export:
                Intent intent1 = new Intent(getActivity(), ExportDeviceUserCon.class);
                startActivity(intent1);
                break;

            case R.id.action_import:
                Intent intent2 = new Intent(getActivity(), ImportDevice.class);
                startActivity(intent2);
                break;
        }


        return true;
    }
}
