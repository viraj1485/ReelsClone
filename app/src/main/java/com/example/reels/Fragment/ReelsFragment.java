package com.example.reels.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reels.Adapter.ReelAdapter;
import com.example.reels.Adapter.UserListAdapter;
import com.example.reels.Model.PostModel;
import com.example.reels.Model.SignUp;
import com.example.reels.R;
import com.example.reels.databinding.FragmentReelsBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ReelsFragment extends Fragment {

    FragmentReelsBinding reelsBinding;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ArrayList<PostModel> arr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        reelsBinding = FragmentReelsBinding.inflate(inflater, container, false);
        View view = getActivity().findViewById(R.id.tool);
        view.setVisibility(View.GONE);
        readUsers();

        return reelsBinding.getRoot();
    }

    private void readUsers() {
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
                reelsBinding.viewpager.setAdapter(new ReelAdapter(arr, getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}