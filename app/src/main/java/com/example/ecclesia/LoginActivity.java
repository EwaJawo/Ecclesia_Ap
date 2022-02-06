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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private EditText emailEdit, passwordEdit;
    private Button loginIn;
    private TextView signUp;
    private TextView forgotPassword;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private Boolean emailAddressCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance ();

        emailEdit = findViewById (R.id.emailLogin);
        passwordEdit = findViewById (R.id.passwordLogin);
        loginIn = findViewById (R.id.btnLogin);
        signUp = findViewById (R.id.textSignUp);
        forgotPassword = findViewById (R.id.textForgotPassword);
        progressBar = findViewById ( R.id.progressBar);

        loginIn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Login ();
            }
        } );

        signUp.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent ( LoginActivity.this, Registration.class );
                startActivity ( intent );
                finish ();
            }
        } );
    }

    private void Login() {
        String email = emailEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();

        if (TextUtils.isEmpty (email)) {
            emailEdit.setError ("Wpisz e-mail");
            return;
        } else if (TextUtils.isEmpty (password)) {
            passwordEdit.setError ("Wpisz hasło");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);


        firebaseAuth.signInWithEmailAndPassword ( email, password ).addOnCompleteListener ( this, new OnCompleteListener<AuthResult> () {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful ()) {
                    VerificationEmail ();

                } else {
                    Toast.makeText ( LoginActivity.this, "Logowanie nie powiodło się!", Toast.LENGTH_LONG ).show ();
                }
            }
        } );
    }

    private void VerificationEmail()
        {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            emailAddressCheck = user.isEmailVerified();

            if (emailAddressCheck)
            {
                Intent intent = new Intent ( LoginActivity.this, HomeActivity.class );
                startActivity ( intent );
                finish ();
            }
            else
            {
                Toast.makeText ( LoginActivity.this, "Zweryfikuj swój email", Toast.LENGTH_LONG ).show ();
                FirebaseAuth.getInstance().signOut ();

            }
        }
}




