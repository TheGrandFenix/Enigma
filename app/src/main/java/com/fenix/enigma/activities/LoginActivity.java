package com.fenix.enigma.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fenix.enigma.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import static android.content.ContentValues.TAG;


public class LoginActivity extends Activity {

    public FirebaseAuth enigmaAuth;
    private EditText emailField;
    private EditText passwordField;
    private EditText usernameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        enigmaAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        emailField = (EditText) findViewById(R.id.loginEmail);
        passwordField = (EditText) findViewById(R.id.loginPassword);
        usernameField = (EditText)findViewById(R.id.usernameInput);
    }

    public void login(View view) {
        startLoading();
        final String email = emailField.getText().toString();
        final String password = passwordField.getText().toString();
        enigmaAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            finish();
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            recreate();
                        }
                    }
                });
    }

    public void signup(View view) {
        findViewById(R.id.loginInputLayout).setVisibility(View.GONE);
        findViewById(R.id.signupInputLayout).setVisibility(View.VISIBLE);
    }

    public void finishSignup(View view) {
        startLoading();
        final String signup_email = emailField.getText().toString();
        final String signup_password = passwordField.getText().toString();
        final String signup_username = usernameField.getText().toString();
        enigmaAuth.createUserWithEmailAndPassword(signup_email, signup_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = enigmaAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(signup_username)
                                    .build();

                            if(user != null) user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }
                                        }
                                    });

                            finish();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            recreate();
                        }
                    }
                });
    }

    private void startLoading() {
        findViewById(R.id.loginInputLayout).setVisibility(View.GONE);
        findViewById(R.id.signupInputLayout).setVisibility(View.GONE);
        findViewById(R.id.loginLoading).setVisibility(View.VISIBLE);
    }
}
