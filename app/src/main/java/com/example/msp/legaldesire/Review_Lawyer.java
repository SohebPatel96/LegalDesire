package com.example.msp.legaldesire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Review_Lawyer extends AppCompatActivity {
    String lawyer_id;
    ArrayList<Float> rating = new ArrayList<>();
    ArrayList<String> user_name = new ArrayList<>();
    ArrayList<String> review = new ArrayList<>();
    ListView listView;
    Review_Adapter review_adapter;
    DatabaseReference reviewDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Reviews");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review__lawyer);
        Bundle extras = getIntent().getExtras();
        lawyer_id = extras.getString("lawyer_id");
        reviewDatabase = FirebaseDatabase.getInstance().getReference().child("User").child("Lawyer").child(lawyer_id).child("Review");


        listView = (ListView) findViewById(R.id.list_reviews);
        review_adapter = new Review_Adapter(getBaseContext(), user_name, review, rating);
        listView.setAdapter(review_adapter);

        reviewDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    long rat = post.child("Rating").getValue(Long.class);
                    String rev = post.child("Review").getValue(String.class);
                    String user = post.child("User Name").getValue(String.class);
                    rating.add((float) rat);
                    review.add(rev);
                    user_name.add(user);
                    review_adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
