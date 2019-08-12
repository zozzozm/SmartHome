package ir.yektasmart.smarthome.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import ir.yektasmart.smarthome.Const;
import ir.yektasmart.smarthome.MainActivity;
import ir.yektasmart.smarthome.R;
import ir.yektasmart.smarthome.currentView;
public class ChangepasswordFragment extends Fragment {

    EditText newText;
    EditText confirmText;
    EditText oldPass;

    String oldPassContent = "+++++++++";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_changepassword,container,false);
    }

    @Override
    public void onStart() {
        getActivity().setTitle(getResources().getString(R.string.settings));
        super.onStart();
        MainActivity.currPage = currentView.settingActPage;

        View view = getView();
        if(view != null){

            newText     = (EditText) view.findViewById(R.id.changePass_newPass);
            confirmText = (EditText) view.findViewById(R.id.changePass_confText);
            oldPass     = (EditText) view.findViewById(R.id.changePass_oldpass);

            oldPassContent = MainActivity.shP.loadExtraString(Const.SP_LoginPaswword);

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /*super.onCreateOptionsMenu(menu, inflater);*/
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_save:
                if (newText.getText().length() == 4) {
                    if (newText.getText().toString().equals(confirmText.getText().toString()) &&
                            oldPassContent.equals(oldPass.getText().toString())) {
                        MainActivity.shP.saveExtraString(Const.SP_LoginPaswword, newText.getText().toString());
                        Toast.makeText(getActivity(), getResources().getString(R.string.passchanged), Toast.LENGTH_SHORT).show();
                        View view = getActivity().getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }

                        getActivity().onBackPressed();

                    }else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.passwordMissmatch), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.passwordLenError), Toast.LENGTH_SHORT).show();
                }
                break;

        }
        return true;
    }
}
