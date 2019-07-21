package yektasmart.com.geniusremote;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by YektaCo on 03/01/2017.
 */

public class yektaMessage {

    private String rawData;

    public yektaMessage(String msg) {
        this.rawData = msg;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public String getName(){
        return rawData.substring(20,rawData.length()-50);
    }

    public String getUUID(){
        return rawData.substring(rawData.length()-50,rawData.length());
    }

    public String getNameViaQr(){
        return rawData.substring(20,rawData.length()-65);
    }

    public String getUUIDViaQr(){
        return rawData.substring(rawData.length()-65,rawData.length()-15);
    }

    public String getIP(){
        return rawData.substring(rawData.length()-15);
    }
}
