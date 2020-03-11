package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity  {
    LinearLayout home_page;
    LinearLayout main_page;
    CardView first_card_view;
    CardView second_card_view;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.
    private ActionBarDrawerToggle drawerToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        home_page = findViewById(R.id.home_page);
        main_page = findViewById(R.id.test);
        home_page.setBackgroundColor(Color.rgb(72,99,160));


       // toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // This will display an Up icon (<-), we will replace it with hamburger later
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find our drawer view
        //mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);

// Start animation

        main_page.startAnimation(slide_down);

        main_page.postDelayed(new Runnable() {
            @Override
            public void run() {
                final Animation animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
              //  first_card_view.startAnimation(animShake);
                //second_card_view.startAnimation(animShake);
            }
        }, 200);

    //    test2.startAnimation(slide_down);
        // main_page = findViewById(R.id.main_page);



    }

    public void openMyProfilePage(View view) {

        startActivity(new Intent(this, MyProfileActivity.class));
    }
    public void openHomePage(View view) {


    }


    public void openAddCourseActivity(View view) {

        startActivity(new Intent(this, New_Course_Activity.class));
    }

    public void openMathCourses(View view) {
        Intent new_intent = new Intent(this, Courses_List_Activity.class);
        new_intent.putExtra("course_type", "Math");
        new_intent.putExtra("icon", R.drawable.math);
        startActivity(new_intent);

    }

    public void openProgrammingCourses(View view) {
        Intent new_intent = new Intent(this, Courses_List_Activity.class);
        new_intent.putExtra("course_type", "Programming");
        new_intent.putExtra("icon", R.drawable.programming);
        startActivity(new_intent);
    }

    public void openPhysicsCourses(View view) {
        Intent new_intent = new Intent(this, Courses_List_Activity.class);
        new_intent.putExtra("course_type", "Physics");
        new_intent.putExtra("icon", R.drawable.physics);
        startActivity(new_intent);
    }

    public void openChemistryCourses(View view) {
        Intent new_intent = new Intent(this, Courses_List_Activity.class);
        new_intent.putExtra("course_type", "Chemistry");
        new_intent.putExtra("icon", R.drawable.chemistry);

        startActivity(new_intent);
    }

    public void openLanguageCourses(View view) {
        Intent new_intent = new Intent(this, Courses_List_Activity.class);
        new_intent.putExtra("course_type", "Languages");
        new_intent.putExtra("icon", R.drawable.languages);

        startActivity(new_intent);
    }

    public void openMusicCourses(View view) {
        Intent new_intent = new Intent(this, Courses_List_Activity.class);
        new_intent.putExtra("course_type", "Music");
        new_intent.putExtra("icon", R.drawable.music_last);

        startActivity(new_intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
