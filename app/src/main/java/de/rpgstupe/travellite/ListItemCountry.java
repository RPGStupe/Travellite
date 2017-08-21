package de.rpgstupe.travellite;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListItemCountry extends LinearLayout {

    private TextView title;
    private String key;
    private LinearLayout background;
    private boolean selected = false;

    public ListItemCountry(Context context) {
        super(context);
        init(context);
    }

    public ListItemCountry(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ListItemCountry(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.list_item_country, this);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        title = (TextView) findViewById(R.id.tv_list_item_country);
        background = (LinearLayout) findViewById(R.id.list_item_country);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isEnabled()) {
                    setSelected(!selected);
                }
            }
        });
    }

    public void setTitleText(String text) {
        title.setText(text);
    }

    public void setSelected(boolean selected) {
        background.setBackgroundColor(selected ? getResources().getColor(R.color.colorPrimary) : Color.parseColor("#DBDCDE"));
        ((TextView)background.findViewById(R.id.tv_list_item_country)).setTextColor(selected ? Color.parseColor("#FFFFFF") : Color.parseColor("#97989A"));
        this.selected = selected;
    }

    public boolean isCountrySelected() {
        return this.selected;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
