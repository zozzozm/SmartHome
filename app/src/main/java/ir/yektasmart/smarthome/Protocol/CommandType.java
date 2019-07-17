package ir.yektasmart.smarthome.Protocol;

/**
 * Created by macbookpro on 10/25/16 AD.
 */
public enum CommandType {
    SEARCH_REQ,
    SEARCH_RESPONSE,
    STATUS_REQ,
    STATUS_RESPONSE,
    MONITORING_REQ,
    MONITORING_RESPONSE,
    ACTION_COMMAND,
    ACTION_RESPONSE,
    TRIG_COMMAND,
    SETTING_COMMAND;

    public byte[] getByte()
    {
        byte[] code;

        switch (this){

            case SEARCH_REQ:
                code = new byte[]{0,0};
                return code;
            case SEARCH_RESPONSE:
                code = new byte[]{0,1};
                return code;
            case STATUS_REQ:
                code = new byte[]{1,0};
                return code;
            case STATUS_RESPONSE:
                code = new byte[]{1,1};
                return code;
            case MONITORING_REQ:
                code = new byte[]{2,0};
                return code;
            case MONITORING_RESPONSE:
                code = new byte[]{2,1};
                return code;
            case ACTION_COMMAND:
                code = new byte[]{3,2};
                return code;
            case ACTION_RESPONSE:
                code = new byte[]{3,1};
                return code;
            case TRIG_COMMAND:
                code = new byte[]{4,2};
                return code;
            case SETTING_COMMAND:
                code = new byte[]{5,2};
                return code;
            default:
                code = new byte[]{(byte) 0xFF, (byte) 0xFF};
                return code;
        }
    }

}
