package com.fenix.enigma.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

        //Test user preview [REMOVE]
        TextView userPreview = (TextView) findViewById(R.id.userPreview);
        if (user != null) userPreview.setText(user.getEmail());
    }

    public void sendMessage(View view) {

    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
    }
}
