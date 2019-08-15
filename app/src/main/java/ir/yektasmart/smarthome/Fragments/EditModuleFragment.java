package ir.yektasmart.smarthome.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ir.yektasmart.smarthome.Adapter.UserAdapter;
import ir.yektasmart.smarthome.MainActivity;
import ir.yektasmart.smarthome.Model.BaseDevice;
import ir.yektasmart.smarthome.Model.User;
import ir.yektasmart.smarthome.R;
import ir.yektasmart.smarthome.UDPListenerService;
import ir.yektasmart.smarthome.currentView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditModuleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditModuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditModuleFragment extends Fragment {

    private static final String TAG = "Edit Device";
    //Module g;
    int baseDevId = -1;
    BaseDevice baseDevice;

    public static int USER_ID = -1;
    public static int SET_OR_RESET = -1;
    public static boolean RESULT_STATUS = false;
    public static String MAC_ADDRESS = "---";

    EditText etModuleName;
    EditText etModulePass;
    ListView userListView;
    TextView userTitle;
    private BroadcastReceiver mReceiver;

    ArrayList<User> usersArray;
    UserAdapter users;

    private OnFragmentInteractionListener mListener;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();
            handleResult(bundle);
        }

    };

    public EditModuleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param _baseDevId Parameter 1.
     * @return A new instance of fragment EditModuleFragment.
     */
    public static EditModuleFragment newInstance(int _baseDevId) {
        EditModuleFragment fragment = new EditModuleFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(MainActivity.ARG_BaseID, _baseDevId);
        fragment.setArguments(arguments);
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
        return inflater.inflate(R.layout.fragment_edit_module, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /*super.onCreateOptionsMenu(menu, inflater);*/
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_save, menu);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        /*return super.onOptionsItemSelected(item);*/
        switch (item.getItemId()) {
            case R.id.action_save:
                if(checkInputParameters())
                {
                    int selectedCounter = 0;
                    for (int i = 0; i < users.getCount(); i++) {
                        if (users.getItem(i).isSelected()) selectedCounter++;
                    }
                    //Toast.makeText(getActivity(), selectedCounter + " is selected", Toast.LENGTH_SHORT).show();
                    // Check if no view has focus: then close soft keyboard
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    MainActivity.mDB.updateBase(baseDevId,etModuleName.getText().toString(),etModulePass.getText().toString());

                    Toast.makeText(getActivity(), "Successfully Changed.", Toast.LENGTH_SHORT).show();

                }
                break;

        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        //getActivity().registerReceiver(this.broadcastReceiver, new IntentFilter("aliIntentProvider"));
        getActivity().registerReceiver(receiver, new IntentFilter(UDPListenerService.UDP_BROADCAST));


    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(this.receiver);
    }

    private void handleResult(Bundle bundle) {
        if (bundle != null) {
            String string = bundle.getString("sender");
            String message = bundle.getString("message");
            byte[] msg = bundle.getByteArray("msg");
            //Toast.makeText(getActivity(), string + ":" + msg, Toast.LENGTH_SHORT).show();
            if (ValidateMessage(msg))
                EditModuleFragment.RESULT_STATUS = true;
        }
    }

    private boolean ValidateMessage(byte[] msg) {

        //Log.e(TAG, "ReceiveMsgValidation: " + msg.length);
        int i = msg[36] * 256 + msg[37];
        //Log.e(TAG, "ReceiveMsgValidation: uid " + i + "=" + EditModuleFragment.USER_ID);

        byte[] _mac = new byte[12];
        for (int j = 0; j < 12; j++) {
            _mac[j] = msg[4+j];
        }
        String mac = new String(_mac);

        Toast.makeText(getContext(), "from mac: " + mac, Toast.LENGTH_SHORT).show();

        if (i == EditModuleFragment.USER_ID && EditModuleFragment.MAC_ADDRESS.equals(mac) ) {

            //Log.e(TAG, "ReceiveMsgValidation: isSuccessfully " + msg[44]);
            if(msg[43] == EditModuleFragment.SET_OR_RESET) {
                if (msg[44] == 1) {
                    return true;//is successfully or failed
                }
                return false;
            }
        }
        return false;
    }


    private boolean checkInputParameters() {

        boolean flag = true;
        // TODO: 10/20/16 AD  change setError icon
        if(etModulePass.length() != 4)
        {
            etModulePass.setError(getResources().getString(R.string.devPassLenWrong));
            flag = false;
        }
        if(etModuleName.length() > 64)
        {
            etModuleName.setError(getResources().getString(R.string.devNameWrong));
            flag = false;
        }
        if(etModuleName.length() == 0)
        {
            etModuleName.setError(getResources().getString(R.string.devNameWrongEmpty));
            flag = false;
        }
        return  flag;
    }


    @Override
    public void onStart() {

        MainActivity.currPage = currentView.deviceActPage;

        if (baseDevId == -1)
            Log.e(TAG, "onStart: " + "baseDevId is -1 in starting fragment");
        else {
            baseDevice = MainActivity.mDB.getBaseDevice(baseDevId);
            usersArray = MainActivity.mDB.getBaseDeviceUsers(baseDevId);
            if (baseDevice == null || usersArray == null) {
                Log.e(TAG, "onStart: " + "baseDevId is null in getting from database or something else");
            } else {
                getActivity().setTitle(baseDevice.getName());
                super.onStart();

                View view = getView();
                if (view != null) {
                    etModuleName = (EditText) view.findViewById(R.id.editModuleName);
                    etModulePass = (EditText) view.findViewById(R.id.editModulePass);
                    userListView = (ListView) view.findViewById(R.id.userListView);
                    userTitle = (TextView) view.findViewById(R.id.userTitle);
                    etModuleName.setText(baseDevice.getName());
                    etModulePass.setText(baseDevice.getPass());

                    if (baseDevice.getType().equals("INTERNET")){
                        baseDevice.setPermission(1);// to be != 0 and  disable checkboxes
                        etModulePass.setEnabled(false);//to disable password field
                        for (int i = 0; i < usersArray.size() ; i++) {
                            usersArray.get(i).setIsActive(1);
                        }
                    }

                    users = new UserAdapter(getContext(), R.layout.row_user, this.usersArray);
                    users.setBaseDevice(baseDevice);
                    //users.setBaseDevice(baseDevice);
                    userListView.setAdapter(users);
                    //userListView.setOnItemClickListener(this);

                    userTitle.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            Toast.makeText(getContext(), "Mac:" + baseDevice.getMac()+ "\nPort:"+baseDevice.getPort(), Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });


                }
            }
        }
    }

}
