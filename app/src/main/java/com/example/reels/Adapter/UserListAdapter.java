package com.example.reels.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.reels.Activity.PersonalReel;
import com.example.reels.Model.SignUp;
import com.example.reels.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyView> {
    List<SignUp> signUps;
    Context context;
    FirebaseUser firebaseUser;

    public UserListAdapter(List<SignUp> signUps, Context context) {
        this.signUps = signUps;
        this.context = context;
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlistlayout, parent, false);
        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        holder.follow.setVisibility(View.VISIBLE);
        SignUp signUp = signUps.get(position);
        if (signUp.getUid().equals(firebaseUser.getUid())) {
            holder.follow.setVisibility(View.GONE);
        }
        isFollowing(signUp.getUid(), holder.follow);
        holder.fullname.setText(signUp.getFullname());
        holder.username.setText(signUp.getUsername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PersonalReel.class);
                intent.putExtra("uid", signUp.getUid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return signUps.size();
    }

    public class MyView extends RecyclerView.ViewHolder {

        TextView fullname, username;
        Button follow;

        public MyView(@NonNull View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.fullname);
            username = itemView.findViewById(R.id.username);
            follow = itemView.findViewById(R.id.follow);
        }
    }
    public void isFollowing(String userid, Button button) {
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(userid).exists()) {
                    button.setText("Following");
                } else {
                    button.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
