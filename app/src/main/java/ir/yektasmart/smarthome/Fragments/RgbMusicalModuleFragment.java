package ir.yektasmart.smarthome.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Objects;

import ir.yektasmart.smarthome.HSVColorPickerDialog;
import ir.yektasmart.smarthome.MainActivity;
import ir.yektasmart.smarthome.Model.BaseDevice;
import ir.yektasmart.smarthome.Model.RgbDevice;
import ir.yektasmart.smarthome.Model.Value;
import ir.yektasmart.smarthome.Model.yColor;
import ir.yektasmart.smarthome.Protocol.Command;
import ir.yektasmart.smarthome.R;
import ir.yektasmart.smarthome.currentView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RgbMusicalModuleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RgbMusicalModuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RgbMusicalModuleFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private int baseDevId = -1;
    private String baseName = "";
    private RgbDevice rgbDevice;
    private BaseDevice baseDevice;
    private int currentBackgroundColor = 0xffff0080;

    ListView lvSimple;
    private ImageView[] fav = new ImageView[11];
    private ImageView playMode;
    private CheckBox whitheCheckbox;
    private SeekBar dimSeekbar;
    private TextView whiteValue;

    private Spinner mode;
    private RelativeLayout colorLayout;

    private boolean firstRunFlag = true;

    private OnFragmentInteractionListener mListener;

    public RgbMusicalModuleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param baseDevId id of baseDevice.
     * @param devName name of device.
     * @return A new instance of fragment RgbMusicalModuleFragment.
     */
    public static RgbMusicalModuleFragment newInstance(int baseDevId, String devName) {
        RgbMusicalModuleFragment fragment = new RgbMusicalModuleFragment();
        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_BaseID, baseDevId);
        args.putString(MainActivity.ARG_BaseName, devName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            baseDevId = getArguments().getInt(MainActivity.ARG_BaseID);
            baseName = getArguments().getString(MainActivity.ARG_BaseName);
        }
        this.setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        MainActivity.currPage = currentView.deviceActPage;
        try {
            rgbDevice = MainActivity.mDB.getRgbDevice(baseDevId);
            baseDevice = MainActivity.mDB.getBaseDevice(baseDevId);
            Objects.requireNonNull(getActivity()).setTitle(baseDevice.getName());

            if (rgbDevice != null) {
                super.onStart();

                View view = getView();
                if (view != null) {

                    colorLayout =  view.findViewById(R.id.colorLayout);
                    whiteValue =  view.findViewById(R.id.whiteLightValue);
                    mode =  view.findViewById(R.id.modeSpinner);
                    playMode =  view.findViewById(R.id.toggle_play);
                    whitheCheckbox =  view.findViewById(R.id.whiteCheckBox);
                    dimSeekbar =  view.findViewById(R.id.dimSeekbar);
                    dimSeekbar.setProgress(rgbDevice.getWhiteValue());

                    if (rgbDevice.getWhiteOn() != 1 || rgbDevice.getBaseDevice().getOnOff() == 0) {
                        dimSeekbar.setEnabled(false);
                        whitheCheckbox.setChecked(false);
                        whiteValue.setText(getResources().getString(R.string.off));
                    } else {

                        dimSeekbar.setEnabled(true);
                        whitheCheckbox.setChecked(true);
                        whiteValue.setText(dimSeekbar.getProgress() * 100 / 255 + "%");
                        //whiteValue.setText(getResources().getString(R.string.on));
                    }

                    mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                if (firstRunFlag) {
                                    //Log.e(TAG, "onItemSelected: first" );
                                    firstRunFlag = false;
                                } else {
                                    Value v = new Value();
                                    if (position == 0) {
                                        //Log.e(TAG, "onItemSelected: 0" );
                                        //colorLayout.setVisibility(View.VISIBLE);
                                        MainActivity.mDB.updateMode(rgbDevice.getId(), 0);
                                        MainActivity.mDB.updateOnOff(baseDevice.getId(), 1);
                                        rgbDevice.setMode(0);
                                        layoutVisibility(true);
                                        v.setMusicalMode(1);
                                        sendCommand( Command.MusicalModeChange, mode.getId(), v);
                                    } else {
                                        //Log.e(TAG, "onItemSelected: 1" );
                                        MainActivity.mDB.updateMode(rgbDevice.getId(), 1);
                                        MainActivity.mDB.updateOnOff(baseDevice.getId(), 1);
                                        rgbDevice.setMode(1);
                                        layoutVisibility(false);
                                        v.setMusicalMode(2);
                                        sendCommand( Command.MusicalModeChange, mode.getId(), v);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    dimSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                            whiteValue.setText(seekBar.getProgress() * 100 / 255 + "%");
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                            rgbDevice.setWhiteValue(seekBar.getProgress());
                            rgbDevice.getBaseDevice().setOnOff(1);
                            MainActivity.mDB.updateWhiteValue(rgbDevice.getId(), seekBar.getProgress());
                            MainActivity.mDB.updateOnOff(rgbDevice.getBaseDevice().getId(), 1);

                            Value v = new Value();
                            v.setDimmer(seekBar.getProgress());
                            sendCommand(Command.setWhiteIntensity,seekBar.getId(),v);

                        }
                    });

                    if (rgbDevice.getAnimate() == 1)
                        playMode.setImageResource(R.drawable.pause);
                    else
                        playMode.setImageResource(R.drawable.play);

                    fav[0] =  view.findViewById(R.id.picker);
                    fav[1] =  view.findViewById(R.id.pickerFav1);
                    fav[2] =  view.findViewById(R.id.pickerFav2);
                    fav[3] =  view.findViewById(R.id.pickerFav3);
                    fav[4] =  view.findViewById(R.id.pickerFav4);
                    fav[5] =  view.findViewById(R.id.pickerFav5);
                    fav[6] =  view.findViewById(R.id.pickerFav6);
                    fav[7] =  view.findViewById(R.id.pickerFav7);
                    fav[8] =  view.findViewById(R.id.pickerFav8);
                    fav[9] =  view.findViewById(R.id.pickerFav9);
                    fav[10] = view.findViewById(R.id.pickerFav10);

                    whitheCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
                            dimSeekbar.setEnabled(isChecked);
                            MainActivity.mDB.updateWhiteOn(rgbDevice.getId(), isChecked);

                            if (isChecked) {

                                whiteValue.setText(dimSeekbar.getProgress() * 100 / 255 + "%");
                                rgbDevice.getBaseDevice().setOnOff(1);
                                rgbDevice.setWhiteOn(1);
                                MainActivity.mDB.updateOnOff(rgbDevice.getBaseDevice().getId(), 1);
                                MainActivity.mDB.updateWhiteOn(rgbDevice.getId(), true);

                                sendCommand(Command.TurnOnWhite, checkBox.getId(), null);

                            } else {
                                whiteValue.setText(getResources().getString(R.string.off));
                                rgbDevice.setWhiteOn(0);
                                MainActivity.mDB.updateWhiteOn(rgbDevice.getId(), false);

                                sendCommand(Command.TurnOffWhite, checkBox.getId(), null);
                            }
                        }
                    });

                    for (ImageView imageView : fav) {
                        imageView.setOnClickListener(this);
                        imageView.setOnLongClickListener(this);
                    }

                    playMode.setOnClickListener(this);
                    currentBackgroundColor = rgbDevice.getColor();
                    setPaleteColors();

                    if (rgbDevice.getMode() == 0) {
                        mode.setSelection(0);
                        layoutVisibility(true);
                    } else {
                        mode.setSelection(1);
                        layoutVisibility(false);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rgb_musical_module, container, false);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_edit_remove, menu);
    }


    @Override
    public void onClick(final View view) {

        Value v = new Value() ;
        int red, green, blue;
        red = Color.red(currentBackgroundColor);
        green = Color.green(currentBackgroundColor);
        blue = Color.blue(currentBackgroundColor);

        ImageView vv = fav[0];
        GradientDrawable drawable = (GradientDrawable) vv.getDrawable();

        switch(view.getId())
        {
            case R.id.toggle_play:
                if(rgbDevice.getAnimate()==0) {
                    final ImageView play = (ImageView) view;
                    final Animation anim_out = AnimationUtils.loadAnimation(getActivity(), R.anim.play_btn_fade_out);
                    final Animation anim_in  = AnimationUtils.loadAnimation(getActivity(), R.anim.play_btn_fade_in);
                    anim_out.setAnimationListener(new Animation.AnimationListener()
                    {
                        @Override public void onAnimationStart(Animation animation) {}
                        @Override public void onAnimationRepeat(Animation animation) {}
                        @Override public void onAnimationEnd(Animation animation)
                        {
                            play.setImageResource(R.drawable.pause);
                            anim_in.setAnimationListener(new Animation.AnimationListener() {
                                @Override public void onAnimationStart(Animation animation) {}
                                @Override public void onAnimationRepeat(Animation animation) {}
                                @Override public void onAnimationEnd(Animation animation) {}
                            });
                            play.startAnimation(anim_in);
                        }
                    });
                    play.startAnimation(anim_out);
                    rgbDevice.setAnimate(1);
                    //play.setImageResource(R.drawable.pause);
                    MainActivity.mDB.updateAnimate(rgbDevice.getId(),1);
                    MainActivity.mDB.updateOnOff(rgbDevice.getBaseDevice().getId(),1);

//g.setOnOff(1);
                    v.setPlay(true);
                    v.setColors(rgbDevice);
//v.setEffect(effect.getSelectedItemPosition());
                }else{
                    final ImageView play = (ImageView) view;
                    final Animation anim_out = AnimationUtils.loadAnimation(getActivity(), R.anim.play_btn_fade_out);
                    final Animation anim_in  = AnimationUtils.loadAnimation(getActivity(), R.anim.play_btn_fade_in);
                    anim_out.setAnimationListener(new Animation.AnimationListener()
                    {
                        @Override public void onAnimationStart(Animation animation) {}
                        @Override public void onAnimationRepeat(Animation animation) {}
                        @Override public void onAnimationEnd(Animation animation)
                        {
                            play.setImageResource(R.drawable.play);
                            anim_in.setAnimationListener(new Animation.AnimationListener() {
                                @Override public void onAnimationStart(Animation animation) {}
                                @Override public void onAnimationRepeat(Animation animation) {}
                                @Override public void onAnimationEnd(Animation animation) {}
                            });
                            play.startAnimation(anim_in);
                        }
                    });
                    play.startAnimation(anim_out);
                    rgbDevice.setAnimate(0);
                    play.setImageResource(R.drawable.play);

                    MainActivity.mDB.updateAnimate(rgbDevice.getId(),0);

                    v.setPlay(false);
                }
                //Log.e(TAG, "onClick: ", )
                break;
            case R.id.picker:

                try {
                    HSVColorPickerDialog cpd = new HSVColorPickerDialog(getActivity(), currentBackgroundColor, new HSVColorPickerDialog.OnColorSelectedListener() {
                        @Override
                        public void OnDone(Integer selectedColor) {

                            if(currentBackgroundColor == selectedColor) {

                                rgbDevice.setColor(selectedColor);
                                rgbDevice.getBaseDevice().setOnOff(1);
                                MainActivity.mDB.updateColor(rgbDevice.getId(),selectedColor);
                                MainActivity.mDB.updateOnOff(rgbDevice.getBaseDevice().getId(), 1);

                                Value v = new Value();
                                int red = Color.red(selectedColor);
                                int green = Color.green(selectedColor);
                                int blue = Color.blue(selectedColor);
                                v.setColor(new yColor(red, green, blue));
                                sendCommand( Command.SetColor, view.getId(), v);

                            }
                            else
                            {
                                changeBackgroundColor(selectedColor);
                                MainActivity.mDB.updateColor(rgbDevice.getId(),selectedColor);
                                MainActivity.mDB.updateOnOff(rgbDevice.getBaseDevice().getId(),1);
                                rgbDevice.setColor(selectedColor);
                                rgbDevice.getBaseDevice().setOnOff(1);

                            }
                        }

                        @Override
                        public void OnCancel(Integer selectedColor) {

                            if(rgbDevice.getBaseDevice().getOnOff() == 1) {

                                Value v = new Value();
                                int red = Color.red(selectedColor);
                                int green = Color.green(selectedColor);
                                int blue = Color.blue(selectedColor);
                                v.setColor(new yColor(red, green, blue));
                                sendCommand( Command.SetColor, view.getId(), v);
                            }
                        }

                        @Override
                        public void OnColorSelect(Integer color) {
                            Value v = new Value();
                            int red = Color.red(color);
                            int green = Color.green(color);
                            int blue = Color.blue(color);
                            v.setColor(new yColor(red, green, blue));
                            sendCommand( Command.SetColor, view.getId(), v);
                        }

                    });
                    cpd.setTitle(getResources().getString(R.string.pick_a_color));
                    cpd.show();
                }catch (Exception e){ e.printStackTrace();}

                break;

            case R.id.pickerFav1:
                red = Color.red(rgbDevice.getFav01());
                green = Color.green(rgbDevice.getFav01());
                blue = Color.blue(rgbDevice.getFav01());
                MainActivity.mDB.updateOnOff(rgbDevice.getBaseDevice().getId(),1);
                break;

            case R.id.pickerFav2:
                red = Color.red(rgbDevice.getFav02());
                green = Color.green(rgbDevice.getFav02());
                blue = Color.blue(rgbDevice.getFav02());
                MainActivity.mDB.updateOnOff(rgbDevice.getBaseDevice().getId(),1);
                break;

            case R.id.pickerFav3:
                red = Color.red(rgbDevice.getFav03());
                green = Color.green(rgbDevice.getFav03());
                blue = Color.blue(rgbDevice.getFav03());
                MainActivity.mDB.updateOnOff(rgbDevice.getBaseDevice().getId(),1);
                break;

            case R.id.pickerFav4:
                red = Color.red(rgbDevice.getFav04());
                green = Color.green(rgbDevice.getFav04());
                blue = Color.blue(rgbDevice.getFav04());
                MainActivity.mDB.updateOnOff(rgbDevice.getBaseDevice().getId(),1);
                break;

            case R.id.pickerFav5:
                red = Color.red(rgbDevice.getFav05());
                green = Color.green(rgbDevice.getFav05());
                blue = Color.blue(rgbDevice.getFav05());
                MainActivity.mDB.updateOnOff(rgbDevice.getBaseDevice().getId(),1);
                break;

            case R.id.pickerFav6:
                red = Color.red(rgbDevice.getFav06());
                green = Color.green(rgbDevice.getFav06());
                blue = Color.blue(rgbDevice.getFav06());
                MainActivity.mDB.updateOnOff(rgbDevice.getBaseDevice().getId(),1);
                break;

            case R.id.pickerFav7:
                red = Color.red(rgbDevice.getFav07());
                green = Color.green(rgbDevice.getFav07());
                blue = Color.blue(rgbDevice.getFav07());
                MainActivity.mDB.updateOnOff(rgbDevice.getBaseDevice().getId(),1);
                break;

            case R.id.pickerFav8:
                red = Color.red(rgbDevice.getFav08());
                green = Color.green(rgbDevice.getFav08());
                blue = Color.blue(rgbDevice.getFav08());
                MainActivity.mDB.updateOnOff(rgbDevice.getBaseDevice().getId(),1);
                break;

            case R.id.pickerFav9:
                red = Color.red(rgbDevice.getFav09());
                green = Color.green(rgbDevice.getFav09());
                blue = Color.blue(rgbDevice.getFav09());
                MainActivity.mDB.updateOnOff(rgbDevice.getBaseDevice().getId(),1);
                break;

            case R.id.pickerFav10:
                red = Color.red(rgbDevice.getFav10());
                green = Color.green(rgbDevice.getFav10());
                blue = Color.blue(rgbDevice.getFav10());
                MainActivity.mDB.updateOnOff(rgbDevice.getBaseDevice().getId(),1);
                break;
        }

        if(view.getId() != R.id.picker) {

            MainActivity.mDB.updateColor(rgbDevice.getId(),Color.rgb(red,green,blue));
            rgbDevice.setColor(Color.rgb(red, green, blue));
            currentBackgroundColor = rgbDevice.getColor();
        }
        drawable.setColor(Color.rgb(red,green,blue));
        v.setColor(new yColor(red,green,blue));
        if(view.getId() != R.id.picker)
            sendCommand( null,view.getId(),v);
    }

    @Override
    public boolean onLongClick(View view) {


        ImageView vv =(ImageView) view;
        GradientDrawable drawable = (GradientDrawable) vv.getDrawable();
        drawable.setColor(currentBackgroundColor);

        //MainActivity.mDB.open();
        switch (view.getId())
        {
            case R.id.pickerFav1:
                MainActivity.mDB.updateFavColor(rgbDevice.getId(),currentBackgroundColor,1);// 1 means fav1
                rgbDevice.setFav01(currentBackgroundColor);
                break;

            case R.id.pickerFav2:
                MainActivity.mDB.updateFavColor(rgbDevice.getId(),currentBackgroundColor,2);// 2 means fav2
                rgbDevice.setFav02(currentBackgroundColor);
                break;

            case R.id.pickerFav3:
                MainActivity.mDB.updateFavColor(rgbDevice.getId(),currentBackgroundColor,3);// 3 means fav3
                rgbDevice.setFav03(currentBackgroundColor);
                break;

            case R.id.pickerFav4:
                MainActivity.mDB.updateFavColor(rgbDevice.getId(),currentBackgroundColor,4);// 4 means fav4
                rgbDevice.setFav04(currentBackgroundColor);
                break;

            case R.id.pickerFav5:
                MainActivity.mDB.updateFavColor(rgbDevice.getId(),currentBackgroundColor,5);// 5 means fav5
                rgbDevice.setFav05(currentBackgroundColor);
                break;

            case R.id.pickerFav6:
                MainActivity.mDB.updateFavColor(rgbDevice.getId(),currentBackgroundColor,6);// 6 means fav6
                rgbDevice.setFav06(currentBackgroundColor);
                break;

            case R.id.pickerFav7:
                MainActivity.mDB.updateFavColor(rgbDevice.getId(),currentBackgroundColor,7);// 7 means fav7
                rgbDevice.setFav07(currentBackgroundColor);
                break;

            case R.id.pickerFav8:
                MainActivity.mDB.updateFavColor(rgbDevice.getId(),currentBackgroundColor,8);// 8 means fav8
                rgbDevice.setFav08(currentBackgroundColor);
                break;

            case R.id.pickerFav9:
                MainActivity.mDB.updateFavColor(rgbDevice.getId(),currentBackgroundColor,9);// 9 means fav9
                rgbDevice.setFav09(currentBackgroundColor);
                break;

            case R.id.pickerFav10:
                MainActivity.mDB.updateFavColor(rgbDevice.getId(),currentBackgroundColor,10);// 10 means fav10
                rgbDevice.setFav10(currentBackgroundColor);
                break;
        }
        //MainActivity.mDB.close();
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

    private void setPaleteColors() {


        GradientDrawable drawable = (GradientDrawable) fav[0].getDrawable();
        drawable.setColor(rgbDevice.getColor());

        drawable = (GradientDrawable) fav[1].getDrawable();
        drawable.setColor(rgbDevice.getFav01());

        drawable = (GradientDrawable) fav[2].getDrawable();
        drawable.setColor(rgbDevice.getFav02());

        drawable = (GradientDrawable) fav[3].getDrawable();
        drawable.setColor(rgbDevice.getFav03());

        drawable = (GradientDrawable) fav[4].getDrawable();
        drawable.setColor(rgbDevice.getFav04());

        drawable = (GradientDrawable) fav[5].getDrawable();
        drawable.setColor(rgbDevice.getFav05());

        drawable = (GradientDrawable) fav[6].getDrawable();
        drawable.setColor(rgbDevice.getFav06());

        drawable = (GradientDrawable) fav[7].getDrawable();
        drawable.setColor(rgbDevice.getFav07());

        drawable = (GradientDrawable) fav[8].getDrawable();
        drawable.setColor(rgbDevice.getFav08());

        drawable = (GradientDrawable) fav[9].getDrawable();
        drawable.setColor(rgbDevice.getFav09());

        drawable = (GradientDrawable) fav[10].getDrawable();
        drawable.setColor(rgbDevice.getFav10());
    }

    private void layoutVisibility(boolean visibility) {
        for (ImageView imageView : fav) {
            imageView.setEnabled(visibility);
            if (visibility)
                imageView.setAlpha((float) 1);
            else
                imageView.setAlpha((float) 0.5);
        }
        playMode.setEnabled(visibility);
        whitheCheckbox.setEnabled(visibility);
        if(visibility) {
            if (whitheCheckbox.isChecked()) dimSeekbar.setEnabled(visibility);
        }
        else
            dimSeekbar.setEnabled(visibility);
        if (visibility){
            playMode.setAlpha((float) 1);
            whitheCheckbox.setAlpha((float) 1);
            dimSeekbar.setAlpha((float) 1);
        }else {
            playMode.setAlpha((float) 0.5);
            whitheCheckbox.setAlpha((float) 0.5);
            dimSeekbar.setAlpha((float) 0.5);
        }

    }

    private void changeBackgroundColor(int selectedColor) {

        currentBackgroundColor = selectedColor;
        ImageView vv = fav[0];
        GradientDrawable drawable = (GradientDrawable) vv.getDrawable();
        int red = Color.red(selectedColor);
        int green = Color.green(selectedColor);
        int blue = Color.blue(selectedColor);
        int alpha = Color.alpha(selectedColor);
        drawable.setColor(Color.argb(alpha,red,green,blue));
/*        Value v = new Value();
        v.setColor(new yColor(red,green,blue));*/
        rgbDevice.setColor(selectedColor);
/*        MainActivity.mDB.open();
        MainActivity.mDB.updateModuleColor(g.getId(),selectedColor);
        MainActivity.mDB.close();*/
        // MainActivity.CommunicationProtocol(g, null,fav[0].getId(),v);
    }

    private void sendCommand(Command cmd, int id, Value v){
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
