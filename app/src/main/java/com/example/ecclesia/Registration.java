package com.example.ecclesia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private EditText nameUserEdit, emailEdit, passwordEdit, confirmPasswordEdit;
    private Button register;
    private TextView loginInApp;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_registration );

        mAuth = FirebaseAuth.getInstance ();

        register = findViewById ( R.id.btnRegister );
        register.setOnClickListener(this);

        nameUserEdit = findViewById ( R.id.nameUser );
        emailEdit = findViewById ( R.id.email );
        passwordEdit = findViewById ( R.id.password );
        confirmPasswordEdit = findViewById ( R.id.confirmPassword );
        loginInApp = findViewById ( R.id.textLoginIn );
        progressBar = findViewById ( R.id.progressBar );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRegister:
                registerUser(); 
                break;
        }
    }



    private void registerUser() {
        String name = nameUserEdit.getText().toString().trim();
        String email = emailEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        String confirmPassword = confirmPasswordEdit.getText().toString().trim ();

        if (name.isEmpty()) {
            nameUserEdit.setError("Wpisz Imię");
            nameUserEdit.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            emailEdit.setError ("Wpisz e-mail");
            emailEdit.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher ( email ).matches ()) {
            emailEdit.setError ("Proszę, podac poprawny adres e-mail" );
            emailEdit.requestFocus ();
        }
        if (password.isEmpty ()) {
            passwordEdit.setError ("Wpisz hasło");
            passwordEdit.requestFocus ();
            return;
        }
        if (password.length () < 6) {
            passwordEdit.setError ("Hasło powinno zawierać minimum 6 znaków");
            passwordEdit.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty ()) {
            confirmPasswordEdit.setError ("Powtórz hasło");
            confirmPasswordEdit.requestFocus ();
            return;
        }
        if (!password.equals (confirmPassword )) {
            confirmPasswordEdit.setError ("Podałeś różne hasła");
            return;
        }

        progressBar.setVisibility (View.VISIBLE );

        mAuth.createUserWithEmailAndPassword ( email, password )
                .addOnCompleteListener ( new OnCompleteListener<AuthResult> () {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful ()) {
                            User user = new User ( email, password );
                            FirebaseDatabase.getInstance ().getReference ( "Users" )
                                    .child ( FirebaseAuth.getInstance ().getCurrentUser ().getUid () )
                                    .setValue ( user ).addOnCompleteListener ( new OnCompleteListener<Void> () {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful ())
                                    {
                                        SendEmailVerification();
                                        Toast.makeText ( Registration.this, "Rejestracja zakończona pomyślnie", Toast.LENGTH_LONG ).show ();
                                        progressBar.setVisibility ( View.VISIBLE );

                                    } else {
                                        Toast.makeText ( Registration.this, "Rejestracja nie powiodła się!, spróbuj jeszcze raz ", Toast.LENGTH_LONG ).show ();
                                        progressBar.setVisibility ( View.VISIBLE );
                                    }
                                }
                            } );
                        }
                    }
                } );
    }

    private void SendEmailVerification() {
            FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();
            if (user != null) {

                user.sendEmailVerification ().addOnCompleteListener ( new OnCompleteListener<Void> () {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful ()) {
                            Toast.makeText ( Registration.this, " Sprawdź swoj adrers e-mail", Toast.LENGTH_LONG ).show ();
                            Intent intent = new Intent ( Registration.this, LoginActivity.class );
                            startActivity ( intent );
                            FirebaseAuth.getInstance().signOut ();
                        } else {
                            Toast.makeText ( Registration.this, "Werfikacja nie powiodła się", Toast.LENGTH_LONG ).show ();
                            FirebaseAuth.getInstance().signOut ();
                        }
                    }
                } );

            }

    }
}





