package rivnam.akash.contactspro;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

/**
 * Created by Akash on 21-02-2019.
 */

public class PhoneBookAdapter extends BaseAdapter {

    private Activity act;
    private LayoutInflater inflater;
    private List<PhoneBookPojo> pbList;
    private Context context;

    public PhoneBookAdapter(Activity act, List<PhoneBookPojo> pbList) {
        this.act = act;
        this.pbList = pbList;
    }

    @Override
    public int getCount() {
        return pbList.size();
    }

    @Override
    public Object getItem(int position) {
        return pbList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.phonebook_contact_view, null);


        TextView p1 = (TextView) convertView.findViewById(R.id.t1);
        TextView p2 = (TextView) convertView.findViewById(R.id.t2);
        AvatarView av= (AvatarView)convertView.findViewById(R.id.avatar);

        final PhoneBookPojo pb = pbList.get(position);
        context = convertView.getContext();
        IImageLoader imageLoader;
        imageLoader = new PicassoLoader();
        imageLoader.loadImage(av, null, pb.getName(),60);
        p1.setText(pb.getName());
        p1.setOnClickListener(new ListItemClickListener(position, pb));
        p2.setOnClickListener(new ListItemClickListener2(position, pb,Constants.simNumber));

        return convertView;
    }

    private class ListItemClickListener implements View.OnClickListener {

        int position;
        PhoneBookPojo incompleteList;

        public ListItemClickListener(int position, PhoneBookPojo list) {
            this.position=position;
            this.incompleteList=list;
        }

        @Override
        public void onClick(View v) {
            AppDatabaseHandler appDatabaseHandler=new AppDatabaseHandler(context);
            final Dialog d = new Dialog(v.getRootView().getContext());
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setCancelable(false);
            final PhoneBookPojo list = appDatabaseHandler.fetchContactDetails(incompleteList.getName().trim(),incompleteList.getNumber().trim());
            d.setContentView(R.layout.contact_detailed_view);
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            final TextView t1 = (TextView) d.findViewById(R.id.t1);
            TextView t2 = (TextView) d.findViewById(R.id.t2);
            TextView t3 = (TextView) d.findViewById(R.id.t3);
            TextView t4 = (TextView) d.findViewById(R.id.t4);
            TextView t5 = (TextView) d.findViewById(R.id.t5);
            TextView t6 = (TextView) d.findViewById(R.id.t6);
            TextView call = (TextView) d.findViewById(R.id.call);
            TextView sms = (TextView) d.findViewById(R.id.sms);
            final TextView whatsapp = (TextView) d.findViewById(R.id.whatsapp);
            final EditText e2 = (EditText) d.findViewById(R.id.e2);
            final EditText e3 = (EditText) d.findViewById(R.id.e3);
            final EditText e4 = (EditText) d.findViewById(R.id.e4);
            final EditText e5 = (EditText) d.findViewById(R.id.e5);
            final EditText e6 = (EditText) d.findViewById(R.id.e6);
            AvatarView av=(AvatarView)d.findViewById(R.id.avatar);
            Button cancel=(Button)d.findViewById(R.id.cancel);
            final Button edit=(Button)d.findViewById(R.id.edit);
            IImageLoader imageLoader;
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(av, null, list.getName(),60);
            if(list.getStatus().trim().equals("NO"))
                whatsapp.setBackgroundResource(R.drawable.whatsapp_deny);
            t1.setText(list.getName());
            e2.setText(list.getNumber());
            e3.setText(list.getEmail());
            e4.setText(list.getType());
            if(list.getStatus().trim().equals("YES"))
                e5.setText("WHATSAPP");
            else
                e5.setText("NO WHATSAPP");
            e6.setText(list.getAddress());

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(edit.getText().toString().trim().equals("SAVE"))
                    {
                        edit.setText("EDIT");
                        String name,number,type,email,address;
                        name=t1.getText().toString().trim();
                        number=e2.getText().toString().trim();
                        email=e3.getText().toString().trim();
                        type=e4.getText().toString().trim();
                        address=e6.getText().toString().trim();
                        deleteContact(v.getContext(),list.getNumber().trim(),list.getName().trim(),"");
                        AddContact c=new AddContact(v.getContext(),name,number);
                        c.Add();
                        AppDatabaseHandler appDatabaseHandler=new AppDatabaseHandler(context);
                        appDatabaseHandler.updateContact(new PhoneBookPojo(name,number,email,type,list.getStatus().trim(),address),list.getName(),list.getNumber());
                        e2.setEnabled(false);
                        e3.setEnabled(false);
                        e4.setEnabled(false);
                        e6.setEnabled(false);
                        Toast.makeText(context,"Saved contact "+list.getName(),Toast.LENGTH_SHORT).show();
                    }
                    else {
                        edit.setText("SAVE");
                        e2.setEnabled(true);
                        e3.setEnabled(true);
                        e4.setEnabled(true);
                        e6.setEnabled(true);
                    }
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(edit.getText().toString().trim().equals("SAVE"))
                    {
                        edit.setText("EDIT");
                        e2.setEnabled(false);
                        e3.setEnabled(false);
                        e4.setEnabled(false);
                        e6.setEnabled(false);
                    }
                    else
                    {
                        d.dismiss();
                    }
                }
            });

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + list.getNumber().trim()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        v.getContext().startActivity(intent);
                        d.dismiss();
                    }
                }
            });

            sms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + list.getNumber().trim()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                    d.dismiss();
                }
            });

            whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppDatabaseHandler appDatabaseHandler=new AppDatabaseHandler(context);
                    if((list.getStatus()).trim().equals("YES"))
                    {
                        whatsapp.setBackgroundResource(R.drawable.whatsapp_deny);
                        e5.setText("NO WHATSAPP");
                        appDatabaseHandler.updateContact(new PhoneBookPojo(list.getName(),list.getNumber(),list.getEmail(),list.getType(),"NO",list.getAddress()),list.getName(),list.getNumber());
                        deleteContact(v.getContext(),list.getNumber().trim(),list.getName().trim(),"Blocked");
                        notifyDataSetChanged();
                    }
                    else
                    {
                        whatsapp.setBackgroundResource(R.drawable.whatsapp_access);
                        e5.setText("WHATSAPP");
                        appDatabaseHandler.updateContact(new PhoneBookPojo(list.getName(),list.getNumber(),list.getEmail(),list.getType(),"YES",list.getAddress()),list.getName(),list.getNumber());
                        AddContact c=new AddContact(v.getContext(),list.getName().trim(),list.getNumber().trim());
                        c.Add();
                        notifyDataSetChanged();
                    }
                }
            });
            d.show();
        }
    }

    private class ListItemClickListener2 implements View.OnClickListener {
        int position;
        PhoneBookPojo list;
        int sim = 3;

        public ListItemClickListener2(int position, PhoneBookPojo list, int sim) {
            this.position = position;
            this.list = list;
            this.sim = sim;
        }

        @Override
        public void onClick(View v) {
            this.sim=Constants.simNumber;
            if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                if(sim==3)
                {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + list.getNumber().trim()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
                else {
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
                        String simSlotName[] = {"extra_asus_dial_use_dualsim", "com.android.phone.extra.slot", "slot", "simslot",
                                "sim_slot", "subscription", "Subscription", "phone", "com.android.phone.DialingMode", "simSlot", "slot_id",
                                "simId", "simnum", "phone_type", "slotId", "slotIdx", "android."};
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + list.getNumber().trim()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("com.android.phone.force.slot", true);
                        intent.putExtra("Cdma_Supp", true);
                        for (String s : simSlotName)
                            intent.putExtra(s, sim);
                        v.getContext().startActivity(intent);
                    } else {
                        TelecomManager telecomManager = (TelecomManager) v.getContext().getSystemService(Context.TELECOM_SERVICE);
                        List<PhoneAccountHandle> phnacchandle = telecomManager.getCallCapablePhoneAccounts();
                        Uri uri = Uri.fromParts("tel", list.getNumber().trim(), null);
                        Bundle extras = new Bundle();
                        if (phnacchandle.size() > 1)
                            extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phnacchandle.get(sim));
                        else
                            extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phnacchandle.get(0));
                        telecomManager.placeCall(uri, extras);
                    }
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
            Toast.makeText(ctx,name+" "+operation+"!!",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ctx, "Permission not granted!! Grant permission!!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
