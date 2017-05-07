package com.fenix.enigma.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fenix.enigma.Message;
import com.fenix.enigma.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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
        dataref = database.getReference().child("latestMessage");
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

        ValueEventListener newMessageListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Message latestMessage = dataSnapshot.getValue(Message.class);
                if (latestMessage != null && latestMessage.name != null && latestMessage.message != null) {
                    addMessage(latestMessage.name, latestMessage.message);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        dataref.addValueEventListener(newMessageListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void sendMessage(View view) {
        Message messageToSend = new Message(user.getDisplayName(), messageInput.getText().toString());
        dataref.setValue(messageToSend);
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
        usernameText.setTextColor(ContextCompat.getColor(this, R.color.enigma_purple));
        messageText.setText(message);

        messageLayout.setOrientation(LinearLayout.HORIZONTAL);
        messageLayout.addView(usernameText);
        messageLayout.addView(messageText);
        messageLayout.setPadding(0,5,0,5);

        messageList.addView(messageLayout);
        scrollToBottom();
    }

    private void scrollToBottom() {
        messageScroll.postDelayed(new Runnable() {
            @Override
            public void run() {
                messageScroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100);
    }

}
