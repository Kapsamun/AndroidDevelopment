package com.mirea.kapsamunelenanikolaevna.buttonclicker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private TextView textViewStudent;
    private Button btnWhoAmI;
    private Button btnIsNotMe;
    private CheckBox chBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        textViewStudent = findViewById(R.id.tvOut);
        btnWhoAmI = findViewById(R.id.btnWhoAmI);
        btnIsNotMe = findViewById(R.id.btnItIsNotMe);
        chBox = findViewById(R.id.checkBox);

        View.OnClickListener octBtnWhoAmI = new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                textViewStudent.setText("Мой номер по списку № 9");
                chBox.setChecked(true);
            }
        };
        btnWhoAmI.setOnClickListener(octBtnWhoAmI);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }
    public  void onMyButtonClick(View view)
    {
        textViewStudent.setText("Это не я сделал!");
        chBox.setChecked(false);

    }
}