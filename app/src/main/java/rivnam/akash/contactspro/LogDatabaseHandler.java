package rivnam.akash.contactspro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akash on 21-02-2019.
 */

public class LogDatabaseHandler {
    private SQLiteHelperForLog dbHelper;

    public LogDatabaseHandler(Context context){dbHelper=new SQLiteHelperForLog(context);}

    int addSingleLogRecord(LogPojo lp)
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues v=new ContentValues();
        v.put(dbHelper.Name,lp.getName());
        v.put(dbHelper.Number,lp.getNumber());
        v.put(dbHelper.Type,lp.getType());
        v.put(dbHelper.Duration,lp.getDuration());
        v.put(dbHelper.Simid,lp.getSimid());
        v.put(dbHelper.Calldate,lp.getCalldate());
        long idE=0;
        try{idE=db.insert(dbHelper.TABLE,null,v);}
        catch(Exception e)
        {
            e.printStackTrace();
        }
        db.close();
        return (int)idE;
    }

    public List<LogPojo> showAllLog()
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        List<LogPojo> lpList=new ArrayList<LogPojo>();
        String sqlquery="SELECT  * FROM "+dbHelper.TABLE+" ORDER BY "+dbHelper.Calldate+" DESC";
        Cursor c=db.rawQuery(sqlquery,null);
        if(c.moveToFirst())
        {
            do{
                LogPojo lp=new LogPojo();
                lp.setName(c.getString(0));
                lp.setNumber(c.getString(1));
                lp.setType(c.getString(2));
                lp.setDuration(c.getString(3));
                lp.setSimid(c.getString(4));
                lp.setCalldate(c.getString(5));
                lpList.add(lp);
            }while(c.moveToNext());
        }
        db.close();
        return lpList;
    }

    public int isLogPresent(String number, String calldate)
    {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        int count=0;
        count= (int)DatabaseUtils.queryNumEntries(db,dbHelper.TABLE,dbHelper.Number+" = '"+number+"' AND "+dbHelper.Calldate+"='"+calldate+"'");
        db.close();
        return count;
    }

    public int updateLogRecord(LogPojo lp, String number, String calldate)
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues v=new ContentValues();
        v.put(dbHelper.Name,lp.getName());
        v.put(dbHelper.Number,lp.getNumber());
        v.put(dbHelper.Type,lp.getType());
        v.put(dbHelper.Duration,lp.getDuration());
        v.put(dbHelper.Simid,lp.getSimid());
        v.put(dbHelper.Calldate,lp.getCalldate());
        int rows=db.update(dbHelper.TABLE,v,dbHelper.Number+"= ? and "+dbHelper.Calldate+"= ?",new String[]{number,calldate});
        db.close();
        return rows;
    }

    public int clearLogRecord(int number)
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        String sqlquery="SELECT "+dbHelper.Number+","+dbHelper.Calldate+" FROM "+dbHelper.TABLE+" ORDER BY "+dbHelper.Calldate;
        Cursor c=db.rawQuery(sqlquery,null);
        if(c.moveToFirst())
        {
            while (number-->0)
            {
                db.delete(dbHelper.TABLE,dbHelper.Number+"= ? AND "+dbHelper.Calldate+"= ?",new String[]{c.getString(0), c.getString(1)});
                c.moveToNext();
            }
        }
        db.close();
        return 1;
    }

    public int getLogCount(){

        SQLiteDatabase db=dbHelper.getReadableDatabase();
        int count=0;
        count= (int)DatabaseUtils.queryNumEntries(db,dbHelper.TABLE);
        db.close();
        return count;
    }

    public List<LogPojo> showTopTenLog(){

        SQLiteDatabase db=dbHelper.getWritableDatabase();
        List<LogPojo> lpList=new ArrayList<LogPojo>();
        String sqlquery="SELECT "+dbHelper.Name+","+dbHelper.Number+",COUNT(*) AS OCCURENCES FROM " + dbHelper.TABLE+" WHERE ROWID IN ( SELECT ROWID FROM LOGBOOK WHERE NOT "+dbHelper.Name+" ='' ORDER BY "+dbHelper.Calldate+" DESC LIMIT 300 ) GROUP BY NAME ORDER BY OCCURENCES DESC LIMIT 10";
        Cursor c=db.rawQuery(sqlquery,null);
        if(c.moveToFirst())
        {
            do{
                ContentValues v=new ContentValues();
                v.put(dbHelper.Name,c.getString(0));
                v.put(dbHelper.Number,c.getString(1));
                v.put(dbHelper.Type,"RECOMMENDED");
                long idE=0;
                try{idE=db.insert(dbHelper.FAV_TABLE,null,v);}
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }while(c.moveToNext());
        }
        db.close();
        return lpList;
    }

    int addFavRecord(FavoritePojo fp)
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues v=new ContentValues();
        v.put(dbHelper.Name,fp.getName());
        v.put(dbHelper.Number,fp.getNumber());
        v.put(dbHelper.Type,fp.getType());
        long idE=0;
        try{idE=db.insert(dbHelper.FAV_TABLE,null,v);}
        catch(Exception e)
        {
            e.printStackTrace();
        }
        db.close();
        return (int)idE;
    }

    public List<Pair<FavoritePojo,FavoritePojo>> showAllFavRecord()
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        List<Pair<FavoritePojo,FavoritePojo>> fpList=new ArrayList<Pair<FavoritePojo,FavoritePojo>>();
        String sqlquery="SELECT  * FROM "+dbHelper.FAV_TABLE+" ORDER BY "+dbHelper.Type;
        Cursor c=db.rawQuery(sqlquery,null);
        if(c.moveToFirst())
        {
            do{
                FavoritePojo fp1=new FavoritePojo();
                FavoritePojo fp2=new FavoritePojo();
                fp1.setName(c.getString(0));
                fp1.setNumber(c.getString(1));
                fp1.setType(c.getString(2));
                if(c.moveToNext())
                {
                    fp2.setName(c.getString(0));
                    fp2.setNumber(c.getString(1));
                    fp2.setType(c.getString(2));
                    fpList.add(new Pair(fp1,fp2));
                }
                else
                {
                    fp2.setName("");
                    fp2.setNumber("");
                    fp2.setType("");
                    fpList.add(new Pair(fp1,fp2));
                    break;
                }
                if(c.moveToNext())
                {
                    ;
                }
                else
                {
                    break;
                }
            }while(true);
        }
        db.close();
        return fpList;
    }

    public int clearFavRecord()
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(dbHelper.FAV_TABLE,dbHelper.Type+"= ?",new String[]{"RECOMMENDED"});
        db.close();
        return 1;
    }

    public int deleteFavRecord(String name)
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(dbHelper.FAV_TABLE,dbHelper.Name+"= ?",new String[]{name});
        db.close();
        return 1;
    }
}
