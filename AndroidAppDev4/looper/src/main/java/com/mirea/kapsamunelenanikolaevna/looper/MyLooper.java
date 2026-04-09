package com.mirea.kapsamunelenanikolaevna.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MyLooper extends Thread {
    public Handler mHandler;
    private Handler mainHandler;

    public MyLooper(Handler mainThreadHandler) {
        mainHandler = mainThreadHandler;
    }

    public void run() {
        Log.d("MyLooper", "Запуск Looper потока");
        Looper.prepare();

        mHandler = new Handler(Looper.myLooper()) {
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String key = bundle.getString("KEY");

                Log.d("MyLooper", "Получено сообщение с ключом: " + key);


                if ("mirea".equals(key)) {
                    Log.d("MyLooper", "Обработка демо-сообщения mirea");
                    String data = "mirea";
                    int count = data.length();

                    Message message = Message.obtain();
                    Bundle resultBundle = new Bundle();
                    resultBundle.putString("result", String.format("Количество букв в слове %s: %d", data, count));
                    message.setData(resultBundle);
                    mainHandler.sendMessage(message);
                    Log.d("MyLooper", "Демо-результат отправлен");
                }

                // Обработка основной задачи (возраст и профессия)
                if ("myTask".equals(key)) {
                    int age = bundle.getInt("age");
                    String job = bundle.getString("job");

                    Log.d("MyLooper", "Обработка: возраст=" + age + ", профессия=" + job);

                    try {
                        // Задержка на количество секунд, равное возрасту
                        Log.d("MyLooper", "Начинаю задержку на " + age + " секунд");
                        Thread.sleep(age * 1000L);
                        Log.d("MyLooper", "Задержка завершена");

                        String result = String.format("Вам %d лет, Вы работаете %s. Задержка составила %d секунд.",age, job, age);

                        // Отправляем результат обратно в главный поток
                        Message message = Message.obtain();
                        Bundle resultBundle = new Bundle();
                        resultBundle.putString("result", result);
                        message.setData(resultBundle);
                        mainHandler.sendMessage(message);

                        Log.d("MyLooper", "Результат отправлен в главный поток: " + result);

                    } catch (InterruptedException e) {
                        Log.e("MyLooper", "Ошибка при задержке: " + e.getMessage());
                        e.printStackTrace();

                        // Отправляем сообщение об ошибке
                        Message message = Message.obtain();
                        Bundle resultBundle = new Bundle();
                        resultBundle.putString("result", "Ошибка при обработке данных!");
                        message.setData(resultBundle);
                        mainHandler.sendMessage(message);
                    }
                }
            }
        };

        Looper.loop();
        Log.d("MyLooper", "Looper завершил работу");
    }
}