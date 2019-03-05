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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.activity_logs, container, false);
        lView=(ListView)view.findViewById(R.id.log_list);
        db=new LogDatabaseHandler(this.getActivity());
        lpList=db.showAllLog();
        ld=new LogAdapter(this.getActivity(),lpList);
        lView.setAdapter(ld);

        return view;

    }


}
