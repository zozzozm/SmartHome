package ir.yektasmart.smarthome.Protocol;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;

import ir.yektasmart.smarthome.Const;
import ir.yektasmart.smarthome.DelayThread;
import ir.yektasmart.smarthome.MainActivity;
import ir.yektasmart.smarthome.Model.BaseDevice;
import ir.yektasmart.smarthome.Model.Value;
import ir.yektasmart.smarthome.Mqtt_push;
import ir.yektasmart.smarthome.R;
import ir.yektasmart.smarthome.SendUdpSingle;

import static ir.yektasmart.smarthome.MainActivity.pool;
import static ir.yektasmart.smarthome.MainActivity.vib_delay;

public class UtilFunc extends Activity implements Mqtt_push.Mqtt_push_result {

    private static String TAG = "UtilFunc";
    public static Vibrator vib;
    Context context;

    public UtilFunc(Context context) {
        this.context = context;
    }

    public  void CommunicationProtocol(BaseDevice baseDevice, Command cmd, int _id, Value v) {

        byte[] pass = baseDevice.getPass().getBytes();

        byte[] recMac = baseDevice.getMac().getBytes();

        byte[] trType = new byte[]{0, 0, 0, 0};
        byte[] recType = this.getType(baseDevice.getType());

        byte[] comType = CommandType.ACTION_COMMAND.getByte();
        byte[] comm = new byte[]{(byte) 0xff};

        byte[] uid = new byte[2];

        byte[] len = new byte[]{0, 0};
        byte[] ext = new byte[]{0};

        uid[0] = (byte) (baseDevice.getUid()/256);
        uid[1] = (byte) (baseDevice.getUid()%256);

        switch (_id) {
            case R.id.toggle_play:
                if (v.isPlay()) {
                    /*
                        set auto  mode send 2 segment data : 1.send colors, 2.send start auto mode.
                        between 2 segment have 1s delay to be ready Device.
                        The getCount() showing now witch segment should be execute.
                    */

                    if( v.getCount() == 0) {//first segment send colors

                        comm = Command.SetAutoColor.getByte();
                        comType = CommandType.SETTING_COMMAND.getByte();
                        len = new byte[]{1, 3};
                        ext = null;
                        ext = new byte[30];
                        int ii = 0;
                        for (int i = 0; i < 10; i++) {
                            ext[ii++] = (byte) v.getColors().get(i).getRed();
                            ext[ii++] = (byte) v.getColors().get(i).getGreen();
                            ext[ii++] = (byte) v.getColors().get(i).getBlue();
                        }
                        byte[] setColor = concat(pass, "200000000000".getBytes(), recMac, trType, recType,uid, comType, len, comm, ext);
                        SendUdp(setColor, baseDevice.getPort());

/*                      comm = Command.SetAutoEffect.getByte();
                        comType = CommandType.SETTING_COMMAND.getByte();
                        len = new byte[]{2, 0};
                        ext = null;
                        ext = new byte[1];
                        ext[0] = (byte) v.getEffect();
                        byte[] setEffect = concat(pass, macAddress, recMac, trType, recType, comType, len, comm, ext);
                        MainActivity.SendUdp(setEffect, "255.255.255.255");
*/
                        v.setCount(v.getCount()+1);//first segment complete and pass to next segment
                        final BaseDevice gg = baseDevice;
                        final int idd = _id;
                        final Value vv = v;
                        Handler time = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                CommunicationProtocol(gg, null, idd, vv);//call next segment
                            }
                        };
                        DelayThread t = new DelayThread(time, 1000);
                        t.execute();
                    }
                    else {//second segment send start auto mode
                        comm = Command.StartAutoMode.getByte();
                        comType = CommandType.ACTION_COMMAND.getByte();
                        len = new byte[]{1, 0};
                        ext = null;
                        ext = new byte[]{0};
                    }

                }
                else{

                    comm = Command.StopAutoMode.getByte();
                    len = new byte[]{1, 0};

                }
                break;
            case R.id.rowModuleSwitch://row switch in device list
//            case R.id.groupPagePowerSwitch://
//                if (v.isOnOff())
//                    comm = Command.TurnOn.getByte();
//                else
//                    comm = Command.TurnOff.getByte();
//                len = new byte[]{1, 0};
//
//                //todo #extra data send - ext adds to sent data should be removed
//                break;

            case R.id.btn1://btn 1 of rf remote
                comm = cmd.getByte();
                ext = ExtCommand.Btn1.getByte();
                len = new byte[]{2, 0};
                break;

            case R.id.btn2://btn 2 of rf remote
                comm = cmd.getByte();
                ext = ExtCommand.Btn2.getByte();
                len = new byte[]{2, 0};
                break;

            case R.id.btn3://btn 3 of rf remote
                comm = cmd.getByte();
                ext = ExtCommand.Btn3.getByte();
                len = new byte[]{2, 0};
                break;

            case R.id.btn4://btn 4 of rf remote
                comm = cmd.getByte();
                ext = ExtCommand.Btn4.getByte();
                len = new byte[]{2, 0};
                break;

            case R.id.btn5://btn 5 of rf remote
                comm = cmd.getByte();
                ext = ExtCommand.Btn5.getByte();
                len = new byte[]{2, 0};
                break;

            case R.id.btn6://btn 6 of rf remote
                comm = cmd.getByte();
                ext = ExtCommand.Btn6.getByte();
                len = new byte[]{2, 0};
                break;

            case R.id.btn7://btn 7 of rf remote
                comm = cmd.getByte();
                ext = ExtCommand.Btn7.getByte();
                len = new byte[]{2, 0};
                break;

            case R.id.btn8://btn 8 of rf remote
                comm = cmd.getByte();
                ext = ExtCommand.Btn8.getByte();
                len = new byte[]{2, 0};
                break;

            case R.id.btn9://btn 9 of rf remote
                comm = cmd.getByte();
                ext = ExtCommand.Btn9.getByte();
                len = new byte[]{2, 0};
                break;

            case R.id.btn10://btn 10 of rf remote
                comm = cmd.getByte();
                ext = ExtCommand.Btn10.getByte();
                len = new byte[]{2, 0};
                break;

            case R.id.btn11://btn 11 of rf remote
                comm = cmd.getByte();
                ext = ExtCommand.Btn11.getByte();
                len = new byte[]{2, 0};
                break;

            case R.id.btn12://btn 12 of rf remote
                comm = cmd.getByte();
                ext = ExtCommand.Btn12.getByte();
                len = new byte[]{2, 0};
                break;

            case R.id.btn13://btn 13 of rf remote
                comm = cmd.getByte();
                ext = ExtCommand.Btn13.getByte();
                len = new byte[]{2, 0};
                break;

            case R.id.btn14://btn 14 of rf remote
                comm = cmd.getByte();
                ext = ExtCommand.Btn14.getByte();
                len = new byte[]{2, 0};
                break;

            case R.id.btn15://btn 15 of rf remote
                comm = cmd.getByte();
                ext = ExtCommand.Btn15.getByte();
                len = new byte[]{2, 0};
                break;

//            case R.id.latch_module_switch://latch switch
//                if (v.isOnOff())
//                    comm = Command.TurnOff.getByte();
//                else
//                    comm = Command.TurnOn.getByte();
//                len = new byte[]{1, 0};
//                break;

            case R.id.picker://circle color picker
            case R.id.pickerFav1://fav color 1
            case R.id.pickerFav2://fav color 2
            case R.id.pickerFav3://fav color 3
            case R.id.pickerFav4://fav color 4
            case R.id.pickerFav5://fav color 5
            case R.id.pickerFav6://fav color 6
            case R.id.pickerFav7://fav color 7
            case R.id.pickerFav8://fav color 8
            case R.id.pickerFav9://fav color 9
            case R.id.pickerFav10://fav color 10
//            case R.id.groupPagePicker:
                comm = Command.SetColor.getByte();
                len = new byte[] {4,0};
                ext = new byte[]{(byte) v.getColor().getRed(), (byte) v.getColor().getGreen(), (byte) v.getColor().getBlue()};
                break;
//
//            case R.id.groupPageWhiteSwitch:
            case R.id.whiteCheckBox://turn on white led
                comm = cmd.getByte();
                len = new byte[] {1,0};
                break;

//            case R.id.groupPageWhiteSeekbar:
            case R.id.dimSeekbar://dim white led
                comm = cmd.getByte();
                len = new byte[]{2, 0};
                ext = new byte[]{(byte) v.getDimmer()};
                break;
 /*           case R.id.groupOnOffSwitch://row for group switch
                if (v.isOnOff())
                    comm = Command.TurnOn.getByte();//comm = Command.TurnOnPending.getByte();
                else
                    comm = Command.TurnOff.getByte();//comm = Command.TurnOffPending.getByte();
                len = new byte[]{1, 0};
                break;*/

            case R.id.modeSpinner://select MANUAL or MUSICAL mode
                comm = cmd.getByte();
                comType = CommandType.SETTING_COMMAND.getByte();
                ext = null;
                ext = new byte[]{(byte) v.getMusicalMode()};
                len = new byte[] {2,0};
                break;


            case R.id.ToggleButton01://latch001
            case R.id.ToggleButton02://latch002
                comm = cmd.getByte();
                ext = null;
                ext = new byte[]{(byte) v.getDimmer()};
                len = new byte[] {2,0};
                break;

            case R.id.rightArr://set or reset permission
                comm = cmd.getByte();
                comType = CommandType.SETTING_COMMAND.getByte();
                len = new byte[]{3, 0};
                ext = null;
                ext = new byte[2];

                byte[] newUid = new byte[2];
                newUid[0] = (byte) (v.getDimmer()/256);
                newUid[1] = (byte) (v.getDimmer()%256);
                ext = newUid ;
                break;

            default:
                break;
        }

        byte[] msg = concat(pass, "200000000000".getBytes(), recMac, trType, recType,uid, comType, len, comm, ext);

        SendUdp(msg, baseDevice.getPort());
        //MainActivity.SendUdpCuncurr(msg, "255.255.255.255",g.getIndex());

    }

    private  byte[] getType(String type) {
        byte[] typeCode;
        switch (type) {

            case "INTERNET":
                typeCode = new byte[]{0, 0, 0, 1};
                return typeCode;

            case "RF":
                typeCode = new byte[]{1, 4, 0, 1};
                return typeCode;
            case "RGB_LAMP":
                typeCode = new byte[]{1, 1, 0, 3};
                return typeCode;

            case "RGB_STRIP":
                typeCode = new byte[]{1, 2, 0, 1};
                return typeCode;

            case "RGBW_LAMP":
                typeCode = new byte[]{1, 1, 0, 3};
                return typeCode;
            case "RGBW_STRIP":
                typeCode = new byte[]{1, 2, 0, 1};
                return typeCode;
            case "MUSICAL_STRIP":
                typeCode = new byte[]{1, 2, 0, 2};
                return typeCode;
            case "IR":
                typeCode = new byte[]{1, 3, 0, 1};
                return typeCode;

            case "DIMMER":
                typeCode = new byte[]{1, 1, 0, 2};
                return typeCode;

            case "SWITCH":
                typeCode = new byte[]{1, 1, 0, 1};
                return typeCode;

            case "LATCH001":
                typeCode = new byte[]{1, 0, 0, 1};
                return typeCode;

            case "LATCH002":
                typeCode = new byte[]{1, 0, 0, 2};
                return typeCode;

            case "LATCH003":
                typeCode = new byte[]{1, 0, 0, 3};
                return typeCode;

            case "LATCH004":
                typeCode = new byte[]{1, 0, 0, 4};
                return typeCode;

            case "LATCH005":
                typeCode = new byte[]{1, 0, 0, 5};
                return typeCode;

            case "LATCH006":
                typeCode = new byte[]{1, 0, 0, 6};
                return typeCode;

            case "LATCH007":
                typeCode = new byte[]{1, 0, 0, 7};
                return typeCode;

            case "LATCH008":
                typeCode = new byte[]{1, 0, 0, 8};
                return typeCode;

            case "LATCH009":
                typeCode = new byte[]{1, 0, 0, 9};
                return typeCode;

            case "LATCH010":
                typeCode = new byte[]{1, 0, 0, 10};
                return typeCode;

            case "LATCH011":
                typeCode = new byte[]{1, 0, 0, 11};
                return typeCode;

            case "LATCH012":
                typeCode = new byte[]{1, 0, 0, 12};
                return typeCode;

            case "LATCH013":
                typeCode = new byte[]{1, 0, 0, 13};
                return typeCode;

            case "LATCH014":
                typeCode = new byte[]{1, 0, 0, 14};
                return typeCode;

            case "LATCH015":
                typeCode = new byte[]{1, 0, 0, 15};
                return typeCode;

            case "LATCH016":
                typeCode = new byte[]{1, 0, 0, 16};
                return typeCode;

            default:
                typeCode = new byte[]{(byte) 0XFF, (byte) 0XFF, (byte) 0XFF, (byte) 0XFF};
                return typeCode;

        }
    }

    static public byte[] concat(byte[]... bufs) {
        if (bufs.length == 0)
            return null;
        if (bufs.length == 1)
            return bufs[0];
        for (int i = 0; i < bufs.length - 1; i++) {
            byte[] res = Arrays.copyOf(bufs[i], bufs[i].length + bufs[i + 1].length);
            System.arraycopy(bufs[i + 1], 0, res, bufs[i].length, bufs[i + 1].length);
            bufs[i + 1] = res;
        }
        return bufs[bufs.length - 1];
    }

    public  synchronized void SendUdp(byte[] msg, int port) {

        try {

            if(!Const.isAwayMode) {
                vib = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
                if (Const.isVibrateMode) {
                    vib.vibrate(vib_delay);
                }
                if (Const.BROADCAST_IP != null) {

                    SendUdpSingle task1 = new SendUdpSingle(msg, Const.BROADCAST_IP, port);
                    SendUdpSingle task2 = new SendUdpSingle(msg, Const.BROADCAST_IP, port);
                    SendUdpSingle task3 = new SendUdpSingle(msg, Const.BROADCAST_IP, port);
                    pool.execute(task1);
                    pool.execute(task2);
                    pool.execute(task3);


                } else {
                    Log.e("SendUdp", "Const.BROADCAST_IP == null)");
                }
            }else {
                establish_mqtt(msg, port);
            }
            establish_mqtt(msg, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void establish_mqtt(byte[] msg, int port){

        byte[] portByte = new byte[2];
        portByte[0] = (byte)(port/256);
        portByte[1] = (byte)(port%256);

        try {
            if(MainActivity.InternetDevices.size() > 0) {
                Mqtt_push push = new Mqtt_push(concat(portByte, msg), "0001" + MainActivity.InternetDevices.get(0).getMac(), Const.UUID.substring(1,16));
                push.setListener(this);
                Log.e(TAG, "establish_mqtt: " + "0001" + MainActivity.InternetDevices.get(0).getMac() );
                push.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void Mqtt_push_failed() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(context, "Mqtt push failed.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Mqtt_push_failed: " + "Mqtt push failed" );
            }
        });
    }

    @Override
    public void Mqtt_push_sucess() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(context, "Mqtt push sucessed.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Mqtt_push_failed: " + "Mqtt push sucessed" );
            }
        });
    }

}