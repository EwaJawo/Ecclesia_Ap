package com.example.ecclesia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText emailEdit;
    private Button resetPasswordButton;
    private ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_forgot_password );

        emailEdit = findViewById ( R.id.email );
        resetPasswordButton = findViewById ( R.id.btnButtonForgotPassword );
        progressBar = findViewById ( R.id.progressBar );
        mAuth = FirebaseAuth.getInstance ();

        resetPasswordButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                resetPassword ();
            }
        } );
    }

    private void resetPassword() {
        String email = emailEdit.getText ().toString ().trim ();

        if (email.isEmpty ()) {
            emailEdit.setError ( "Wpisz e-mail" );
            emailEdit.requestFocus ();
            retur;
        }
        if (Patterns.EMAIL_ADDRESS.matcher ( email ).matches ()) {
            emailEdit.setError ( "Proszę podać poprawny adres e-mail" );
            emailEdit.requestFocus ();
            return;
        }
        progressBar.setVisibility ( View.VISIBLE );
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener ( new OnCompleteListener<Void> () {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Sprawdź swój adres e-mail, aby zresetować hasło",Toast .LENGTH_LONG).show ();
                }else{
                    Toast.makeText(ForgotPassword.this, "Błąd, spróbuj ponownie", Toast.LENGTH_LONG).show();
                }
            }
        } );
    }
}