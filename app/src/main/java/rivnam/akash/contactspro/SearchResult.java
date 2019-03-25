package rivnam.akash.contactspro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akash on 24-02-2019.
 */

public class SearchResult extends Fragment{
    TextView searchArea;
    ListView lView;
    SearchAdapter searchAdapter;
    AppDatabaseHandler db;
    List<PhoneBookPojo> pbList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_search, container, false);
        searchArea=(TextView)view.findViewById(R.id.search_area);
        lView=(ListView)view.findViewById(R.id.search_list);
        searchArea.setText("ALL CONTACTS");
        db=new AppDatabaseHandler(this.getActivity());
        if(Constants.query.length()==0)
        {
            pbList=db.showAllContact();
        }
        else{
            pbList=db.showMatchedContactByName(Constants.query);
        }
        if(pbList.size()==0)
        {
            if(Constants.query.matches("[0-9]+"))
            {
                searchArea.setText("Call "+Constants.query);
                searchArea.setTypeface(null, Typeface.NORMAL);
                searchArea.setPadding(20,25,0,0);
                searchArea.setTextColor(Color.BLUE);
                searchArea.setClickable(true);
                searchArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constants.query.trim()));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            v.getContext().startActivity(intent);
                        }
                    }
                });
            }
            else{
                searchArea.setText("No contacts found...Oops!!");
                searchArea.setTypeface(null, Typeface.NORMAL);
                searchArea.setPadding(20,25,0,0);
            }
        }
        else {
            searchAdapter = new SearchAdapter(this.getActivity(), pbList);
            lView.setAdapter(searchAdapter);
        }
        return view;
    }
}
