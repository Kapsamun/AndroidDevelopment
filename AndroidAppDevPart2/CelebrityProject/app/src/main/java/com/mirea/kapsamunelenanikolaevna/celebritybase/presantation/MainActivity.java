package com.mirea.kapsamunelenanikolaevna.celebritybase.presantation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mirea.kapsamunelenanikolaevna.celebritybase.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);

        if (savedInstanceState == null) {
            openFragment(new CelebritiesListFragment(), false);
        }

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_list) {
                // очищаем стек детали и показываем список
                getSupportFragmentManager().popBackStack(null,
                        androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                openFragment(new CelebritiesListFragment(), false);
                return true;
            } else if (id == R.id.nav_profile) {
                getSupportFragmentManager().popBackStack(null,
                        androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                openFragment(new ProfileFragment(), false);
                return true;
            }
            return false;
        });
    }

    private void openFragment(Fragment fragment, boolean addToBackStack) {
        var tx = getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, fragment);
        if (addToBackStack) {
            tx.addToBackStack(null);
        }
        tx.commit();
    }
}