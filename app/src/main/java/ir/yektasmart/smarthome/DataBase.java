package ir.yektasmart.smarthome;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import ir.yektasmart.smarthome.Model.Access;
import ir.yektasmart.smarthome.Model.BaseDevice;
import ir.yektasmart.smarthome.Model.Group;
import ir.yektasmart.smarthome.Model.RgbDevice;
import ir.yektasmart.smarthome.Model.SelectableItem;
import ir.yektasmart.smarthome.Model.User;


/**
 * Created by macbookpro on 10/18/16 AD.
 */
public class DataBase {

    public enum Confilict {
        abort,replace;
    }
    private static final String TAG = "DataBase";
    private static final String _FIELD_COUNT = "countField";
    private final Context ourContext;
    private DBHelper ourHelper;
    private SQLiteDatabase ourDatabase;

    private static final String DATABASE_NAME = "YektaSmartHome";
    private static final int DATABASE_VERSION = 5;
    //DataBase tables
    private static final String _TABLE_MODULE       = "module_table";
    private static final String _TABLE_GROUP        = "group_Table";
    private static final String _TABLE_TYPE         = "type_Table";
    private static final String _TABLE_HOME         = "home_table";
    private static final String _TABLE_USER      = "user_table";
    private static final String _TABLE_ACCESS    = "access_table";
    private static final String _TABLE_RGBW      = "rgb_table";
    private static final String _TABLE_LATCH     = "latch_table";
    private static final String _TABLE_RF        = "rf_table";
    private static final String _TABLE_HISTORY      = "history_Table";
    //DataBase Fields

     //_TABLE_MODULE/////////////////////////////////////////////////////////////////
    private static final String _FIELD_MODULE_ID         = "module_ID";//PK
    private static final String _FIELD_MODULE_NAME       = "module_name";
    private static final String _FIELD_MODULE_MAC        = "module_mac";
    private static final String _FIELD_MODULE_PASS       = "module_pass";
    private static final String _FIELD_MODULE_AVETAR     = "module_avet";
    private static final String _FIELD_MODULE_INDEX      = "module_index";
    private static final String _FIELD_MODULE_ONOFF      = "module_onoff";
    private static final String _FIELD_MODULE_PERMISSION = "module_permission";
    private static final String _FIELD_MODULE_PORT       = "module_port";
    private static final String _FIELD_LATCH_PORT        = "latch_port";
    //private static final String _FIELD_TYPE_ID;//F.K
    //private static final String _FIELD_HOME_ID;//F.K
    //private static final String _FIELD_GROUP_ID;//F.K
    //END_OF _MODULE_TABLE

    //_TABLE_GROUP////////////////////////////////////////////////////////////////////
    private static final String _FIELD_GROUP_ID     = "group_id";
    private static final String _FIELD_GROUP_NAME   = "group_name";
    private static final String _FIELD_GROUP_INDEX  = "group_index";
    private static final String _FIELD_GROUP_ONOFF  = "group_onOff";
    private static final String _FIELD_GROUP_COLOR  = "group_color";
    private static final String _FIELD_GROUP_DIMMER = "group_dimmer";
    private static final String _FIELD_GROUP_WHITE_ON    = "group_white_on";
    private static final String _FIELD_GROUP_WHITE_VALUE = "group_whiteVal";
    //private static final String _FIELD_HOME_ID;//F.K
    //END_OF _MODULE_GROUP

    //_TABLE_TYPE/////////////////////////////////////////////////////////////////////
    private static final String _FIELD_TYPE_ID   = "type_id";
    private static final String _FIELD_TYPE_NAME = "type_name";
    private static final String _FIELD_TYPE_CODE = "type_code";
    private static final String _FIELD_TYPE_CODE_NAME = "type_codeName";
    private static final String _FIELD_TYPE_GROUPABLE = "type_groupable";
    //END_OF _TYPE_TABLE

    //_TABLE_HOME/////////////////////////////////////////////////////////////////////
    private static final String _FIELD_HOME_ID   = "home_id";
    private static final String _FIELD_HOME_NAME = "home_name";
    private static final String _FIELD_HOME_ADDRESS = "home_address";
    //END_OF _TABLE_HOME

    //_TABLE_USER////////////////////////////////////////////////////////////////////
    private static final String _FIELD_USER_ID          = "user_id";
    private static final String _FIELD_USER_UNIQUE_ID   = "user_unique_id";
    private static final String _FIELD_USER_NAME        = "user_name";
    private static final String _FIELD_USER_CELL        = "user_cell";
    private static final String _FIELD_USER_PIC         = "user_pic";
    private static final String _FIELD_USER_DESCRIPTION = "user_desc";
    private static final String _FIELD_USER_IS_ACTIVE   = "user_isActive";
    //END_OF _TABLE_USER

    //_TABLE_ACCESS//////////////////////////////////////////////////////////////////
    private static final String _FIELD_ACCESS_ID = "access_id";
    //private static final String _FIELD_USER_ID;   //F.K
    //private static final String _FIELD_MODULE_ID; //F.K
    private static final String _FIELD_ACCESS_STATUS = "access_status";
    //END_OF _TABLE_ACCESS

    //_TABLE_RGBW/////////////////////////////////////////////////////////////////////
    private static final String _FIELD_RGBW_ID    = "rgbw_id";
    //private static final String _FIELD_MODULE_ID; //F.K
    private static final String _FIELD_RGBW_COLOR = "rgbw_color";
    private static final String _FIELD_RGBW_WHITE_ON = "rgbw_whiteOn";
    private static final String _FIELD_RGBW_WHITE_VALUE = "rgbw_whiteVal";
    private static final String _FIELD_RGBW_ANIMATE  = "rgbw_animate";
    private static final String _FIELD_RGBW_MODE  = "rgbw_mode";
    private static final String _FIELD_RGBW_FAV01 = "rgbw_fav1";
    private static final String _FIELD_RGBW_FAV02 = "rgbw_fav2";
    private static final String _FIELD_RGBW_FAV03 = "rgbw_fav3";
    private static final String _FIELD_RGBW_FAV04 = "rgbw_fav4";
    private static final String _FIELD_RGBW_FAV05 = "rgbw_fav5";
    private static final String _FIELD_RGBW_FAV06 = "rgbw_fav6";
    private static final String _FIELD_RGBW_FAV07 = "rgbw_fav7";
    private static final String _FIELD_RGBW_FAV08 = "rgbw_fav8";
    private static final String _FIELD_RGBW_FAV09 = "rgbw_fav9";
    private static final String _FIELD_RGBW_FAV10 = "rgbw_fav10";

    private static final int[] color_palete = {0xFFFF0000,0xFFFFFF00,0xFF00FF00,0xFF00FFFF,0xFF0000FF,0xFFFF00FF,0xFFFFFFFF,0xFFFF0080,0xFFFF8000,0xFF010101};
    //END_OF _TABLE_RGBW

/*    //_TABLE_LATCH//////////////////////////////////////////////////////////////////////
    private static final String _FIELD_LATCH_ID   = "latch_id";
    //private static final String _FIELD_MODULE_ID; //F.K
    private static final String _FIELD_LATCH_PORT = "latch_port";
    //END_OF _TABLE_LATCH*/

    //END FIELD DEFINITION///////////////////////////////////////////////////////////////////

    public DataBase(Context c) {
        this.ourContext = c;
    }



    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            try {
                Log.e(TAG, "onCreate: "+ " creating" );

                db.execSQL("CREATE TABLE " + _TABLE_MODULE + " (" +
                        _FIELD_MODULE_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                        _FIELD_MODULE_NAME       + " TEXT NOT NULL , " +
                        _FIELD_MODULE_MAC        + " TEXT NOT NULL , " +
                        _FIELD_MODULE_PASS       + " TEXT NOT NULL , " +
                        _FIELD_MODULE_AVETAR     + " INTEGER , " +
                        _FIELD_MODULE_INDEX      + " INTEGER , " +
                        _FIELD_MODULE_ONOFF      + " INTEGER NOT NULL , " +
                        _FIELD_MODULE_PERMISSION + " INTEGER NOT NULL , " +
                        _FIELD_MODULE_PORT       + " INTEGER NOT NULL , " +
                        _FIELD_LATCH_PORT        + " INTEGER NOT NULL , " +
                        _FIELD_TYPE_ID           + " INTEGER NOT NULL , " +//F.K
                        _FIELD_HOME_ID           + " INTEGER NOT NULL , " +//F.K
                        _FIELD_GROUP_ID          + " INTEGER NOT NULL , " +//F.K
                        _FIELD_USER_ID           + " INTEGER NOT NULL , " +//F.K"
                        " UNIQUE ( " + _FIELD_MODULE_MAC + "," + _FIELD_LATCH_PORT + "));");

                db.execSQL("CREATE TABLE " + _TABLE_GROUP + " (" +
                        _FIELD_GROUP_ID           + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                        _FIELD_GROUP_NAME         + " TEXT NOT NULL , "+
                        _FIELD_GROUP_INDEX        + " INTEGER , "      +
                        _FIELD_GROUP_ONOFF        + " INTEGER , "      +
                        _FIELD_GROUP_COLOR        + " INTEGER , "      +
                        _FIELD_GROUP_DIMMER       + " INTEGER , "      +
                        _FIELD_GROUP_WHITE_ON     + " INTEGER , "      +
                        _FIELD_GROUP_WHITE_VALUE  + " INTEGER , "      +
                        _FIELD_HOME_ID            + " INTEGER NOT NULL );"); //F.K);");

                db.execSQL("CREATE TABLE " + _TABLE_TYPE + " (" +
                        _FIELD_TYPE_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                        _FIELD_TYPE_NAME       + " TEXT NOT NULL , "    +
                        _FIELD_TYPE_CODE       + " INTEGER NOT NULL , " +
                        _FIELD_TYPE_CODE_NAME  + " TEXT NOT NULL , "    +
                        _FIELD_TYPE_GROUPABLE  + " INTEGER NOT NULL );");


                db.execSQL("CREATE TABLE " + _TABLE_HOME + " (" +
                        _FIELD_HOME_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                        _FIELD_HOME_NAME      + " TEXT NOT NULL UNIQUE , "   +
                        _FIELD_HOME_ADDRESS   + " TEXT );");
                        //"UNIQUE ( " + _FIELD_GROUP_NAME + "," + _FIELD_TYPE_ID + ");");

                db.execSQL("CREATE TABLE " + _TABLE_USER + " (" +
                        _FIELD_USER_ID           + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                        _FIELD_USER_UNIQUE_ID    + " TEXT NOT NULL UNIQUE , "  +
                        _FIELD_USER_NAME         + " TEXT NOT NULL , " +
                        _FIELD_USER_CELL         + " TEXT , " +
                        _FIELD_USER_PIC          + " BLOB , " +
                        _FIELD_USER_DESCRIPTION  + " TEXT , " +
                        _FIELD_USER_IS_ACTIVE    + " INTEGER );");

                db.execSQL("CREATE TABLE "+_TABLE_ACCESS+" (" +
                        _FIELD_ACCESS_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                        _FIELD_MODULE_ID         + " INTEGER NOT NULL ," +
                        _FIELD_USER_ID           + " INTEGER NOT NULL ," +
                        _FIELD_ACCESS_STATUS     + " INTEGER NOT NULL ," +
                        _FIELD_MODULE_PERMISSION + " INTEGER ," +
                        "UNIQUE ( " + _FIELD_MODULE_ID + "," + _FIELD_USER_ID + ") );");

                db.execSQL("CREATE TABLE " + _TABLE_RGBW + " (" +
                        _FIELD_RGBW_ID           + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                        _FIELD_MODULE_ID         + " INTEGER UNIQUE, " +//F.K
                        _FIELD_RGBW_COLOR        + " INTEGER NOT NULL , " +
                        _FIELD_RGBW_WHITE_ON     + " INTEGER NOT NULL , " +
                        _FIELD_RGBW_WHITE_VALUE  + " INTEGER NOT NULL , " +
                        _FIELD_RGBW_ANIMATE      + " INTEGER NOT NULL , " +
                        _FIELD_RGBW_MODE         + " INTEGER NOT NULL , " +
                        _FIELD_RGBW_FAV01        + " INTEGER NOT NULL , " +
                        _FIELD_RGBW_FAV02        + " INTEGER NOT NULL , " +
                        _FIELD_RGBW_FAV03        + " INTEGER NOT NULL , " +
                        _FIELD_RGBW_FAV04        + " INTEGER NOT NULL , " +
                        _FIELD_RGBW_FAV05        + " INTEGER NOT NULL , " +
                        _FIELD_RGBW_FAV06        + " INTEGER NOT NULL , " +
                        _FIELD_RGBW_FAV07        + " INTEGER NOT NULL , " +
                        _FIELD_RGBW_FAV08        + " INTEGER NOT NULL , " +
                        _FIELD_RGBW_FAV09        + " INTEGER NOT NULL , " +
                        _FIELD_RGBW_FAV10        + " INTEGER NOT NULL );");
/*

                db.execSQL("CREATE TABLE " + _TABLE_LATCH + " (" +
                        _FIELD_LATCH_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                        _FIELD_MODULE_ID   + " INTEGER , " + //F.K
                        _FIELD_LATCH_PORT  + " INTEGER NOT NULL );");
*/


                // create valid type
                String insert = "INSERT INTO " + _TABLE_TYPE + " VALUES ";
                insert += "(0 ,'Internet',0001 ,'INTERNET',0),";
                insert += "(1 ,'Radio Frequency',1401 ,'RF',0),";
                insert += "(2 ,'Infrared',1301 ,'IR',0),";
                insert += "(3 ,'Colorful',1103 ,'RGBW_LAMP',1),";
                insert += "(4 ,'Colorful',1201,'RGBW_STRIP',1),";
                insert += "(5 ,'Colorful',1202,'MUSICAL_STRIP',1),";
                insert += "(6 ,'Dimmer', 1102,'DIMMER',1),";
                insert += "(7 ,'Unknow', 0,'UNKNOW',0),";
                insert += "(8 ,'Switch', 1101,'SWITCH',1),";
                insert += "(9 ,'Switch', 1001,'LATCH001',1), ";
                insert += "(10,'Switch', 1002,'LATCH002',1), ";
                insert += "(11,'Switch', 1003,'LATCH003',1), ";
                insert += "(12,'Switch', 1004,'LATCH004',1), ";
                insert += "(13,'Switch', 1005,'LATCH005',1), ";
                insert += "(14,'Switch', 1006,'LATCH006',1), ";
                insert += "(15,'Switch', 1007,'LATCH007',1), ";
                insert += "(16,'Switch', 1008,'LATCH008',1), ";
                insert += "(17,'Switch', 1009,'LATCH009',1), ";
                insert += "(18,'Switch', 1000,'LATCH010',1), ";
                insert += "(19,'Switch', 1002,'LATCH011',1), ";
                insert += "(20,'Switch', 1002,'LATCH012',1), ";
                insert += "(21,'Switch', 1002,'LATCH013',1), ";
                insert += "(22,'Switch', 1002,'LATCH014',1), ";
                insert += "(23,'Switch', 1002,'LATCH015',1), ";
                insert += "(24,'Switch', 1002,'LATCH016',1), ";
                insert += "(25,'WaterAC',1305,'WaterAC' ,0) " ;

                insert += ";";
                db.execSQL(insert);

            } catch (Exception e) {
                Log.e(TAG, "onCreate: " + e.getMessage());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IS EXIST " + _TABLE_TYPE);
            db.execSQL("DROP TABLE IS EXIST " + _TABLE_GROUP);
            db.execSQL("DROP TABLE IS EXIST " + _TABLE_MODULE);
            db.execSQL("DROP TABLE IS EXIST " + _TABLE_HISTORY);
            db.execSQL("DROP TABLE IS EXIST " + _TABLE_RGBW);
            db.execSQL("DROP TABLE IS EXIST " + _TABLE_ACCESS);
            db.execSQL("DROP TABLE IS EXIST " + _TABLE_USER);
            db.execSQL("DROP TABLE IS EXIST " + _TABLE_HOME);
            onCreate(db);
        }
    }

    public DataBase open() throws SQLException {
        ourHelper = new DBHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        ourHelper.close();
    }

    public int addBaseDevice(String _mac, String _name , String _pass, int _tid , int _avet, int _permission , int _port, int _latchPort , int userId , Confilict confilict) {
        long a = -1;
        try {
            open();
            ContentValues cv = new ContentValues();
            cv.put(_FIELD_MODULE_NAME, _name);
            cv.put(_FIELD_TYPE_ID, _tid);
            cv.put(_FIELD_MODULE_MAC, _mac);
            cv.put(_FIELD_MODULE_PASS, _pass);
            cv.put(_FIELD_MODULE_AVETAR, _avet);
            cv.put(_FIELD_GROUP_ID, -1);
            cv.put(_FIELD_MODULE_INDEX, 0);
            cv.put(_FIELD_MODULE_ONOFF, 0);
            cv.put(_FIELD_MODULE_PERMISSION, _permission);
            cv.put(_FIELD_HOME_ID, -1);
            cv.put(_FIELD_LATCH_PORT, _latchPort);
            cv.put(_FIELD_MODULE_PORT, _port);
            cv.put(_FIELD_USER_ID , userId );

            if (confilict == Confilict.abort)
                a = ourDatabase.insertWithOnConflict(_TABLE_MODULE, null, cv, SQLiteDatabase.CONFLICT_ABORT);
            else if (confilict == Confilict.replace)
                a = ourDatabase.insertWithOnConflict(_TABLE_MODULE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
            else
                a = ourDatabase.insertWithOnConflict(_TABLE_MODULE, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        }catch (Exception e){
            e.printStackTrace();
            a = -1;
        }
        finally {
            close();
            return (int) a;
        }
    }

    public int addType(int TYPE_ID, String TYPE_NAME , int TYPE_CODE, String TYPE_CODE_NAME , int TYPE_GROUPABLE ) {

        long a = -1;

        try {
            open();
            ContentValues cv = new ContentValues();
            cv.put(_FIELD_TYPE_ID, TYPE_ID);
            cv.put(_FIELD_TYPE_NAME, TYPE_NAME);
            cv.put(_FIELD_TYPE_CODE, TYPE_CODE);
            cv.put(_FIELD_TYPE_CODE_NAME, TYPE_CODE_NAME);
            cv.put(_FIELD_TYPE_GROUPABLE, TYPE_GROUPABLE);

            a = ourDatabase.insertWithOnConflict(_TABLE_TYPE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        }catch (Exception e){
            e.printStackTrace();
            a = -1;
        }
        finally {
            close();
            return (int) a;
        }
    }

    public int addBaseDeviceImport(String _mac, String _name , String _pass, int _tid , int _avet, int _permission , int _port, int _latchPort, int _onOff, int _uid) {
        long a = -1;
        try {

            int avetar = -1;
            switch (_tid){
                case 1:
                    avetar = R.drawable.rf_remote;
                    break;

                case 2:
                    avetar = R.drawable.question;
                    break;

                case 3:
                    avetar = R.drawable.hue;
                    break;

                case 4:
                    avetar = R.drawable.hue;
                    break;

                case 5:
                    avetar = R.drawable.musical;
                    break;

                case 6:
                    avetar = R.drawable.question;
                    break;

                case 7:
                    avetar = R.drawable.question;
                    break;

                case 8:
                    avetar = R.drawable.lamp;
                    break;

                case 9:
                    avetar = R.drawable.latch001;
                    break;

                case 10:
                    avetar = R.drawable.latch002;
                    break;

                default:
                    avetar = R.drawable.question;

            }
            open();
            ContentValues cv = new ContentValues();
            cv.put(_FIELD_MODULE_NAME, _name);
            cv.put(_FIELD_TYPE_ID, _tid);
            cv.put(_FIELD_MODULE_MAC, _mac);
            cv.put(_FIELD_MODULE_PASS, _pass);
            cv.put(_FIELD_MODULE_AVETAR, avetar);
            cv.put(_FIELD_GROUP_ID, -1);
            cv.put(_FIELD_MODULE_INDEX, 0);
            cv.put(_FIELD_MODULE_ONOFF, _onOff);
            cv.put(_FIELD_MODULE_PERMISSION, _permission);
            cv.put(_FIELD_HOME_ID, -1);
            cv.put(_FIELD_LATCH_PORT, _latchPort);
            cv.put(_FIELD_MODULE_PORT, _port);
            cv.put(_FIELD_USER_ID , _uid );

            a = ourDatabase.insertWithOnConflict(_TABLE_MODULE, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        }catch (Exception e){
            e.printStackTrace();
            a = -1;
        }
        finally {
            close();
            return (int) a;
        }
    }
    public void addChildDevice(int _id,int _tid){

        try {
            if (_id != -1) {
                open();
                switch (_tid) {
                    case 1:// is 'RF' module and don't have any seperate table
                        break;

                    case 2:// is 'IR' modules and not implemented yet
                        break;

                    case 3:// is 'RGBW_LAMP'
                    case 4:// is 'RGBW_STRIP'
                    case 5:// is 'MUSICAL_STRIP'
                        //enter module related data to his table
                        ContentValues cvv = new ContentValues();
                        cvv.put(_FIELD_MODULE_ID, _id);
                        cvv.put(_FIELD_RGBW_COLOR, color_palete[0]);
                        cvv.put(_FIELD_RGBW_FAV01, color_palete[0]);
                        cvv.put(_FIELD_RGBW_FAV02, color_palete[1]);
                        cvv.put(_FIELD_RGBW_FAV03, color_palete[2]);
                        cvv.put(_FIELD_RGBW_FAV04, color_palete[3]);
                        cvv.put(_FIELD_RGBW_FAV05, color_palete[4]);
                        cvv.put(_FIELD_RGBW_FAV06, color_palete[5]);
                        cvv.put(_FIELD_RGBW_FAV07, color_palete[6]);
                        cvv.put(_FIELD_RGBW_FAV08, color_palete[7]);
                        cvv.put(_FIELD_RGBW_FAV09, color_palete[8]);
                        cvv.put(_FIELD_RGBW_FAV10, color_palete[9]);

                        cvv.put(_FIELD_RGBW_WHITE_ON, 0);
                        cvv.put(_FIELD_RGBW_WHITE_VALUE, 0);
                        cvv.put(_FIELD_RGBW_ANIMATE, 0);
                        cvv.put(_FIELD_RGBW_MODE, 0);

                        ourDatabase.insertWithOnConflict(_TABLE_RGBW, null, cvv, SQLiteDatabase.CONFLICT_IGNORE);
                        break;

                    case 6:// is 'DIMMER' and not implemented yet
                        break;

                    case 8:// is 'SWITCH' and don't have any seperate table
                        break;

                    case 9: //is 'LATCH001' is implemented on module table
                    case 10://is 'LATCH002' and should be as 2 number of latch001
                    case 11://is 'LATCH003' and should be as 3 number of latch001
                    case 12://is 'LATCH004' and should be as 4 number of latch001
                    case 13://is 'LATCH005' and should be as 5 number of latch001
                    case 14://is 'LATCH006' and should be as 6 number of latch001
                    case 15://is 'LATCH007' and should be as 7 number of latch001
                    case 16://is 'LATCH008' and should be as 8 number of latch001
                    case 17://is 'LATCH009' and should be as 9 number of latch001
                    case 18://is 'LATCH010' and should be as 10 number of latch001
                    case 19://is 'LATCH011' and should be as 11 number of latch001
                    case 20://is 'LATCH012' and should be as 12 number of latch001
                    case 21://is 'LATCH013' and should be as 13 number of latch001
                    case 22://is 'LATCH014' and should be as 14 number of latch001
                    case 23://is 'LATCH015' and should be as 15 number of latch001
                    case 24://is 'LATCH016' and should be as 16 number of latch001
                        //implement in code by calling this function multiple time
                        //because we need each module's id to create seperate page to each port of latch
                        break;

                    default:
                        break;
                }

                close();
            }
        }catch (Exception e){ e.printStackTrace();}
    }

    public int addDevice(String _mac, String _name , String _pass, int _tid , int _permission , int _port, int _latchPort , int _userId, Confilict confilict){

        int avetar = -1;
        switch (_tid){

            case 0:
                avetar = R.drawable.internet;
                break;

            case 1:
                avetar = R.drawable.rf_remote;
                break;

            case 2:
                avetar = R.drawable.question;
                break;

            case 3:
                avetar = R.drawable.hue;
                break;

            case 4:
                avetar = R.drawable.hue;
                break;

            case 5:
                avetar = R.drawable.musical;
                break;

            case 6://dim
                avetar = R.drawable.question;
                break;

            case 7://unk
                avetar = R.drawable.question;
                break;

            case 8:
                avetar = R.drawable.lamp;
                break;

            case 9:
                avetar = R.drawable.latch001;
                break;

            case 10:
                avetar = R.drawable.latch002;
                break;

            default:
                avetar = R.drawable.question;

        }
        int _id = addBaseDevice(_mac,_name,_pass,_tid,avetar,_permission,_port,_latchPort,_userId,confilict);
        if(_id != -1 ) // baseDevice is added with id = '_id'
            addChildDevice(_id,_tid);
        else
            return -1;

        return _id;
    }

    public ArrayList<BaseDevice> getInternetDevice() {
        try {
            open();
            ArrayList<BaseDevice> modules = new ArrayList<>();

            String select = " " +
                    _FIELD_MODULE_ID + "," + _FIELD_MODULE_NAME + "," + _FIELD_MODULE_MAC + "," +
                    _FIELD_MODULE_PASS + "," + _FIELD_MODULE_AVETAR + "," + _FIELD_MODULE_INDEX + "," +
                    _FIELD_MODULE_ONOFF + "," + _FIELD_MODULE_PERMISSION + "," + _FIELD_MODULE_PORT + "," +
                    _FIELD_LATCH_PORT + "," + _TABLE_MODULE + "." + _FIELD_TYPE_ID + "," +
                    _TABLE_TYPE + "." + _FIELD_TYPE_CODE_NAME + "," + _FIELD_HOME_ID + "," +
                    _FIELD_GROUP_ID + "," + _FIELD_USER_ID;

            String from = " " + _TABLE_MODULE + "," + _TABLE_TYPE;
            String where = " " + _TABLE_MODULE + "." + _FIELD_TYPE_ID + " = " + _TABLE_TYPE + "." + _FIELD_TYPE_ID + " AND " + _TABLE_TYPE + "." + _FIELD_TYPE_NAME + " == " + "'Internet'" ;
            String orderBy = " " + _TABLE_MODULE + "." + _FIELD_MODULE_INDEX + "," + _TABLE_MODULE + "." + _FIELD_MODULE_ID;
            String selectQuery = "SELECT " + select + " FROM " + from + " WHERE " + where + " ORDER BY " + orderBy;
            //Log.e(TAG, "getModules: "+ selectQuery );

            Cursor c = ourDatabase.rawQuery(selectQuery, null);

            int iId = c.getColumnIndex(_FIELD_MODULE_ID);
            int iName = c.getColumnIndex(_FIELD_MODULE_NAME);
            int iMac = c.getColumnIndex(_FIELD_MODULE_MAC);
            int iPass = c.getColumnIndex(_FIELD_MODULE_PASS);
            int iImage = c.getColumnIndex(_FIELD_MODULE_AVETAR);
            int iIndex = c.getColumnIndex(_FIELD_MODULE_INDEX);
            int iOnOff = c.getColumnIndex(_FIELD_MODULE_ONOFF);
            int iPermission = c.getColumnIndex(_FIELD_MODULE_PERMISSION);
            int iPort = c.getColumnIndex(_FIELD_MODULE_PORT);
            int iLatchPort = c.getColumnIndex(_FIELD_LATCH_PORT);//
            int iTypeId = c.getColumnIndex(_FIELD_TYPE_ID);//
            int iType = c.getColumnIndex(_FIELD_TYPE_CODE_NAME);//
            int iHome = c.getColumnIndex(_FIELD_HOME_ID);
            int iGid = c.getColumnIndex(_FIELD_GROUP_ID);
            int iUid = c.getColumnIndex(_FIELD_USER_ID);

            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {

                    BaseDevice bd = new BaseDevice();
                    bd.setId(c.getInt(iId));
                    bd.setName(c.getString(iName));
                    bd.setMac(c.getString(iMac));
                    bd.setPass(c.getString(iPass));
                    bd.setAvetar(c.getInt(iImage));
                    bd.setIndex(c.getInt(iIndex));
                    bd.setOnOff(c.getInt(iOnOff));
                    bd.setPermission(c.getInt(iPermission));
                    bd.setPort(c.getInt(iPort));
                    bd.setLatchPort(c.getInt(iLatchPort));
                    bd.setTypeId(c.getInt(iTypeId));
                    bd.setType(c.getString(iType));
                    bd.setHomeId(c.getInt(iHome));
                    bd.setGroupId(c.getInt(iGid));
                    bd.setUid(c.getInt(iUid));
                    bd.setSelected(false);

                    modules.add(bd);
                    //Log.i(TAG, i + ": Module in Database: MAC:" + bd.getMac() + " Name:" + bd.getName() + " Index:" + bd.getIndex() + " Type:" + bd.getType() + " Avetar:" + bd.getAvetar());
                    c.moveToNext();
                }
            }
            c.close();
            close();
            return modules;
        }catch (Exception e){e.printStackTrace();}
        return null;
    }

    public ArrayList<BaseDevice> getBaseModules() {

        try {
            open();
            ArrayList<BaseDevice> modules = new ArrayList<>();

            String select = " " +
                    _FIELD_MODULE_ID + "," + _FIELD_MODULE_NAME + "," + _FIELD_MODULE_MAC + "," +
                    _FIELD_MODULE_PASS + "," + _FIELD_MODULE_AVETAR + "," + _FIELD_MODULE_INDEX + "," +
                    _FIELD_MODULE_ONOFF + "," + _FIELD_MODULE_PERMISSION + "," + _FIELD_MODULE_PORT + "," +
                    _FIELD_LATCH_PORT + "," + _TABLE_MODULE + "." + _FIELD_TYPE_ID + "," +
                    _TABLE_TYPE + "." + _FIELD_TYPE_CODE_NAME + "," + _FIELD_HOME_ID + "," +
                    _FIELD_GROUP_ID + "," + _FIELD_USER_ID;

            String from = " " + _TABLE_MODULE + "," + _TABLE_TYPE;
            String where = " " + _TABLE_MODULE + "." + _FIELD_TYPE_ID + " = " + _TABLE_TYPE + "." + _FIELD_TYPE_ID ;
            String orderBy = " " + _TABLE_MODULE + "." + _FIELD_MODULE_INDEX + "," + _TABLE_MODULE + "." + _FIELD_MODULE_ID;
            String selectQuery = "SELECT " + select + " FROM " + from + " WHERE " + where + " ORDER BY " + orderBy;
            //Log.e(TAG, "getModules: "+ selectQuery );

            Cursor c = ourDatabase.rawQuery(selectQuery, null);

            int iId = c.getColumnIndex(_FIELD_MODULE_ID);
            int iName = c.getColumnIndex(_FIELD_MODULE_NAME);
            int iMac = c.getColumnIndex(_FIELD_MODULE_MAC);
            int iPass = c.getColumnIndex(_FIELD_MODULE_PASS);
            int iImage = c.getColumnIndex(_FIELD_MODULE_AVETAR);
            int iIndex = c.getColumnIndex(_FIELD_MODULE_INDEX);
            int iOnOff = c.getColumnIndex(_FIELD_MODULE_ONOFF);
            int iPermission = c.getColumnIndex(_FIELD_MODULE_PERMISSION);
            int iPort = c.getColumnIndex(_FIELD_MODULE_PORT);
            int iLatchPort = c.getColumnIndex(_FIELD_LATCH_PORT);//
            int iTypeId = c.getColumnIndex(_FIELD_TYPE_ID);//
            int iType = c.getColumnIndex(_FIELD_TYPE_CODE_NAME);//
            int iHome = c.getColumnIndex(_FIELD_HOME_ID);
            int iGid = c.getColumnIndex(_FIELD_GROUP_ID);
            int iUid = c.getColumnIndex(_FIELD_USER_ID);

            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {

                    BaseDevice bd = new BaseDevice();
                    bd.setId(c.getInt(iId));
                    bd.setName(c.getString(iName));
                    bd.setMac(c.getString(iMac));
                    bd.setPass(c.getString(iPass));
                    bd.setAvetar(c.getInt(iImage));
                    bd.setIndex(c.getInt(iIndex));
                    bd.setOnOff(c.getInt(iOnOff));
                    bd.setPermission(c.getInt(iPermission));
                    bd.setPort(c.getInt(iPort));
                    bd.setLatchPort(c.getInt(iLatchPort));
                    bd.setTypeId(c.getInt(iTypeId));
                    bd.setType(c.getString(iType));
                    bd.setHomeId(c.getInt(iHome));
                    bd.setGroupId(c.getInt(iGid));
                    bd.setUid(c.getInt(iUid));
                    bd.setSelected(false);

                    modules.add(bd);
                    //Log.i(TAG, i + ": Module in Database: MAC:" + bd.getMac() + " Name:" + bd.getName() + " Index:" + bd.getIndex() + " Type:" + bd.getType() + " Avetar:" + bd.getAvetar());
                    c.moveToNext();
                }
            }
            c.close();
            close();
            return modules;
        }catch (Exception e){e.printStackTrace();}
        return null;
    }
    public ArrayList<BaseDevice> getBaseModulesForExport() {

        try {
            open();
            ArrayList<BaseDevice> modules = new ArrayList<>();

            String select = " " +
                    _FIELD_MODULE_ID + "," + _FIELD_MODULE_NAME + "," + _FIELD_MODULE_MAC + "," +
                    _FIELD_MODULE_PASS + "," + _FIELD_MODULE_AVETAR + "," + _FIELD_MODULE_INDEX + "," +
                    _FIELD_MODULE_ONOFF + "," + _FIELD_MODULE_PERMISSION + "," + _FIELD_MODULE_PORT + "," +
                    _FIELD_LATCH_PORT + "," + _TABLE_MODULE + "." + _FIELD_TYPE_ID + "," +
                    _TABLE_TYPE + "." + _FIELD_TYPE_CODE_NAME + "," + _FIELD_HOME_ID + "," +
                    _FIELD_GROUP_ID + "," + _FIELD_USER_ID;

            String from = " " + _TABLE_MODULE + "," + _TABLE_TYPE;
            String where = " " + _TABLE_MODULE + "." + _FIELD_TYPE_ID + " = " + _TABLE_TYPE + "." + _FIELD_TYPE_ID +
                    " AND (" + _FIELD_MODULE_PERMISSION + " = 0 OR " + _FIELD_MODULE_PERMISSION + " = 1 )"  ;
            String orderBy = " " + _TABLE_MODULE + "." + _FIELD_MODULE_INDEX + "," + _TABLE_MODULE + "." + _FIELD_MODULE_ID;
            String selectQuery = "SELECT " + select + " FROM " + from + " WHERE " + where + " ORDER BY " + orderBy;
            //Log.e(TAG, "getModules: "+ selectQuery );

            Cursor c = ourDatabase.rawQuery(selectQuery, null);

            int iId = c.getColumnIndex(_FIELD_MODULE_ID);
            int iName = c.getColumnIndex(_FIELD_MODULE_NAME);
            int iMac = c.getColumnIndex(_FIELD_MODULE_MAC);
            int iPass = c.getColumnIndex(_FIELD_MODULE_PASS);
            int iImage = c.getColumnIndex(_FIELD_MODULE_AVETAR);
            int iIndex = c.getColumnIndex(_FIELD_MODULE_INDEX);
            int iOnOff = c.getColumnIndex(_FIELD_MODULE_ONOFF);
            int iPermission = c.getColumnIndex(_FIELD_MODULE_PERMISSION);
            int iPort = c.getColumnIndex(_FIELD_MODULE_PORT);
            int iLatchPort = c.getColumnIndex(_FIELD_LATCH_PORT);//
            int iTypeId = c.getColumnIndex(_FIELD_TYPE_ID);//
            int iType = c.getColumnIndex(_FIELD_TYPE_CODE_NAME);//
            int iHome = c.getColumnIndex(_FIELD_HOME_ID);
            int iGid = c.getColumnIndex(_FIELD_GROUP_ID);
            int iUid = c.getColumnIndex(_FIELD_USER_ID);

            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {

                    BaseDevice bd = new BaseDevice();
                    bd.setId(c.getInt(iId));
                    bd.setName(c.getString(iName));
                    bd.setMac(c.getString(iMac));
                    bd.setPass(c.getString(iPass));
                    bd.setAvetar(c.getInt(iImage));
                    bd.setIndex(c.getInt(iIndex));
                    bd.setOnOff(c.getInt(iOnOff));
                    bd.setPermission(c.getInt(iPermission));
                    bd.setPort(c.getInt(iPort));
                    bd.setLatchPort(c.getInt(iLatchPort));
                    bd.setTypeId(c.getInt(iTypeId));
                    bd.setType(c.getString(iType));
                    bd.setHomeId(c.getInt(iHome));
                    bd.setGroupId(c.getInt(iGid));
                    bd.setUid(c.getInt(iUid));
                    bd.setSelected(false);

                    modules.add(bd);
                    //Log.i(TAG, i + ": Module in Database: MAC:" + bd.getMac() + " Name:" + bd.getName() + " Index:" + bd.getIndex() + " Type:" + bd.getType() + " Avetar:" + bd.getAvetar());
                    c.moveToNext();
                }
            }
            c.close();
            close();
            return modules;
        }catch (Exception e){e.printStackTrace();}
        return null;
    }
    public ArrayList<BaseDevice> getBaseModules(int _gid) {

        try {
            open();
            ArrayList<BaseDevice> modules = new ArrayList<>();

            String select = " " +
                    _FIELD_MODULE_ID + "," + _FIELD_MODULE_NAME + "," + _FIELD_MODULE_MAC + "," +
                    _FIELD_MODULE_PASS + "," + _FIELD_MODULE_AVETAR + "," + _FIELD_MODULE_INDEX + "," +
                    _FIELD_MODULE_ONOFF + "," + _FIELD_MODULE_PERMISSION + "," + _FIELD_MODULE_PORT + "," +
                    _FIELD_LATCH_PORT + "," + _TABLE_MODULE + "." + _FIELD_TYPE_ID + "," +
                    _TABLE_TYPE + "." + _FIELD_TYPE_CODE_NAME + "," + _FIELD_HOME_ID + "," +
                    _FIELD_GROUP_ID + "," + _FIELD_USER_ID;

            String from = " " + _TABLE_MODULE + "," + _TABLE_TYPE;
            String where = " " + _TABLE_MODULE + "." + _FIELD_TYPE_ID + " = " + _TABLE_TYPE + "." + _FIELD_TYPE_ID +
                    " AND " + _TABLE_MODULE + "." + _FIELD_GROUP_ID + " = " + _gid;
            String orderBy = " " + _TABLE_MODULE + "." + _FIELD_MODULE_INDEX + "," + _TABLE_MODULE + "." + _FIELD_MODULE_ID;
            String selectQuery = "SELECT " + select + " FROM " + from + " WHERE " + where + " ORDER BY " + orderBy;
            //Log.e(TAG, "getModules: "+ selectQuery );

            Cursor c = ourDatabase.rawQuery(selectQuery, null);

            int iId = c.getColumnIndex(_FIELD_MODULE_ID);
            int iName = c.getColumnIndex(_FIELD_MODULE_NAME);
            int iMac = c.getColumnIndex(_FIELD_MODULE_MAC);
            int iPass = c.getColumnIndex(_FIELD_MODULE_PASS);
            int iImage = c.getColumnIndex(_FIELD_MODULE_AVETAR);
            int iIndex = c.getColumnIndex(_FIELD_MODULE_INDEX);
            int iOnOff = c.getColumnIndex(_FIELD_MODULE_ONOFF);
            int iPermission = c.getColumnIndex(_FIELD_MODULE_PERMISSION);
            int iPort = c.getColumnIndex(_FIELD_MODULE_PORT);
            int iLatchPort = c.getColumnIndex(_FIELD_LATCH_PORT);//
            int iTypeId = c.getColumnIndex(_FIELD_TYPE_ID);//
            int iType = c.getColumnIndex(_FIELD_TYPE_CODE_NAME);//
            int iHome = c.getColumnIndex(_FIELD_HOME_ID);
            int iGid = c.getColumnIndex(_FIELD_GROUP_ID);
            int iUid = c.getColumnIndex(_FIELD_USER_ID);

            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {

                    BaseDevice bd = new BaseDevice();
                    bd.setId(c.getInt(iId));
                    bd.setName(c.getString(iName));
                    bd.setMac(c.getString(iMac));
                    bd.setPass(c.getString(iPass));
                    bd.setAvetar(c.getInt(iImage));
                    bd.setIndex(c.getInt(iIndex));
                    bd.setOnOff(c.getInt(iOnOff));
                    bd.setPermission(c.getInt(iPermission));
                    bd.setPort(c.getInt(iPort));
                    bd.setLatchPort(c.getInt(iLatchPort));
                    bd.setTypeId(c.getInt(iTypeId));
                    bd.setType(c.getString(iType));
                    bd.setHomeId(c.getInt(iHome));
                    bd.setGroupId(c.getInt(iGid));
                    bd.setUid(c.getInt(iUid));
                    bd.setSelected(false);

                    modules.add(bd);
                    //Log.i(TAG, i + ": Module in Database: MAC:" + bd.getMac() + " Name:" + bd.getName() + " Index:" + bd.getIndex() + " Type:" + bd.getType() + " Avetar:" + bd.getAvetar());
                    c.moveToNext();
                }
            }
            c.close();
            close();
            return modules;
        }catch (Exception e){e.printStackTrace();}
        return null;
    }

    public ArrayList<BaseDevice> getBaseModulesWithFree(int _gid) {

        try {
            open();
            ArrayList<BaseDevice> modules = new ArrayList<>();

            String select = " " +
                    _FIELD_MODULE_ID + "," + _FIELD_MODULE_NAME + "," + _FIELD_MODULE_MAC + "," +
                    _FIELD_MODULE_PASS + "," + _FIELD_MODULE_AVETAR + "," + _FIELD_MODULE_INDEX + "," +
                    _FIELD_MODULE_ONOFF + "," + _FIELD_MODULE_PERMISSION + "," + _FIELD_MODULE_PORT + "," +
                    _FIELD_LATCH_PORT + "," + _TABLE_MODULE + "." + _FIELD_TYPE_ID + "," +
                    _TABLE_TYPE + "." + _FIELD_TYPE_CODE_NAME + "," + _FIELD_HOME_ID + "," +
                    _FIELD_GROUP_ID + "," + _FIELD_USER_ID;

            String from = " " + _TABLE_MODULE + "," + _TABLE_TYPE;
            String where = " " + _TABLE_MODULE + "." + _FIELD_TYPE_ID + " = " + _TABLE_TYPE + "." + _FIELD_TYPE_ID +
                    " AND (" + _TABLE_MODULE + "." + _FIELD_GROUP_ID + " = " + _gid + " OR " +
                    _TABLE_MODULE + "." + _FIELD_GROUP_ID + " = -1 )" + " AND " + _FIELD_TYPE_GROUPABLE + " = " + "1" ;
            String orderBy = " " + _TABLE_MODULE + "." + _FIELD_GROUP_ID + " DESC";
            String selectQuery = "SELECT " + select + " FROM " + from + " WHERE " + where + " ORDER BY " + orderBy;
            //Log.e(TAG, "getModules: "+ selectQuery );

            Cursor c = ourDatabase.rawQuery(selectQuery, null);

            int iId = c.getColumnIndex(_FIELD_MODULE_ID);
            int iName = c.getColumnIndex(_FIELD_MODULE_NAME);
            int iMac = c.getColumnIndex(_FIELD_MODULE_MAC);
            int iPass = c.getColumnIndex(_FIELD_MODULE_PASS);
            int iImage = c.getColumnIndex(_FIELD_MODULE_AVETAR);
            int iIndex = c.getColumnIndex(_FIELD_MODULE_INDEX);
            int iOnOff = c.getColumnIndex(_FIELD_MODULE_ONOFF);
            int iPermission = c.getColumnIndex(_FIELD_MODULE_PERMISSION);
            int iPort = c.getColumnIndex(_FIELD_MODULE_PORT);
            int iLatchPort = c.getColumnIndex(_FIELD_LATCH_PORT);//
            int iTypeId = c.getColumnIndex(_FIELD_TYPE_ID);//
            int iType = c.getColumnIndex(_FIELD_TYPE_CODE_NAME);//
            int iHome = c.getColumnIndex(_FIELD_HOME_ID);
            int iGid = c.getColumnIndex(_FIELD_GROUP_ID);
            int iUid = c.getColumnIndex(_FIELD_USER_ID);

            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {

                    BaseDevice bd = new BaseDevice();
                    bd.setId(c.getInt(iId));
                    bd.setName(c.getString(iName));
                    bd.setMac(c.getString(iMac));
                    bd.setPass(c.getString(iPass));
                    bd.setAvetar(c.getInt(iImage));
                    bd.setIndex(c.getInt(iIndex));
                    bd.setOnOff(c.getInt(iOnOff));
                    bd.setPermission(c.getInt(iPermission));
                    bd.setPort(c.getInt(iPort));
                    bd.setLatchPort(c.getInt(iLatchPort));
                    bd.setTypeId(c.getInt(iTypeId));
                    bd.setType(c.getString(iType));
                    bd.setHomeId(c.getInt(iHome));
                    bd.setGroupId(c.getInt(iGid));
                    bd.setUid(c.getInt(iUid));
                    bd.setSelected(false);

                    modules.add(bd);
                    //Log.i(TAG, i + ": Module in Database: MAC:" + bd.getMac() + " Name:" + bd.getName() + " Index:" + bd.getIndex() + " Type:" + bd.getType() + " Avetar:" + bd.getAvetar());
                    c.moveToNext();
                }
            }
            c.close();
            close();
            return modules;
        }catch (Exception e){e.printStackTrace();}
        return null;
    }

    public BaseDevice getBaseDevice(int baseDevId) {
        try {
            open();

            String select = " " +
                    _FIELD_MODULE_ID + "," + _FIELD_MODULE_NAME + "," + _FIELD_MODULE_MAC + "," +
                    _FIELD_MODULE_PASS + "," + _FIELD_MODULE_AVETAR + "," + _FIELD_MODULE_INDEX + "," +
                    _FIELD_MODULE_ONOFF + "," + _FIELD_MODULE_PERMISSION + "," + _FIELD_MODULE_PORT + "," +
                    _FIELD_LATCH_PORT + "," + _TABLE_MODULE + "." + _FIELD_TYPE_ID + "," +
                    _TABLE_TYPE + "." + _FIELD_TYPE_CODE_NAME + "," + _FIELD_HOME_ID + "," +
                    _FIELD_GROUP_ID + "," + _FIELD_USER_ID;

            String from = " " + _TABLE_MODULE + "," + _TABLE_TYPE;
            String where = " " + _TABLE_MODULE + "." + _FIELD_TYPE_ID + " = " + _TABLE_TYPE + "." +
                    _FIELD_TYPE_ID + " AND " + _FIELD_MODULE_ID + " = " + baseDevId;
            String orderBy = " " + _TABLE_MODULE + "." + _FIELD_MODULE_INDEX + "," + _TABLE_MODULE + "." + _FIELD_MODULE_ID;
            String selectQuery = "SELECT " + select + " FROM " + from + " WHERE " + where + " ORDER BY " + orderBy;
            //Log.e(TAG, "getModules: "+ selectQuery );

            Cursor c = ourDatabase.rawQuery(selectQuery, null);

            int iId = c.getColumnIndex(_FIELD_MODULE_ID);
            int iName = c.getColumnIndex(_FIELD_MODULE_NAME);
            int iMac = c.getColumnIndex(_FIELD_MODULE_MAC);
            int iPass = c.getColumnIndex(_FIELD_MODULE_PASS);
            int iImage = c.getColumnIndex(_FIELD_MODULE_AVETAR);
            int iIndex = c.getColumnIndex(_FIELD_MODULE_INDEX);
            int iOnOff = c.getColumnIndex(_FIELD_MODULE_ONOFF);
            int iPermission = c.getColumnIndex(_FIELD_MODULE_PERMISSION);
            int iPort = c.getColumnIndex(_FIELD_MODULE_PORT);
            int iLatchPort = c.getColumnIndex(_FIELD_LATCH_PORT);//
            int iTypeId = c.getColumnIndex(_FIELD_TYPE_ID);//
            int iType = c.getColumnIndex(_FIELD_TYPE_CODE_NAME);//
            int iHome = c.getColumnIndex(_FIELD_HOME_ID);
            int iGid = c.getColumnIndex(_FIELD_GROUP_ID);
            int iUid = c.getColumnIndex(_FIELD_USER_ID);

            if (c.moveToFirst()) {
                    BaseDevice bd = new BaseDevice();
                    bd.setId(c.getInt(iId));
                    bd.setName(c.getString(iName));
                    bd.setMac(c.getString(iMac));
                    bd.setPass(c.getString(iPass));
                    bd.setAvetar(c.getInt(iImage));
                    bd.setIndex(c.getInt(iIndex));
                    bd.setOnOff(c.getInt(iOnOff));
                    bd.setPermission(c.getInt(iPermission));
                    bd.setPort(c.getInt(iPort));
                    bd.setLatchPort(c.getInt(iLatchPort));
                    bd.setTypeId(c.getInt(iTypeId));
                    bd.setType(c.getString(iType));
                    bd.setHomeId(c.getInt(iHome));
                    bd.setGroupId(c.getInt(iGid));
                    bd.setUid(c.getInt(iUid));
                    bd.setSelected(false);

                    c.close();
                    close();
                    return bd;
                    //Log.i(TAG, i + ": Module in Database: MAC:" + bd.getMac() + " Name:" + bd.getName() + " Index:" + bd.getIndex() + " Type:" + bd.getType() + " Avetar:" + bd.getAvetar());
            }

            c.close();
            close();

        }catch (Exception e){e.printStackTrace();}
        return null;
    }
    public ArrayList<Group> getGroups() {

        ArrayList<Group> groups = new ArrayList<>();

        try {
            open();
/*            String select = " COUNT(*) AS '" + _FIELD_COUNT + "' , " +
                    _TABLE_GROUP + "." +_FIELD_GROUP_ID + "," + _FIELD_GROUP_NAME + "," + _FIELD_GROUP_INDEX + "," +
                    _FIELD_GROUP_ONOFF + "," + _FIELD_GROUP_COLOR + "," + _FIELD_GROUP_DIMMER + "," +
                    _FIELD_GROUP_WHITE_ON + "," + _FIELD_GROUP_WHITE_VALUE+ "," + _FIELD_HOME_ID ;*/

            String[] col = new String[]{" COUNT(*) AS '" + _FIELD_COUNT + "' ",_TABLE_GROUP + "." +_FIELD_GROUP_ID,
                    _FIELD_GROUP_NAME,_FIELD_GROUP_INDEX,_FIELD_GROUP_ONOFF,_FIELD_GROUP_COLOR,_FIELD_GROUP_DIMMER,
                    _FIELD_GROUP_WHITE_ON,_FIELD_GROUP_WHITE_VALUE//,_TABLE_GROUP + "." + _FIELD_HOME_ID
                     };

            String from = " " + _TABLE_GROUP + "," + _TABLE_MODULE;
            String where = " " + _TABLE_MODULE + "." + _FIELD_GROUP_ID + " = " + _TABLE_GROUP + "." + _FIELD_GROUP_ID ;
            String groupBy = " " + _TABLE_MODULE + "." + _FIELD_GROUP_ID;
            String orderBy = " " + _TABLE_GROUP + "." + _FIELD_GROUP_INDEX ;
           // String selectQuery = "SELECT " + select + " FROM " + from + " WHERE " + where + " GROUP BY " + groupBy ;
            //Log.e(TAG, "getModules: "+ selectQuery );
            Cursor c = ourDatabase.query(from, col, where, null, groupBy, null, null);

           // Cursor c = ourDatabase.rawQuery(selectQuery, null);

            int iId = c.getColumnIndex(_FIELD_GROUP_ID);
            int iName = c.getColumnIndex(_FIELD_GROUP_NAME);
            int iIndex = c.getColumnIndex(_FIELD_GROUP_INDEX);
            int iOnOff = c.getColumnIndex(_FIELD_GROUP_ONOFF);
            int iColor = c.getColumnIndex(_FIELD_GROUP_COLOR);
            int iDimmer = c.getColumnIndex(_FIELD_GROUP_DIMMER);
            int iWhiteOn = c.getColumnIndex(_FIELD_GROUP_WHITE_ON);
            int iWhiteValue = c.getColumnIndex(_FIELD_GROUP_WHITE_VALUE);
            //int iHomeId = c.getColumnIndex(_FIELD_HOME_ID);
            int iCount = c.getColumnIndex(_FIELD_COUNT);
            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {

                    Group gr = new Group();
                    gr.setId(c.getInt(iId));
                    gr.setName(c.getString(iName));
                    gr.setIndex(c.getInt(iIndex));
                    gr.setOnOff(c.getInt(iOnOff));
                    gr.setColor(c.getInt(iColor));
                    gr.setDimmer(c.getInt(iDimmer));
                    gr.setWhiteOn(c.getInt(iWhiteOn));
                    gr.setWhiteValue(c.getInt(iWhiteValue));
                    //gr.setHomeId(c.getInt(iHomeId));
                    gr.setSelected(false);
                    gr.setCounter(c.getInt(iCount));
                    groups.add(gr);
                    //Log.i(TAG, i + ": Module in Database: MAC:" + bd.getMac() + " Name:" + bd.getName() + " Index:" + bd.getIndex() + " Type:" + bd.getType() + " Avetar:" + bd.getAvetar());
                    c.moveToNext();
                }
            }
            c.close();
            close();
            return groups;
        }catch (Exception e){e.printStackTrace();}
        return groups;
    }

    public Group getGroup(int _gid) {

        Group gr0 = new Group();
        try {
            open();
/*            String select = " COUNT(*) AS '" + _FIELD_COUNT + "' , " +
                    _TABLE_GROUP + "." +_FIELD_GROUP_ID + "," + _FIELD_GROUP_NAME + "," + _FIELD_GROUP_INDEX + "," +
                    _FIELD_GROUP_ONOFF + "," + _FIELD_GROUP_COLOR + "," + _FIELD_GROUP_DIMMER + "," +
                    _FIELD_GROUP_WHITE_ON + "," + _FIELD_GROUP_WHITE_VALUE+ "," + _FIELD_HOME_ID ;*/

            String[] col = new String[]{_FIELD_GROUP_ID,
                    _FIELD_GROUP_NAME,_FIELD_GROUP_INDEX,_FIELD_GROUP_ONOFF,_FIELD_GROUP_COLOR,_FIELD_GROUP_DIMMER,
                    _FIELD_GROUP_WHITE_ON,_FIELD_GROUP_WHITE_VALUE//,_TABLE_GROUP + "." + _FIELD_HOME_ID
            };

            String from = " " + _TABLE_GROUP ;
            String where = " " +_FIELD_GROUP_ID + " = " + _gid;
            // String selectQuery = "SELECT " + select + " FROM " + from + " WHERE " + where + " GROUP BY " + groupBy ;
            //Log.e(TAG, "getModules: "+ selectQuery );
            Cursor c = ourDatabase.query(from, col, where, null, null, null, null);

            // Cursor c = ourDatabase.rawQuery(selectQuery, null);

            int iId = c.getColumnIndex(_FIELD_GROUP_ID);
            int iName = c.getColumnIndex(_FIELD_GROUP_NAME);
            int iIndex = c.getColumnIndex(_FIELD_GROUP_INDEX);
            int iOnOff = c.getColumnIndex(_FIELD_GROUP_ONOFF);
            int iColor = c.getColumnIndex(_FIELD_GROUP_COLOR);
            int iDimmer = c.getColumnIndex(_FIELD_GROUP_DIMMER);
            int iWhiteOn = c.getColumnIndex(_FIELD_GROUP_WHITE_ON);
            int iWhiteValue = c.getColumnIndex(_FIELD_GROUP_WHITE_VALUE);
            //int iHomeId = c.getColumnIndex(_FIELD_HOME_ID);
            if (c.moveToFirst()) {

                    Group gr = new Group();
                    gr.setId(c.getInt(iId));
                    gr.setName(c.getString(iName));
                    gr.setIndex(c.getInt(iIndex));
                    gr.setOnOff(c.getInt(iOnOff));
                    gr.setColor(c.getInt(iColor));
                    gr.setDimmer(c.getInt(iDimmer));
                    gr.setWhiteOn(c.getInt(iWhiteOn));
                    gr.setWhiteValue(c.getInt(iWhiteValue));
                    //gr.setHomeId(c.getInt(iHomeId));
                    gr.setSelected(false);
                    //Log.i(TAG, i + ": Module in Database: MAC:" + bd.getMac() + " Name:" + bd.getName() + " Index:" + bd.getIndex() + " Type:" + bd.getType() + " Avetar:" + bd.getAvetar());
                    c.close();
                    close();
                    gr0 = gr;
                    return gr;

            }
            c.close();
            close();
            return gr0;
        }catch (Exception e){e.printStackTrace();}
        return gr0;
    }

    public ArrayList<Access> getAccessForUid(int uid) {
        ArrayList<Access> accesses = new ArrayList<>();

        try {

            open();

            String select = " " +
                    _FIELD_MODULE_ID + "," + _FIELD_USER_ID + "," + _FIELD_MODULE_PERMISSION ;

            String from = " " + _TABLE_ACCESS  ;
            String where = " " + _FIELD_USER_ID + " = " + uid ;

            String selectQuery = "SELECT " + select + " FROM " + from + " WHERE " + where ;
            //Log.e(TAG, "getBaseDeviceUsers: "+ selectQuery );

            Cursor c = ourDatabase.rawQuery(selectQuery, null);

            int iMid = c.getColumnIndex(_FIELD_MODULE_ID);
            int iUid = c.getColumnIndex(_FIELD_USER_ID);
            int iPer = c.getColumnIndex(_FIELD_MODULE_PERMISSION);
            //Log.e(TAG, "getAccessForUid: have " + c.getCount()+" device" );
            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {
                    Access  access = new Access();
                    access.setMid(c.getInt(iMid));
                    access.setUid(c.getInt(iUid));
                    access.setPermission(c.getInt(iPer));
                    accesses.add(access);
                    //Log.e(TAG, "getAccessForUid: I:" + i + " m:" + access.getMid() + " u:" + access.getUid() );
                    c.moveToNext();
                }

                c.close();
                close();
                return accesses;
                //Log.i(TAG, i + ": Module in Database: MAC:" + bd.getMac() + " Name:" + bd.getName() + " Index:" + bd.getIndex() + " Type:" + bd.getType() + " Avetar:" + bd.getAvetar());
            }

            c.close();
            close();

        }catch (Exception e){e.printStackTrace();}
        return accesses;
    }
    public ArrayList<User> getUsers(String _uuid) {
        ArrayList<User> users = new ArrayList<>();

        try {

            open();

            String select = " " +
                    _FIELD_USER_ID + "," + _FIELD_USER_NAME + "," + _FIELD_USER_UNIQUE_ID + "," +
                    _FIELD_USER_CELL + "," + _FIELD_USER_DESCRIPTION + "," + _FIELD_USER_PIC + "," +
                    _FIELD_USER_IS_ACTIVE ;

            String from = " " + _TABLE_USER  ;
            String where = " " + _FIELD_USER_UNIQUE_ID + " = '" + _uuid + "'";

            String selectQuery = "SELECT " + select + " FROM " + from + " WHERE " + where ;
            //Log.e(TAG, "getBaseDeviceUsers: "+ selectQuery );

            Cursor c = ourDatabase.rawQuery(selectQuery, null);

            int iUid = c.getColumnIndex(_FIELD_USER_ID);
            int iName = c.getColumnIndex(_FIELD_USER_NAME);
            int iUuid = c.getColumnIndex(_FIELD_USER_UNIQUE_ID);
            int iCell = c.getColumnIndex(_FIELD_USER_CELL);
            int iDesc = c.getColumnIndex(_FIELD_USER_DESCRIPTION);
            int iPic = c.getColumnIndex(_FIELD_USER_PIC);
            int iIsActive = c.getColumnIndex(_FIELD_USER_IS_ACTIVE);

            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {
                    User user = new User();
                    user.setId(c.getInt(iUid));
                    user.setName(c.getString(iName));
                    user.setUuid(c.getString(iUuid));
                    user.setCell(c.getString(iCell));
                    user.setDescription(c.getString(iDesc));
// user.setPic(c.getBlob(iPic));
// user.setIsActive(c.getInt(iIsActive));
                    users.add(user);
                    c.moveToNext();
                }

                c.close();
                close();
                return users;
                //Log.i(TAG, i + ": Module in Database: MAC:" + bd.getMac() + " Name:" + bd.getName() + " Index:" + bd.getIndex() + " Type:" + bd.getType() + " Avetar:" + bd.getAvetar());
            }

            c.close();
            close();

        }catch (Exception e){e.printStackTrace();}
        return users;
    }
    public ArrayList<User> getBaseDeviceUsers(int baseDevId) {

        ArrayList<User> users = new ArrayList<>();

        try {

            open();

            String select = " " +
                    _TABLE_USER + "." +_FIELD_USER_ID + "," + _FIELD_USER_NAME + "," + _FIELD_USER_UNIQUE_ID + "," +
                    _FIELD_USER_CELL + "," + _FIELD_USER_DESCRIPTION + "," + _FIELD_USER_PIC + "," +
                    _FIELD_ACCESS_STATUS ;

            String from = " " + _TABLE_USER + "," + _TABLE_ACCESS ;
            String where = " " + _TABLE_ACCESS + "." + _FIELD_USER_ID + " = " + _TABLE_USER + "." + _FIELD_USER_ID +
                    " AND "  + _TABLE_ACCESS + "." + _FIELD_MODULE_ID + " = " + baseDevId;

            String selectQuery = "SELECT " + select + " FROM " + from + " WHERE " + where ;
            //Log.e(TAG, "getBaseDeviceUsers: "+ selectQuery );

            Cursor c = ourDatabase.rawQuery(selectQuery, null);

            int iUid = c.getColumnIndex(_FIELD_USER_ID);
            int iName = c.getColumnIndex(_FIELD_USER_NAME);
            int iUuid = c.getColumnIndex(_FIELD_USER_UNIQUE_ID);
            int iCell = c.getColumnIndex(_FIELD_USER_CELL);
            int iDesc = c.getColumnIndex(_FIELD_USER_DESCRIPTION);
            int iPic = c.getColumnIndex(_FIELD_USER_PIC);
            int iAccessStatus = c.getColumnIndex(_FIELD_ACCESS_STATUS);

            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {
                    User user = new User();
                    user.setId(c.getInt(iUid));
                    user.setName(c.getString(iName));
                    user.setUuid(c.getString(iUuid));
                    user.setCell(c.getString(iCell));
                    user.setDescription(c.getString(iDesc));
// user.setPic(c.getBlob(iPic));
                    user.setIsActive(c.getInt(iAccessStatus));
                    users.add(user);
                    c.moveToNext();
                }

                c.close();
                close();
                return users;
                //Log.i(TAG, i + ": Module in Database: MAC:" + bd.getMac() + " Name:" + bd.getName() + " Index:" + bd.getIndex() + " Type:" + bd.getType() + " Avetar:" + bd.getAvetar());
            }

            c.close();
            close();

        }catch (Exception e){e.printStackTrace();}
        return users;
    }
    public RgbDevice getRgbDevice(int baseDevId) {
        BaseDevice baseDevice = getBaseDevice(baseDevId);
        if (baseDevice != null) {
            try {

                RgbDevice rgbDevice = new RgbDevice(baseDevice);

                open();
                String select = " " +
                        _FIELD_RGBW_ID + "," + _FIELD_RGBW_COLOR + "," + _FIELD_RGBW_WHITE_ON + "," +
                        _FIELD_RGBW_WHITE_VALUE + "," + _FIELD_RGBW_ANIMATE + "," + _FIELD_RGBW_MODE + "," +
                        _FIELD_RGBW_FAV01 + "," + _FIELD_RGBW_FAV02 + "," + _FIELD_RGBW_FAV03 + "," +
                        _FIELD_RGBW_FAV04 + "," + _FIELD_RGBW_FAV05 + "," + _FIELD_RGBW_FAV06 + "," +
                        _FIELD_RGBW_FAV07 + "," + _FIELD_RGBW_FAV08 + "," + _FIELD_RGBW_FAV09 + "," +
                        _FIELD_RGBW_FAV10 + "," + _FIELD_MODULE_ID;

                String from = " " + _TABLE_RGBW ;
                String where = " " + _FIELD_MODULE_ID + " = " + baseDevId;
                String selectQuery = "SELECT " + select + " FROM " + from + " WHERE " + where ;
                //Log.e(TAG, "getModules: "+ selectQuery );

                Cursor c = ourDatabase.rawQuery(selectQuery, null);

                int iId = c.getColumnIndex(_FIELD_RGBW_ID);
                int iColor = c.getColumnIndex(_FIELD_RGBW_COLOR);
                int iWhiteOn = c.getColumnIndex(_FIELD_RGBW_WHITE_ON);
                int iWhiteValue = c.getColumnIndex(_FIELD_RGBW_WHITE_VALUE);
                int iAnimate = c.getColumnIndex(_FIELD_RGBW_ANIMATE);
                int iMode = c.getColumnIndex(_FIELD_RGBW_MODE);
                int iFav01 = c.getColumnIndex(_FIELD_RGBW_FAV01);
                int iFav02 = c.getColumnIndex(_FIELD_RGBW_FAV02);
                int iFav03 = c.getColumnIndex(_FIELD_RGBW_FAV03);
                int iFav04 = c.getColumnIndex(_FIELD_RGBW_FAV04);
                int iFav05 = c.getColumnIndex(_FIELD_RGBW_FAV05);
                int iFav06 = c.getColumnIndex(_FIELD_RGBW_FAV06);
                int iFav07 = c.getColumnIndex(_FIELD_RGBW_FAV07);
                int iFav08 = c.getColumnIndex(_FIELD_RGBW_FAV08);
                int iFav09 = c.getColumnIndex(_FIELD_RGBW_FAV09);
                int iFav10 = c.getColumnIndex(_FIELD_RGBW_FAV10);

                if (c.moveToFirst()) {

                    rgbDevice.setId(c.getInt(iId));
                    rgbDevice.setColor(c.getInt(iColor));
                    rgbDevice.setWhiteOn(c.getInt(iWhiteOn));
                    rgbDevice.setWhiteValue(c.getInt(iWhiteValue));
                    rgbDevice.setAnimate(c.getInt(iAnimate));
                    rgbDevice.setMode(c.getInt(iMode));
                    rgbDevice.setFav01(c.getInt(iFav01));
                    rgbDevice.setFav02(c.getInt(iFav02));
                    rgbDevice.setFav03(c.getInt(iFav03));
                    rgbDevice.setFav04(c.getInt(iFav04));
                    rgbDevice.setFav05(c.getInt(iFav05));
                    rgbDevice.setFav06(c.getInt(iFav06));
                    rgbDevice.setFav07(c.getInt(iFav07));
                    rgbDevice.setFav08(c.getInt(iFav08));
                    rgbDevice.setFav09(c.getInt(iFav09));
                    rgbDevice.setFav10(c.getInt(iFav10));

                    c.close();
                    close();
                    //Log.i(TAG,  ": Module in rgb table: id:" + rgbDevice.getId() + " col:" + rgbDevice.getColor() + " fav1:" + rgbDevice.getFav01());

                    return rgbDevice;
                }
                else {
                    Log.e(TAG, "getRgbDevice: " + "not exist related device in rgb table" );
                }
                c.close();
                close();

            }catch (Exception e){e.printStackTrace();}

        }
        return null;
    }

    public ArrayList<RgbDevice> getRgbDevices() {


        if (true) {
            try {

                ArrayList<RgbDevice> rgbDevices = new ArrayList<>();
                open();
                String select = " " +
                        _FIELD_RGBW_ID + "," + _FIELD_RGBW_COLOR + "," + _FIELD_RGBW_WHITE_ON + "," +
                        _FIELD_RGBW_WHITE_VALUE + "," + _FIELD_RGBW_ANIMATE + "," + _FIELD_RGBW_MODE + "," +
                        _FIELD_RGBW_FAV01 + "," + _FIELD_RGBW_FAV02 + "," + _FIELD_RGBW_FAV03 + "," +
                        _FIELD_RGBW_FAV04 + "," + _FIELD_RGBW_FAV05 + "," + _FIELD_RGBW_FAV06 + "," +
                        _FIELD_RGBW_FAV07 + "," + _FIELD_RGBW_FAV08 + "," + _FIELD_RGBW_FAV09 + "," +
                        _FIELD_RGBW_FAV10 + "," + _FIELD_MODULE_ID;

                String from = " " + _TABLE_RGBW ;
                String where = " 1";
                String selectQuery = "SELECT " + select + " FROM " + from + " WHERE " + where ;
                //Log.e(TAG, "getModules: "+ selectQuery );

                Cursor c = ourDatabase.rawQuery(selectQuery, null);

                int iId = c.getColumnIndex(_FIELD_RGBW_ID);
                int iPid = c.getColumnIndex(_FIELD_MODULE_ID);
                int iColor = c.getColumnIndex(_FIELD_RGBW_COLOR);
                int iWhiteOn = c.getColumnIndex(_FIELD_RGBW_WHITE_ON);
                int iWhiteValue = c.getColumnIndex(_FIELD_RGBW_WHITE_VALUE);
                int iAnimate = c.getColumnIndex(_FIELD_RGBW_ANIMATE);
                int iMode = c.getColumnIndex(_FIELD_RGBW_MODE);
                int iFav01 = c.getColumnIndex(_FIELD_RGBW_FAV01);
                int iFav02 = c.getColumnIndex(_FIELD_RGBW_FAV02);
                int iFav03 = c.getColumnIndex(_FIELD_RGBW_FAV03);
                int iFav04 = c.getColumnIndex(_FIELD_RGBW_FAV04);
                int iFav05 = c.getColumnIndex(_FIELD_RGBW_FAV05);
                int iFav06 = c.getColumnIndex(_FIELD_RGBW_FAV06);
                int iFav07 = c.getColumnIndex(_FIELD_RGBW_FAV07);
                int iFav08 = c.getColumnIndex(_FIELD_RGBW_FAV08);
                int iFav09 = c.getColumnIndex(_FIELD_RGBW_FAV09);
                int iFav10 = c.getColumnIndex(_FIELD_RGBW_FAV10);

                if (c.moveToFirst()) {

                    for (int i = 0; i < c.getCount(); i++) {

                        RgbDevice rgbDevice = new RgbDevice();
                        rgbDevice.setId(c.getInt(iId));
                        rgbDevice.setColor(c.getInt(iColor));
                        rgbDevice.setWhiteOn(c.getInt(iWhiteOn));
                        rgbDevice.setWhiteValue(c.getInt(iWhiteValue));
                        rgbDevice.setAnimate(c.getInt(iAnimate));
                        rgbDevice.setMode(c.getInt(iMode));
                        rgbDevice.setFav01(c.getInt(iFav01));
                        rgbDevice.setFav02(c.getInt(iFav02));
                        rgbDevice.setFav03(c.getInt(iFav03));
                        rgbDevice.setFav04(c.getInt(iFav04));
                        rgbDevice.setFav05(c.getInt(iFav05));
                        rgbDevice.setFav06(c.getInt(iFav06));
                        rgbDevice.setFav07(c.getInt(iFav07));
                        rgbDevice.setFav08(c.getInt(iFav08));
                        rgbDevice.setFav09(c.getInt(iFav09));
                        rgbDevice.setFav10(c.getInt(iFav10));
                        rgbDevice.setPid(c.getInt(iPid));
                        rgbDevices.add(rgbDevice);

                        c.moveToNext();
                    }
                    c.close();
                    close();
                    //Log.i(TAG,  ": Module in rgb table: id:" + rgbDevice.getId() + " col:" + rgbDevice.getColor() + " fav1:" + rgbDevice.getFav01());

                    return rgbDevices;
                }
                else {
                    Log.e(TAG, "getRgbDevice: " + "not exist related device in rgb table" );
                }
                c.close();
                close();

            }catch (Exception e){e.printStackTrace();}

        }
        return null;
    }

    public ArrayList<BaseDevice> getFreeBaseDevice() {

        try {
            open();
            ArrayList<BaseDevice> modules = new ArrayList<>();

            String select = " " +
                    _FIELD_MODULE_ID + "," + _FIELD_MODULE_NAME + "," + _FIELD_MODULE_MAC + "," +
                    _FIELD_MODULE_PASS + "," + _FIELD_MODULE_AVETAR + "," + _FIELD_MODULE_INDEX + "," +
                    _FIELD_MODULE_ONOFF + "," + _FIELD_MODULE_PERMISSION + "," + _FIELD_MODULE_PORT + "," +
                    _FIELD_LATCH_PORT + "," + _TABLE_MODULE + "." + _FIELD_TYPE_ID + "," +
                    _TABLE_TYPE + "." + _FIELD_TYPE_CODE_NAME + "," + _FIELD_HOME_ID + "," +
                    _FIELD_GROUP_ID + "," + _FIELD_USER_ID;

            String from = " " + _TABLE_MODULE + "," + _TABLE_TYPE;
            String where = " " + _TABLE_MODULE + "." + _FIELD_TYPE_ID + " = " + _TABLE_TYPE + "." + _FIELD_TYPE_ID +
                    " AND " + _FIELD_GROUP_ID + " = " + "-1" + " AND " + _FIELD_TYPE_GROUPABLE + " = " + "1";
            String orderBy = " " + _TABLE_MODULE + "." + _FIELD_MODULE_INDEX + "," + _TABLE_MODULE + "." + _FIELD_MODULE_ID;
            String selectQuery = "SELECT " + select + " FROM " + from + " WHERE " + where + " ORDER BY " + orderBy;
            //Log.e(TAG, "getModules: "+ selectQuery );

            Cursor c = ourDatabase.rawQuery(selectQuery, null);

            int iId = c.getColumnIndex(_FIELD_MODULE_ID);
            int iName = c.getColumnIndex(_FIELD_MODULE_NAME);
            int iMac = c.getColumnIndex(_FIELD_MODULE_MAC);
            int iPass = c.getColumnIndex(_FIELD_MODULE_PASS);
            int iImage = c.getColumnIndex(_FIELD_MODULE_AVETAR);
            int iIndex = c.getColumnIndex(_FIELD_MODULE_INDEX);
            int iOnOff = c.getColumnIndex(_FIELD_MODULE_ONOFF);
            int iPermission = c.getColumnIndex(_FIELD_MODULE_PERMISSION);
            int iPort = c.getColumnIndex(_FIELD_MODULE_PORT);
            int iLatchPort = c.getColumnIndex(_FIELD_LATCH_PORT);//
            int iTypeId = c.getColumnIndex(_FIELD_TYPE_ID);//
            int iType = c.getColumnIndex(_FIELD_TYPE_CODE_NAME);//
            int iHome = c.getColumnIndex(_FIELD_HOME_ID);
            int iGid = c.getColumnIndex(_FIELD_GROUP_ID);
            int iUid = c.getColumnIndex(_FIELD_USER_ID);

            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {

                    BaseDevice bd = new BaseDevice();
                    bd.setId(c.getInt(iId));
                    bd.setName(c.getString(iName));
                    bd.setMac(c.getString(iMac));
                    bd.setPass(c.getString(iPass));
                    bd.setAvetar(c.getInt(iImage));
                    bd.setIndex(c.getInt(iIndex));
                    bd.setOnOff(c.getInt(iOnOff));
                    bd.setPermission(c.getInt(iPermission));
                    bd.setPort(c.getInt(iPort));
                    bd.setLatchPort(c.getInt(iLatchPort));
                    bd.setTypeId(c.getInt(iTypeId));
                    bd.setType(c.getString(iType));
                    bd.setHomeId(c.getInt(iHome));
                    bd.setGroupId(c.getInt(iGid));
                    bd.setUid(c.getInt(iUid));
                    bd.setSelected(false);

                    modules.add(bd);
                    //Log.i(TAG, i + ": Module in Database: MAC:" + bd.getMac() + " Name:" + bd.getName() + " Index:" + bd.getIndex() + " Type:" + bd.getType() + " Avetar:" + bd.getAvetar());
                    c.moveToNext();
                }
            }
            c.close();
            close();
            return modules;
        }catch (Exception e){e.printStackTrace();}
        return null;
    }

    public int updateUser(int _id, String name) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(_FIELD_USER_NAME, name);
        int rowEffected = ourDatabase.update(_TABLE_USER, cv, _FIELD_USER_ID + "=" + _id ,null);
        close();
        return rowEffected;
    }
    public int  updateOnOff(int _id, int _onOff) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(_FIELD_MODULE_ONOFF, _onOff);
        int rowEffected = ourDatabase.update(_TABLE_MODULE, cv, _FIELD_MODULE_ID + "=" + _id ,null);
        close();
        return rowEffected;
    }

    public int updateBase(int baseDevId, String devName, String pass) {
        open();
        int rowEffected = 0 ;
        ContentValues cvv = new ContentValues();
        cvv.put(_FIELD_MODULE_NAME, devName);
        cvv.put(_FIELD_MODULE_PASS, pass);
        rowEffected = ourDatabase.update(_TABLE_MODULE, cvv, _FIELD_MODULE_ID + "=" + baseDevId ,null);

        close();
        return rowEffected;
    }

    public int updateGroup(String _gName, int _groupId, ArrayList<SelectableItem> _devices) {
        open();
        int rowEffected = 0 ;

        ContentValues cvv = new ContentValues();
        cvv.put(_FIELD_GROUP_NAME, _gName);
        ourDatabase.update(_TABLE_GROUP, cvv, _FIELD_GROUP_ID + "=" + _groupId ,null);

        for (int i = 0; i < _devices.size(); i++) {
            if (_devices.get(i).isSelected()) {
                ContentValues cv = new ContentValues();
                cv.put(_FIELD_GROUP_ID, _groupId);
                rowEffected += ourDatabase.update(_TABLE_MODULE, cv, _FIELD_MODULE_ID + "=" + _devices.get(i).getId() ,null);
            }else {
                ContentValues cv = new ContentValues();
                cv.put(_FIELD_GROUP_ID, -1);
                ourDatabase.update(_TABLE_MODULE, cv, _FIELD_MODULE_ID + "=" + _devices.get(i).getId() ,null);
            }
        }
        close();
        return rowEffected;
    }

    public int  updateGroupOnOff(int _id, int _onOff) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(_FIELD_GROUP_ONOFF, _onOff);
        int rowEffected = ourDatabase.update(_TABLE_GROUP, cv, _FIELD_GROUP_ID + "=" + _id ,null);
        close();
        return rowEffected;
    }
    public int updateGroupOnOffDevices(int groupId, int _onOff) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(_FIELD_MODULE_ONOFF, _onOff);
        int rowEffected = ourDatabase.update(_TABLE_MODULE, cv, _FIELD_GROUP_ID + "=" + groupId ,null);
        close();
        return rowEffected;
    }

    public int updateWhiteValue(int _id, int _progress) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(_FIELD_RGBW_WHITE_VALUE, _progress);
        int rowEffected = ourDatabase.update(_TABLE_RGBW, cv, _FIELD_RGBW_ID + "=" + _id ,null);
        close();
        return rowEffected;
    }
    public int updateGroupWhiteValue(int _gid, int _progress) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(_FIELD_GROUP_WHITE_VALUE, _progress);
        int rowEffected = ourDatabase.update(_TABLE_GROUP, cv, _FIELD_GROUP_ID + "=" + _gid ,null);
        close();
        return rowEffected;
    }
    public int updateWhiteOn(int _id, boolean _isChecked) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(_FIELD_RGBW_WHITE_ON, _isChecked ? 1 : 0);
        int rowEffected = ourDatabase.update(_TABLE_RGBW, cv, _FIELD_RGBW_ID + "=" + _id ,null);
        close();
        return rowEffected;
    }
    public int updateGroupWhiteOn(int _gid, int _isChecked) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(_FIELD_GROUP_WHITE_ON, _isChecked);
        int rowEffected = ourDatabase.update(_TABLE_GROUP, cv, _FIELD_GROUP_ID + "=" + _gid ,null);
        close();
        return rowEffected;
    }
    public int updateAnimate(int _id,int _anim) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(_FIELD_RGBW_ANIMATE, _anim);
        int rowEffected = ourDatabase.update(_TABLE_RGBW, cv, _FIELD_RGBW_ID + "=" + _id ,null);
        close();
        return rowEffected;
    }

    public int updateMode(int _id, int _mode) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(_FIELD_RGBW_MODE, _mode);
        int rowEffected = ourDatabase.update(_TABLE_RGBW, cv, _FIELD_RGBW_ID + " = " + _id ,null);
        close();
        return rowEffected;
    }
    public int updateFavColor(int _id, int color, int i) {
        open();
        ContentValues cv = new ContentValues();
        switch (i){
            case 1:
                cv.put(_FIELD_RGBW_FAV01, color);
                break;
            case 2:
                cv.put(_FIELD_RGBW_FAV02, color);
                break;
            case 3:
                cv.put(_FIELD_RGBW_FAV03, color);
                break;
            case 4:
                cv.put(_FIELD_RGBW_FAV04, color);
                break;
            case 5:
                cv.put(_FIELD_RGBW_FAV05, color);
                break;
            case 6:
                cv.put(_FIELD_RGBW_FAV06, color);
                break;
            case 7:
                cv.put(_FIELD_RGBW_FAV07, color);
                break;
            case 8:
                cv.put(_FIELD_RGBW_FAV08, color);
                break;
            case 9:
                cv.put(_FIELD_RGBW_FAV09, color);
                break;
            case 10:
                cv.put(_FIELD_RGBW_FAV10, color);
                break;
        }

        int rowEffected = ourDatabase.update(_TABLE_RGBW, cv, _FIELD_RGBW_ID + " = " + _id ,null);
        close();
        return rowEffected;
    }

    public int updateColor(int _id, int color) {

        open();
        ContentValues cv = new ContentValues();
        cv.put(_FIELD_RGBW_COLOR, color);
        int rowEffected = ourDatabase.update(_TABLE_RGBW, cv, _FIELD_RGBW_ID + " = " + _id ,null);
        close();
        return rowEffected;
    }

    public int updateGroupColor(int _gid, int color) {

        open();
        ContentValues cv = new ContentValues();
        cv.put(_FIELD_GROUP_COLOR, color);
        int rowEffected = ourDatabase.update(_TABLE_GROUP, cv, _FIELD_GROUP_ID + " = " + _gid ,null);
        close();
        return rowEffected;
    }
    public int updateAccess(int baseDeviceId, int uid, int status) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(_FIELD_ACCESS_STATUS, status);
        int rowEffected = ourDatabase.update(_TABLE_ACCESS, cv, _FIELD_MODULE_ID + " = " +
                baseDeviceId + " AND " + _FIELD_USER_ID + " = " + uid ,null);
        close();
        return rowEffected;
    }
    public int removeDevice(int _id,int _typeid) {

        open();
        int rowEffected = ourDatabase.delete(_TABLE_MODULE,_FIELD_MODULE_ID + " = " + _id,null);
        ourDatabase.delete(_TABLE_ACCESS,_FIELD_MODULE_ID + " = " + _id,null);
        if(rowEffected>0){
            switch (_typeid)
            {
                case 1://rf
                case 2://ir
                    break;

                case 3://rgb lamp
                case 4://rgb strip
                case 5://rgb musical
                    rowEffected = ourDatabase.delete(_TABLE_RGBW,_FIELD_MODULE_ID + " = " + _id,null);
                    break;

                case 6://dimmer
                    break;

                case 7://unknow
                    break;

                case 8://switch
                case 9://latch001
                case 10://latch002
                case 11://latch003
                case 12://latch004
                case 13://latch005
                case 14://latch006
                case 15://latch007
                case 16://latch008
                case 17://latch009
                case 18://latch010
                case 19://latch011
                case 20://latch012
                case 21://latch013
                case 22://latch014
                case 23://latch015
                case 24://latch016

                    break;
                default:
                    break;
            }
        }
        close();
        return rowEffected;

    }

    public int removeGroup(int _gid) {
        try {
            open();
            ourDatabase.delete(_TABLE_GROUP, _FIELD_GROUP_ID + " = " + _gid, null);

            ContentValues cv = new ContentValues();
            cv.put(_FIELD_GROUP_ID, -1);
            int rowEffected = ourDatabase.update(_TABLE_MODULE, cv, _FIELD_GROUP_ID + " = " + _gid, null);
            close();
            return rowEffected;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }


    public int addUser(String _uuid, String _name, int _pic, String _des, String _cell) {
        long a = -1;
        try {
            open();
            ContentValues cv = new ContentValues();
            cv.put(_FIELD_USER_UNIQUE_ID, _uuid);
            cv.put(_FIELD_USER_NAME, _name);
            cv.put(_FIELD_USER_PIC, _pic);
            cv.put(_FIELD_USER_DESCRIPTION, _des);
            cv.put(_FIELD_USER_CELL, _cell);
            cv.put(_FIELD_USER_IS_ACTIVE, 1);

            a = ourDatabase.insertWithOnConflict(_TABLE_USER, null, cv, SQLiteDatabase.CONFLICT_ABORT);
        }catch (Exception e){
            e.printStackTrace();
            a = -1;
        }
        finally {
            close();
            return (int) a;
        }
    }

    public int addAccess(int _mid, int _uid,int permission,int status) {
        long a = -1;
        try {
            open();
            ContentValues cv = new ContentValues();
            cv.put(_FIELD_MODULE_ID, _mid);
            cv.put(_FIELD_USER_ID, _uid);
            cv.put(_FIELD_MODULE_PERMISSION , permission);
            cv.put(_FIELD_ACCESS_STATUS , status);

            a = ourDatabase.insertWithOnConflict(_TABLE_ACCESS, null, cv, SQLiteDatabase.CONFLICT_ABORT);
        }catch (Exception e){
            e.printStackTrace();
            a = -1;
        }
        finally {
            close();
            return (int) a;
        }
    }

    public int addGroup(String _name) {

        long a = -1;
        try {
            open();
            ContentValues cv = new ContentValues();
            cv.put(_FIELD_GROUP_NAME, _name);
            cv.put(_FIELD_GROUP_COLOR, color_palete[1]);
            cv.put(_FIELD_GROUP_DIMMER,0);
            cv.put(_FIELD_GROUP_INDEX,0);
            cv.put(_FIELD_GROUP_ONOFF,0);
            cv.put(_FIELD_GROUP_WHITE_ON,0);
            cv.put(_FIELD_GROUP_WHITE_VALUE,0);
            cv.put(_FIELD_HOME_ID, -1);

            a = ourDatabase.insertWithOnConflict(_TABLE_GROUP, null, cv, SQLiteDatabase.CONFLICT_ABORT);
        }catch (Exception e){
            e.printStackTrace();
            a = -1;
        }
        finally {
            close();
            return (int) a;
        }
    }
    public int addBaseDeviceToGroup(int _id, ArrayList<SelectableItem> freeDev) {

        open();
        int rowEffected=0;
        for (int i = 0; i < freeDev.size(); i++) {

            if(freeDev.get(i).isSelected()) {
                ContentValues cv = new ContentValues();
                cv.put(_FIELD_GROUP_ID, _id);
                rowEffected += ourDatabase.update(_TABLE_MODULE, cv, _FIELD_MODULE_ID + "=" + freeDev.get(i).getId(), null);
            }
        }
        close();
        return rowEffected;
    }
    public void seeModules(){
        open();

        Log.e(TAG, "seeModules: ---------------");

        String[] col = new String[]{_FIELD_MODULE_ID,_FIELD_MODULE_NAME,_FIELD_MODULE_MAC,_FIELD_MODULE_PASS,_FIELD_GROUP_ID,_FIELD_TYPE_ID,_FIELD_MODULE_AVETAR,_FIELD_USER_ID};
        Cursor c = ourDatabase.query(_TABLE_MODULE, col, null, null, null, null, _FIELD_MODULE_INDEX);
        int iID = c.getColumnIndex(_FIELD_MODULE_ID);
        int iName = c.getColumnIndex(_FIELD_MODULE_NAME);
        int iAddress = c.getColumnIndex(_FIELD_MODULE_MAC);
        int iType = c.getColumnIndex(_FIELD_GROUP_ID);
        int t = c.getColumnIndex(_FIELD_TYPE_ID);
        int avt = c.getColumnIndex(_FIELD_MODULE_AVETAR);
        int uid = c.getColumnIndex(_FIELD_USER_ID);
        if (c.moveToFirst()) {
            for (int i = 0; i < c.getCount(); i++) {

                String str = "id: " + c.getInt(iID) + "' mac: " + c.getString(iAddress) + " ,name: " + c.getString(iName) + " ,gid: " + c.getInt(iType) + " type:" + c.getInt(t) + " avt:" + c.getInt(avt) + " uid:" + c.getInt(uid);
                Log.e(TAG, "seeModules: " + i + ": " + str);
                c.moveToNext();
            }
        }
        close();
    }


    public void seeRgbModules(){

        try {
            open();
            Log.e(TAG, "seeRgbModules: ---------------");
            String[] col = new String[]{_FIELD_MODULE_ID,_FIELD_RGBW_ID,_FIELD_RGBW_COLOR};
            Cursor c = ourDatabase.query(_TABLE_RGBW, col, null, null, null, null, null);
            int iID = c.getColumnIndex(_FIELD_MODULE_ID);
            int iName = c.getColumnIndex(_FIELD_RGBW_ID);
            int iAddress = c.getColumnIndex(_FIELD_RGBW_COLOR);
            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {

                    String str = "pid: " + c.getInt(iID) + " id: " + c.getInt(iName) + " ,color: " + c.getInt(iAddress)  ;
                    Log.e(TAG, "seeRgbModules: i:" + i + ": " + str);
                    c.moveToNext();
                }
            }
        }catch (Exception e){e.printStackTrace();}
        finally {

            close();
        }
    }


    public void seeUsers() {
        try {
            open();
            Log.e(TAG, "seeUsers: ---------------");
            String[] col = new String[]{_FIELD_USER_UNIQUE_ID, _FIELD_USER_ID, _FIELD_USER_NAME};
            Cursor c = ourDatabase.query(_TABLE_USER, col, null, null, null, null, null);
            int iID = c.getColumnIndex(_FIELD_USER_UNIQUE_ID);
            int iName = c.getColumnIndex(_FIELD_USER_ID);
            int iAddress = c.getColumnIndex(_FIELD_USER_NAME);
            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {

                    String str = "uuid: " + c.getString(iID) + " id: " + c.getInt(iName) + "  name: " + c.getString(iAddress);
                    Log.e(TAG, "seeUsers: i:" + i + ": " + str);
                    c.moveToNext();
                }
            }
        }catch (Exception e){e.printStackTrace();}
        finally {

            close();
        }
    }

    public void seeAccess() {
        try {


            open();
            Log.e(TAG, "seeAccess: ---------------");
            String[] col = new String[]{_FIELD_MODULE_ID, _FIELD_USER_ID, _FIELD_ACCESS_STATUS};
            Cursor c = ourDatabase.query(_TABLE_ACCESS, col, null, null, null, null, null);
            int iID = c.getColumnIndex(_FIELD_MODULE_ID);
            int iName = c.getColumnIndex(_FIELD_USER_ID);
            int iAccessStatus = c.getColumnIndex(_FIELD_ACCESS_STATUS);
            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {

                    String str = "mid: " + c.getInt(iID) + " uid: " + c.getInt(iName) + " accessStatus: " + c.getInt(iAccessStatus);
                    Log.e(TAG, "seeAccess: i:" + i + ": " + str);
                    c.moveToNext();
                }
            }
        }catch (Exception e){e.printStackTrace();}
        finally {

            close();
        }
    }

    public void seeTypes() {
        open();
        Log.e(TAG, "seeTypes: ---------------");
        String[] col = new String[]{_FIELD_TYPE_ID,_FIELD_TYPE_NAME};
        Cursor c = ourDatabase.query(_TABLE_TYPE, col, null, null, null, null, null);
        int iID = c.getColumnIndex(_FIELD_TYPE_ID);
        int iName = c.getColumnIndex(_FIELD_TYPE_NAME);
        if (c.moveToFirst()) {
            for (int i = 0; i < c.getCount(); i++) {

                String str = "tid: " + c.getInt(iID) + " tName: " + c.getString(iName) ;
                Log.e(TAG, "seeTypes: i:" + i + ": " + str);
                c.moveToNext();
            }
        }
        close();
    }

    public void seeGroups() {
        open();
        Log.e(TAG, "seeGroups: ---------------");
        String[] col = new String[]{_FIELD_GROUP_NAME,_FIELD_GROUP_ID};
        Cursor c = ourDatabase.query(_TABLE_GROUP, col, null, null, null, null, null);
        int iID = c.getColumnIndex(_FIELD_GROUP_ID);
        int iName = c.getColumnIndex(_FIELD_GROUP_NAME);
        if (c.moveToFirst()) {
            for (int i = 0; i < c.getCount(); i++) {

                String str = "gid: " + c.getInt(iID) + " gName: " + c.getString(iName) ;
                Log.e(TAG, "seeGroups: i:" + i + ": " + str);
                c.moveToNext();
            }
        }
        close();
    }

    public void seeGroups2() {
        try {
            open();
            Log.e(TAG, "seeGroups2: ---------------");
            String[] col = new String[]{_FIELD_GROUP_NAME,_TABLE_GROUP+"."+ _FIELD_GROUP_ID, " COUNT(*) AS 'count'"};
            String where = _TABLE_GROUP + "." + _FIELD_GROUP_ID + " = " + _TABLE_MODULE + "." + _FIELD_GROUP_ID;

            Cursor c = ourDatabase.query(_TABLE_GROUP + "," + _TABLE_MODULE, col, where, null, _TABLE_MODULE+"."+_FIELD_GROUP_ID, null, null);
            int iID = c.getColumnIndex(_FIELD_GROUP_ID);
            int iName = c.getColumnIndex(_FIELD_GROUP_NAME);
            int iCount = c.getColumnIndex("count");
            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {

                    String str = "gid: " + c.getInt(iID) + " gName: " + c.getString(iName) + " count:" + c.getInt(iCount);
                    Log.e(TAG, "seeGroups2: i:" + i + ": " + str);
                    c.moveToNext();
                }
            }
        }catch (Exception e){e.printStackTrace();}
        close();
    }
}
