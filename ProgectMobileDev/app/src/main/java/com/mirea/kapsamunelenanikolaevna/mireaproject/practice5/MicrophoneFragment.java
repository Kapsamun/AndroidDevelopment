package com.mirea.kapsamunelenanikolaevna.mireaproject.practice5;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.mirea.kapsamunelenanikolaevna.mireaproject.databinding.FragmentMicrophoneBinding;

import java.io.File;
import java.io.IOException;

public class MicrophoneFragment extends Fragment {

    private static final String TAG = "MicrophoneFragment";

    private FragmentMicrophoneBinding binding;
    private String recordFilePath = null;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private boolean isStartRecording = true;
    private boolean isStartPlaying = true;

    private ActivityResultLauncher<String> permissionLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        binding.btnStartRecording.setEnabled(true);
                        binding.btnStartRecording.setText("Начать запись");
                        binding.tvStatus.setText("Микрофон доступен");
                        binding.tvAdvice.setText("Нажмите 'Начать запись' для записи");
                        isStartRecording = true;
                        Toast.makeText(getContext(), "Разрешение получено", Toast.LENGTH_SHORT).show();
                    } else {
                        binding.btnStartRecording.setEnabled(false);
                        binding.tvStatus.setText("Нет разрешения на микрофон");
                        binding.tvAdvice.setText("Разрешите доступ к микрофону в настройках");
                        Toast.makeText(getContext(), "Нужно разрешение на микрофон", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMicrophoneBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recordFilePath = (new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "audiorecordtest.3gp")).getAbsolutePath();

        binding.btnPlayRecording.setEnabled(false);
        binding.btnStartRecording.setEnabled(false);

        binding.btnStartRecording.setText("Начать запись");
        binding.btnPlayRecording.setText("Начать проигрывание");

        // Проверяем разрешение
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            binding.btnStartRecording.setEnabled(true);
            binding.tvStatus.setText("Микрофон доступен");
            binding.tvAdvice.setText("Нажмите 'Начать запись' для записи");
        } else {
            binding.tvStatus.setText("Запрашиваем разрешение...");
            binding.tvAdvice.setText("Разрешите доступ к микрофону");
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }

        binding.btnStartRecording.setOnClickListener(v -> {
            if (isStartRecording) {
                binding.btnStartRecording.setText("Остановить запись");
                binding.btnPlayRecording.setEnabled(false);
                binding.tvStatus.setText(" Идет запись...");
                binding.tvAdvice.setText("Говорите, запись активна");
                startRecording();
            } else {
                binding.btnStartRecording.setText("Начать запись");
                binding.btnPlayRecording.setEnabled(true);
                binding.tvStatus.setText("Запись сохранена");
                binding.tvAdvice.setText("Запись сохранена. Можете воспроизвести");
                stopRecording();
            }
            isStartRecording = !isStartRecording;
        });

        // Кнопка воспроизведения
        binding.btnPlayRecording.setOnClickListener(v -> {
            if (isStartPlaying) {
                binding.btnPlayRecording.setText("Остановить воспроизведение");
                binding.btnStartRecording.setEnabled(false);
                binding.tvStatus.setText("Воспроизведение записи");
                binding.tvAdvice.setText("Слушаем запись");
                startPlaying();
            } else {
                binding.btnPlayRecording.setText("Начать воспроизведение");
                binding.btnStartRecording.setEnabled(true);
                binding.tvStatus.setText("Воспроизведение остановлено");
                binding.tvAdvice.setText("Нажмите 'Начать запись' для новой записи");
                stopPlaying();
            }
            isStartPlaying = !isStartPlaying;
        });
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC); //источник звука
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(recordFilePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); //значение кодекса (частота дискретизации 8 кГц)

        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            binding.tvStatus.setText("Ошибка записи");
            binding.tvAdvice.setText("Не удалось начать запись");
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
                recorder = null;
            } catch (RuntimeException e) {
                binding.tvStatus.setText("Ошибка при остановке");
            }
        }
    }

    private void startPlaying() {
        File file = new File(recordFilePath);
        if (!file.exists()) {
            binding.tvStatus.setText("Нет записи");
            binding.tvAdvice.setText("Сначала сделайте запись");
            return;
        }

        player = new MediaPlayer();
        try {
            player.setDataSource(recordFilePath);
            player.prepare();
            player.start();

            player.setOnCompletionListener(mp -> {
                stopPlaying();
                binding.btnPlayRecording.setText("Воспроизвести");
                binding.btnStartRecording.setEnabled(true);
                binding.tvStatus.setText("Воспроизведение завершено");
                binding.tvAdvice.setText("Нажмите 'Начать запись' для новой записи");
                isStartPlaying = true;
            });

        } catch (IOException e) {
            binding.tvStatus.setText("Ошибка воспроизведения");
            binding.tvAdvice.setText("Файл поврежден");
        }
    }

    private void stopPlaying() {
        if (player != null) {
            try {
                player.stop();
                player.release();
                player = null;
            } catch (Exception e) {
                Log.e(TAG, "player.stop() failed", e);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}