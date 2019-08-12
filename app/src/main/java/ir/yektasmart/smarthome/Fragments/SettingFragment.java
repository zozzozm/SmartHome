package ir.yektasmart.smarthome.Fragments;

import android.content.Context;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import ir.yektasmart.smarthome.Const;
import ir.yektasmart.smarthome.MainActivity;
import ir.yektasmart.smarthome.R;
import ir.yektasmart.smarthome.currentView;

import static ir.yektasmart.smarthome.MainActivity.shP;

public class SettingFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    Switch vibration;
    Switch protection;
    Switch awayMode;
    WifiManager wifi;
    RelativeLayout changePassRow;

    public interface AwayModeChangeListener{
        void awayModeChanged(Boolean isAway);
    }

    AwayModeChangeListener awayModeListener;

    public AwayModeChangeListener getAwayModeListener() {
        return awayModeListener;
    }

    public void setAwayModeListener(AwayModeChangeListener awayModeListener) {
        this.awayModeListener = awayModeListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.settingSecurity:
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ChangepasswordFragment changepasswordFragment = new ChangepasswordFragment();
                ft.replace(R.id.contentContainer, changepasswordFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
                break;
        }
    }

    private OnFragmentInteractionListener mListener;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_setting,container,false);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /*super.onCreateOptionsMenu(menu, inflater);*/
        menu.clear();
        //getActivity().getMenuInflater().inflate(R.menu.menu_save, menu);
    }

    @Override
    public void onStart() {
        getActivity().setTitle(getResources().getString(R.string.settings));
        super.onStart();
        MainActivity.currPage = currentView.settingPage;

        View view = getView();
        if(view != null){

            vibration = (Switch) view.findViewById(R.id.settingVib);
            protection = (Switch) view.findViewById(R.id.settingProtection);
            awayMode = (Switch) view.findViewById(R.id.settingAwayMode);
            changePassRow = (RelativeLayout) view.findViewById(R.id.settingSecurity);


            if (Const.isProtectedMode){
                protection.setChecked(true);
            }else {
                protection.setChecked(false);
            }

            vibration.setChecked(Const.isVibrateMode);
            awayMode.setChecked(Const.isAwayMode);

            vibration.setOnCheckedChangeListener(this);
            protection.setOnCheckedChangeListener(this);
            awayMode.setOnCheckedChangeListener(this);
            changePassRow.setOnClickListener(this);

            View viewS = getActivity().getCurrentFocus();
            if (viewS != null) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }


            if(MainActivity.PERMISSIONS == 0 ){
                awayMode.setEnabled(false);
            }

            awayMode.setEnabled(true);
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

        switch(compoundButton.getId()){

            case R.id.settingAwayMode:
                awayModeListener.awayModeChanged(isChecked);
                break;

            case  R.id.settingVib:
                saveExt(Const.SP_VibrateMode,isChecked);
                Const.isVibrateMode = isChecked;
                break;

            case R.id.settingProtection:
                if(isChecked)
                    Toast.makeText(getActivity(),getResources().getString(R.string.securityOn), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(),getResources().getString(R.string.securityOff), Toast.LENGTH_SHORT).show();
                saveExt(Const.SP_LoginIsProtected,isChecked);
                Const.isProtectedMode = isChecked;

                if(protection.isChecked()){
                    Toast.makeText(getActivity(), "Protection mode Enabled.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "Protection mode Disabled.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void saveExt(String key, boolean isChecked) {
        shP.saveExtraBool(key,isChecked);
    }

    @Override
    public void onDestroy() {
        awayModeListener = null;
        super.onDestroy();
    }

    @Override
    public void onPause() {
        awayModeListener = null;
        super.onPause();
    }
}
