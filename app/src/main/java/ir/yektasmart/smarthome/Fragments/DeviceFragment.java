package ir.yektasmart.smarthome.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import ir.yektasmart.smarthome.Adapter.BaseDeviceAdapter;
import ir.yektasmart.smarthome.MainActivity;
import ir.yektasmart.smarthome.Model.BaseDevice;
import ir.yektasmart.smarthome.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeviceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceFragment extends Fragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ArrayList<BaseDevice> baseDeviceArrayList;
    ListView lv_Device;
    BaseDeviceAdapter modules;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DeviceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeviceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeviceFragment newInstance(String param1, String param2) {
        DeviceFragment fragment = new DeviceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onClickDev(BaseDevice g);
        void onFragmentInteraction(Uri uri);
    }
}
