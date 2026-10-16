package com.mirea.kapsamunelenanikolaevna.resultapifragmentapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

 //Настраиваем слушатель результатов на уровне главного FragmentManager'а экрана
        getSupportFragmentManager().setFragmentResultListener(
                "requestKey",
                this,
                (requestKey, bundle) -> {
                    String result = bundle.getString("key");
                    Log.d("MainActivity", "I'm MainActivity " + result);
                }
        );

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, new DataFragment())
                    .commit();
        }
    }
}