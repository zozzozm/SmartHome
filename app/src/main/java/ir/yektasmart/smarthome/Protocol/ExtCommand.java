package ir.yektasmart.smarthome.Protocol;

/**
 * Created by macbookpro on 10/29/16 AD.
 */
public enum ExtCommand {
    Btn1,
    Btn2,
    Btn3,
    Btn4,
    Btn5,
    Btn6,
    Btn7,
    Btn8,
    Btn9,
    Btn10,
    Btn11,
    Btn12,
    Btn13,
    Btn14,
    Btn15;

    byte[] code;

    public byte[] getByte(){
        switch(this)
        {

            case Btn1:
                code = new byte[]{0x01};
                return code;

            case Btn2:
                code = new byte[]{0x02};
                return code;

            case Btn3:
                code = new byte[]{0x03};
                return code;

            case Btn4:
                code = new byte[]{0x04};
                return code;

            case Btn5:
                code = new byte[]{0x05};
                return code;

            case Btn6:
                code = new byte[]{0x06};
                return code;

            case Btn7:
                code = new byte[]{0x07};
                return code;

            case Btn8:
                code = new byte[]{0x08};
                return code;

            case Btn9:
                code = new byte[]{0x09};
                return code;

            case Btn10:
                code = new byte[]{0x0A};
                return code;

            case Btn11:
                code = new byte[]{0xB};
                return code;

            case Btn12:
                code = new byte[]{0x0C};
                return code;

            case Btn13:
                code = new byte[]{0x0D};
                return code;

            case Btn14:
                code = new byte[]{0x0E};
                return code;

            case Btn15:
                code = new byte[]{0x0F};
                return code;




            default:
                code = new byte[] {(byte) 255};
                return code;
        }
    }
}
