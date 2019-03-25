package rivnam.akash.contactspro;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

/**
 * Created by Akash on 13-03-2019.
 */

public class DialpadAdapter extends BaseAdapter {

    private Activity act;
    private LayoutInflater inflater;
    private List<PhoneBookPojo> pbList;
    private Context context;

    public DialpadAdapter(Activity act, List<PhoneBookPojo> pbList) {
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
            convertView = inflater.inflate(R.layout.number_search_contact_view, null);

        RelativeLayout relativeLayout=(RelativeLayout)convertView.findViewById(R.id.relLayout);
        TextView p1 = (TextView) convertView.findViewById(R.id.t1);
        TextView p2 = (TextView) convertView.findViewById(R.id.t2);
        AvatarView av= (AvatarView)convertView.findViewById(R.id.avatar);

        final PhoneBookPojo pb = pbList.get(position);
        context = convertView.getContext();
        IImageLoader imageLoader;
        imageLoader = new PicassoLoader();
        imageLoader.loadImage(av, null, pb.getName(),60);
        p1.setText(pb.getName());
        p2.setText(pb.getNumber());

        relativeLayout.setOnClickListener(new ListItemClickListener(position, pb));

        return convertView;
    }

    private class ListItemClickListener implements View.OnClickListener {
        int position;
        PhoneBookPojo list;

        public ListItemClickListener(int position, PhoneBookPojo list) {
            this.position = position;
            this.list = list;
        }

        @Override
        public void onClick(View v) {
            if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + list.getNumber().trim()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        }
    }
}
