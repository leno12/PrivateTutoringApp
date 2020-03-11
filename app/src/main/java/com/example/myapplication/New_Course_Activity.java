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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nullable;

public class New_Course_Activity extends AppCompatActivity {
    LinearLayout new_course;
    MaterialBetterSpinner materialBetterSpinner ;
    MaterialBetterSpinner price_spinner;
    MaterialBetterSpinner city_spinner;
    EditText courseName;
    EditText courseDescription;
    ProgressBar progressBar;
    FirebaseAuth fbaseAuth;
    FirebaseFirestore fStore;
    String[] SPINNER_DATA = {"Math","Programming", "Music", "Chemistry", "Physics", "Languages"};
    String[] SPINNER_DATA_PRICES = new String[10000];
    String[] SPINNER_CITY = {"Vienna", "Graz", "Linz", "Salzburg","Innsbruck", "Klagenfurt", "Villach",
    "Wales", "Sankt Poelten", "Dornbirn"};
    String TAG = "New Course Creation";
    boolean check_course_exists = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_course_);
        courseName = findViewById(R.id.course_name);
        courseDescription = findViewById(R.id.course_description);
        new_course = findViewById(R.id.new_course);
        new_course.setBackgroundColor(Color.rgb(39,90,132));
        materialBetterSpinner = (MaterialBetterSpinner)findViewById(R.id.course_type_spinner);
        materialBetterSpinner.setTextColor(Color.parseColor("#ffffff"));
        materialBetterSpinner.setHint("Course Type");
        materialBetterSpinner.setHintTextColor(Color.parseColor("#ffffff"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA);
        materialBetterSpinner.setAdapter(adapter);
        for(int i = 0; i < 10000; i++)
        {
            SPINNER_DATA_PRICES[i] = String.valueOf(i + 1) + "\u20ac";
        }
        price_spinner = (MaterialBetterSpinner)findViewById(R.id.price_spinner);
        price_spinner.setTextColor(Color.parseColor("#ffffff"));
        price_spinner.setHint("Price per Hour");
        price_spinner.setHintTextColor(Color.parseColor("#ffffff"));
        ArrayAdapter<String> adapter_prices = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA_PRICES);
        price_spinner.setAdapter(adapter_prices);
        city_spinner = (MaterialBetterSpinner)findViewById(R.id.city_spinner);
        city_spinner.setTextColor(Color.parseColor("#ffffff"));
        city_spinner.setHint("City");
        city_spinner.setHintTextColor(Color.parseColor("#ffffff"));
        ArrayAdapter<String> adapter_cities = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, SPINNER_CITY);
        city_spinner.setAdapter(adapter_cities);
        fbaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        fStore.setFirestoreSettings(settings);
        progressBar = findViewById(R.id.new_course_pb);


    }


    public void openHomePage(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }
    public void openMyProfilePage(View view) {

        startActivity(new Intent(this, MyProfileActivity.class));
    }

    public void creteNewCourse(View view) {
        final String course_name = courseName.getText().toString().trim();
        final String course_type = materialBetterSpinner.getText().toString().trim();
        final String course_price = price_spinner.getText().toString().trim();
        final String course_city = city_spinner.getText().toString().trim();
        final String course_description = courseDescription.getText().toString().trim();
        if(course_name.isEmpty())
        {
            courseName.setError("Course Name is required");
            return;
        }
        if(course_type.isEmpty())
        {
            materialBetterSpinner.setError("Course Type is required");
            return;
        }
        if(course_price.isEmpty())
        {
            price_spinner.setError("Course Price is required");
            return;
        }
        if(course_city.isEmpty())
        {
            city_spinner.setError("Course City is required");
            return;
        }
        if(course_description.length() < 50)
        {
            courseDescription.setError("Course description must be at least 50 Characters long");
            return;
        }
        check_course_exists = false;
        CollectionReference course_collection = fStore.collection("course");
        course_collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                progressBar.setVisibility(View.VISIBLE);

                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots)
                {

                    if (Objects.equals(documentSnapshot.getString("name"), course_name))
                    {
                        Toast.makeText(New_Course_Activity.this, "Course with that name already exists", Toast.LENGTH_SHORT).show();
                        check_course_exists = true;
                        progressBar.setVisibility(View.INVISIBLE);



                    }
                }

                if(!check_course_exists)
                {

                    final DocumentReference doc_reference = fStore.collection("course").document(course_name);
                    final DocumentReference user_ref = fStore.collection("user").document(fbaseAuth.getCurrentUser().getUid());
                    user_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            HashMap<String, Object> course = new HashMap<>();
                            course.put("tutor", documentSnapshot.getString("full_name"));
                            course.put("name", course_name);
                            course.put("type", course_type);
                            course.put("price", course_price);
                            course.put("city", course_city);
                            course.put("description", course_description);
                            doc_reference.set(course).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: Course created successfully");
                                    Toast.makeText(New_Course_Activity.this, "Course created successfully", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onSuccess: Course created successfully");
                                    Toast.makeText(New_Course_Activity.this, "Error occured" + e.toString(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);

                                }
                            });

                        }
                    });




                }

            }
        });




    }
}
