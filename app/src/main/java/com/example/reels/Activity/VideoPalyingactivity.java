package com.example.reels.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.reels.Fragment.HomeFragment;
import com.example.reels.Fragment.ReelsFragment;
import com.example.reels.Fragment.SearchFragment;
import com.example.reels.R;
import com.example.reels.databinding.ActivityVideoPalyingactivityBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class VideoPalyingactivity extends AppCompatActivity {

    ActivityVideoPalyingactivityBinding videoPalyingactivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoPalyingactivity = ActivityVideoPalyingactivityBinding.inflate(getLayoutInflater());
        setContentView(videoPalyingactivity.getRoot());
        this.getWindow().setStatusBarColor(Color.parseColor("#4AA7F3"));
        getSupportFragmentManager().beginTransaction().add(R.id.container, new HomeFragment()).commit();

        videoPalyingactivity.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Search:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SearchFragment()).commit();
                        return true;
                    case R.id.Reels:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ReelsFragment()).commit();
                        return true;
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
                        return true;
                }
                return false;
            }
        });
    }

}