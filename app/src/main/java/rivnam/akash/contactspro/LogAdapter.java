package rivnam.akash.contactspro;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
            convertView = inflater.inflate(R.layout.phonebook_contact_view, null);


        TextView p1 = (TextView) convertView.findViewById(R.id.t1);
        TextView p2 = (TextView) convertView.findViewById(R.id.t2);
        TextView p3 = (TextView) convertView.findViewById(R.id.t3);
        
        final LogPojo list = lpList.get(position);
        context = convertView.getContext();
        Date d = new Date(Long.parseLong(list.getCalldate()));
        DateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm:ss zzz");
        p1.setMaxLines(10);
        p1.setTextSize(18);
        p1.setText(list.getName()+"\n "+list.getNumber()+" \n"+String.valueOf(df.format(d)));
        String dir;
        p2.setText("    ");
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
        p3.setText(" "+list.getDuration()+"  ");
        p3.setTextSize(15);
        return convertView;
    }
}
