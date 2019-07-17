package ir.yektasmart.smarthome.Protocol;

/**
 * Created by macbookpro on 10/25/16 AD.
 */
public enum Effect {
    EFFECT1,
    EFFECT2,
    EFFECT3,
    EFFECT4,
    EFFECT5;

    public byte getByte()
    {
        switch (this)
        {

            case EFFECT1:
                return 0X01;
            case EFFECT2:
                return 0X02;
            case EFFECT3:
                return 0X03;
            case EFFECT4:
                return 0X04;
            case EFFECT5:
                return 0X05;
            default:
                return (byte) 0xFF;
        }
    }
}
