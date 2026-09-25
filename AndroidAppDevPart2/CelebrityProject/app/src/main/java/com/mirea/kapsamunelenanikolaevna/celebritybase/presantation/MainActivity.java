package com.mirea.kapsamunelenanikolaevna.celebritybase.presantation;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mirea.kapsamunelenanikolaevna.celebritybase.R;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.CelebrityRepoImpl;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.storage.ClientRepositoryImpl;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.models.Celebrity;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.repo.ClientRepository;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ClientRepository clientRepo = new ClientRepositoryImpl(this);
        String email = clientRepo.getClientEmail();
        String name = clientRepo.getClientName();
        boolean isGuest = clientRepo.isGuest();

        CelebrityRepoImpl repo = new CelebrityRepoImpl(this);

        // NetworkApi
        List<Celebrity> celebrities = repo.getCelebrities();
        StringBuilder networkInfo = new StringBuilder("2. NetworkApi\nНайдено: " + celebrities.size() + "\n");
        for (Celebrity c : celebrities) {
            networkInfo.append("• ").append(c.getName()).append("\n");
        }

        findViewById(android.R.id.content).postDelayed(() ->
                showBigToast(networkInfo.toString()), 3000);

        // Room
        findViewById(android.R.id.content).postDelayed(() -> {
            if (!celebrities.isEmpty()) {
                Celebrity first = celebrities.get(0);
                repo.addToFavorites(first);
                List<Celebrity> favorites = repo.getFavorites();

                showBigToast(
                        "3. Room\n" +
                                "Добавлен: " + first.getName() + "\n" +
                                "В избранном: " + favorites.size()
                );
            } else {
                showBigToast("3. Room\nСписок пуст");
            }
        }, 6000);

        // Поиск
        findViewById(android.R.id.content).postDelayed(() -> {
            List<Celebrity> found = repo.searchCelebrities("Зен");
            StringBuilder searchInfo = new StringBuilder("4. Поиск «Зен»\nНайдено: " + found.size() + "\n");
            for (Celebrity c : found) {
                searchInfo.append("• ").append(c.getName()).append("\n");
            }
            showBigToast(searchInfo.toString());
        }, 9000);
    }

    private void showBigToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_custom, null);

        TextView tv = layout.findViewById(R.id.tvToast);
        tv.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}