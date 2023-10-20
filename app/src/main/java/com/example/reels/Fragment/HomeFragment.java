package com.example.reels.Fragment;
import static android.content.Context.MODE_PRIVATE;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.reels.Activity.MainActivity;
import com.example.reels.Activity.POstActivity;
import com.example.reels.R;
import com.example.reels.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class HomeFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FragmentHomeBinding homeBinding;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        textView = getActivity().findViewById(R.id.tabname);
        textView.setText("Home");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String email = snapshot.child("email").getValue(String.class);
                String fullname = snapshot.child("fullname").getValue(String.class);
                String passord = snapshot.child("passord").getValue(String.class);
                String username = snapshot.child("username").getValue(String.class);

                homeBinding.username.setText("Username - " + fullname);
                homeBinding.email.setText("Email - " + email);
                homeBinding.mobileno.setText("FullName - " + fullname);
                homeBinding.password.setText("Password - " + passord);
                homeBinding.username.setText("Username - " + username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        homeBinding.Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), POstActivity.class);
                getActivity().startActivity(intent);
            }
        });
        homeBinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("Flag", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("login", false);
                editor.apply();

                Toast.makeText(getContext(), "User Log Out SuccessFully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
//        View view = inflater.inflate(R.layout.fragment_home, container, false);

        return homeBinding.getRoot();
    }

}