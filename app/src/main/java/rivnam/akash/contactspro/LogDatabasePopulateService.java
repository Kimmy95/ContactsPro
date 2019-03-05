package rivnam.akash.contactspro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.JobIntentService;

/**
 * Created by Akash on 25-02-2019.
 */

public class LogDatabasePopulateService extends JobIntentService {

    LogDatabaseHandler db;
    AppDatabaseHandler supportDb;
    String dataString = "";

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        dataString = intent.getStringExtra("attachString");
        db = new LogDatabaseHandler(this);
        supportDb=new AppDatabaseHandler(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
            String name="",number="",type="",duration="",simid="",calldate="";
            if(dataString.equals("firstrun"))
            {
                if(cursor.moveToLast())
                {
                    do{
                        number=cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                        type=cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                        duration=cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
                        simid=cursor.getString(cursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_COMPONENT_NAME));
                        calldate=cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                        name=supportDb.fetchCallerName(number);
                        db.addSingleLogRecord(new LogPojo(name,number,type,duration,simid,calldate));
                    }while (cursor.moveToPrevious());
                }
                db.clearFavRecord();
                db.showTopTenLog();
            }
            else {
                if (cursor.moveToLast()) {
                    do {
                        number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                        type = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                        duration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
                        simid = cursor.getString(cursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID));
                        calldate = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                        if (db.isLogPresent(number, calldate) == 0) {
                            name = supportDb.fetchCallerName(number);
                            db.addSingleLogRecord(new LogPojo(name, number, type, duration, simid, calldate));
                        } else {
                            name = supportDb.fetchCallerName(number);
                            db.updateLogRecord(new LogPojo(name, number, type, duration, simid, calldate), number, calldate);
                        }
                    } while (cursor.moveToPrevious());
                }
                db.clearFavRecord();
                db.showTopTenLog();
            }
            db.clearLogRecord(db.getLogCount()-1111);
        }
    }
}
