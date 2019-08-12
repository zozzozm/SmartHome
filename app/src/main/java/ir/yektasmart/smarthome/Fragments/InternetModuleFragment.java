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
import android.widget.Toast;

import ir.yektasmart.smarthome.Const;
import ir.yektasmart.smarthome.MainActivity;
import ir.yektasmart.smarthome.Model.BaseDevice;
import ir.yektasmart.smarthome.R;
import ir.yektasmart.smarthome.currentView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InternetModuleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InternetModuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InternetModuleFragment extends Fragment {

    int baseDevId = -1;
    String baseName = "";
    BaseDevice baseDevice;

    private OnFragmentInteractionListener mListener;

    public InternetModuleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param baseDevId Parameter 1.
     * @param devName Parameter 2.
     * @return A new instance of fragment InternetModuleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InternetModuleFragment newInstance(int baseDevId, String devName) {
        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_BaseID,baseDevId);
        args.putString(MainActivity.ARG_BaseName,devName);
        InternetModuleFragment fragment = new InternetModuleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            super.onCreate(savedInstanceState);
            baseDevId = this.getArguments().getInt(MainActivity.ARG_BaseID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_internet_module,container,false);
    }

    @Override
    public void onStart() {

        getActivity().setTitle(baseName);
        super.onStart();
        MainActivity.currPage = currentView.deviceActPage;

        baseDevice = MainActivity.mDB.getBaseDevice(baseDevId);
        getActivity().setTitle(baseDevice.getName());
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
        getActivity().getMenuInflater().inflate(R.menu.menu_edit_remove, menu);
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
}
