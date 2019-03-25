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
    private SQLiteHelperForPhoneBook dbHelper;

    public AppDatabaseHandler(Context context){dbHelper=new SQLiteHelperForPhoneBook(context);}

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

    public List<PhoneBookPojo> showAllContact(){

        SQLiteDatabase db=dbHelper.getWritableDatabase();
        List<PhoneBookPojo> pbList=new ArrayList<PhoneBookPojo>();
        String sqlquery="SELECT  "+dbHelper.Name+","+dbHelper.Number+" FROM " + dbHelper.TABLE+" ORDER BY "+dbHelper.Name;
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

    public List<PhoneBookPojo> showWhatsappContact(){

        SQLiteDatabase db=dbHelper.getWritableDatabase();
        List<PhoneBookPojo> pbList=new ArrayList<PhoneBookPojo>();
        String sqlquery="SELECT  "+dbHelper.Name+","+dbHelper.Number+" FROM " + dbHelper.TABLE+" WHERE STATUS='YES' ORDER BY "+dbHelper.Name+" COLLATE NOCASE";
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
        String sqlquery="SELECT  "+dbHelper.Name+","+dbHelper.Number+" FROM " + dbHelper.TABLE+" WHERE STATUS='NO' ORDER BY "+dbHelper.Name+" COLLATE NOCASE";
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
        db.close();
        return count;
    }

    public int getAntiWhatsappContactCount(){

        SQLiteDatabase db=dbHelper.getReadableDatabase();
        int count=0;
        count= (int)DatabaseUtils.queryNumEntries(db,dbHelper.TABLE,dbHelper.Status+" = 'NO'");
        db.close();
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
        int rows=db.update(dbHelper.TABLE,v,dbHelper.Name+"= ? and "+dbHelper.Number+"= ?",new String[]{name,number});
        db.close();
        return rows;
    }

    public PhoneBookPojo fetchContactDetails(String name, String number)
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        PhoneBookPojo pb=new PhoneBookPojo();
        String sqlquery="SELECT * FROM " + dbHelper.TABLE+" WHERE "+dbHelper.Name+"='"+name+"' AND "+dbHelper.Number+"='"+number+"'";
        Cursor c=db.rawQuery(sqlquery,null);
        if(c.moveToFirst())
        {
                pb.setName(c.getString(0));
                pb.setNumber(c.getString(1));
                pb.setEmail(c.getString(2));
                pb.setType(c.getString(3));
                pb.setStatus(c.getString(4));
                pb.setAddress(c.getString(5));
        }
        db.close();
        return pb;
    }

    public int isContactPresent(String name, String number)
    {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        int count=0;
        count= (int)DatabaseUtils.queryNumEntries(db,dbHelper.TABLE,dbHelper.Name+" = '"+name+"' AND "+dbHelper.Number+"='"+number+"'");
        db.close();
        return count;
    }

    int addFtsContact(PhoneBookPojo pb)
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues v=new ContentValues();
        v.put(dbHelper.Name,pb.getName());
        v.put(dbHelper.Number,pb.getNumber());
        v.put(dbHelper.Status,pb.getStatus());
        long idE=0;
        try{idE=db.insert(dbHelper.FTS_TABLE,null,v);}
        catch(Exception e)
        {
            e.printStackTrace();
        }
        db.close();
        return (int)idE;
    }

    public List<PhoneBookPojo> showMatchedContactByName(String query){

        SQLiteDatabase db=dbHelper.getWritableDatabase();
        List<PhoneBookPojo> pbList=new ArrayList<PhoneBookPojo>();
        String sqlquery="SELECT  * FROM " + dbHelper.FTS_TABLE+" WHERE "+dbHelper.Name+" MATCH '*"+query+"*' ORDER BY "+dbHelper.Name+" COLLATE NOCASE";
        Cursor c=db.rawQuery(sqlquery,null);
        if(c.moveToFirst())
        {
            do{
                PhoneBookPojo pb=new PhoneBookPojo();
                pb.setName(c.getString(0));
                pb.setNumber(c.getString(1));
                pb.setStatus(c.getString(2));
                pbList.add(pb);
            }while(c.moveToNext());
        }
        db.close();
        return pbList;
    }

    public List<PhoneBookPojo> showMatchedContactByNumber(String query){

        SQLiteDatabase db=dbHelper.getWritableDatabase();
        List<PhoneBookPojo> pbList=new ArrayList<PhoneBookPojo>();
        String processedQuery="";
        for(int i=0;i<query.length()-1;i++)
        {
            processedQuery=processedQuery+String.valueOf(query.charAt(i))+"*";
        }
        processedQuery=processedQuery+String.valueOf(query.charAt(query.length()-1));
        String sqlquery="SELECT  * FROM " + dbHelper.FTS_TABLE+" WHERE "+dbHelper.Number+" MATCH '*"+query+"*' AND NOT "+dbHelper.Name+"='' ORDER BY "+dbHelper.Name+" COLLATE NOCASE LIMIT 10";
        Cursor c=db.rawQuery(sqlquery,null);
        if(c.moveToFirst())
        {
            do{
                PhoneBookPojo pb=new PhoneBookPojo();
                pb.setName(c.getString(0));
                pb.setNumber(c.getString(1));
                pb.setStatus(c.getString(2));
                pbList.add(pb);
            }while(c.moveToNext());
        }
        db.close();
        return pbList;
    }

    public int isFtsContactPresent(String name, String number)
    {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        int count=0;
        count= (int)DatabaseUtils.queryNumEntries(db,dbHelper.FTS_TABLE,dbHelper.Name+" = '"+name+"' AND "+dbHelper.Number+"='"+number+"'");
        db.close();
        return count;
    }

    public String fetchCallerName(String number)
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        String name="";
        String sqlquery="SELECT  * FROM " + dbHelper.FTS_TABLE+" WHERE "+dbHelper.Number+" MATCH '"+number+"' ORDER BY "+dbHelper.Name;
        Cursor c=db.rawQuery(sqlquery,null);
        if(c.moveToFirst())
        {
            name=c.getString(0);
        }
        db.close();
        return name;
    }
}
