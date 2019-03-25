package rivnam.akash.contactspro;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

/**
 * Created by Akash on 07-02-2019.
 */

public class PhoneBook extends Fragment{

    AppDatabaseHandler db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_phonebook, container, false);

        db=new AppDatabaseHandler(this.getActivity());
        Constants.whatsappCount=db.getWhatsappContactCount();
        Constants.antiwhatsppCount=db.getAntiWhatsappContactCount();

        final TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("WAPP("+Constants.whatsappCount+")"));
        tabLayout.addTab(tabLayout.newTab().setText("OTHERS("+Constants.antiwhatsppCount+")"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        displaySelectedScreen(Constants.activeTabIndex);
        tabLayout.getTabAt(Constants.activeTabIndex).select();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Constants.whatsappCount=db.getWhatsappContactCount();
                Constants.antiwhatsppCount=db.getAntiWhatsappContactCount();
                tabLayout.getTabAt(0).setText("WAPP("+Constants.whatsappCount+")");
                tabLayout.getTabAt(1).setText("OTHERS("+Constants.antiwhatsppCount+")");
                displaySelectedScreen(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Constants.whatsappCount=db.getWhatsappContactCount();
                Constants.antiwhatsppCount=db.getAntiWhatsappContactCount();
                tabLayout.getTabAt(0).setText("WAPP("+Constants.whatsappCount+")");
                tabLayout.getTabAt(1).setText("OTHERS("+Constants.antiwhatsppCount+")");
                displaySelectedScreen(tab.getPosition());
            }
        });
        return view;
    }

    public void displaySelectedScreen(int tabIndex)
    {
        Fragment fragment = null;
        switch (tabIndex) {
            case 0:
                fragment = new WhatsApp();
                break;

            case 1:
                fragment = new AntiWhatsApp();
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.phonebook_frame, fragment);
            ft.commit();
        }

    }


}
