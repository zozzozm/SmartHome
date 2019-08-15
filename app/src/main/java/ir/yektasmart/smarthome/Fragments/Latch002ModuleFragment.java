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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.suke.widget.SwitchButton;

import ir.yektasmart.smarthome.Const;
import ir.yektasmart.smarthome.MainActivity;
import ir.yektasmart.smarthome.Model.BaseDevice;
import ir.yektasmart.smarthome.Model.Value;
import ir.yektasmart.smarthome.Protocol.Command;
import ir.yektasmart.smarthome.Protocol.UtilFunc;
import ir.yektasmart.smarthome.R;
import ir.yektasmart.smarthome.currentView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Latch002ModuleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Latch002ModuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Latch002ModuleFragment extends Fragment implements SwitchButton.OnCheckedChangeListener {


    private static final String TAG = "Latch002";
    private int baseDevId;
    BaseDevice baseDevice;

    com.suke.widget.SwitchButton tb1;
    com.suke.widget.SwitchButton tb2;
    TextView btnDesc;
    int status;

    private OnFragmentInteractionListener mListener;

    public Latch002ModuleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param baseDevId Parameter 1.
     * @param baseName Parameter 2.
     * @return A new instance of fragment Latch002ModuleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Latch002ModuleFragment newInstance(int baseDevId,String baseName) {
        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_BaseID,baseDevId);
        Latch002ModuleFragment fragment = new Latch002ModuleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            baseDevId = this.getArguments().getInt(MainActivity.ARG_BaseID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_latch002_module, container, false);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        MainActivity.currPage = currentView.deviceActPage;

        super.onStart();

        baseDevice = MainActivity.mDB.getBaseDevice(baseDevId);

        getActivity().setTitle(baseDevice.getName());

        status = baseDevice.getOnOff();
        //Log.e(TAG, "onStart: "+status );
        View view = getView();
        if (view != null) {


            tb1 =  view.findViewById(R.id.ToggleButton01);
            tb2 =  view.findViewById(R.id.ToggleButton02);

            tb1.setChecked((status & 0x1) == 0x1);
            tb2.setChecked((status & 0x2) == 0x2);

            tb1.setOnCheckedChangeListener(this);
            tb2.setOnCheckedChangeListener(this);

        }
    }

    @Override
    public void onCheckedChanged(SwitchButton view, boolean on) {

        switch(view.getId()){
            case R.id.ToggleButton01:
                status = status ^  0x1 ;
                MainActivity.mDB.updateOnOff(baseDevId,status);
                Value v =  new Value();
                v.setDimmer(0x1);
                sendCommand((on ? Command.RelayOn : Command.RelayOff),tb1.getId(),v);
                break;
            case R.id.ToggleButton02:
                status = status ^  0x2 ;
                MainActivity.mDB.updateOnOff(baseDevId,status);
                Value vv =  new Value();
                vv.setDimmer(0x2);
                sendCommand((on ? Command.RelayOn : Command.RelayOff),tb2.getId(),vv);
                break;
        }

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
        if(Const.isAwayMode) {
            UtilFunc utilFunc = new UtilFunc(getActivity().getBaseContext());
            utilFunc.CommunicationProtocol(baseDevice, cmd, id, v);
        }else {
            UtilFunc utilFunc = new UtilFunc(getActivity().getBaseContext());
            utilFunc.CommunicationProtocol(baseDevice, cmd, id, v);
        }
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
}
