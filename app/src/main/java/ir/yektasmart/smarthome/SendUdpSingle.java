package ir.yektasmart.smarthome;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by YektaCo on 11/10/2016.
 */
public class SendUdpSingle extends Thread {

    String IP;
    int PORT;
    byte[] msg;
    boolean running = false;
    //InetAddress ip ;

    public SendUdpSingle(byte[] msg, String ip , int port){
        IP=ip;
        PORT=port;
        this.msg=msg;
        running = true;
    }

    public void Runing(boolean rn){
        running = rn;
    }
    @Override
    public void run()  {
        try {

            DatagramSocket clientSocket;
            clientSocket = new DatagramSocket();
            clientSocket.setSoTimeout(2000);
            InetAddress IPAddress = InetAddress.getByName(IP);
            DatagramPacket sendPacket = new DatagramPacket(msg, msg.length, IPAddress, PORT);
            clientSocket.send(sendPacket);
            clientSocket.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


