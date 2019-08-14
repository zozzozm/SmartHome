package ir.yektasmart.smarthome;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * Created by YektaCo on 03/01/2017.
 */

public class sendToSocket implements Runnable {

    Socket socket;
    String msg;
    public sendToSocket(Socket socket, String msg){

        this.socket = socket;
        this.msg = msg;
    }
    @Override
    public void run()  {
        try {

            try {

                //String str = "this is an answer to " + socket.getInetAddress();
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.write(msg.getBytes());
                out.flush();
                //out.print(message);
                //out.flush();
               //out.close();
               // Log.e("TAG", "run: Sent" );
                Thread.sleep(100);
                //socket.close();
            }
            catch (IOException e) {
                Log.e("TAG", "run: notConnected" );
                //e.printStackTrace();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
