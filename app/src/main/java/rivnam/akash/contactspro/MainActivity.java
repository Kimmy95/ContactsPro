package rivnam.akash.contactspro;

import android.Manifest;
import android.app.Dialog;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_SMS;

public class MainActivity extends AppCompatActivity {

    Intent databasePopulateService,logDatabasePopulateService;
    View shadowView;
    FloatingActionMenu actionMenu;
    FloatingActionButton actionButton;
    SearchView searchView;
    private static final int DPS_JOB_ID = 1000;
    private static final int LDPS_JOB_ID = 1001;
    static int number_of_request=0;
    public static SharedPreferences shpref;
    public final static String ss = "fname";
    public final static String k1 = "a";
    String number="";
    List<PhoneBookPojo> pbList;
    DialpadAdapter dialpadAdapter;
    AppDatabaseHandler db;
    LogDatabaseHandler supportDb;
    Field mBehaviorField;
    BottomSheetBehavior behavior;

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
        shpref = MainActivity.this.getSharedPreferences(ss, Context.MODE_PRIVATE);
        Constants.simNumber=Integer.parseInt(shpref.getString(k1, String.valueOf(3)));

        final ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.ic_settings_black_24dp);
        actionButton = new FloatingActionButton.Builder(this).setContentView(icon).build();
        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(200, 200);
        layoutParams.setMargins(0,0,30,190);
        actionButton.setPosition(4, layoutParams);
        final FabView add_contact = new FabView(this);
        add_contact.setIcon(R.drawable.ic_person_add_black_24dp);
        add_contact.setText("Add Contact");
        final FabView ask_sim = new FabView(this);
        ask_sim.setIcon(R.drawable.ic_sim_card_black_24dp);
        switch (Constants.simNumber){
            case 0:
                ask_sim.setText("Sim 1");
                break;
            case 1:
                ask_sim.setText("Sim 2");
                break;
            case 3:
                ask_sim.setText("Ask Sim");
                break;
            default:
                ask_sim.setText("Ask Sim");
                break;
        }
        final FrameLayout.LayoutParams layoutParams2=new FrameLayout.LayoutParams(350, ViewGroup.LayoutParams.WRAP_CONTENT);
        ask_sim.setLayoutParams(layoutParams2);
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
                final Dialog d = new Dialog(v.getRootView().getContext());
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setCancelable(false);
                d.setContentView(R.layout.add_contact);
                d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                final TextView t1 = (TextView) d.findViewById(R.id.t1);
                TextView t2 = (TextView) d.findViewById(R.id.t2);
                TextView t3 = (TextView) d.findViewById(R.id.t3);
                TextView t4 = (TextView) d.findViewById(R.id.t4);
                TextView t5 = (TextView) d.findViewById(R.id.t5);
                final EditText e1 = (EditText) d.findViewById(R.id.e1);
                final EditText e2 = (EditText) d.findViewById(R.id.e2);
                final EditText e3 = (EditText) d.findViewById(R.id.e3);
                final EditText e4 = (EditText) d.findViewById(R.id.e4);
                final EditText e5 = (EditText) d.findViewById(R.id.e5);
                Button cancel=(Button)d.findViewById(R.id.cancel);
                final Button done=(Button)d.findViewById(R.id.done);
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name,number,type,email,address;
                        name=e1.getText().toString().trim();
                        number=e2.getText().toString().trim();
                        email=e3.getText().toString().trim();
                        type=e4.getText().toString().trim();
                        address=e5.getText().toString().trim();
                        AddContact c=new AddContact(v.getContext(),name,number);
                        c.Add();
                        AppDatabaseHandler appDatabaseHandler=new AppDatabaseHandler(MainActivity.this);
                        appDatabaseHandler.addContact(new PhoneBookPojo(name,number,email,type,"YES",address));
                        Toast.makeText(MainActivity.this,"Added contact "+name,Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                d.show();
            }
        });
        ask_sim.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constants.simNumber==0)
                {
                    Constants.simNumber = 1;
                    SharedPreferences.Editor editor=shpref.edit();
                    editor.putString(k1,String.valueOf(Constants.simNumber));
                    editor.commit();
                    ask_sim.setText("Sim 2");
                    Toast.makeText(MainActivity.this, "Sim 2 selected", Toast.LENGTH_SHORT).show();
                }
                else if(Constants.simNumber==1)
                {
                    Constants.simNumber = 3;
                    SharedPreferences.Editor editor=shpref.edit();
                    editor.putString(k1,String.valueOf(Constants.simNumber));
                    editor.commit();
                    ask_sim.setText("Ask Sim");
                    Toast.makeText(MainActivity.this, "Ask Sim", Toast.LENGTH_SHORT).show();
                }
                else if(Constants.simNumber==3) {
                    Constants.simNumber = 0;
                    SharedPreferences.Editor editor = shpref.edit();
                    editor.putString(k1, String.valueOf(Constants.simNumber));
                    editor.commit();
                    ask_sim.setText("Sim 1");
                    Toast.makeText(MainActivity.this, "Sim 1 selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        dialpad.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog dialog=new BottomSheetDialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialpad_view);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                try {
                    mBehaviorField = dialog.getClass().getDeclaredField("mBehavior");
                    mBehaviorField.setAccessible(true);
                    behavior = (BottomSheetBehavior) mBehaviorField.get(dialog);
                    behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                        @Override
                        public void onStateChanged(@NonNull View bottomSheet, int newState) {
                            if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }
                        }

                        @Override
                        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        }
                    });
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                final TextView searchArea=(TextView)dialog.findViewById(R.id.search_area);;
                final ListView lView=(ListView)dialog.findViewById(R.id.search_list);
                final TextView numbox=(TextView)dialog.findViewById(R.id.numberbox);
                final TextView bspace=(TextView)dialog.findViewById(R.id.backspace);
                final android.support.design.widget.FloatingActionButton makeCall=(android.support.design.widget.FloatingActionButton)dialog.findViewById(R.id.call);
                final TextView k1=(TextView)dialog.findViewById(R.id.key1);
                final TextView k2=(TextView)dialog.findViewById(R.id.key2);
                final TextView k3=(TextView)dialog.findViewById(R.id.key3);
                final TextView k4=(TextView)dialog.findViewById(R.id.key4);
                final TextView k5=(TextView)dialog.findViewById(R.id.key5);
                final TextView k6=(TextView)dialog.findViewById(R.id.key6);
                final TextView k7=(TextView)dialog.findViewById(R.id.key7);
                final TextView k8=(TextView)dialog.findViewById(R.id.key8);
                final TextView k9=(TextView)dialog.findViewById(R.id.key9);
                final TextView k10=(TextView)dialog.findViewById(R.id.key10);
                final TextView k0=(TextView)dialog.findViewById(R.id.key0);
                final TextView k11=(TextView)dialog.findViewById(R.id.key11);
                number="";

                k1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number=numbox.getText().toString();
                        number=number+"1";
                        numbox.setText(number);
                    }
                });
                k2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number=numbox.getText().toString();
                        number=number+"2";
                        numbox.setText(number);
                    }
                });
                k3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number=numbox.getText().toString();
                        number=number+"3";
                        numbox.setText(number);
                    }
                });
                k1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number=numbox.getText().toString();
                        number=number+"1";
                        numbox.setText(number);
                    }
                });
                k4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number=numbox.getText().toString();
                        number=number+"4";
                        numbox.setText(number);
                    }
                });
                k5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number=numbox.getText().toString();
                        number=number+"5";
                        numbox.setText(number);
                    }
                });
                k6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number=numbox.getText().toString();
                        number=number+"6";
                        numbox.setText(number);
                    }
                });
                k7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number=numbox.getText().toString();
                        number=number+"7";
                        numbox.setText(number);
                    }
                });
                k8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number=numbox.getText().toString();
                        number=number+"8";
                        numbox.setText(number);
                    }
                });
                k9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number=numbox.getText().toString();
                        number=number+"9";
                        numbox.setText(number);
                    }
                });
                k10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number=numbox.getText().toString();
                        number=number+"*";
                        numbox.setText(number);
                    }
                });
                k0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number=numbox.getText().toString();
                        number=number+"0";
                        numbox.setText(number);
                    }
                });
                k11.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number=numbox.getText().toString();
                        number=number+"#";
                        numbox.setText(number);
                    }
                });
                bspace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number=numbox.getText().toString();
                        if(number.trim().length()>0)
                        {
                            number=number.substring(0,number.length()-1);
                        }
                        numbox.setText(number);
                    }
                });
                bspace.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        number="";
                        numbox.setText(number);
                        return false;
                    }
                });
                makeCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number=numbox.getText().toString();
                        if(number.trim().length()==0)
                        {
                            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                                Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, android.provider.CallLog.Calls.DATE + " DESC LIMIT 1;");
                                if (cursor.moveToLast()) {
                                        number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                                }
                            }
                            numbox.setText(number);
                        }
                        else
                        {
                            if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number.trim()));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                v.getContext().startActivity(intent);
                            }
                        }
                    }
                });

                db=new AppDatabaseHandler(MainActivity.this);
                supportDb=new LogDatabaseHandler(MainActivity.this);
                Constants.numQuery="";
                if(Constants.numQuery.length()==0) {
                    searchArea.setText("SUGGESTED");
                    pbList = supportDb.showLastTenLog();
                    dialpadAdapter = new DialpadAdapter(MainActivity.this, pbList);
                    lView.setAdapter(dialpadAdapter);
                }
                numbox.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        if(numbox!=null) {
                            Constants.numQuery = numbox.getText().toString();
                        }
                        if(Constants.numQuery.length()==0)
                        {
                            pbList=supportDb.showLastTenLog();
                        }
                        else{
                            pbList=db.showMatchedContactByNumber(Constants.numQuery);
                        }
                        if(pbList.size()==0)
                        {
                            lView.setVisibility(View.GONE);
                            searchArea.setText("Call "+Constants.numQuery);
                            searchArea.setTypeface(null, Typeface.NORMAL);
                            searchArea.setPadding(50,100,0,10);
                            searchArea.setTextColor(Color.BLUE);
                            searchArea.setClickable(true);
                            searchArea.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constants.numQuery.trim()));
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        v.getContext().startActivity(intent);
                                    }
                                }
                            });
                        }
                        else {
                            searchArea.setText("SUGGESTED");
                            searchArea.setTypeface(null, Typeface.NORMAL);
                            searchArea.setPadding(50,10,0,5);
                            searchArea.setTextColor(Color.parseColor("#252525"));
                            searchArea.setClickable(false);
                            lView.setVisibility(View.VISIBLE);
                            dialpadAdapter = new DialpadAdapter(MainActivity.this, pbList);
                            lView.setAdapter(dialpadAdapter);
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(numbox!=null) {
                            Constants.numQuery = numbox.getText().toString();
                        }
                        if(Constants.numQuery.length()==0)
                        {
                            pbList=supportDb.showLastTenLog();
                        }
                        else{
                            pbList=db.showMatchedContactByNumber(Constants.numQuery);
                        }
                        if(pbList.size()==0)
                        {
                            lView.setVisibility(View.GONE);
                            searchArea.setText("Call "+Constants.numQuery);
                            searchArea.setTypeface(null, Typeface.NORMAL);
                            searchArea.setPadding(50,100,0,10);
                            searchArea.setTextColor(Color.BLUE);
                            searchArea.setClickable(true);
                            searchArea.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constants.numQuery.trim()));
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        v.getContext().startActivity(intent);
                                    }
                                }
                            });
                        }
                        else {
                            searchArea.setText("SUGGESTED");
                            searchArea.setTypeface(null, Typeface.NORMAL);
                            searchArea.setPadding(50,10,0,5);
                            searchArea.setTextColor(Color.parseColor("#252525"));
                            searchArea.setClickable(false);
                            lView.setVisibility(View.VISIBLE);
                            dialpadAdapter = new DialpadAdapter(MainActivity.this, pbList);
                            lView.setAdapter(dialpadAdapter);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(numbox!=null) {
                            Constants.numQuery = numbox.getText().toString();
                        }
                        if(Constants.numQuery.length()==0)
                        {
                            pbList=supportDb.showLastTenLog();
                        }
                        else{
                            pbList=db.showMatchedContactByNumber(Constants.numQuery);
                        }
                        if(pbList.size()==0)
                        {
                            lView.setVisibility(View.GONE);
                            searchArea.setText("Call "+Constants.numQuery);
                            searchArea.setTypeface(null, Typeface.NORMAL);
                            searchArea.setPadding(50,100,0,10);
                            searchArea.setTextColor(Color.BLUE);
                            searchArea.setClickable(true);
                            searchArea.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constants.numQuery.trim()));
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        v.getContext().startActivity(intent);
                                    }
                                }
                            });
                        }
                        else {
                            searchArea.setText("SUGGESTED");
                            searchArea.setTypeface(null, Typeface.NORMAL);
                            searchArea.setPadding(50,10,0,5);
                            searchArea.setTextColor(Color.parseColor("#252525"));
                            searchArea.setClickable(false);
                            lView.setVisibility(View.VISIBLE);
                            dialpadAdapter = new DialpadAdapter(MainActivity.this, pbList);
                            lView.setAdapter(dialpadAdapter);
                        }
                    }
                });
                dialog.show();
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

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
                actionButton.setVisibility(View.GONE);
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
                actionButton.setVisibility(View.VISIBLE);
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
                ProgressDialog progressDialog;
                progressDialog = ProgressDialog.show(MainActivity.this,"UNBLOCKING ALL CONTACTS...","Please wait...",false,false);
                AppDatabaseHandler database=new AppDatabaseHandler(MainActivity.this);
                List<PhoneBookPojo> pbList=new ArrayList<PhoneBookPojo>();
                pbList=database.showAntiWhatsappContact();
                for(int i=0;i<pbList.size();i++)
                {

                    AddContact contact=new AddContact(MainActivity.this,pbList.get(i).getName(),pbList.get(i).getNumber());
                    contact.Add();
                    pbList.get(i).setStatus("YES");
                    database.updateContact(pbList.get(i),pbList.get(i).getName(),pbList.get(i).getNumber());
                }
                displaySelectedScreen(2);
                progressDialog.dismiss();
                return true;
            case R.id.xxxx2:
                ProgressDialog progressDialog2;
                progressDialog2 = ProgressDialog.show(MainActivity.this,"BLOCKING ALL CONTACTS...","Please wait...",false,false);
                AppDatabaseHandler database2=new AppDatabaseHandler(MainActivity.this);
                List<PhoneBookPojo> pbList2=new ArrayList<PhoneBookPojo>();
                pbList2=database2.showWhatsappContact();
                for(int i=0;i<pbList2.size();i++)
                {

                    deleteContact(MainActivity.this,pbList2.get(i).getNumber(),pbList2.get(i).getName(),"");
                    pbList2.get(i).setStatus("NO");
                    database2.updateContact(pbList2.get(i),pbList2.get(i).getName(),pbList2.get(i).getNumber());
                }
                displaySelectedScreen(2);
                progressDialog2.dismiss();
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
                    actionButton.setVisibility(View.VISIBLE);
                    searchView.onActionViewCollapsed();
                    Constants.frameBeforeSearch=0;
                    displaySelectedScreen(0);
                    return true;
                case R.id.tab_logs:
                    actionButton.setVisibility(View.VISIBLE);
                    searchView.onActionViewCollapsed();
                    Constants.frameBeforeSearch=1;
                    displaySelectedScreen(1);
                    return true;
                case R.id.tab_phoneBook:
                    actionButton.setVisibility(View.VISIBLE);
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
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.GET_ACCOUNTS};
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
    public static boolean deleteContact(Context ctx, String phone, String name, String operation) {
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        Cursor cur = ctx.getContentResolver().query(contactUri, null, null, null, null);
        try {
            if (cur.moveToFirst()) {
                do {
                    if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(name)) {
                        String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                        ctx.getContentResolver().delete(uri, null, null);
                        return true;
                    }
                } while (cur.moveToNext());
            }
            if(operation.length()>0)
            Toast.makeText(ctx,name+" "+operation+"!!",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ctx, "Permission not granted!! Grant permission!!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
