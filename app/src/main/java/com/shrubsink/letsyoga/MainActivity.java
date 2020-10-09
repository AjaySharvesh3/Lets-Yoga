package com.shrubsink.letsyoga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore mFirebaseFirestore;
    GoogleSignInClient googleSignInClient;

    FragmentManager fragmentManager;

    CircleImageView profileImageView;
    ChipNavigationBar chipNavigationBar;


    public static void startActivity(Context context, String username) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("username", username);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        mFirebaseAuth = FirebaseAuth.getInstance();
        googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);
        mFirebaseFirestore = FirebaseFirestore.getInstance();

        includeProfileImage();

        initHomeFragment(savedInstanceState);
        switchFragment();
    }

    public void initViews() {
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

        profileImageView = findViewById(R.id.profile_image);
        chipNavigationBar = findViewById(R.id.bottom_nav);
    }


    public void includeProfileImage() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            Uri personPhoto = acct.getPhotoUrl();
            Glide.with(this).load(personPhoto).placeholder(R.drawable.profile_placeholder).into(profileImageView);
        }
    }


    public void switchFragment() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment fragment = null;
                switch (id) {
                    case R.id.home: fragment = new HomeFragment(); break;
                    case R.id.learn: fragment = new LearnFragment(); break;
                    case R.id.routines: fragment = new RoutinesFragment(); break;
                    case R.id.inbox: fragment = new InboxFragment(); break;
                }

                if (fragment != null) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                } else {
                    Log.e("MAIN_ACTIVITY", "Error in creating fragment");
                }
            }
        });
    }


    public void initHomeFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            chipNavigationBar.setItemSelected(R.id.home, true);
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.more_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout: {
                signOut();
                break;
            }
            case R.id.action_my_activities: {
                /*Intent myActivities = new Intent(MainActivity.this, MyActivitiesActivity.class);
                startActivity(myActivities);*/
                break;
            }
        }
        return true;
    }


    private void signOut() {
        mFirebaseAuth.signOut();
        googleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent googleSignInActivity = new Intent(MainActivity.this, GoogleSignInActivity.class);
                        startActivity(googleSignInActivity);
                    }
                });
    }
}