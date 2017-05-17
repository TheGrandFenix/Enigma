package com.fenix.enigma.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.fenix.enigma.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import static android.os.SystemClock.sleep;


public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.activity_start);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        sleep(75);
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (user.getDisplayName() == null) {
            startActivity(new Intent(this, UserdataActivity.class));
        } else {
            startActivity(new Intent(this, ChatActivity.class));
            finish();
        }
    }
}
