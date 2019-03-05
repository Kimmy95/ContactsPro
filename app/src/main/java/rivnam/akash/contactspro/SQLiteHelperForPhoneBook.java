package rivnam.akash.contactspro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelperForPhoneBook extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;

    public static final String DATABASE_NAME="CONTACTS";
    public static final String TABLE="PHONEBOOK";
    public static final String FTS_TABLE="VIRTUAL_PHONEBOOK";
    public static final String Name="NAME";
    public static final String Number="NUMBER";
    public static final String Email="EMAIL";
    public static final String Type="TYPE";
    public static final String Status="STATUS";
    public static final String Address="ADDRESS";



    public SQLiteHelperForPhoneBook(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db)
    {
        String p="CREATE TABLE "+TABLE+" ("+Name+" TEXT,"+Number+" TEXT,"+Email+" TEXT,"+Type+" TEXT,"+Status+" TEXT,"+Address+" TEXT, PRIMARY KEY ("+Name+","
                +Number+"))";
        db.execSQL(p);
        p="CREATE VIRTUAL TABLE "+FTS_TABLE+" USING fts4("+Name+" TEXT,"+Number+" TEXT,"+Status+" TEXT, PRIMARY KEY ("+Name+","+Number+"))";
        db.execSQL(p);
    }

    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+FTS_TABLE);
        onCreate(db);
    }
}
