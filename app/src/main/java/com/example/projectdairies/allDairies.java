package com.example.projectdairies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class allDairies extends AppCompatActivity {
    private CardView newD;


    ImageView home;
    ImageView search;
    EditText searchD;
    RecyclerView recyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<firebasemodel,DairieViewHolder> dairieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_dairies);
        Date currentTime = Calendar.getInstance().getTime();
        String formattedDate = DateFormat.getDateInstance().format(currentTime);
        newD=findViewById(R.id.imageView4);
        home = findViewById(R.id.imageView16);
        search = findViewById(R.id.imageView17);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();



        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(allDairies.this,homePage.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(allDairies.this,searchPage.class);
                startActivity(intent);
            }
        });

        newD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(allDairies.this,dairiePage.class);
                startActivity(intent);

                /*if(query.equals(null)){
                    Intent intent= new Intent(allDairies.this,dairiePage.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(), "already exist ", Toast.LENGTH_SHORT).show();



                }*/

            }
        });


        Query query = firebaseFirestore.collection("dairies").document(firebaseUser.getUid()).collection("myDairies").orderBy("date",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<firebasemodel> alluserDairies = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query,firebasemodel.class).build();

        dairieAdapter = new FirestoreRecyclerAdapter<firebasemodel, DairieViewHolder>(alluserDairies) {
            @Override
            protected void onBindViewHolder(@NonNull DairieViewHolder dHolder, int position, @NonNull firebasemodel model) {
                dHolder.ddate.setText(model.getDate());
                String docId = dairieAdapter.getSnapshots().getSnapshot(position).getId();
                dHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(),editDairie.class);
                        v.getContext().startActivity(intent);
                        intent.putExtra("date",model.getDate());
                        intent.putExtra("dairie",model.getDairie());
                        intent.putExtra("dairieId",docId);
                        v.getContext().startActivity(intent);
                    }
                });




            }

            @NonNull
            @Override
            public DairieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
                return new DairieViewHolder(view);
            }
        };


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(dairieAdapter);








    }
    public static class DairieViewHolder extends RecyclerView.ViewHolder{
        private TextView ddate;
        private EditText dcontent;
        LinearLayout ldairie;
        public DairieViewHolder(@NonNull View itemView) {
            super(itemView);
            ddate = itemView.findViewById(R.id.textDate);
            ldairie = itemView.findViewById(R.id.dairieL);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        dairieAdapter.startListening();
    }
    @Override
    protected void onStop(){
        super.onStop();
        if(dairieAdapter != null){
            dairieAdapter.stopListening();
        }
    }

}