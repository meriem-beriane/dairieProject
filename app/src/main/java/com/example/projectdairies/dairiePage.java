package com.example.projectdairies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class dairiePage extends AppCompatActivity {
    EditText note;
    ImageView saveNote;
    TextView date;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairie_page);

        Date currentTime = Calendar.getInstance().getTime();
        String formattedDate = DateFormat.getDateInstance().format(currentTime);
        saveNote = findViewById(R.id.saveD);
        note = findViewById(R.id.displayNote);
        date = findViewById(R.id.date1);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        date.setText(formattedDate);
        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteToSave = note.getText().toString();

                DocumentReference documentReference = firebaseFirestore.collection("dairies").document(firebaseUser.getUid()).collection("myDairies").document();
                Map<String ,Object> dairie = new HashMap<>();
                dairie.put("date", formattedDate);
                dairie.put("dairie", noteToSave);

                documentReference.set(dairie).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Toast.makeText(getApplicationContext(), "Dairie created succesffuly", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(dairiePage.this,allDairies.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to create: "+e.getMessage()+firebaseUser.getUid(), Toast.LENGTH_SHORT).show();
                        Log.e("create", "Failed "+firebaseUser.getUid(), e);

                    }
                });

            }
        });
    }
}