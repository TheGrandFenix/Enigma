package com.fenix.enigma.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fenix.enigma.EnigmaNotification;
import com.fenix.enigma.Message;
import com.fenix.enigma.R;
import com.fenix.enigma.layouts.MessageLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class ChatActivity extends Activity {
    //Firebase variables
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference dataref;

    //View instance variables
    LinearLayout messageList;
    EditText messageInput;
    ScrollView messageScroll;

    //Custom variables
    Context thisContext;
    boolean foreground = true;
    MessageLayout lastMessage;

    //Override onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Parent constructor
        super.onCreate(savedInstanceState);

        //Reference to this
        thisContext = this;

        //Get Firebase user and database details
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dataref = database.getReference().child("messages");
        dataref.keepSynced(true);

        //Set layout file and get view instances
        setContentView(R.layout.activity_chat);
        messageScroll = (ScrollView) findViewById(R.id.messageScroll);
        messageList = (LinearLayout) findViewById(R.id.messageList);
        messageInput = (EditText) findViewById(R.id.inputMessage);

        //Set listener to message input field
        messageInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendMessage(view);
                    return true;
                }
                return false;
            }
        });

        //Listen for activation of the EditText view for automatic scrolling
        messageInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollToBottom();
                return false;
            }
        });

        //Listener for database changes
        ChildEventListener messageEventListener = new ChildEventListener() {

            //Runs once per message + for every new message
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                Message latestMessage = dataSnapshot.getValue(Message.class);
                addMessage(latestMessage.name, latestMessage.message);
                if(!foreground) EnigmaNotification.sendNotification(thisContext);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };

        //Attach database event listener
        dataref.addChildEventListener(messageEventListener);

        //???
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    //Override onStart - future uses
    @Override
    protected void onStart() {
        super.onStart();
    }

    //Send new message to the database
    public void sendMessage(View view) {
        Message messageToSend = new Message(user.getDisplayName(), messageInput.getText().toString());
        dataref.push().setValue(messageToSend);
        messageInput.setText("");
    }

    //Inactive - logout from account
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        recreate();
    }

    //Add new message to the layout
    public void addMessage(String username, String message) {
        if(lastMessage!=null && lastMessage.getName().equals(username)) {
            lastMessage.addSameUserMessage(this, message);
            scrollToBottom();
        } else {
            lastMessage = new MessageLayout(this, username, message);
            messageList.addView(lastMessage);
            scrollToBottom();
        }
    }

    //Scroll to bottom of message ScrollView
    private void scrollToBottom() {
        messageScroll.postDelayed(new Runnable() {
            @Override
            public void run() {
                messageScroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 200);
    }

    //Override back button closing Activity to minimize app
    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    //Keep track when app is returned to foreground
    @Override
    public void onResume() {
        super.onResume();
        foreground = true;
        scrollToBottom();
    }

    //Keep track when app is put into background
    @Override
    public void onPause() {
        super.onPause();
        foreground = false;
    }
}
