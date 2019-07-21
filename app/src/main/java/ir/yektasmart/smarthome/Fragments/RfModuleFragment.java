package ir.yektasmart.smarthome.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ir.yektasmart.smarthome.Const;
import ir.yektasmart.smarthome.MainActivity;
import ir.yektasmart.smarthome.Model.BaseDevice;
import ir.yektasmart.smarthome.Protocol.Command;
import ir.yektasmart.smarthome.Protocol.UtilFunc;
import ir.yektasmart.smarthome.R;
import ir.yektasmart.smarthome.currentView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RfModuleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RfModuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RfModuleFragment extends Fragment implements View.OnTouchListener, View.OnLongClickListener, View.OnClickListener {

    int baseDevId;
    String baseName;
    BaseDevice baseDevice;

    Button[] btn = new Button[15];
    TextView btnDesc;

    boolean[] flags = new boolean[15];

    private OnFragmentInteractionListener mListener;

    public RfModuleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param baseDevId Parameter 1.
     * @param devName Parameter 2.
     * @return A new instance of fragment RfModuleFragment.
     */
    public  static RfModuleFragment newInstance(int baseDevId, String devName ) {

        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_BaseID,baseDevId);
        args.putString(MainActivity.ARG_BaseName,devName);
        RfModuleFragment fragment = new RfModuleFragment();
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
        return inflater.inflate(R.layout.fragment_rf_module, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        MainActivity.currPage = currentView.deviceActPage;

        baseDevice = MainActivity.mDB.getBaseDevice(baseDevId);
        getActivity().setTitle(baseDevice.getName());

        super.onStart();
        View view = getView();
        if (view != null) {
            btn[0]  = (Button) view.findViewById(R.id.btn1);
            btn[1]  = (Button) view.findViewById(R.id.btn2);
            btn[2]  = (Button) view.findViewById(R.id.btn3);
            btn[3]  = (Button) view.findViewById(R.id.btn4);
            btn[4]  = (Button) view.findViewById(R.id.btn5);
            btn[5]  = (Button) view.findViewById(R.id.btn6);
            btn[6]  = (Button) view.findViewById(R.id.btn7);
            btn[7]  = (Button) view.findViewById(R.id.btn8);
            btn[8]  = (Button) view.findViewById(R.id.btn9);
            btn[9]  = (Button) view.findViewById(R.id.btn10);
            btn[10] = (Button) view.findViewById(R.id.btn11);
            btn[11] = (Button) view.findViewById(R.id.btn12);
            btn[12] = (Button) view.findViewById(R.id.btn13);
            btn[13] = (Button) view.findViewById(R.id.btn14);
            btn[14] = (Button) view.findViewById(R.id.btn15);

            for (int index = 0; index < btn.length; index++) {
                btn[index].setOnClickListener(this);
                btn[index].setOnLongClickListener(this);
                btn[index].setOnTouchListener(this);
            }
            btnDesc = (TextView) view.findViewById(R.id.rfClickDes);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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

    @Override
    public void onClick(View view) {

        sendCommand(Command.RemoteClick,view.getId());
    }

    @Override
    public boolean onLongClick(View view) {

        sendCommand(Command.RemoteLongClickDown,view.getId());
        switch(view.getId())
        {
            case R.id.btn1:
                flags[0] = true;
                break;

            case R.id.btn2:
                flags[1] = true;
                break;

            case R.id.btn3:
                flags[2] = true;
                break;

            case R.id.btn4:
                flags[3] = true;
                break;

            case R.id.btn5:
                flags[4] = true;
                break;

            case R.id.btn6:
                flags[5] = true;
                break;

            case R.id.btn7:
                flags[6] = true;
                break;

            case R.id.btn8:
                flags[7] = true;
                break;

            case R.id.btn9:
                flags[8] = true;
                break;

            case R.id.btn10:
                flags[9] = true;
                break;

            case R.id.btn11:
                flags[10] = true;
                break;

            case R.id.btn12:
                flags[11] = true;
                break;

            case R.id.btn13:
                flags[12] = true;
                break;

            case R.id.btn14:
                flags[13] = true;
                break;

            case R.id.btn15:
                flags[14] = true;
                break;
        }
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int id = view.getId();
        switch (id)
        {
            case R.id.btn1:
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    return false;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    if(flags[0]){
                        flags[0] = false;
                        sendCommand(Command.RemoteLongClickUp,view.getId());
                    }
                }
                break;
            case R.id.btn2:
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    return false;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    if(flags[1]){
                        flags[1] = false;
                        sendCommand(Command.RemoteLongClickUp,view.getId());
                    }
                }
                break;
            case R.id.btn3:
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    return false;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    if(flags[2]){
                        flags[2] = false;
                        sendCommand(Command.RemoteLongClickUp,view.getId());
                    }
                }
                break;
            case R.id.btn4:
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    return false;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    if(flags[3]){
                        flags[3] = false;
                        sendCommand(Command.RemoteLongClickUp,view.getId());
                    }
                }
                break;
            case R.id.btn5:
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    return false;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    if(flags[4]){
                        flags[4] = false;
                        sendCommand(Command.RemoteLongClickUp,view.getId());
                    }
                }
                break;
            case R.id.btn6:
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    return false;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    if(flags[5]){
                        flags[5] = false;
                        sendCommand(Command.RemoteLongClickUp,view.getId());
                    }
                }
                break;
            case R.id.btn7:
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    return false;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    if(flags[6]){
                        flags[6] = false;
                        sendCommand(Command.RemoteLongClickUp,view.getId());
                    }
                }
                break;
            case R.id.btn8:
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    return false;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    if(flags[7]){
                        flags[7] = false;
                        sendCommand(Command.RemoteLongClickUp,view.getId());
                    }
                }
                break;
            case R.id.btn9:
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    return false;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    if(flags[8]){
                        flags[8] = false;
                        sendCommand(Command.RemoteLongClickUp,view.getId());
                    }
                }
                break;
            case R.id.btn10:
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    return false;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    if(flags[9]){
                        flags[9] = false;
                        sendCommand(Command.RemoteLongClickUp,view.getId());
                    }
                }
                break;
            case R.id.btn11:
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    return false;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    if(flags[10]){
                        flags[10] = false;
                        sendCommand(Command.RemoteLongClickUp,view.getId());
                    }
                }
                break;
            case R.id.btn12:
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    return false;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    if(flags[11]){
                        flags[1] = false;
                        sendCommand(Command.RemoteLongClickUp,view.getId());
                    }
                }
                break;
            case R.id.btn13:
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    return false;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    if(flags[12]){
                        flags[12] = false;
                        sendCommand(Command.RemoteLongClickUp,view.getId());
                    }
                }
                break;
            case R.id.btn14:
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    return false;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    if(flags[13]){
                        flags[13] = false;
                        sendCommand(Command.RemoteLongClickUp,view.getId());
                    }
                }
                break;
            case R.id.btn15:
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    return false;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    if(flags[14]){
                        flags[14] = false;
                        sendCommand(Command.RemoteLongClickUp,view.getId());
                    }
                }
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*return super.onOptionsItemSelected(item);*/

        switch (item.getItemId())
        {
            case R.id.action_edit:

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                EditModuleFragment editModuleFragment = EditModuleFragment.newInstance(baseDevice.getId());
                ft.replace(R.id.contentContainer, editModuleFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.action_remove:
                if (baseDevice.getUid() != 0) {
                    final YektaDialogFragment myDialogFragment = new YektaDialogFragment(getResources().getString(R.string.DELETE),
                            getResources().getString(R.string.delete_device), new OnYektaDialogReturn() {
                        @Override
                        public void negetive() {
                        }

                        @Override
                        public void posotive() {

                            MainActivity.mDB.removeDevice(baseDevice.getId(), baseDevice.getTypeId());

                            DeviceFragment devFragment = new DeviceFragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.contentContainer, devFragment)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .addToBackStack(null)
                                    .commit();
                            Toast.makeText(getActivity(), "Removed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    myDialogFragment.show(getFragmentManager(), "DialogFragment");
                }else {
                    final YektaPromptFragment myDialogFragment = new YektaPromptFragment(getResources().getString(R.string.DELETE),
                            getResources().getString(R.string.admin_delete_device), new OnYektaPromptReturn() {
                        @Override
                        public void negetive() {
                        }

                        @Override
                        public void posotive(String withInput) {

                            if(MainActivity.shP.loadExtraString(Const.SP_LoginPaswword).equals(withInput)) {
                                MainActivity.mDB.removeDevice(baseDevice.getId(), baseDevice.getTypeId());

                                DeviceFragment devFragment = new DeviceFragment();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.contentContainer, devFragment)
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .addToBackStack(null)
                                        .commit();
                                Toast.makeText(getActivity(), "Removed", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(), "Wrong password.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    myDialogFragment.show(getFragmentManager(), "DialogFragment");
                }
                break;
        }

        return true;
    }

    void sendCommand(Command cmd, int id){
        if(Const.isAwayMode) {
            UtilFunc utilFunc = new UtilFunc(getActivity().getBaseContext());
            utilFunc.CommunicationProtocol(baseDevice, cmd, id, null);
        }else {
            UtilFunc utilFunc = new UtilFunc(getActivity().getBaseContext());
            utilFunc.CommunicationProtocol(baseDevice, cmd, id, null);
        }
    }
}
