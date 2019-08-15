package ir.yektasmart.smarthome;

import android.content.Context;
import android.net.wifi.WifiManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import static android.content.Context.WIFI_SERVICE;

public class UdpServer extends Thread{

    public static DatagramSocket socket;
    OnMessageReceived mListener = null;

    String TAG = "UdpServer";
    private Context context;
    boolean running;
    int serverPort;

    public interface OnMessageReceived {
        public void UdpMessageReceived(String message,String ip);
    }

    public UdpServer(int serverPort,Context context ,OnMessageReceived listener) {
        this.serverPort = serverPort;
        this.mListener = listener;
        this.context = context;
        running = true;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public  void close() {
        if(socket != null) {
            //socket.close();
            try {

                running = false;
                mListener = null;
                Thread t = new Thread(new SendUdpSingle("end".getBytes(),"127.0.0.1",serverPort));
                t.start();
                //socket = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void kill(){
        //Thread.currentThread().
    }
    @Override
    public void run() {

        running = true;
        try {
            //InetAddress broadcastIP = InetAddress.getByName(Const.BROADCAST_IP_REC);
            InetSocketAddress bindSock = new InetSocketAddress(serverPort);
            //socket.setReuseAddress(true);
            socket = new DatagramSocket(null);
            //if(!socket.isBound())
            socket.bind(bindSock);

            while (running) {
                byte[] buf = new byte[1024*16];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);     //this code block the program flow
                InetAddress address = packet.getAddress();
                //int port = packet.getPort();

                WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
                int ipAddress = wm.getConnectionInfo().getIpAddress();

                InetAddress myIpAddress = Util.intToInet(ipAddress);
                byte[] pack = packet.getData();

                if ((!address.toString().equals(myIpAddress.toString())) && packet.getLength() >10)//means is not echo
                {
                    try {
                        byte[] _str = subArray(pack, 0, packet.getLength());
                        String str = new String(_str);
                        if (mListener != null)
                            mListener.UdpMessageReceived(str,address.toString().substring(1));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
                //Log.e(TAG, "socket.close()");
            }
        }
    }

    public byte[] subArray(byte[] str, int from, int len) {
        byte[] temp = new byte[len];
        for (int i = from; i < from + len; i++) {
            temp[i - from] = str[i];
        }

        return temp;
    }

}

