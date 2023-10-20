package com.example.reels.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reels.Fragment.HomeFragment;
import com.example.reels.R;
import com.example.reels.databinding.ActivityPostBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class POstActivity extends AppCompatActivity {

    ActivityPostBinding activityPostBinding;
    ExoPlayer exoPlayer;
    MediaItem mediaItem;
    TextView textView;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPostBinding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(activityPostBinding.getRoot());
        exoPlayer = new ExoPlayer.Builder(POstActivity.this).build();
        activityPostBinding.exoplayer.setPlayer(exoPlayer);
        activityPostBinding.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 100);
            }
        });
        textView = findViewById(R.id.tabname);
        textView.setText("Post");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            activityPostBinding.exoplayer.setVisibility(View.VISIBLE);
            uri = data.getData();
            mediaItem = MediaItem.fromUri(data.getData());
            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.prepare();
            activityPostBinding.sendpost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (data.getData() == null) {
                        Toast.makeText(POstActivity.this, "Please 1st Select Post", Toast.LENGTH_SHORT).show();
                    } else {
                        uploadVideo(data.getData());
                        activityPostBinding.progresspost.setVisibility(View.VISIBLE);
                    }

                }
            });
        }

    }

    public void uploadVideo(Uri uri) {
        String CurrentTime = "" + System.currentTimeMillis();
        String VedioFile = "ChatVideo/" + "Video" + CurrentTime;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(VedioFile);
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                while (!task.isSuccessful()) ;
                String downloadvideo = task.getResult().toString();
                String desc = activityPostBinding.desciption12.getText().toString();
                if (task.isSuccessful()) {

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Video");
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("Video", downloadvideo);
                    hashMap.put("description", activityPostBinding.desciption12.getText().toString());
                    hashMap.put("postuid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    databaseReference.push().setValue(hashMap);
                    Toast.makeText(POstActivity.this, "Video Uploaded SuccessFully", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(POstActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}