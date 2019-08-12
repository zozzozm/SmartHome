package ir.yektasmart.smarthome;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import ir.yektasmart.smarthome.MainActivity;

/**
 * Created by YektaCo on 08/05/2017.
 */

public class Mqtt_push extends Thread {

        String topic;
        byte[] msg;
        String clientId;
        boolean running = false;
        public static String TAG = "Mqtt_push";

        private Mqtt_push_result mMessageListener = null;

        public interface Mqtt_push_result {
            void Mqtt_push_failed();
            void Mqtt_push_sucess();
        }

        public void setListener(Mqtt_push_result mMessageListener){
            this.mMessageListener = mMessageListener;
        }
        public Mqtt_push(byte[] msg,String topic,String clientId){
            this.topic = topic;
            this.msg=msg;
            this.clientId = clientId;
            running = true;
        }

        public void Runing(boolean rn){
            running = rn;
        }
        @Override
        public void run()  {

            try {


                //String topic        = "con.yektasmart.adminUUidRec";
                //String content      = "Message from MqttPublishSample";
                int qos             = 2;// TODO: 8/15/2017 what QoS is needed?

                //String clientId     = "JavaSample34rerw";
                MemoryPersistence persistence = new MemoryPersistence();

                MqttClient sampleClient = new MqttClient(Const.BROKER, clientId, persistence);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);
                Log.e(TAG, "Connecting to broker: "+ Const.BROKER);
                sampleClient.connect(connOpts);
                Log.e(TAG, "Connected");
                Log.e(TAG, "Publishing message: "+msg);
                MqttMessage message = new MqttMessage(msg);
                message.setQos(qos);
                sampleClient.publish(topic, message);
                Log.e(TAG, "Message published");
                sampleClient.disconnect();
                Log.e(TAG, "Disconnected");

                if (mMessageListener != null)
                    mMessageListener.Mqtt_push_sucess();
            } catch(MqttException me) {
                Log.e(TAG, "reason "+me.getReasonCode());
                Log.e(TAG, "msg "+me.getMessage());
                Log.e(TAG, "loc "+me.getLocalizedMessage());
                Log.e(TAG, "cause "+me.getCause());
                Log.e(TAG, "excep "+me);
                //Const.isAwayMode = false;

                if (mMessageListener != null)
                    mMessageListener.Mqtt_push_failed();
                me.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
