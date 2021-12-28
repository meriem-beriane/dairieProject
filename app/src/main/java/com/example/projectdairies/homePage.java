package com.example.projectdairies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class homePage extends AppCompatActivity {
    private ImageView allD,song,nosong;
    String number;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    TextView nd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        allD=findViewById(R.id.imageView9);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        nd = findViewById(R.id.textView4);
        song = findViewById(R.id.imageView5);
        nosong = findViewById(R.id.imageView7);

        nosong.setEnabled(false);
        nosong.setVisibility(View.INVISIBLE);



        final MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.euphoria);
        mediaPlayer.start();


        song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                song.setEnabled(false);
                nosong.setEnabled(true);
                nosong.setVisibility(View.VISIBLE);

            }
        });
        nosong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                song.setEnabled(true);
                mediaPlayer.start();

                nosong.setVisibility(View.INVISIBLE);


            }
        });

        firebaseFirestore.collection("dairies").document(firebaseUser.getUid()).collection("myDairies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;

                            for (DocumentSnapshot document : task.getResult()) {
                                count++;
                            }

                        } else {
                            Log.e("Tag", "Error getting documents: ", task.getException());
                        }
                    }


        });
        nd.setText("06");


        allD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(homePage.this,allDairies.class);
                startActivity(intent);
            }
        });


    }
}