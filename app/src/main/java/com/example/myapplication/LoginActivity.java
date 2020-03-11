package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    FloatingActionButton loginButton;
    ProgressBar progressBar;
    FirebaseAuth fbaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBarLogin);
        fbaseAuth = FirebaseAuth.getInstance();
        if(fbaseAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));

        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_email = email.getText().toString().trim();
                String user_password = password.getText().toString().trim();
                if(user_email.isEmpty())
                {
                    email.setError("Email is required");
                    return;
                }
                if(user_password.isEmpty())
                {
                    password.setError("Password is required");
                    return;
                }
                if(password.length() < 6)
                {
                    password.setError("Password must have at least 6 chracters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                fbaseAuth.signInWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));


                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Error ocurred! " + task.getException(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
            }
        });


    }

    public void openSignUpPage(View view) {


        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));

    }
}
