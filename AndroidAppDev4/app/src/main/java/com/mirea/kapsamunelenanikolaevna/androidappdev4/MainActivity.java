package com.mirea.kapsamunelenanikolaevna.androidappdev4;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.mirea.kapsamunelenanikolaevna.androidappdev4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private Fragment fragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fragment = new music_fragmentbinding();

        binding.editTextText.setText("Мой номер по списку №9");

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(MainActivity.class.getSimpleName(), "onClickListener");
            }
        });
    }
    public void onButtonMusicClick(View view){
        Log.i(MainActivity.class.getSimpleName(), "MusicFragment");
        fragmentManager = getSupportFragmentManager();
        if (view.getId() == R.id.button2){
            fragmentManager.beginTransaction().replace(R.id.main, fragment).commit();
        }
    }

}