package ir.yektasmart.smarthome;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * Created by Admin on 05/12/2016.
 */
public class SendTcp implements Callable<Boolean> {


    byte[] message;
    String ipAdd;
    int PORT;
    int delay;

    public SendTcp(byte[] msg, String ip, int port, int delay ) {

        this.message = msg;
        this.ipAdd = ip;
        this.PORT = port;
        this.delay = delay;
    }
    public SendTcp ()
    {
        this.ipAdd = "192.168.43.176";
        this.PORT = 6000;
        message = "Hiiiii".getBytes();
        this.delay = 20;
    }

    public Boolean call() {
        try {
            InetAddress serverAddr = InetAddress.getByName(ipAdd);
            Socket socket = null;
            try {
                //PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                // WHERE YOU ISSUE THE COMMANDS

                //socket.setSoTimeout(20);
                socket = new Socket(serverAddr, PORT);
                if (socket.isConnected())
                Thread.sleep(20);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.write(message);
                //out.print(message);
                //out.flush();
                out.close();
                Log.e("TAG", "run: Sent" );
                Thread.sleep(delay);
                socket.close();
                return true;
            }
            catch (IOException e) {
                Log.e("TAG", "run: notConnected" );
                //e.printStackTrace();
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}