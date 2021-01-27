package com.frarisstudio.lavapiatti.system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.frarisstudio.lavapiatti.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Button btnRecoverPsw;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().hide();

        btnRecoverPsw = findViewById(R.id.btnRecoverPsw);
        email = findViewById(R.id.txtEmailRecovery);
        btnRecoverPsw.setOnClickListener(v -> newPsw());

    }

    public void newPsw() {
        String emailString = email.getText().toString();
        mAuth = FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(emailString)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            showAlertDialogButtonClicked();
                        } else {
                            Toast.makeText(ForgotPassword.this, task.getException().toString(), Toast.LENGTH_LONG);
                        }
                    }
                });
    }


    public void showAlertDialogButtonClicked() {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.forgot_pass_done_title));
        builder.setMessage((getResources().getString(R.string.forgot_pass_done_subtitle)));
        // add the buttons
        builder.setPositiveButton("Apri email", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                startActivity(intent);
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.choose_email_client)));
            }
        });
        builder.setNeutralButton(getResources().getString(R.string.back), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              finish();
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}