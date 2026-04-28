package com.mirea.kapsamunelenanikolaevna.mireaproject.practice7;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.mirea.kapsamunelenanikolaevna.mireaproject.databinding.FragmentTimeServiceBinding;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class TimeServiceFragment extends Fragment {
    private static final String TAG = "TimeServiceFragment";
    private FragmentTimeServiceBinding binding;

    String host = "time-a.nist.gov"; // или "time.nist.gov"
    private final int port = 13;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTimeServiceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonGetTime.setOnClickListener(v -> {
            GetTimeTask timeTask = new GetTimeTask();
            timeTask.execute();
        });
    }

    // Фоновый поток для выполнения сетевого запроса
    private class GetTimeTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.buttonGetTime.setEnabled(false);
            binding.textViewStatus.setText("Подключение к серверу времени...");
        }

        @Override
        protected String doInBackground(Void... params) {
            String timeResult = "";
            try {
                Socket socket = new Socket(host, port);
                BufferedReader reader = SocketUtils.getReader(socket);
                reader.readLine(); // игнорируем первую строку
                timeResult = reader.readLine(); // считываем вторую строку
                Log.d(TAG, "Ответ сервера: " + timeResult);
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "Ошибка подключения", e);
                timeResult = "ERROR: " + e.getMessage();
            }
            return timeResult;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            binding.buttonGetTime.setEnabled(true);

            if (result == null || result.isEmpty() || result.startsWith("ERROR")) {
                binding.textViewStatus.setText("Ошибка подключения");
                binding.textViewFullResponse.setText(result);
                Toast.makeText(getContext(), "Не удалось получить время", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.textViewFullResponse.setText(result);
            binding.textViewStatus.setText("Время получено успешно!");

            try {
                String[] parts = result.split(" ");
                if (parts.length >= 3) {
                    binding.textViewTime.setText("Время: " + parts[2]);
                    binding.textViewDate.setText("Дата: " + parts[1]);


                    Toast.makeText(getContext(), "Время получено: " + parts[2], Toast.LENGTH_SHORT).show();
                } else {
                    binding.textViewTime.setText("Ошибка парсинга");
                    binding.textViewDate.setText("Неверный формат");
                }
            } catch (Exception e) {
                binding.textViewTime.setText("Ошибка: " + e.getMessage());
                Log.e(TAG, "Ошибка парсинга", e);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}