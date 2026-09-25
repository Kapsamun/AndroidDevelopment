package com.mirea.kapsamunelenanikolaevna.scrollviewapp;

import android.os.Bundle;import android.view.View;import android.widget.LinearLayout;import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout wrapper = findViewById(R.id.wrapper);

        // Геометрическая прогрессия: a_n = 1 * 2^(n-1)
        // 1, 2, 4, 8, 16, ... до 100-го элемента
        long value = 1; // первый член

        for (int i = 1; i <= 100; i++) {
            View view = getLayoutInflater().inflate(R.layout.item, null, false);

            TextView text = view.findViewById(R.id.textView);
            text.setText(String.format("Элемент %d: %d", i, value));

            wrapper.addView(view);

            // следующий член прогрессии
            value *= 2;
        }
    }
}