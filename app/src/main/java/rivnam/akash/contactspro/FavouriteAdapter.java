package rivnam.akash.contactspro;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
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

        return convertView;
    }
}
