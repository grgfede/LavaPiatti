package com.frarisstudio.lavapiatti.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.frarisstudio.lavapiatti.MainActivity;
import com.frarisstudio.lavapiatti.R;
import com.frarisstudio.lavapiatti.system.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;

public class SignUp extends AppCompatActivity {

    Button signUp;

    TextInputLayout nameL, surnameL, emailL, passwordL;
    TextView name, surname, email, password;

    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        progressBar = findViewById(R.id.progressBarSignUp);

        nameL = findViewById(R.id.NameLayoutSignUp);
        surnameL = findViewById(R.id.SurnameLayoutSignUp);
        emailL = findViewById(R.id.EmailLayoutSignUp);
        passwordL = findViewById(R.id.PasswordLayoutSignUp);

        name = (EditText) findViewById(R.id.txtNameSignUp);
        surname = (EditText) findViewById(R.id.txtSurnameSignUp);
        email = (EditText) findViewById(R.id.txtEmailSignUp);
        password = (EditText) findViewById(R.id.txtPasswordSignUp);

        signUp = findViewById(R.id.btnSignUp);
        signUp.setOnClickListener(v -> verifyFields());

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    nameL.setError(null);
            }
        });
        surname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    surnameL.setError(null);
            }
        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    emailL.setError(null);
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    passwordL.setError(null);
            }
        });
    }

    private void verifyFields() {
        progressBar.setVisibility(View.VISIBLE);
        String nameS, surnameS, emailS, pswS;


        boolean error = false;

        nameS = name.getText().toString();
        surnameS = surname.getText().toString();
        emailS = email.getText().toString();
        pswS = password.getText().toString();

        if (nameS.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            AndroidDeviceVibrate();  // Android Device Vibrate
            nameL.startAnimation(VibrateError());
            nameL.setError(getResources().getString(R.string.error_name_missing));
            error = true;
        } else if (surnameS.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            AndroidDeviceVibrate();  // Android Device Vibrate
            surnameL.startAnimation(VibrateError());
            surnameL.setError(getResources().getString(R.string.error_surname_missing));
            error = true;
        } else if (emailS.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            AndroidDeviceVibrate();  // Android Device Vibrate
            emailL.startAnimation(VibrateError());
            emailL.setError(getResources().getString(R.string.error_email_missing));
            error = true;
        } else if (pswS.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            AndroidDeviceVibrate();  // Android Device Vibrate
            passwordL.startAnimation(VibrateError());
            passwordL.setError(getResources().getString(R.string.error_password_missing));
            error = true;
        }

        if (!error)
            doSignUp(nameS, surnameS, emailS, pswS);

    }

    private void doSignUp(String name, String surname, String email, String psw) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, psw).addOnCompleteListener(SignUp.this, task -> {
            // If sign in fails, display a message to the user. If sign in succeeds
            // the auth state listener will be notified and logic to handle the
            // signed in user can be handled in the listener.
            if (!task.isSuccessful()) {
                progressBar.setVisibility(View.GONE);
                try {
                    throw task.getException();
                } catch (FirebaseAuthWeakPasswordException e) {
                    AndroidDeviceVibrate();  // Android Device Vibrate
                    passwordL.startAnimation(VibrateError());
                    passwordL.setError(getString(R.string.error_weak_password));
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    AndroidDeviceVibrate();  // Android Device Vibrate
                    emailL.startAnimation(VibrateError());
                    emailL.setError(getString(R.string.error_invalid_email));
                } catch (Exception e) {
                    AndroidDeviceVibrate();  // Android Device Vibrate
                    emailL.startAnimation(VibrateError());
                    emailL.setError(getString(R.string.error_user_exists));
                }
            } else {
                // mDatabase =  FirebaseDatabase.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                String uid = user.getUid();
                User u = new User(uid, name, surname, email);
                db = FirebaseFirestore.getInstance();
                db.collection("users").document(uid).set(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUp.this, getResources().getText(R.string.error_signup_writeDB), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(SignUp.this, MainActivity.class));
                            finish();
                        }
                    }

                });

            }
        });
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


}