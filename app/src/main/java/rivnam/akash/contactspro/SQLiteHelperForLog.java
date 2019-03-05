package rivnam.akash.contactspro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;

public class SQLiteHelperForLog extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;

    public static final String DATABASE_NAME="LOGS";
    public static final String TABLE="LOGBOOK";
    public static final String FAV_TABLE="FAVBOOK";
    public static final String Name="NAME";
    public static final String Number="NUMBER";
    public static final String Type="TYPE";
    public static final String Duration="DURATION";
    public static final String Simid="SIMID";
    public static final String Calldate="CALLDATE";


    public SQLiteHelperForLog(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db)
    {
        String p="CREATE TABLE "+TABLE+" ("+Name+" TEXT,"+Number+" TEXT,"+Type+" TEXT,"+Duration+" TEXT,"+Simid+" TEXT,"
                +Calldate+" TEXT, PRIMARY KEY ("+Number+","+Calldate+"))";
        db.execSQL(p);
        String pp="CREATE TABLE "+FAV_TABLE+" ("+Name+" TEXT,"+Number+" TEXT,"+Type+" TEXT, PRIMARY KEY ("+Name+"))";
        db.execSQL(pp);
    }

    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+FAV_TABLE);
        onCreate(db);
    }
}
