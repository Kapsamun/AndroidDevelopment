package com.mirea.kapsamunelenanikolaevna.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mirea.kapsamunelenanikolaevna.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MyLooper myLooper;
    private EditText editTextAge;
    private EditText editTextJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        editTextAge = binding.editTextAge;
        editTextJob = binding.editTextJob;

        // Handler для получения результатов из фонового потока
        Handler mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Bundle bundle = msg.getData();
                String result = bundle.getString("result");
                Log.d(MainActivity.class.getSimpleName(), "Получен результат: " + result);
                binding.textView.setText(result);
            }
        };

        myLooper = new MyLooper(mainThreadHandler);
        myLooper.start();

        binding.textView.setText("Мой номер по списку № 9");
    }

    public void onButtonClick(View view) {
        Message message = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putString("KEY", "mirea");
        message.setData(bundle);
        myLooper.mHandler.sendMessage(message);
        Log.d("MainActivity", "Отправлено демо-сообщение");
    }

    public void onButtonSendMessage(View view) {
        String ageStr = editTextAge.getText().toString();
        String job = editTextJob.getText().toString();

        if (ageStr.isEmpty() || job.isEmpty()) {
            Log.d("MainActivity", "Поля не заполнены");
            binding.textView.setText("Пожалуйста, заполните оба поля!");
            return;
        }

            int age = Integer.parseInt(ageStr);

            if (age <= 0) {
                binding.textView.setText("Возраст должен быть больше 0!");
                return;
            }

            // Создаем сообщение с данными
            Message message = Message.obtain();
            Bundle bundle = new Bundle(); //контейнер данных
            bundle.putString("KEY", "myTask");
            bundle.putInt("age", age);
            bundle.putString("job", job);
            message.setData(bundle);

            // Отправляем сообщение в фоновый поток
            myLooper.mHandler.sendMessage(message); //сообщение в очереди

            Log.d("MainActivity", "Отправлено сообщение: возраст=" + age + ", профессия=" + job);
            binding.textView.setText("Задержка " + age + " секунд");

    }

}