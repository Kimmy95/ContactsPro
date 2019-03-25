package rivnam.akash.contactspro;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Akash on 27-02-2019.
 */

public class FavouriteAdapter extends BaseAdapter {
    private Activity act;
    private LayoutInflater inflater;
    List<Pair<FavoritePojo,FavoritePojo>> fpList;
    private Context context;

    public FavouriteAdapter(Activity act, List<Pair<FavoritePojo,FavoritePojo>> fpList) {
        this.act = act;
        this.fpList = fpList;
    }

    @Override
    public int getCount() {
        return fpList.size();
    }

    @Override
    public Object getItem(int position) {
        return fpList.get(position);
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
            convertView = inflater.inflate(R.layout.favorite_contact_view, null);


        CardView cv_left=(CardView)convertView.findViewById(R.id.card_left);
        CardView cv_right=(CardView)convertView.findViewById(R.id.card_right);
        AvatarView av_left=(AvatarView)convertView.findViewById(R.id.favorite_left);
        AvatarView av_right=(AvatarView)convertView.findViewById(R.id.favorite_right);
        TextView name_left=(TextView)convertView.findViewById(R.id.text_left);
        TextView name_right=(TextView)convertView.findViewById(R.id.text_right);

        Pair<FavoritePojo,FavoritePojo> fpPair=fpList.get(position);
        FavoritePojo fp1=fpPair.first;
        FavoritePojo fp2=fpPair.second;
        IImageLoader imageLoader;
        imageLoader = new PicassoLoader();
        name_left.setText(fp1.getName());
        imageLoader.loadImage(av_left, null, fp1.getName(),60);
        if(fp2.getName().length()>0) {
            name_right.setText(fp2.getName());
            imageLoader.loadImage(av_right, null, fp2.getName(),60);
        }
        else
        {
            cv_right.setVisibility(View.GONE);
        }

        cv_left.setOnClickListener(new CardViewClickListener(fp1.getNumber(),Constants.simNumber));
        cv_right.setOnClickListener(new CardViewClickListener(fp2.getNumber(),Constants.simNumber));

        return convertView;
    }
    private class CardViewClickListener implements View.OnClickListener {
        String number="";
        int sim = 3;

        public CardViewClickListener(String number, int sim) {
            this.number = number;
            this.sim = sim;
        }

        @Override
        public void onClick(View v) {
            this.sim=Constants.simNumber;
            if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                if(sim==3)
                {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number.trim()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
                else {
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
                        String simSlotName[] = {"extra_asus_dial_use_dualsim", "com.android.phone.extra.slot", "slot", "simslot",
                                "sim_slot", "subscription", "Subscription", "phone", "com.android.phone.DialingMode", "simSlot", "slot_id",
                                "simId", "simnum", "phone_type", "slotId", "slotIdx", "android."};
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number.trim()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("com.android.phone.force.slot", true);
                        intent.putExtra("Cdma_Supp", true);
                        for (String s : simSlotName)
                            intent.putExtra(s, sim);
                        v.getContext().startActivity(intent);
                    } else {
                        TelecomManager telecomManager = (TelecomManager) v.getContext().getSystemService(Context.TELECOM_SERVICE);
                        List<PhoneAccountHandle> phnacchandle = telecomManager.getCallCapablePhoneAccounts();
                        Uri uri = Uri.fromParts("tel", number.trim(), null);
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
