package ir.yektasmart.smarthome.Protocol;

/**
 * Created by macbookpro on 10/19/16 AD.
 */

public enum ModuleType {
    INTERNET,
    RF,
    IR,
    RGBW_LAMP,
    RGBW_STRIP,
    SWITCH,
    DIMMER,
    MUSICAL_STRIP,
    LATCH001,LATCH002,LATCH003,LATCH004,LATCH005,LATCH006,LATCH007,LATCH008,
    LATCH009,LATCH010,LATCH011,LATCH012,LATCH013,LATCH014,LATCH015,LATCH016,
    UNKNOW;
    //TODO : ADD LATCH010 TILL LATCH256

    byte[] code;

    public byte[] getCode() {
        switch (this) {

            case INTERNET:
                code = new byte[]{0, 0, 0, 1};
                return code;
            case RF:
                code = new byte[]{1, 4, 0, 1};
                return code;
            case IR:
                code = new byte[]{1, 3, 0, 1};
                return code;
            case RGBW_LAMP:
                code = new byte[]{1, 1, 0, 3};
                return code;
            case RGBW_STRIP:
                code = new byte[]{1, 2, 0, 1};
                return code;
            case DIMMER:
                code = new byte[]{1, 1, 0, 2};
                return code;
            case MUSICAL_STRIP:
                code = new byte[]{1,2, 0, 2};
                return code;
            case SWITCH://lamp
                code = new byte[]{1, 1, 0, 1};
                return code;
            case LATCH001:
                code = new byte[]{1, 0, 0, 1};
                return code;
            case LATCH002:
                code = new byte[]{1, 0, 0, 2};
                return code;
            case LATCH003:
                code = new byte[]{1, 0, 0, 3};
                return code;
            case LATCH004:
                code = new byte[]{1, 0, 0, 4};
                return code;
            case LATCH005:
                code = new byte[]{1, 0, 0, 5};
                return code;
            case LATCH006:
                code = new byte[]{1, 0, 0, 6};
                return code;
            case LATCH007:
                code = new byte[]{1, 0, 0, 7};
                return code;
            case LATCH008:
                code = new byte[]{1, 0, 0, 8};
                return code;
            case LATCH009:
                code = new byte[]{1, 0, 0, 9};
                return code;
            case LATCH010:
                code = new byte[]{1, 0, 0, 10};
                return code;
            case LATCH011:
                code = new byte[]{1, 0, 0, 11};
                return code;
            case LATCH012:
                code = new byte[]{1, 0, 0, 12};
                return code;
            case LATCH013:
                code = new byte[]{1, 0, 0, 13};
                return code;
            case LATCH014:
                code = new byte[]{1, 0, 0, 14};
                return code;
            case LATCH015:
                code = new byte[]{1, 0, 0, 15};
                return code;
            case LATCH016:
                code = new byte[]{1, 0, 0, 16};
                return code;

            default:
                code = new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255};
                return code;
        }
    }

    public String ToString()
    {
        switch (this)
        {
            case INTERNET:
                return "INTERNET";
            case RF:
                return "RF";
            case IR:
                return "IR";
            case RGBW_LAMP:
                return "RGBW_LAMP";
            case RGBW_STRIP:
                return "RGBW_STRIP";
            case MUSICAL_STRIP:
                return "MUSICAL_STRIP";
            case DIMMER:
                return "DIMMER";
            case SWITCH:
                return "SWITCH";
            case UNKNOW:
                return "UNKNOW";
            case LATCH001:
                return "LATCH001";
            case LATCH002:
                return "LATCH002";
            case LATCH003:
                return "LATCH003";
            case LATCH004:
                return "LATCH004";
            case LATCH005:
                return "LATCH005";
            case LATCH006:
                return "LATCH006";
            case LATCH007:
                return "LATCH007";
            case LATCH008:
                return "LATCH008";
            case LATCH009:
                return "LATCH009";
            case LATCH010:
                return "LATCH010";
            case LATCH011:
                return "LATCH011";
            case LATCH012:
                return "LATCH012";
            case LATCH013:
                return "LATCH013";
            case LATCH014:
                return "LATCH014";
            case LATCH015:
                return "LATCH015";
            case LATCH016:
                return "LATCH016";
            default:
                return "UNKNOWING TYPE";
        }
    }
}
