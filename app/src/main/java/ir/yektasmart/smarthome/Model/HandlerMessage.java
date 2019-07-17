package ir.yektasmart.smarthome.Model;

public class HandlerMessage {
    NetStatus state;
    byte[] message;

    public NetStatus getState() {
        return state;
    }

    public void setState(NetStatus state) {
        this.state = state;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }
}
