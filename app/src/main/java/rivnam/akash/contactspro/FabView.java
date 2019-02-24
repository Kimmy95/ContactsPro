package rivnam.akash.contactspro;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Akash on 21-02-2019.
 */

public class FabView extends LinearLayout{

    private TextView textView;
    private FloatingActionButton fab;
    private CardView cardView;
    public FabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams layoutParams=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=inflater.inflate(R.layout.phonebook_subfab_look, this, true);
        v.setLayoutParams(layoutParams);
        cardView=(CardView)v.findViewById(R.id.card_view_fab);
        textView=(TextView)v.findViewById(R.id.text_sub_fab);
        fab=(FloatingActionButton)v.findViewById(R.id.fab_sub);
        fab.setClickable(true);
    }
    public FabView(Context context) {
        this(context, null);
    }
    public void setText(String s)
    {
        textView.setText(s);
    }
    public void setIcon(@DrawableRes int id)
    {
        fab.setImageDrawable(ContextCompat.getDrawable(getContext(), id));
    }

}
