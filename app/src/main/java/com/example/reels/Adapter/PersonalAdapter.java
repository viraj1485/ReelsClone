package com.example.reels.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.reels.Activity.PersonalVideoPlayingActivity;
import com.example.reels.Model.PostModel;
import com.example.reels.R;

import java.util.ArrayList;

public class PersonalAdapter extends RecyclerView.Adapter<PersonalAdapter.MyView> {
    ArrayList<PostModel> arr;
    Context context;

    public PersonalAdapter(ArrayList<PostModel> arr, Context context) {
        this.arr = arr;
        this.context = context;
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personalreel, parent, false);
        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {

        holder.textView.setText(arr.get(position).getDescription());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PersonalVideoPlayingActivity.class);
                intent.putExtra("desc", arr.get(position).getDescription());
                intent.putExtra("videourl", arr.get(position).getVideo());
                intent.putExtra("postid", arr.get(position).getPostuid());
                context.startActivity(intent);
            }
        });
        Glide.with(context).load(arr.get(position).getVideo()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (arr.size() == 0)
            Toast.makeText(context, "NO-POST TO VIEW", Toast.LENGTH_SHORT).show();
        return arr.size();
    }

    public class MyView extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textView;
        ImageView imageView;

        public MyView(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
            textView = itemView.findViewById(R.id.header);
            imageView = itemView.findViewById(R.id.img);
        }
    }
}
