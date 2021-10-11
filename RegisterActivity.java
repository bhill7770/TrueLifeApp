package com.example.truelifemobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Half;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText mEmailEt, mPasswordEt, mPhonenumberEt;
    Button mRegisterBtn;
    TextView mHaveAccountTv;

    ProgressDialog progressDialog;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar(); //actionbar
        actionBar.setTitle("Create Personal Account");
        actionBar.setDisplayHomeAsUpEnabled(true); //back button
        actionBar.setDisplayShowHomeEnabled(true);
        mEmailEt = findViewById(R.id.emailEt);
        mPasswordEt = findViewById(R.id.passwordEt);
        mRegisterBtn = findViewById(R.id.register_btn);
        mPhonenumberEt = findViewById(R.id.phonenumberEt);
        mHaveAccountTv = findViewById(R.id.have_accountTv);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");


        mRegisterBtn.setOnContextClickListener(new View.OnContextClickListener() {
            @Override
            public boolean onContextClick(View view) {
               // return false;
                String email = mEmailEt.getText().toString().trim(); //input email, password and phonenumber
                String password = mPasswordEt.getText().toString().trim();
                String phonenumber = mPhonenumberEt.getText().toString().trim();
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){ //validate
                        mEmailEt.setError("Invalid Email");
                        mEmailEt.setFocusable(true);

                    }
                    else if (password.length()<6){
                        mPasswordEt.setError("Password must be at least 6 characters");
                        mPasswordEt.setFocusable(true);
                    }
                    else {
                        registerUser(email, password); //register users

                    }
                return false;
            }
        });
        mHaveAccountTv.setOnContextClickListener(new View.OnContextClickListener() {
            @Override
            public boolean onContextClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                return false;
            }
        });

    }

    private void registerUser(String email, String password){
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();

                            FirebaseUser user = mAuth.getCurrentUser();//setting up user profile
                            String email = user.getEmail();
                            String uid = user.getUid();

                            HashMap<Object, String > hashMap = new HashMap<>();
                            hashMap.put("email", email);
                            hashMap.put("uid", ""); //edit in profile
                            hashMap.put("image", ""); //edit in profile
                            hashMap.put("name", ""); //edit in profile
                            hashMap.put("age", ""); //edit in profile
                            hashMap.put("hometown", ""); //edit in profile
                            hashMap.put("bio", ""); //edit in profile

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Users");
                                reference.child(uid).setValue(hashMap);

                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Registered...\n"+user.getEmail(), Toast.LENGTH_SHORT);
                            startActivity(new Intent(RegisterActivity.this, ProfileActivity.class));
                            finish();
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication Failed.", Toast.LENGTH_LONG);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
