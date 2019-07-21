package ir.yektasmart.smarthome;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendUdp extends Thread {

    String IP;
    int PORT;
    byte[] msg;
    boolean running = false;

    public SendUdp(byte[] msg,String ip, int port){
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
            while(running) {
                DatagramSocket clientSocket;
                clientSocket = new DatagramSocket();
                clientSocket.setSoTimeout(2000);
                clientSocket.setBroadcast(true);
                InetAddress IPAddress = InetAddress.getByName(IP);
                DatagramPacket sendPacket = new DatagramPacket(msg, msg.length, IPAddress, PORT);
                clientSocket.send(sendPacket);
                clientSocket.close();
                Thread.sleep(3000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


