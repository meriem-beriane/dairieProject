package com.example.projectdairies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class passwordPage extends AppCompatActivity {
    EditText password;
    EditText email;
    ImageView go;
    TextView register;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_page);

        password = findViewById(R.id.editTextTextPassword);
        email = findViewById(R.id.editTextTextEmailAddress);
        go = findViewById(R.id.imageView12);
        register = findViewById(R.id.textView8);


        firebaseAuth = FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(passwordPage.this,signInPage.class);
                startActivity(intent);
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"login succesfull",Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent= new Intent(passwordPage.this,homePage.class);
                            startActivity(intent);
                        }


                    }
                });







                /*firebaseAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"registration succesfull",Toast.LENGTH_SHORT).show();
                            firebaseAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),"login succesfull",Toast.LENGTH_SHORT).show();
                                        finish();
                                        Intent intent= new Intent(passwordPage.this,dairiePage.class);
                                        startActivity(intent);
                                    }


                                }
                            });

                        }else{
                            FirebaseAuthException e = (FirebaseAuthException )task.getException();
                            Toast.makeText(getApplicationContext(), "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();

                            return;
                        }
                    }
                });*/
            }
        });
    }
}