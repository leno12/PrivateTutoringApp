package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MyProfileActivity extends AppCompatActivity {
    FloatingActionButton logout;
    FirebaseAuth fbaseAuth;
    GridView profile_grid;
    LinearLayout my_profile;
    LinearLayout bottom_bar;
    TextView userName;
    TextView userEmail;
    TextView userPhone;
    TextView userAvgRating;
    FirebaseFirestore fStore;
    List<MyDataList> myDataLists = new ArrayList<>();
    private RecyclerView recyclerView;
    ImageView profile_photo;
    String TAG = "My Profile Activity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        my_profile = findViewById(R.id.my_profile);
        my_profile.setBackgroundColor(Color.rgb(72,99,160));
        fbaseAuth = FirebaseAuth.getInstance();
        userName = findViewById(R.id.profile_name);
        userEmail = findViewById(R.id.user_email);
        userPhone = findViewById(R.id.user_phone);
        userAvgRating = findViewById(R.id.user_avg_rating);
        profile_photo = findViewById(R.id.profile_photo);

        fbaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        fStore.setFirestoreSettings(settings);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewProfile);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final CollectionReference course_ref = fStore.collection("course");
        final CollectionReference user_ref = fStore.collection("user");
        final DocumentReference user_doc = user_ref.document(fbaseAuth.getCurrentUser().getUid());

        user_doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(final DocumentSnapshot documentSnapshot) {

                course_ref.whereEqualTo("tutor", documentSnapshot.getString("full_name")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult()) {



                                MyDataList myDataList = new MyDataList();
                                myDataList.setImageUrl(getIcon(document.getString("type")));
                                myDataList.setCourse_name(document.getString("name"));
                                myDataList.setTutor(document.getString("tutor"));
                                myDataList.setAvg_rating(documentSnapshot.getString("average_rating"));
                                myDataLists.add(myDataList);
                            }

                            recyclerView.setAdapter(new MyAdapter(myDataLists, getApplicationContext()));
                            HashMap<String, Object> user_field = (HashMap<String, Object>) documentSnapshot.getData();
                            if(user_field.containsKey("profile_photo"))
                            {
                                Uri profile_image = Uri.parse(documentSnapshot.getString("profile_photo"));

                                    Glide.with(getApplicationContext())
                                            .load(profile_image)
                                            .into(profile_photo);


                            }

                        }
                        else {
                            Toast.makeText(MyProfileActivity.this, "Cant get the list of courses user profile", Toast.LENGTH_SHORT).show();

                        }

                    }
                });


            }
        });


    }

    public void logout(View view)
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();

    }
    public void openHomePage(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }
    public void openMyProfilePage(View view) {

    }
    public void openAddCourseActivity(View view) {

        startActivity(new Intent(this, New_Course_Activity.class));
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        DocumentReference user_ref = fStore.collection("user").document(fbaseAuth.getCurrentUser().getUid());
        user_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userName.setText(documentSnapshot.getString("full_name"));
                userEmail.setText(documentSnapshot.getString("email"));
                userPhone.setText(documentSnapshot.getString("phone_number"));
                userAvgRating.setText(documentSnapshot.getString("average_rating"));
                userAvgRating.setVisibility(View.VISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private int getIcon(String type)
    {
        switch (type)
        {
            case "Math":
                return R.drawable.math;
            case "Music":
                return R.drawable.music_last;
            case "Programming":
                return R.drawable.programming;
            case "Chemistry":
                return R.drawable.chemistry;
            case "Languages":
                return R.drawable.languages;
            case "Physics":
                return R.drawable.physics;
            default:
                return -1;
        }
    }

    public void changeProfilePhoto(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {

            Uri chosenImageUri = data.getData();


            Bitmap mBitmap = null;
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), chosenImageUri);
                profile_photo.setImageBitmap(mBitmap);
                if(mBitmap.getWidth()  > 2000.00) {
                    profile_photo.setRotation(90);
                }
                UploadImage(mBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void UploadImage(Bitmap profile_image) {
        ByteArrayOutputStream bous = new ByteArrayOutputStream();
        profile_image.compress(Bitmap.CompressFormat.WEBP, 50, bous);
        final StorageReference mstorageRef = FirebaseStorage.getInstance().getReference()
                .child("profile_images")
                .child(fbaseAuth.getCurrentUser().getUid() + ".webp");

        mstorageRef.putBytes(bous.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getDownloadUrl(mstorageRef);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure " + e.getCause());

            }
        });

    }


    private void getDownloadUrl(StorageReference reference)
    {
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(TAG, "onSuccess " +  uri);
                DocumentReference ref = fStore.collection("user").document(fbaseAuth.getCurrentUser().getUid());
                HashMap<String, Object> user = new HashMap<>();
                user.put("profile_photo", uri.toString());
                ref.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Download url success");

                    }
                });

            }
        });
    }

}
