package rivnam.akash.contactspro;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

/**
 * Created by Akash on 28-02-2019.
 */

public class LogAdapter extends BaseAdapter {

    private Activity act;
    private LayoutInflater inflater;
    private List<LogPojo> lpList;
    private Context context;

    public LogAdapter(Activity act, List<LogPojo> lpList) {
        this.act = act;
        this.lpList = lpList;
    }

    @Override
    public int getCount() {
        return lpList.size();
    }

    @Override
    public Object getItem(int position) {
        return lpList.get(position);
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
            convertView = inflater.inflate(R.layout.logs_contact_view, null);


        TextView p1 = (TextView) convertView.findViewById(R.id.t1);
        TextView p2 = (TextView) convertView.findViewById(R.id.t2);
        TextView p3 = (TextView) convertView.findViewById(R.id.t3);
        TextView p4 = (TextView) convertView.findViewById(R.id.t4);
        TextView p5 = (TextView) convertView.findViewById(R.id.t5);
        AvatarView av= (AvatarView)convertView.findViewById(R.id.avatar);

        final LogPojo list = lpList.get(position);
        IImageLoader imageLoader;
        imageLoader = new PicassoLoader();
        if(list.getName().trim().length()>0)
        {
            imageLoader.loadImage(av, null, list.getName(),60);
        }
        else
        {
            imageLoader.loadImage(av, null, "",60);
        }
        context = convertView.getContext();
        Date d = new Date(Long.parseLong(list.getCalldate()));
        DateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a zzz");
        switch (Integer.parseInt(list.getType())) {
            case 2:
                p2.setBackgroundResource(R.drawable.ic_call_made_black_24dp);
                break;

            case 1:
                p2.setBackgroundResource(R.drawable.ic_call_received_black_24dp);
                break;

            case 3:
                p2.setBackgroundResource(R.drawable.ic_call_missed_black_24dp);
                break;

            case 5:
                p2.setBackgroundResource(R.drawable.ic_clear_black_24dp);
                break;
        }
        if(list.getName().trim().length()>0)
        {
            p1.setText(list.getName());
        }
        else
        {
            p1.setText(list.getNumber());
        }
        p3.setText(String.valueOf(df.format(d)));
        p1.setOnClickListener(new ListItemClickListener1(position,list));
        p5.setOnClickListener(new ListItemClickListener2(position,list,Constants.simNumber));

        return convertView;
    }
    private class ListItemClickListener1 implements View.OnClickListener {
        int position;
        LogPojo list;

        public ListItemClickListener1(int position, LogPojo list) {
            this.position = position;
            this.list = list;
        }

        @Override
        public void onClick(View v) {
            final Dialog d = new Dialog(v.getRootView().getContext());
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.logs_entry_detail);
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView t1 = (TextView) d.findViewById(R.id.t1);
            TextView t2 = (TextView) d.findViewById(R.id.t2);
            t1.setText(list.getNumber());
            int duration=Integer.parseInt(list.getDuration());
            String showDuration="";
            if(duration/3600>0)
            {
                showDuration=showDuration+String.valueOf(duration/3600)+"hr ";
                duration=duration%3600;
            }
            if(duration/60>0)
            {
                showDuration=showDuration+String.valueOf(duration/60)+"min ";
                duration=duration%60;
            }
            showDuration=showDuration+String.valueOf(duration)+"sec ";
            t2.setText("Duration: "+showDuration);
            d.show();
        }
    }

    private class ListItemClickListener2 implements View.OnClickListener {
        int position;
        LogPojo list;
        int sim = 3;

        public ListItemClickListener2(int position, LogPojo list, int sim) {
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
}
