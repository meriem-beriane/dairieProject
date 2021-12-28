package com.example.projectdairies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editDairie extends AppCompatActivity {
    EditText displayDairie;
    TextView ddate;
    ImageView delete,save;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dairie);
        delete = findViewById(R.id.deleteDairie);
        save = findViewById(R.id.saveD);

        displayDairie = findViewById(R.id.displayNote);
        ddate = findViewById(R.id.date1);

        Intent data = getIntent();

        ddate.setText(data.getStringExtra("date"));
        displayDairie.setText(data.getStringExtra("dairie"));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = firebaseFirestore.collection("dairies").document(firebaseUser.getUid()).collection("myDairies").document(data.getStringExtra("dairieId"));

                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        Toast.makeText(getApplicationContext(), "Dairie deleted succesffuly", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(editDairie.this,allDairies.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to delete", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteToSave = displayDairie.getText().toString();

                DocumentReference documentReference = firebaseFirestore.collection("dairies").document(firebaseUser.getUid()).collection("myDairies").document(data.getStringExtra("dairieId"));
                Map<String, Object> dairie = new HashMap<>();
                dairie.put("date", data.getStringExtra("date"));
                dairie.put("dairie", noteToSave);

                documentReference.set(dairie).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Toast.makeText(getApplicationContext(), "Dairie updated succesffuly", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(editDairie.this,allDairies.class));
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