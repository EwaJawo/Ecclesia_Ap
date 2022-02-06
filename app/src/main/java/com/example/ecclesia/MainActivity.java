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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private  Button register;
    private EditText emailEdit, passwordEdit;
    private Button loginIn;
    private TextView forgotPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        register = findViewById ( R.id.btnRegister );
        register.setOnClickListener(this);

        loginIn = findViewById (R.id.btnLogin);
        loginIn.setOnClickListener(this );

        emailEdit = findViewById (R.id.emailLogin);
        passwordEdit = findViewById (R.id.passwordLogin);

        progressBar = findViewById (R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        forgotPassword = findViewById(R.id.textForgotPassword);
        forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRegister:
                startActivity (new Intent (this, Registration.class));
                break;

            case  R.id.btnLogin:
                userLogin();
                break;

            case R.id.textForgetPassword:
                startActivity(new Intent (this, ForgotPassword.class));
                break;
        }
    }

    private void userLogin() {
        String email = emailEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();

        if(email.isEmpty()){
            emailEdit.setError("Wpisz e-mail");
            emailEdit.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEdit.setError ("Wpisz poprawny adres e-mail");
            emailEdit.requestFocus();
            return;
         }

        if (password.isEmpty()){
            passwordEdit.setError("Wisz hasło");
            passwordEdit.requestFocus();
            return;
        }
        if(password.length() < 6){
            passwordEdit.setError("Hasło powinno zawierać minimum 6 znaków");
            passwordEdit.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener ( new OnCompleteListener<AuthResult> () {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                        startActivity ( new Intent ( MainActivity.this, HomeActivity.class ) );
                } else {
                    Toast.makeText ( MainActivity.this, "Błąd,prosze sprawdzić dane uwierzytelniające", Toast.LENGTH_LONG ).show ();
                }
            }
        });
    }
}


































