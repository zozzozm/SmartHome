package ir.yektasmart.smarthome;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UDPListenerService  extends Service {
    private static final String TAG = "UDPListenerService";
    public static String UDP_BROADCAST = "UDPBroadcast";

    //Boolean shouldListenForUDPBroadcast = false;
    DatagramSocket socket;


    private void listenAndWaitAndThrowIntent(InetAddress broadcastIP, Integer port) throws Exception {
        byte[] recvBuf = new byte[15000];
        if (socket == null || socket.isClosed()) {
            socket = new DatagramSocket(port, broadcastIP);
            socket.setBroadcast(true);
        }
        //socket.setSoTimeout(1000);
        DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
        //Log.e("UDP", "Waiting for UDP broadcast");
        socket.setBroadcast(true);
        socket.receive(packet);

        String senderIP = packet.getAddress().getHostAddress();
        String message = new String(packet.getData()).trim();
        broadcastIntent(senderIP, message,packet.getData());
        socket.close();
    }

    private void broadcastIntent(String senderIP, String message , byte[] msg) {
        Intent intent = new Intent(UDPListenerService.UDP_BROADCAST);
        intent.putExtra("sender", senderIP);
        intent.putExtra("message", message);
        intent.putExtra("msg" , msg);
        sendBroadcast(intent);
    }

    Thread UDPBroadcastThread;

    void startListenForUDPBroadcast() {
        UDPBroadcastThread = new Thread(new Runnable() {
            public void run() {
                try {
                    InetAddress broadcastIP = InetAddress.getByName(Const.BROADCAST_IP_REC); //172.16.238.42 //192.168.1.255
                    Integer port = Const.UDP_REC_PORT;
                    while (shouldRestartSocketListen) {
                        listenAndWaitAndThrowIntent(broadcastIP, port);
                    }
                    //if (!shouldListenForUDPBroadcast) throw new ThreadDeath();
                } catch (Exception e) {
                    Log.i("UDP", "no longer listening for UDP broadcasts cause of error " + e.getMessage());
                }
            }
        });
        UDPBroadcastThread.start();
    }

    private Boolean shouldRestartSocketListen=true;

    void stopListen() {
        //Toast.makeText(this, "stopListen", Toast.LENGTH_LONG).show();
        shouldRestartSocketListen = false;
        socket.close();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
        // Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        stopListen();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        shouldRestartSocketListen = true;
        startListenForUDPBroadcast();
        //Log.i("UDP", "Service started");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //Toast.makeText(this, "onBind", Toast.LENGTH_LONG).show();
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //Toast.makeText(this, "onUnbind", Toast.LENGTH_LONG).show();
        return super.onUnbind(intent);
    }
}
