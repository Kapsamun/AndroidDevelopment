package com.mirea.kapsamunelenanikolaevna.mireaproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.work.*;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.UUID;

public class BackgroundTaskFragment extends Fragment {

    private static final String TAG = "BackgroundTaskFragment";
    private TextView infoTextView;
    private TextInputEditText editTextOperations;
    private TextInputEditText editTextDelay;
    private MaterialButton startTaskButton;

    private WorkManager workManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_background_task, container, false);

        // Инициализация View
        infoTextView = view.findViewById(R.id.infoTextView);
        editTextOperations = view.findViewById(R.id.editTextOperations);
        editTextDelay = view.findViewById(R.id.editTextDelay);
        startTaskButton = view.findViewById(R.id.startTaskButton);

        // Инициализация WorkManager
        workManager = WorkManager.getInstance(requireContext());

        startTaskButton.setOnClickListener(v -> startBackgroundTask());

        return view;
    }

    private void startBackgroundTask() {
        // Получаем данные из полей ввода
        String operationsStr = editTextOperations.getText().toString();
        String delayStr = editTextDelay.getText().toString();

        try {
            int operationsCount = Integer.parseInt(operationsStr);
            int delaySeconds = Integer.parseInt(delayStr);

            if (operationsCount <= 0 || delaySeconds <= 0) {
                infoTextView.setText("Значения должны быть больше 0!");
                return;
            }

            if (operationsCount > 10) {
                infoTextView.setText("Слишком много операций! Максимум 10");
                return;
            }

            workManager.cancelAllWorkByTag("background_task");

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Подготавливаем входные данные
            Data inputData = new Data.Builder()
                    .putInt(BackgroundWorker.KEY_OPERATIONS_COUNT, operationsCount)
                    .putInt(BackgroundWorker.KEY_DELAY_SECONDS, delaySeconds)
                    .build();

            // Создаем задачу
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(BackgroundWorker.class)
                    .setInputData(inputData)
                    .addTag("background_task")
                    .build();

            // Запускаем задачу
            workManager.enqueue(workRequest);

            // Обновляем UI
            infoTextView.setText("Фоновая задача запущена\n" + "Операций: " + operationsCount + "\n" + "Задержка: " + delaySeconds + " сек.\n" );
            startTaskButton.setEnabled(false);

            Toast.makeText(getContext(), "Фоновая задача запущена!", Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            infoTextView.setText("Введите корректные числа!");
            Toast.makeText(getContext(), "Ошибка ввода данных", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}