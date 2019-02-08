package rivnam.akash.contactspro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Toolbar tool_bar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tool_bar);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView =(SearchView) searchItem.getActionView();

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
                    displaySelectedScreen(0);
                    return true;
                case R.id.tab_phoneBook:
                    displaySelectedScreen(0);
                    return true;
            }
            return false;
        }
    };



    public void displaySelectedScreen(int item) {
        Fragment fragment = null;
        /*switch(item)
        {
            case 0:
                fragment=new tabbed();
                break;
            case 1:
                fragment=new tabbed();
                break;
            case 2:
                fragment=new tabbed();
                break;
        }*/
        if (fragment!=null) {
            FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            try{ft.commit();}
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
