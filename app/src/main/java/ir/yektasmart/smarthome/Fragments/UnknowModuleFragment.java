package ir.yektasmart.smarthome.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ir.yektasmart.smarthome.MainActivity;
import ir.yektasmart.smarthome.Model.BaseDevice;
import ir.yektasmart.smarthome.R;
import ir.yektasmart.smarthome.currentView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UnknowModuleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UnknowModuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnknowModuleFragment extends Fragment {

    int baseDevId = -1;
    String baseName = "";
    BaseDevice baseDevice;
    private OnFragmentInteractionListener mListener;

    public UnknowModuleFragment() {
        // Required empty public constructor
    }


    public static UnknowModuleFragment newInstance(int baseDevId, String devName) {

        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_BaseID,baseDevId);
        args.putString(MainActivity.ARG_BaseName,devName);
        UnknowModuleFragment fragment = new UnknowModuleFragment();
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
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_unknow_module, container, false);
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

        getActivity().setTitle(baseName);
        super.onStart();
        MainActivity.currPage = currentView.deviceActPage;

        baseDevice = MainActivity.mDB.getBaseDevice(baseDevId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /*super.onCreateOptionsMenu(menu, inflater);*/
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_remove, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*return super.onOptionsItemSelected(item);*/

        switch (item.getItemId())
        {
            case R.id.action_remove:
                final YektaDialogFragment  myDialogFragment = new YektaDialogFragment(getResources().getString(R.string.DELETE),
                        getResources().getString(R.string.delete_device),new OnYektaDialogReturn() {
                    @Override
                    public void negetive() {
                    }

                    @Override
                    public void posotive() {

                        MainActivity.mDB.removeDevice(baseDevice.getId(),baseDevice.getTypeId());

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
                break;

        }
        return true;
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
}
