package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    MaterialBetterSpinner materialBetterSpinner ;
    EditText name;
    EditText email;
    EditText password;
    EditText phone;
    FloatingActionButton register_button;
    ProgressBar progressBar;
    FirebaseAuth fbaseAuth;
    FirebaseFirestore fStore;
    String userID;


    String[] SPINNER_DATA = {"Tutor","Student"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        materialBetterSpinner = (MaterialBetterSpinner)findViewById(R.id.material_spinner1);
        name = findViewById(R.id.Name);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        phone = findViewById(R.id.Phone);
        register_button = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);
        fbaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        fStore.setFirestoreSettings(settings);
        materialBetterSpinner.setTextColor(Color.parseColor("#ffffff"));
        materialBetterSpinner.setHint("Please Select your role:");
        materialBetterSpinner.setHintTextColor(Color.parseColor("#ffffff"));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA);

        materialBetterSpinner.setAdapter(adapter);
        if(fbaseAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(SignUpActivity.this, MyProfileActivity.class));

        }

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_email = email.getText().toString().trim();
                String user_password = password.getText().toString().trim();
                final String full_name = name.getText().toString();
                final String phone_number = phone.getText().toString();
                final String role = materialBetterSpinner.getText().toString();
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
                if(role.isEmpty())
                {
                    materialBetterSpinner.setError("Please choose your role");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                fbaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SignUpActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                            userID = fbaseAuth.getCurrentUser().getUid();
                            DocumentReference doc_reference = fStore.collection("user").document(userID);
                            HashMap<String,Object> user = new HashMap<>();
                            user.put("full_name", full_name);
                            user.put("email", user_email);
                            user.put("phone_number", phone_number);
                            user.put("user_role", role);
                            user.put("average_rating", "0.0");
                            doc_reference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.d(TAG, "onSuccess: User profile is created for " + userID);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "OnFailure: Problem while creating the user profile: " + e.toString());
                                }
                            });
                            startActivity(new Intent(SignUpActivity.this, MyProfileActivity.class));


                        }
                        else
                        {
                            Toast.makeText(SignUpActivity.this, "Error ocurred! " + task.getException(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }

                    }
                });
            }
        });
    }
}
