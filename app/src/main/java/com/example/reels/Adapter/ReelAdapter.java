package com.example.reels.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reels.Model.PostModel;
import com.example.reels.Model.SignUp;
import com.example.reels.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReelAdapter extends RecyclerView.Adapter<ReelAdapter.MyViewClass> {
    List<PostModel> postModels;
    Context context;
    DatabaseReference databaseReference;
    ExoPlayer exoPlayer;

    boolean islike = false;
    BottomSheetBehavior bottomSheetBehavior;
    BottomSheetDialog bottomSheetDialog;
    View view1;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ArrayList<SignUp> arr;
    DisplayMetrics displayMetrics;
    RecyclerView recyclerView;


    public ReelAdapter(List<PostModel> postModels, Context context) {
        this.postModels = postModels;
        this.context = context;
        this.exoPlayer = exoPlayer;
    }

    @NonNull
    @Override
    public MyViewClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlereel, parent, false);
        return new MyViewClass(view);
    }

    @Override
    public void onViewRecycled(@NonNull MyViewClass holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewClass holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        PostModel postModel = postModels.get(position);
//        initializePlayer(Uri.parse(postModel.getVideo()), holder);
        holder.desc.setText(postModel.getDescription());
        if (postModel.getVideo() != null) {
            holder.videoView.setVideoURI(Uri.parse(postModel.getVideo()));
        }
        setIslike(postModel.getPostuid(), holder.like);
        numberoflike(postModel.getPostuid(), holder.likecount);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.like.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(postModel.getPostuid())
                            .child(firebaseUser.getUid()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(postModel.getPostuid()).child(firebaseUser.getUid())
                            .removeValue();
                    holder.like.setTag("like");
                }

            }
        });
        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                holder.progressBar.setVisibility(View.GONE);
                holder.videoView.start();
            }
        });
        holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                holder.videoView.start();
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*bottomSheetDialog = new BottomSheetDialog(context);
                view1 = LayoutInflater.from(context).inflate(R.layout.bottomsheetcomment, null, false);
                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.setTitle("Comments");
                bottomSheetBehavior = BottomSheetBehavior.from((View) view1.getParent());
                bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);


                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                int half = displayMetrics.heightPixels / 2;

                bottomSheetBehavior.setMaxHeight(half);
                bottomSheetDialog.show();*/
                Toast.makeText(context, "Under-Progress", Toast.LENGTH_SHORT).show();
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog = new BottomSheetDialog(context);
                view1 = LayoutInflater.from(context).inflate(R.layout.bottomsheetforshare, null, false);
                bottomSheetDialog.setTitle("Persons");
                bottomSheetDialog.setContentView(view1);
                recyclerView = bottomSheetDialog.findViewById(R.id.recycleshare);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                readUsers();
                recyclerView.setAdapter(new shareAdapter(arr, context));
                displayMetrics = context.getResources().getDisplayMetrics();
                bottomSheetBehavior = BottomSheetBehavior.from((View) view1.getParent());
                bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
                int half = displayMetrics.heightPixels / 2;
                bottomSheetBehavior.setMaxHeight(half);
                bottomSheetDialog.show();
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(postModel.getPostuid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("username").getValue(String.class);
                holder.textView.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return postModels.size();
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

    private void readUsers() {
        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        arr = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arr.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    SignUp usersModel = data.getValue(SignUp.class);
                    arr.add(usersModel);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public class MyViewClass extends RecyclerView.ViewHolder {


        TextView textView, desc;
        VideoView videoView;
        ProgressBar progressBar;
        ImageView like, comment;
        ImageView share;
        TextView likecount;

        public MyViewClass(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.singlereel);
            textView = itemView.findViewById(R.id.usesrname);
            desc = itemView.findViewById(R.id.description);
            progressBar = itemView.findViewById(R.id.progressbar);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            share = itemView.findViewById(R.id.share);
            likecount = itemView.findViewById(R.id.likecount);
        }
    }


}
