package rivnam.akash.contactspro;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

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
            convertView = inflater.inflate(R.layout.pro_view_format, null);


        TextView p1 = (TextView) convertView.findViewById(R.id.t1);
        TextView p2 = (TextView) convertView.findViewById(R.id.t2);
        TextView p3 = (TextView) convertView.findViewById(R.id.t3);

        final PhoneBookPojo list = pbList.get(position);
        context = convertView.getContext();
        p1.setText(list.getName());

        return convertView;
    }
}
