package com.example.reels.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.reels.R;
import com.example.reels.databinding.ActivityPersonalVideoPlayingBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PersonalVideoPlayingActivity extends AppCompatActivity {

    ActivityPersonalVideoPlayingBinding activityPersonalVideoPlayingBinding;
    String desc, videourl, postid;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    boolean b = false;
    boolean b1 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPersonalVideoPlayingBinding = ActivityPersonalVideoPlayingBinding.inflate(getLayoutInflater());
        setContentView(activityPersonalVideoPlayingBinding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        this.getWindow().setStatusBarColor(Color.parseColor("#4AA7F3"));
        Intent intent = getIntent();
        desc = intent.getStringExtra("desc");
        videourl = intent.getStringExtra("videourl");
        postid = intent.getStringExtra("postid");

        activityPersonalVideoPlayingBinding.singlereel.setVideoURI(Uri.parse(videourl));
        activityPersonalVideoPlayingBinding.description.setText(desc);


        activityPersonalVideoPlayingBinding.singlereel.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                activityPersonalVideoPlayingBinding.progressbar.setVisibility(View.GONE);
                activityPersonalVideoPlayingBinding.singlereel.start();
            }
        });
        activityPersonalVideoPlayingBinding.singlereel.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                activityPersonalVideoPlayingBinding.singlereel.start();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.child(postid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("username").getValue(String.class);
                activityPersonalVideoPlayingBinding.usesrname.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setIslike(postid, activityPersonalVideoPlayingBinding.like);
        numberoflike(postid, activityPersonalVideoPlayingBinding.likecount);
        activityPersonalVideoPlayingBinding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (activityPersonalVideoPlayingBinding.like.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(postid)
                            .child(firebaseUser.getUid()).setValue(true);
                    activityPersonalVideoPlayingBinding.like.setTag("liked");
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(postid).child(firebaseUser.getUid())
                            .removeValue();
                    activityPersonalVideoPlayingBinding.like.setTag("like");
                }

            }
        });

    }

    private void setIslike(String postid, ImageView imageView) {
        FirebaseUser firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference()
                .child("Likes").child(postid);
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseUser1.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.likethumb);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.thumb);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void numberoflike(String uid, TextView textView) {
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference()
                .child("Likes").child(uid);
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textView.setText(snapshot.getChildrenCount() + "like");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}