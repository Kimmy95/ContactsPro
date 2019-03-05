package rivnam.akash.contactspro;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.List;

/**
 * Created by Akash on 07-02-2019.
 */

public class Favourites extends Fragment{

    TextView favText;
    ListView lView;
    List<Pair<FavoritePojo,FavoritePojo>> fpList;
    FavouriteAdapter fd;
    LogDatabaseHandler db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.activity_favourites, container, false);
        favText=(TextView)view.findViewById(R.id.fav_text);
        lView=(ListView)view.findViewById(R.id.favorite_list);
        favText.setText("SUGGESTED");
        favText.setTextColor(Color.WHITE);
        db=new LogDatabaseHandler(this.getActivity());
        fpList=db.showAllFavRecord();
        fd=new FavouriteAdapter(this.getActivity(),fpList);
        lView.setAdapter(fd);
        return view;
    }


}
