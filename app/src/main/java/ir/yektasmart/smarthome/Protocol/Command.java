package ir.yektasmart.smarthome.Protocol;

/**
 * Created by macbookpro on 10/25/16 AD.
 */
public enum Command {
    TurnOn,
    TurnOff,
    TurnOnPending,
    TurnOffPending,
    RelayOn,
    RelayOff,
    RemoteClick,
    RemoteLongClickDown,
    RemoteLongClickUp,
    SetColor,
    StartAutoMode,
    StopAutoMode,
    SetAutoColor,
    SetAutoEffect,
    SetColorPending,
    ExecPendingCommand,
    setWhiteIntensity,
    TurnOnWhite,
    TurnOffWhite,
    MusicalModeChange,

    UserPermissionSet,
    UserPermissionReset,
    UserPermissionResponse;

    byte[] code;
    public byte[] getByte()
    {

        switch (this)
        {

            case TurnOff:
                code = new byte[]{0X00};
                return code;
            case TurnOn:
                code = new byte[]{0X01};
                return code;
            case UserPermissionSet:
                code = new byte[]{0X02};
                return code;
            case UserPermissionReset:
                code = new byte[]{0X03};
                return code;
            case UserPermissionResponse:
                code = new byte[]{0X04};
                return code;
            case RelayOff:
                code = new byte[]{0X05};
                return code;
            case RelayOn:
                code = new byte[]{0X06};
                return code;
            case RemoteClick:
                code = new byte[]{0X0A};
                return code;
            case RemoteLongClickDown:
                code = new byte[]{0X0B};
                return code;
            case RemoteLongClickUp:
                code = new byte[]{0X0C};
                return code;
            case SetColor:
                code = new byte[]{0X0D};
                return code;
            case StartAutoMode:
                code = new byte[]{0X0E};
                return code;
            case StopAutoMode:
                code = new byte[]{0X0F};
                return code;
            case SetAutoColor:
                code = new byte[]{0X10};
                return code;
            case SetAutoEffect:
                code = new byte[]{0X11};
                return code;
            case SetColorPending:
                code = new byte[]{0X12};
                return code;
            case ExecPendingCommand:
                code = new byte[]{0X13};
                return code;
            case setWhiteIntensity:
                code = new byte[]{0X14};
                return code;
            case TurnOnWhite:
                code = new byte[]{0X15};
                return code;
            case TurnOffWhite:
                code = new byte[]{0X16};
                return code;
            case TurnOffPending:
                code = new byte[]{0X17};
                return code;
            case TurnOnPending:
                code = new byte[]{0X18};
                return code;
            case MusicalModeChange:
                code = new byte[]{0X19};
                return code;
            default:
                code = new byte[]{(byte) 0XFF};
                return code;
        }
    }
}
