package rivnam.akash.contactspro;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;
import android.view.View;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

/**
 * Created by Akash on 25-02-2019.
 */

public class DatabasePopulateService extends JobIntentService {

    AppDatabaseHandler db;
    String dataString="";
    PhoneBookPojo element;

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        dataString= intent.getStringExtra("attachString");
        db=new AppDatabaseHandler(this);
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);
        String name="",number="",status="YES";
        if(dataString.equals("firstrun")) {
            if (cursor.moveToLast()) {
                do {
                    name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    db.addContact(new PhoneBookPojo(name, number, null, null, status, null));
                } while (cursor.moveToPrevious());
            }
        }
        else
        {
            if (cursor.moveToLast()) {
                do {
                    name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if(db.isContactPresent(name,number)==0) {
                        db.addContact(new PhoneBookPojo(name, number, null, null, status, null));
                    }
                } while (cursor.moveToPrevious());
            }
        }
        cursor.close();
        Constants.searchList=db.showAllContact();
        for(int i=0;i<Constants.searchList.size();i++)
        {
            element=Constants.searchList.get(i);
            if(db.isFtsContactPresent(element.getName(),element.getNumber())==0) {
                db.addFtsContact(new PhoneBookPojo(element.getName(), element.getNumber(), element.getStatus()));
            }
        }
    }
}
