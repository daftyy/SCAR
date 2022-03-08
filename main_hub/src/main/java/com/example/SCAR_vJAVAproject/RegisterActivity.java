package com.example.SCAR_vJAVAproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private EditText Fname;
    private EditText Lname;
    private Button register;
    private Chip gotologin;
    private String userID;
    private static final String TAG = "RegisterActivity";

    FirebaseAuth auth;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference rootRef = db.getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        System.out.println("In register");
        email = findViewById(R.id.email_register);
        password = findViewById(R.id.password_register);
        Fname = findViewById(R.id.firstName_register);
        Lname = findViewById(R.id.lastName_register);
        register = findViewById(R.id.register_button);
        gotologin = findViewById(R.id.gotologin);

        auth = FirebaseAuth.getInstance();

        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                final String txt_first = Fname.getText().toString();
                final String txt_last = Lname.getText().toString();



                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(RegisterActivity.this, "Username or Password not Entered", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(txt_first) || TextUtils.isEmpty(txt_last)){
                    Toast.makeText(RegisterActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                }
                else if (txt_password.length()<6){
                    Toast.makeText(RegisterActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.createUserWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                userID = auth.getCurrentUser().getUid();

                                Toast.makeText(RegisterActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                                rootRef.child("users").child(userID).child("info").child("Fname").setValue(txt_first).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Log.d(TAG, "FIRST NAME UPLOADED");
                                        }
                                        else {
                                            Log.d(TAG, "FIRST NAME ERROR");
                                        }
                                    }
                                });
                                rootRef.child("users").child(userID).child("info").child("Lname").setValue(txt_last).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Log.d(TAG, "LAST NAME UPLOADED");
                                        }
                                        else {
                                            Log.d(TAG, "LAST NAME ERROR");
                                        }
                                    }
                                });;
                                rootRef.child("users").child(userID).child("info").child("Email").setValue(txt_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Log.d(TAG, "EMAIL UPLOADED");
                                        }
                                        else {
                                            Log.d(TAG, "EMAIL ERROR");
                                        }
                                    }
                                });

                                startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                finish();
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }


}