package ir.yektasmart.smarthome;

import android.os.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class AsyncServerThread extends Thread {

    public interface yektaAsyncServer {
        void onTcpReceived(Socket socket, String string);
    }
    private static final String TAG = "doInBackground" ;
    private Handler handler;
    private ServerSocket serverSocket;
    private int socketServerPORT ;
    private String message = "";
    private int count = 0;
    private yektaAsyncServer listener;
    private boolean running = false;

    public void Running(boolean r)
    {
        running = r;
        try {
            serverSocket.close();
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    public AsyncServerThread( int port,yektaAsyncServer listener )
    {
        this.socketServerPORT = port;
        this.listener = listener;
        running = true;
    }

    @Override
    public void run() {
        try {

            serverSocket = new ServerSocket(socketServerPORT);

            while (running) {
                Socket socket = serverSocket.accept();
                if(!running)
                {
                    socket.close();
                    serverSocket.close();
                    listener = null;
                    socket = null;
                    serverSocket = null;
                    continue;
                }
                count++;
                message = socket.getInetAddress() + ":"
                        + socket.getPort() + "\n";
                InetAddress ip= socket.getInetAddress();
                int port = socket.getPort();

                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
                String st = input.readLine();
                //socket.close();
                if(listener != null)
                    listener.onTcpReceived(socket,st);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
