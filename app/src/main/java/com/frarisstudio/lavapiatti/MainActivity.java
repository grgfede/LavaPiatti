package com.frarisstudio.lavapiatti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.frarisstudio.lavapiatti.signup.SignUp;
import com.frarisstudio.lavapiatti.system.ForgotPassword;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    ProgressBar progressBarLogin;
    Button login;

    TextInputLayout emailL, passwordL;
    TextView TextViewEmail, TextViewPassword;

    TextView forgotPsw, signUp;


    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();


        autoLogin();
        progressBarLogin = findViewById(R.id.progressBarLogin);
        login = findViewById(R.id.btnRecoverPsw);
        TextViewEmail = (EditText) findViewById(R.id.txtEmailRecovery);
        TextViewPassword = (EditText) findViewById(R.id.txtPassword);

        emailL = findViewById(R.id.txtEmailLayout);
        passwordL = findViewById(R.id.txtPasswordLayout);

        forgotPsw = (TextView)findViewById(R.id.txtForgotPassword);
        signUp = (TextView) findViewById(R.id.txtSignUp);

        signUp.setOnClickListener(v -> doSignUp());
        forgotPsw.setOnClickListener(v -> forgotPsw());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = TextViewEmail.getText().toString();
                password = TextViewPassword.getText().toString();
                if (!((email.isEmpty()) || (password.isEmpty()))) {
                    progressBarLogin.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                                                .edit()
                                                .putString(PREF_USERNAME, email)
                                                .putString(PREF_PASSWORD, password)
                                                .commit();
                                        Intent intent = new Intent(MainActivity.this, Home.class);
                                        intent.putExtra("USER", user);
                                        startActivity(intent);
                                    } else {
                                        progressBarLogin.setVisibility(View.GONE);
                                        Toast.makeText(MainActivity.this, getResources().getString(R.string.error_login), Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                } else if (email.isEmpty()) {
                    progressBarLogin.setVisibility(View.GONE);
                    AndroidDeviceVibrate();  // Android Device Vibrate
                    emailL.startAnimation(VibrateError());
                    emailL.setError(getResources().getString(R.string.error_email_missing));
                } else if (password.isEmpty()) {
                    progressBarLogin.setVisibility(View.GONE);
                    AndroidDeviceVibrate();  // Android Device Vibrate
                    passwordL.startAnimation(VibrateError());
                    passwordL.setError(getResources().getString(R.string.error_password_missing));
                }
            }
        });


        TextViewEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    emailL.setError(null);
            }
        });

        TextViewPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                passwordL.setError(null);
            }
        });
        passwordL.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                passwordL.setError(null);
            }
        });
    }

    private void autoLogin() {
        SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String username = pref.getString(PREF_USERNAME, null);
        String password = pref.getString(PREF_PASSWORD, null);

        if (!(username == null || password == null)) {
            mAuth = FirebaseAuth.getInstance();
            String uid = mAuth.getUid();
            Intent intent = new Intent(MainActivity.this, Home.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        }
    }


    private void AndroidDeviceVibrate() { // Android Device Vibration
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); // Vibrate for 500 milliseconds only
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(500); // deprecated in API 26
        }
    }


    public TranslateAnimation VibrateError() { // Edit text vibrate Animation
        TranslateAnimation vibrate = new TranslateAnimation(0, 10, 0, 0);
        vibrate.setDuration(600);
        vibrate.setInterpolator(new CycleInterpolator(8));
        return vibrate;
    }


    private void forgotPsw(){
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }

    private void doSignUp() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
}


