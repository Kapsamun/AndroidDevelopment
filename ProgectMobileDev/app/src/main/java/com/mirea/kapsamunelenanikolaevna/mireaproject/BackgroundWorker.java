package com.mirea.kapsamunelenanikolaevna.mireaproject;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.concurrent.TimeUnit;

public class BackgroundWorker extends Worker {

    private static final String TAG = "BackgroundWorker";

    // Ключи для передачи данных
    public static final String KEY_OPERATIONS_COUNT = "operations_count";
    public static final String KEY_DELAY_SECONDS = "delay_seconds";
    public static final String KEY_RESULT = "result";

    public BackgroundWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: Фоновая задача запущена в потоке: " + Thread.currentThread().getName());

        try {
            // Получаем входные данные
            Data inputData = getInputData();
            int operationsCount = inputData.getInt(KEY_OPERATIONS_COUNT, 5);
            int delaySeconds = inputData.getInt(KEY_DELAY_SECONDS, 1);

            Log.d(TAG, "doWork: operationsCount=" + operationsCount + ", delaySeconds=" + delaySeconds);

            // Имитируем выполнение фоновой задачи
            for (int i = 1; i <= operationsCount; i++) {
                // Проверяем, не была ли задача отменена
                if (isStopped()) {
                    Log.d(TAG, "doWork: Задача была отменена");
                    return Result.failure();
                }

                // Имитация длительной операции
                Log.d(TAG, "doWork: Выполняется операция " + i + " из " + operationsCount);
                TimeUnit.SECONDS.sleep(delaySeconds);
            }

            // Формируем результат
            String result = String.format("Фоновая задача выполнена!\n" + "Выполнено операций: %d\n" + "Задержка: %d сек/операция\n",
                    operationsCount, delaySeconds, operationsCount * delaySeconds, Thread.currentThread().getName());

            Data outputData = new Data.Builder()
                    .putString(KEY_RESULT, result)
                    .build();

            Log.d(TAG, "doWork: Задача успешно завершена");
            return Result.success(outputData);

        } catch (Exception e) {
            Log.e(TAG, "doWork: Ошибка выполнения", e);
            return Result.failure();
        }
    }
}