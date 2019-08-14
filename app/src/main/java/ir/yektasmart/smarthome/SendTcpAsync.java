package ir.yektasmart.smarthome;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import ir.yektasmart.smarthome.Model.HandlerMessage;
import ir.yektasmart.smarthome.Model.NetStatus;

/**
 * Created by macbookpro on 8/31/16 AD.
 */
public class SendTcpAsync extends AsyncTask<Void, Integer , Void> {

    String TAG = "SendTcpAsync";
    String ipAdd;
    byte[] message;
    byte[] recMessage = new  byte[1024];
    int len;
    int PORT;
    int delay;
    int flag = 0;
    Handler myHandler;
    Message msg;

    public SendTcpAsync(byte[] msg, String ip, int port, int delay ) {

        this.message = msg;
        this.ipAdd = ip;
        this.PORT = port;
        this.delay = delay;

    }
    public SendTcpAsync(Handler handler,byte[] sentMsg,int len)
    {
        this.ipAdd = "192.168.4.1";
        this.PORT = 6000;
        message = sentMsg;
        this.len = len;
        this.delay = 50;
        this.myHandler = handler;
        this.msg  = new Message();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {

            InetAddress serverAddr = InetAddress.getByName(ipAdd);
            Socket socket = null;
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(serverAddr, PORT), 1000);
                socket.setSoTimeout(5000);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream());
                out.write(message,0,len);

                byte[] rec = new byte[in.read(recMessage)];

                out.close();
                in.close();
                socket.close();

                for (int i = 0; i < rec.length; i++)
                    rec[i] = recMessage[i];

                HandlerMessage handlerMessage = new HandlerMessage();
                handlerMessage.setState(NetStatus.CON_SUCCESSFUL);
                handlerMessage.setMessage(rec);
                msg.obj = handlerMessage;
               // Thread.sleep(delay);
            }
            catch (SocketTimeoutException e){
                if(e.getMessage() != null) {
                    //Log.e(TAG, "Server not Running: ");
                    HandlerMessage handlerMessage = new HandlerMessage();
                    handlerMessage.setState(NetStatus.CON_FAILED);
                    msg.obj = handlerMessage;
                    //flag = 0;
                }
                else {
                   // Log.e(TAG, "Server Not Responding");
                    HandlerMessage handlerMessage = new HandlerMessage();
                    handlerMessage.setState(NetStatus.SER_TIMEOUT);
                    msg.obj = handlerMessage;
                    //flag = 1;
                }
                socket.close();
            }
            catch (IOException e) {
               // Log.e("TAG", "run: notConnected " + ipAdd );
                socket.close();
                HandlerMessage handlerMessage = new HandlerMessage();
                handlerMessage.setState(NetStatus.CON_TIMEOUT);
                msg.obj = handlerMessage;
                //e.printStackTrace();
                //flag = -1;
            }

            catch (Exception e){
                //Log.e("TAG", "run: not Exist " + ipAdd );
                socket.close();
                HandlerMessage handlerMessage = new HandlerMessage();
                handlerMessage.setState(NetStatus.ERR);
                msg.obj = handlerMessage;
               // e.printStackTrace();
                //flag = -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        myHandler.sendMessage(msg);
        //Log.e("TAG", "onPostExecute: terminated");
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        //myHandler.sendEmptyMessage(values[0]);
    }
}

