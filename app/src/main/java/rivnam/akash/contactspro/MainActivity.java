package rivnam.akash.contactspro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
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

public class MainActivity extends AppCompatActivity {

    AppDatabaseHandler db;
    private SQLiteHelper dbHelper=new SQLiteHelper(MainActivity.this);
    View shadowView;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db=new AppDatabaseHandler(this);
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);
        String name="",number="",status="YES";
        if(cursor.moveToLast()) {
            do{
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                db.addContact(new PhoneBookPojo(name, number,null,null, status,null));
            }while (cursor.moveToPrevious());
        }
        cursor.close();



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Toolbar tool_bar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tool_bar);
        shadowView=(View)findViewById(R.id.shadow_view);



        final ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.ic_settings_black_24dp);
        final FloatingActionButton actionButton = new FloatingActionButton.Builder(this).setContentView(icon).build();
        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(180, 180);
        layoutParams.setMargins(0,0,20,170);
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
        final FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this).addSubActionView(add_contact).addSubActionView(ask_sim).addSubActionView(dialpad).attachTo(actionButton).setRadius(350).setStartAngle(180).setEndAngle(255).build();

        actionMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu floatingActionMenu) {
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
        SearchView searchView =(SearchView) searchItem.getActionView();
        searchItem.setIcon(R.drawable.ic_search);

        // Configure the search info and add any event listeners...
        MenuItem.OnActionExpandListener expandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when action item collapses
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        };

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.xxxx1:
                // User chose the "xxxx1" action, mark the current item
                // as a xxxx1...
                return true;

            case R.id.xxxx2:
                // User chose the "xxxx2" action, mark the current item
                // as a xxxx2...
                return true;

            case R.id.settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.tab_favourites:
                    displaySelectedScreen(0);
                    return true;
                case R.id.tab_logs:
                    displaySelectedScreen(1);
                    return true;
                case R.id.tab_phoneBook:
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

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        }
        /*if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, 0x1);*/
    }


}
