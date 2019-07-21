package ir.yektasmart.smarthome.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suke.widget.SwitchButton;

import ir.yektasmart.smarthome.MainActivity;
import ir.yektasmart.smarthome.Model.BaseDevice;
import ir.yektasmart.smarthome.Model.Value;
import ir.yektasmart.smarthome.Protocol.Command;
import ir.yektasmart.smarthome.R;
import ir.yektasmart.smarthome.currentView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Latch001ModuleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Latch001ModuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Latch001ModuleFragment extends Fragment implements SwitchButton.OnCheckedChangeListener {

    private static final String TAG = "Latch001";
    int baseDevId;
    String baseName;
    BaseDevice baseDevice;

    com.suke.widget.SwitchButton tb1;
    TextView btnDesc;
    int status;

    private OnFragmentInteractionListener mListener;

    public Latch001ModuleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param baseDevId Parameter 1.
     * @param devName Parameter 2.
     * @return A new instance of fragment Latch001ModuleFragment.
     */
    public static Latch001ModuleFragment newInstance(int baseDevId, String devName ) {

        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_BaseID,baseDevId);
        args.putString(MainActivity.ARG_BaseName,devName);
        Latch001ModuleFragment fragment = new Latch001ModuleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            baseDevId = this.getArguments().getInt(MainActivity.ARG_BaseID);
            baseName = this.getArguments().getString(MainActivity.ARG_BaseName);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_latch001_module, container, false);
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
        MainActivity.currPage = currentView.deviceActPage;
        getActivity().setTitle(baseName);
        super.onStart();

        baseDevice = MainActivity.mDB.getBaseDevice(baseDevId);
        getActivity().setTitle(baseDevice.getName());

        View view = getView();
        if (view != null) {


            tb1 = view.findViewById(R.id.switch_button);

            tb1.setChecked(baseDevice.getOnOff()==1);

            tb1.setOnCheckedChangeListener(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCheckedChanged(SwitchButton view, boolean on) {
        baseDevice.setOnOff(1);
        MainActivity.mDB.updateOnOff(baseDevId,on?1:0);

        Value v =  new Value();
        v.setDimmer(0x1);
        sendCommand((on ? Command.RelayOn : Command.RelayOff),tb1.getId(),v);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /*super.onCreateOptionsMenu(menu, inflater);*/
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_edit_remove, menu);
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
        void onFragmentInteraction(Uri uri);
    }

    void sendCommand(Command cmd, int id, Value v){
//        if(Const.isAwayMode) {
//            //MainActivity.CommunicationProtocol(baseDevice, cmd, id, v);
//            UtilFunc utilFunc = new UtilFunc(getActivity().getBaseContext());
//            utilFunc.CommunicationProtocol(baseDevice, cmd, id, v);
//            //MainActivity.sendSms(baseDevice, cmd,null);
//        }else {
//            //MainActivity.CommunicationProtocol(baseDevice, cmd, id, v);
//            UtilFunc utilFunc = new UtilFunc(getActivity().getBaseContext());
//            utilFunc.CommunicationProtocol(baseDevice, cmd, id, v);
//        }
    }
}
