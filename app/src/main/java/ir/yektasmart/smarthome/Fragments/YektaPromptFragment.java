package ir.yektasmart.smarthome.Fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import ir.yektasmart.smarthome.R;

/**
 * Created by YektaCo on 11/03/2017.
 */
interface OnYektaPromptReturn {
    public void negetive();
    public void posotive(String withInput);
}

public class YektaPromptFragment extends DialogFragment {
    public OnYektaPromptReturn listener;
    Button neg;
    Button pos;
    EditText prompt;
    String ttl;
    String msg;

    public YektaPromptFragment(String title,String massage,OnYektaPromptReturn yektaPromptFragment) {
        this.listener = yektaPromptFragment;
        ttl = title;
        msg = massage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.yekta_prompt_fragment, container,
                false);
        getDialog().setTitle(ttl);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        ((TextView)rootView.findViewById(R.id.about_fragment)).setText(msg);
        ((TextView)rootView.findViewById(R.id.dialog_title)).setText(ttl);
        prompt = (EditText)rootView.findViewById(R.id.promptBox);
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
                listener.posotive(prompt.getText().toString());
                dismiss();
            }
        });
        return rootView;
    }
}
