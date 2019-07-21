package ir.yektasmart.smarthome.Fragments;

/**
 * Created by YektaCo on 02/12/2016.
 */

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import ir.yektasmart.smarthome.R;

interface OnYektaDialogReturn {
    public void negetive();
    public void posotive();
}

public class YektaDialogFragment extends DialogFragment {

    public OnYektaDialogReturn listener;
    Button neg;
    Button pos;
    String ttl;
    String msg;


    public YektaDialogFragment(String title,String massage,OnYektaDialogReturn onYektaDialogReturn) {
        this.listener = onYektaDialogReturn;
        ttl = title;
        msg = massage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.yekta_dialog_fragment, container,
                false);
        getDialog().setTitle(ttl);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        ((TextView)rootView.findViewById(R.id.about_fragment)).setText(msg);
        ((TextView)rootView.findViewById(R.id.dialog_title)).setText(ttl);
        neg = (Button) rootView.findViewById(R.id.dialog_cancel);
        neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.negetive();
                dismiss();
            }
        });

        pos = (Button) rootView.findViewById(R.id.dialog_del);
        pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.posotive();
                dismiss();
            }
        });
        return rootView;
    }

}