package com.mirea.kapsamunelenanikolaevna.mireaproject.practice6;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mirea.kapsamunelenanikolaevna.mireaproject.databinding.FragmentFileWorkBinding;
import com.mirea.kapsamunelenanikolaevna.mireaproject.databinding.DialogAddNoteBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class FileWorkFragment extends Fragment {

    private FragmentFileWorkBinding binding;
    private static final int REQUEST_CODE_STORAGE = 100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFileWorkBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkPermission();


        binding.btnLoad.setOnClickListener(v -> loadNote());

        // FAB открывает диалоговое окно для создания заметки
        FloatingActionButton fab = binding.fabAddNote;
        fab.setOnClickListener(v -> showAddNoteDialog());
    }

    // Проверка разрешения
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Разрешение получено", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Разрешение не получено", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Проверка доступности хранилища
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    // Получить директорию Documents
    private File getDocumentsDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
    }

    private void loadNote() {
        String title = binding.etNoteTitle.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            binding.etNoteTitle.setError("Введите название заметки для загрузки");
            return;
        }

        try {
            File dir = getDocumentsDirectory();
            String fileName = title + ".txt";
            File file = new File(dir, fileName);

            if (!file.exists()) {
                Toast.makeText(getContext(), "Заметка \"" + title + "\" не найдена", Toast.LENGTH_LONG).show();
                binding.etNoteContent.setText("");
                return;
            }

            FileInputStream fis = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuilder content = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();

            binding.etNoteContent.setText(content.toString());
            Toast.makeText(getContext(), "Заметка \"" + title + "\" загружена!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка чтения: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Показать диалоговое окно для создания заметки
    private void showAddNoteDialog() {
        DialogAddNoteBinding dialogBinding = DialogAddNoteBinding.inflate(getLayoutInflater());

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Новая заметка")
                .setView(dialogBinding.getRoot());

        AlertDialog dialog = builder.create();

        // Кнопка сохранения
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Сохранить", (d, which) -> {
            String title = dialogBinding.etDialogTitle.getText().toString().trim();
            String content = dialogBinding.etDialogContent.getText().toString().trim();

            if (TextUtils.isEmpty(title)) {
                dialogBinding.etDialogTitle.setError("Введите название заметки");
                return;
            }

            if (TextUtils.isEmpty(content)) {
                dialogBinding.etDialogContent.setError("Введите текст заметки");
                return;
            }

            saveNewNote(title, content);
            dialog.dismiss();
        });

        // Кнопка отмены
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Отмена", (d, which) -> dialog.dismiss());

        dialog.show();
    }

    // Сохранение новой заметки из диалога
    private void saveNewNote(String title, String content) {
        if (!isExternalStorageWritable()) {
            Toast.makeText(getContext(), "Хранилище недоступно", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            File dir = getDocumentsDirectory();
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = title + ".txt";
            File file = new File(dir, fileName);

            // Проверяем, существует ли уже файл с таким названием
            if (file.exists()) {
                Toast.makeText(getContext(), "Заметка с таким названием уже существует!", Toast.LENGTH_LONG).show();
                return;
            }

            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(content);
            writer.close();

            Toast.makeText(getContext(), "Заметка \"" + title + "\" сохранена!", Toast.LENGTH_LONG).show();

            // Очищаем поля
            binding.etNoteTitle.setText("");
            binding.etNoteContent.setText("");

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}