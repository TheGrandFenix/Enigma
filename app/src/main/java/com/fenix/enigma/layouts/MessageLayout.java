package com.fenix.enigma.layouts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fenix.enigma.R;

//Message view object for adding messages to the layout - based on LinearLayout
public class MessageLayout extends LinearLayout{
    private String name;

    public MessageLayout(Context context, String name, String message) {
        //Super constructor with context creates LinearLayout instance
        super(context);

        //Save name instance
        this.name = name;

        //Create TextViews for username and message data
        TextView usernameText = new TextView(context);
        TextView messageText = new TextView(context);

        //Adjust TextView settings
        usernameText.setText(name);
        usernameText.setTypeface(null, Typeface.BOLD);
        usernameText.setTextSize(14);
        usernameText.setTextColor(ContextCompat.getColor(context, R.color.enigma_purple));
        messageText.setText(message);
        messageText.setTextSize(16);
        messageText.setTextColor(Color.BLACK);

        //Add views to the parent layout and edit layout settings
        int paddingValue = (int)(5*context.getResources().getDisplayMetrics().density);
        setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
        setGravity(FOCUS_LEFT);
        setOrientation(LinearLayout.VERTICAL);
        addView(usernameText);
        addView(messageText);
    }

    public String getName() {
        return name;
    }

    public void addSameUserMessage(Context context, String message) {
        TextView messageText = new TextView(context);
        messageText.setText(message);
        messageText.setTextSize(16);
        messageText.setTextColor(Color.BLACK);
        addView(messageText);
    }
}
