package rivnam.akash.contactspro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Akash on 07-02-2019.
 */

public class Logs extends Fragment{

    List<LogPojo> lpList;
    ListView lView;
    LogAdapter ld;
    LogDatabaseHandler db;
    AppDatabaseHandler supportDb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.activity_logs, container, false);
        lView=(ListView)view.findViewById(R.id.log_list);
        db=new LogDatabaseHandler(this.getActivity());
        supportDb=new AppDatabaseHandler(this.getActivity());
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            Cursor cursor = this.getContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, android.provider.CallLog.Calls.DATE + " DESC LIMIT 5;");
            String name = "", number = "", type = "", duration = "", simid = "", calldate = "";
            int i = 0;
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
                    i++;
                } while (cursor.moveToPrevious() && i < 5);
            }
        }
        lpList=db.showAllLog();
        ld=new LogAdapter(this.getActivity(),lpList);
        lView.setAdapter(ld);

        return view;

    }


}
