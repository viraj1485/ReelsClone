package com.example.reels.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.reels.Model.SignUp;
import com.example.reels.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class shareAdapter extends RecyclerView.Adapter<shareAdapter.MyView> {
    List<SignUp> signUps;
    Context context;
    FirebaseUser firebaseUser;

    public shareAdapter(List<SignUp> signUps, Context context) {
        this.signUps = signUps;
        this.context = context;
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sendinglayout, parent, false);
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
        holder.fullname.setText(signUp.getFullname());
        holder.username.setText(signUp.getUsername());
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
}

