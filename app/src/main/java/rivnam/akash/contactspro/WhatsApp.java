package rivnam.akash.contactspro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Akash on 20-02-2019.
 */

public class WhatsApp extends Fragment {

    List<PhoneBookPojo> pbList;
    ListView lView;
    PhoneBookAdapter ad;
    AppDatabaseHandler db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_whatsapp, container, false);
        lView=(ListView)view.findViewById(R.id.whatsapp_list);
        db=new AppDatabaseHandler(this.getActivity());
        pbList=db.showWhatsappContact();
        ad=new PhoneBookAdapter(this.getActivity(),pbList);
        lView.setAdapter(ad);
        PhoneBookPojo list=new PhoneBookPojo();

        return view;
    }

}
