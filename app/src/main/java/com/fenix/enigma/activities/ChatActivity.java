package com.fenix.enigma.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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

import com.fenix.enigma.Message;
import com.fenix.enigma.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;
import static android.os.SystemClock.sleep;

public class ChatActivity extends Activity {
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference dataref;

    LinearLayout messageList;
    EditText messageInput;
    ScrollView messageScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dataref = database.getReference().child("messages");
        dataref.keepSynced(true);

        setContentView(R.layout.activity_chat);
        messageScroll = (ScrollView) findViewById(R.id.messageScroll);
        messageList = (LinearLayout) findViewById(R.id.messageList);

        messageInput = (EditText) findViewById(R.id.inputMessage);
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
        messageInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollToBottom();
                return false;
            }
        });

        //TEMP Disabled loading old messages
        //loadMessages();

        ChildEventListener messageEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                Message latestMessage = dataSnapshot.getValue(Message.class);
                addMessage(latestMessage.name, latestMessage.message);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        dataref.addChildEventListener(messageEventListener);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void sendMessage(View view) {
        Message messageToSend = new Message(user.getDisplayName(), messageInput.getText().toString());
        dataref.push().setValue(messageToSend);
        messageInput.setText("");
    }

    //Inactive
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        recreate();
    }

    public void addMessage(String username, String message) {
        username = username + ": ";

        LinearLayout messageLayout = new LinearLayout(this);
        TextView usernameText = new TextView(this);
        TextView messageText = new TextView(this);

        usernameText.setText(username);
        usernameText.setTextSize(20);
        usernameText.setTextColor(ContextCompat.getColor(this, R.color.enigma_purple));
        messageText.setText(message);
        messageText.setTextSize(20);
        messageText.setTextColor(Color.BLACK);

        messageLayout.setOrientation(LinearLayout.HORIZONTAL);
        messageLayout.addView(usernameText);
        messageLayout.addView(messageText);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) messageLayout.getLayoutParams();
        params.setMargins(0,10,0,10);

        messageList.addView(messageLayout);
        scrollToBottom();
    }

    private void scrollToBottom() {
        messageScroll.postDelayed(new Runnable() {
            @Override
            public void run() {
                messageScroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 200);
    }

    //TEMP Disabled loading old messages
    /*private void loadMessages() {
        dataref.limitToLast(100).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    addMessage(message.name, message.message);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }*/

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}
