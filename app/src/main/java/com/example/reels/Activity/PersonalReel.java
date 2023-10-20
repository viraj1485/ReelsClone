package com.example.reels.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.reels.Adapter.PersonalAdapter;
import com.example.reels.Model.PostModel;
import com.example.reels.databinding.ActivityPersonalReelBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PersonalReel extends AppCompatActivity {

    ActivityPersonalReelBinding personalReelBinding;

    ArrayList<PostModel> personalpost;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ArrayList<PostModel> arr;

    String uid;
    PersonalAdapter personalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        personalReelBinding = ActivityPersonalReelBinding.inflate(getLayoutInflater());
        setContentView(personalReelBinding.getRoot());
        this.getWindow().setStatusBarColor(Color.parseColor("#4AA7F3"));
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        readUsers();

    }

    private void readUsers() {
        personalpost = new ArrayList<PostModel>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Video");
        arr = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arr.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    PostModel usersModel = data.getValue(PostModel.class);
                    arr.add(usersModel);
                }
                for (int i = 0; i < arr.size(); i++) {
                    if (arr.get(i).getPostuid().equals(uid)) {
                        personalpost.add(arr.get(i));
                    }
                }
                personalReelBinding.recylepersonal.setLayoutManager(new GridLayoutManager(PersonalReel.this, 2));
                personalAdapter = new PersonalAdapter(personalpost, PersonalReel.this);
                personalReelBinding.recylepersonal.setAdapter(personalAdapter);
                personalAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}