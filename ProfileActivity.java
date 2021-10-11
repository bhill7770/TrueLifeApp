package com.example.truelifemobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");

        firebaseAuth = FirebaseAuth.getInstance();

        mProfileTv = findViewById(R.id.profileTv);

    }

    private void checkUserStatus(){
        FirebaseAuth user = firebaseAuth.getCurrentUser(); //getting current user
        if (user !=null){
            mProfileTv.setText(user.getEmail());

        }
        else {
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            finish();
        }
    }



    @Override
    protected void onStart() {
        checkUserStatus(); //check when app starts
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            int id  = item.getItemId(); //logging user out
            if (id == R.id.action_logout){
                firebaseAuth.signOut();
                checkUserStatus();
            }
        return super.onOptionsItemSelected(item);
    }
}
