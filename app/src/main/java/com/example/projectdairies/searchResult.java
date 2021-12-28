package com.example.projectdairies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class searchResult extends AppCompatActivity {

    RecyclerView recyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<firebasemodel, resultViewHolder> dairieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Intent data = getIntent();

        Query query = firebaseFirestore.collection("dairies").document(firebaseUser.getUid()).collection("myDairies").whereEqualTo("date",data.getStringExtra("searchDate"));

        FirestoreRecyclerOptions<firebasemodel> alluserDairies = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query,firebasemodel.class).build();

        dairieAdapter = new FirestoreRecyclerAdapter<firebasemodel, resultViewHolder>(alluserDairies) {
            @Override
            protected void onBindViewHolder(@NonNull resultViewHolder dHolder, int position, @NonNull firebasemodel model) {
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
            }@NonNull
            @Override
            public resultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
                return new resultViewHolder(view);
            }
        };


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(dairieAdapter);








    }
    public class resultViewHolder extends RecyclerView.ViewHolder{
        private TextView ddate;

        LinearLayout ldairie;
        public resultViewHolder(@NonNull View itemView) {
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