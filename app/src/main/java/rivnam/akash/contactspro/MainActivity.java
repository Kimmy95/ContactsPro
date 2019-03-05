package rivnam.akash.contactspro;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_SMS;

public class MainActivity extends AppCompatActivity {

    Intent databasePopulateService,logDatabasePopulateService;
    View shadowView;
    FloatingActionMenu actionMenu;
    SearchView searchView;
    private static final int DPS_JOB_ID = 1000;
    private static final int LDPS_JOB_ID = 1001;
    static int number_of_request=0;

    SearchAdapter searchAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPermission();

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED)
        {
            databasePopulateService=new Intent();
            databasePopulateService.putExtra("attachString","secondrun");
            DatabasePopulateService.enqueueWork(getApplicationContext(),DatabasePopulateService.class,DPS_JOB_ID,databasePopulateService);
            logDatabasePopulateService=new Intent();
            logDatabasePopulateService.putExtra("attachString","secondrun");
            LogDatabasePopulateService.enqueueWork(getApplicationContext(),LogDatabasePopulateService.class,LDPS_JOB_ID,logDatabasePopulateService);
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Toolbar tool_bar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tool_bar);
        shadowView=(View)findViewById(R.id.shadow_view);

        final ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.ic_settings_black_24dp);
        final FloatingActionButton actionButton = new FloatingActionButton.Builder(this).setContentView(icon).build();
        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(200, 200);
        layoutParams.setMargins(0,0,30,190);
        actionButton.setPosition(4, layoutParams);
        final FabView add_contact = new FabView(this);
        add_contact.setIcon(R.drawable.ic_person_add_black_24dp);
        add_contact.setText("Add Contact");
        FabView ask_sim = new FabView(this);
        ask_sim.setIcon(R.drawable.ic_sim_card_black_24dp);
        ask_sim.setText("Ask Sim");
        FabView dialpad = new FabView(this);
        dialpad.setIcon(R.drawable.ic_dialpad_black_24dp);
        dialpad.setText("Dialpad");
        actionMenu = new FloatingActionMenu.Builder(this).addSubActionView(add_contact).addSubActionView(ask_sim).addSubActionView(dialpad).attachTo(actionButton).setRadius(350).setStartAngle(180).setEndAngle(255).build();

        displaySelectedScreen(0);
        actionMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu floatingActionMenu) {
                searchView.onActionViewCollapsed();
                displaySelectedScreen(Constants.frameBeforeSearch);
                shadowView.setVisibility(View.VISIBLE);
                icon.setImageResource(R.drawable.ic_cancel_black_24dp);
            }
            @Override
            public void onMenuClosed(FloatingActionMenu floatingActionMenu) {
                icon.setImageResource(R.drawable.ic_settings_black_24dp);
                shadowView.setVisibility(View.GONE);
            }
        });
        add_contact.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Add Contact", Toast.LENGTH_SHORT).show();
            }
        });
        ask_sim.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Ask_sim", Toast.LENGTH_SHORT).show();
            }
        });
        dialpad.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Dialpad", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        searchView =(SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search contact..");
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySelectedScreen(3);
                actionMenu.close(true);
                Constants.query="";
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Constants.query=query;
                displaySelectedScreen(3);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Constants.query=newText;
                displaySelectedScreen(3);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                displaySelectedScreen(Constants.frameBeforeSearch);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.xxxx1:
                Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.theme:
                return true;
            case R.id.settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.tab_favourites:
                    searchView.onActionViewCollapsed();
                    Constants.frameBeforeSearch=0;
                    displaySelectedScreen(0);
                    return true;
                case R.id.tab_logs:
                    searchView.onActionViewCollapsed();
                    Constants.frameBeforeSearch=1;
                    displaySelectedScreen(1);
                    return true;
                case R.id.tab_phoneBook:
                    searchView.onActionViewCollapsed();
                    Constants.frameBeforeSearch=2;
                    displaySelectedScreen(2);
                    return true;
            }
            return false;
        }
    };

    public void displaySelectedScreen(int item) {
        Fragment fragment = null;
        switch(item)
        {
            case 0:
                fragment=new Favourites();
                break;
            case 1:
                fragment=new Logs();
                break;
            case 2:
                fragment=new PhoneBook();
                break;
            case 3:
                fragment=new SearchResult();
                break;
        }
        if (fragment!=null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            try{ft.commit();}
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void getPermission()
    {
        String permission[]={Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS};
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission[0]) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(MainActivity.this, permission[1]) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(MainActivity.this, permission[2]) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(MainActivity.this, permission[3]) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(MainActivity.this, permission[4]) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, permission, 0x1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int index = 0;
        Map<String, Integer> PermissionsMap = new HashMap<String, Integer>();
        for (String permission : permissions){
            PermissionsMap.put(permission, grantResults[index]);
            index++;
        }
        if((PermissionsMap.get(Manifest.permission.READ_CONTACTS) == 0)
                && PermissionsMap.get(Manifest.permission.READ_CALL_LOG) == 0){
            databasePopulateService=new Intent();
            databasePopulateService.putExtra("attachString","firstrun");
            DatabasePopulateService.enqueueWork(getApplicationContext(),DatabasePopulateService.class,DPS_JOB_ID,databasePopulateService);
            logDatabasePopulateService=new Intent();
            logDatabasePopulateService.putExtra("attachString","firstrun");
            LogDatabasePopulateService.enqueueWork(getApplicationContext(),LogDatabasePopulateService.class,LDPS_JOB_ID,logDatabasePopulateService);
        }
        else
        {
            Toast.makeText(this, "Contact and Phone permissions required", Toast.LENGTH_SHORT).show();
            if(number_of_request<3) {
                getPermission();
            }
            else
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                }
                else
                {
                    finish();
                }
            }

        }
    }
}
