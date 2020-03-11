package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Courses_List_Activity extends AppCompatActivity {
    List<MyDataList> myDataLists = new ArrayList<>();

    private RecyclerView recyclerView;
    FirebaseAuth fbaseAuth;
    FirebaseFirestore fStore;
    String type;
    int icon;
    String user_name = "";
    String avg_rating = "";
    TextView no_courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list_);
        fbaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        fStore.setFirestoreSettings(settings);
        recyclerView =(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Bundle b = this.getIntent().getExtras();
        type = b.getString("course_type");
        icon = Integer.parseInt(b.get("icon").toString());


      //  no_courses = findViewById(R.id.no_courses);

        CollectionReference course_ref = fStore.collection("course");
        final CollectionReference user_ref = fStore.collection("user");


        course_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                boolean course_exists = false;
                for(final QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots)
                {

                    if(Objects.equals(documentSnapshot.getString("type"), type))
                    {
                        course_exists = true;

                        user_ref.whereEqualTo("full_name", documentSnapshot.getString("tutor")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if(task.isSuccessful())
                                {

                                    for (QueryDocumentSnapshot document : task.getResult()) {


                                        MyDataList myDataList= new MyDataList();
                                        myDataList.setImageUrl(icon);
                                        myDataList.setCourse_name(documentSnapshot.getString("name"));
                                        myDataList.setTutor(documentSnapshot.getString("tutor"));
                                        myDataList.setAvg_rating(document.getString("average_rating"));
                                        myDataLists.add(myDataList);
                                    }



                                        recyclerView.setAdapter(new MyAdapter(myDataLists, getApplicationContext()));


                                }
                                else
                                {
                                    Toast.makeText(Courses_List_Activity.this, "Cant get the list of courses" , Toast.LENGTH_SHORT).show();


                                }

                            }
                        });



                    }
                }
                if(!course_exists)
                {
                   // no_courses.setVisibility(View.VISIBLE);
                }


            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

            }
        });


    }
/*
    private List<MyDataList> getData() {
        CollectionReference course_ref = fStore.collection("course");
        course_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots)
                {
                    if(Objects.equals(documentSnapshot.getString("type"), type))
                    {


                            MyDataList myDataList= new MyDataList();
                            myDataList.setImageUrl(R.drawable.math);
                            myDataList.setDescription(documentSnapshot.getString("name"));
                            myDataLists.add(myDataList);

                    }
                }

            }
        });


        return myDataLists;

    }
*/
public void openHomePage(View view) {
    startActivity(new Intent(getApplicationContext(), MainActivity.class));

}
    public void openMyProfilePage(View view) {
        startActivity(new Intent(this, MyProfileActivity.class));


    }
    public void openAddCourseActivity(View view) {

        startActivity(new Intent(this, New_Course_Activity.class));
    }
}

