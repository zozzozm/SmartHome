package ir.yektasmart.smarthome;

import java.net.InetAddress;

/**
 * Created by YektaCo on 25/01/2017.
 */

public class Const {

    //////////////////////////////////////////////////////////////////// Network connections
    public static int UDP_PORT = 65509;
    public static int TCP_PORT = 17319;
    public static int UDP_REC_PORT = 65517;
    public static String BROADCAST_IP = "255.255.255.255";//255.255.255.255
    public static String BROADCAST_IP_REC = "0.0.0.0";
    public static String UDP_HANDSHAKE_MSG = ".p..1d$1f8r+.o)gq-2%";
    public static InetAddress BROADCAST_IPADD  = null;

    public static String CRYPT_KEY = "AliMirzaey";
    public static String CRYPT_IV  = "92476";

    //////////////////////////////////////////////////////////////////// runtime params
    public static String UUID = "";
    public static String USER_NAME = "";
    public static boolean isVibrateMode   = false;
    public static boolean isAwayMode      = false;
    public static boolean isProtectedMode = false;


    /////////////////////////////////////////////////////////////////// shared preferences
    public static String SP_LoginIsProtected = "loginPageIsprotexted";
    public static String SP_LoginPaswword    = "loginPagePassword";
    public static String SP_LoginUserName    = "loginUsername";
    public static String SP_UUID   = "1taUniquUUID";

    public static String SP_VibrateMode = "viberateMode";
    public static String SP_AwayMode    = "awayMode";
    //////////////////////////////////////////////////////////////////// MQTT Broker

    public static String BROKER       ="tcp://iot.eclipse.org:1883";// "tcp://192.168.1.9:1883";//
}
