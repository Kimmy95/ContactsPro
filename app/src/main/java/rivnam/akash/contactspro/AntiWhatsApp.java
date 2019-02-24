package rivnam.akash.contactspro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Akash on 20-02-2019.
 */

public class AntiWhatsApp extends Fragment {

    List<PhoneBookPojo> pbList;
    ListView lView;
    PhoneBookAdapter ad;
    AppDatabaseHandler db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_antiwhatsapp, container, false);

        lView=(ListView)view.findViewById(R.id.whatsapp_list);
        db=new AppDatabaseHandler(this.getActivity());
        pbList=db.showAntiWhatsappContact();
        ad=new PhoneBookAdapter(this.getActivity(),pbList);
        lView.setAdapter(ad);

        return view;
    }
}
