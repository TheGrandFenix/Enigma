package com.fenix.enigma.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fenix.enigma.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ChatActivity extends Activity {
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    @Override
    protected void onStart() {
        super.onStart();

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        EditText messageInput = (EditText) findViewById(R.id.inputMessage);
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

        //Test user preview [REMOVE]
        TextView userPreview = (TextView) findViewById(R.id.userPreview);
        if (user != null) userPreview.setText(user.getEmail());
    }

    public void sendMessage(View view) {
        Toast.makeText(ChatActivity.this, "Stop, there is no chat.",
                Toast.LENGTH_SHORT).show();
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        recreate();
    }
}
