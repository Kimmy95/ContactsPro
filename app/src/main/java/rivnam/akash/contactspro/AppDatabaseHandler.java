package rivnam.akash.contactspro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akash on 21-02-2019.
 */

public class AppDatabaseHandler {
    private SQLiteHelper dbHelper;

    public AppDatabaseHandler(Context context){dbHelper=new SQLiteHelper(context);}

    int addContact(PhoneBookPojo pb)
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues v=new ContentValues();
        v.put(dbHelper.Name,pb.getName());
        v.put(dbHelper.Number,pb.getNumber());
        v.put(dbHelper.Email,pb.getEmail());
        v.put(dbHelper.Type,pb.getType());
        v.put(dbHelper.Status,pb.getStatus());
        v.put(dbHelper.Address,pb.getAddress());
        long idE=0;
        try{idE=db.insert(dbHelper.TABLE,null,v);}
        catch(Exception e)
        {
            e.printStackTrace();
        }
        db.close();
        return (int)idE;
    }

    public List<PhoneBookPojo> showWhatsappContact(){

        SQLiteDatabase db=dbHelper.getWritableDatabase();
        List<PhoneBookPojo> pbList=new ArrayList<PhoneBookPojo>();
        String sqlquery="SELECT  "+dbHelper.Name+","+dbHelper.Number+" FROM " + dbHelper.TABLE+" WHERE STATUS='YES' ORDER BY "+dbHelper.Name;
        Cursor c=db.rawQuery(sqlquery,null);
        if(c.moveToFirst())
        {
            do{
                PhoneBookPojo pb=new PhoneBookPojo();
                pb.setName(c.getString(0));
                pb.setNumber(c.getString(1));
                pbList.add(pb);
            }while(c.moveToNext());
        }
        db.close();
        return pbList;
    }

    public List<PhoneBookPojo> showAntiWhatsappContact(){

        SQLiteDatabase db=dbHelper.getWritableDatabase();
        List<PhoneBookPojo> pbList=new ArrayList<PhoneBookPojo>();
        String sqlquery="SELECT  "+dbHelper.Name+","+dbHelper.Number+" FROM " + dbHelper.TABLE+" WHERE STATUS='NO' ORDER BY "+dbHelper.Name;
        Cursor c=db.rawQuery(sqlquery,null);
        if(c.moveToFirst())
        {
            do{
                PhoneBookPojo pb=new PhoneBookPojo();
                pb.setName(c.getString(0));
                pb.setNumber(c.getString(1));
                pbList.add(pb);
            }while(c.moveToNext());
        }
        db.close();
        return pbList;
    }

    public int getWhatsappContactCount(){

        SQLiteDatabase db=dbHelper.getReadableDatabase();
        int count=0;
        count= (int)DatabaseUtils.queryNumEntries(db,dbHelper.TABLE,dbHelper.Status+" = 'YES'");
        return count;
    }

    public int getAntiWhatsappContactCount(){

        SQLiteDatabase db=dbHelper.getReadableDatabase();
        int count=0;
        count= (int)DatabaseUtils.queryNumEntries(db,dbHelper.TABLE,dbHelper.Status+" = 'NO'");
        return count;
    }

    public int deleteContact(String name, String phone) {

        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(dbHelper.TABLE,dbHelper.Name+"= ? AND "+dbHelper.Number+"= ?",new String[]{name, phone});
        db.close();
        return 1;
    }

    public int updateContact(PhoneBookPojo pb, String name, String number)
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues v=new ContentValues();
        v.put(dbHelper.Name,pb.getName());
        v.put(dbHelper.Number,pb.getNumber());
        v.put(dbHelper.Email,pb.getEmail());
        v.put(dbHelper.Type,pb.getType());
        v.put(dbHelper.Status,pb.getStatus());
        v.put(dbHelper.Address,pb.getAddress());
        return db.update(dbHelper.TABLE,v,dbHelper.Name+"= ? and "+dbHelper.Number+"= ?",new String[]{name,number});
    }


}
