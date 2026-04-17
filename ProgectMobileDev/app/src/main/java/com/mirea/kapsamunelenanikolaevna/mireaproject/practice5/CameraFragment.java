package com.mirea.kapsamunelenanikolaevna.mireaproject.practice5;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.mirea.kapsamunelenanikolaevna.mireaproject.databinding.FragmentCameraBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class CameraFragment extends Fragment {

    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final String TAG = "CameraFragment";

    private FragmentCameraBinding binding;

    private boolean isWork = false;
    private Uri imageUri;
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;

    private String[] quotes = {
            "Отличный кадр",
            "Ты настоящий фотограф",
            "Фотошедевр",
            "У тебя талант",
            "Твой потенциал безграничен"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityResultCallback<ActivityResult> callback = new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (imageUri != null) {
                        binding.ivPhoto.setImageURI(imageUri);
                        binding.ivPhoto.setVisibility(View.VISIBLE);

                        showRandomQuote();

                        Toast.makeText(getContext(), "Фото сохранено!",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d(TAG, "Фото не сделано, resultCode=" + result.getResultCode());
                    showRandomQuote();
                }
            }
        };

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCameraBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showRandomQuote();

        checkPermissions();

        binding.btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Кнопка 'Сделать фото' нажата!");
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (isWork) {
                    try {
                        File photoFile = createImageFile();
                        Log.d(TAG, "Файл создан: " + photoFile.getAbsolutePath());

                        // Получаем безопасный URI через FileProvider
                        String authorities = requireContext().getPackageName() + ".fileprovider";
                        imageUri = FileProvider.getUriForFile(requireContext(), authorities, photoFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        cameraActivityResultLauncher.launch(cameraIntent);
                        Log.d(TAG, "Камера запущена");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Ошибка создания файла", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Ошибка создания файла", e);
                    }
                } else {
                    Log.d(TAG, "Нет разрешений, запрашиваем...");
                    checkPermissions();
                }
            }
        });


        binding.btnNewQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRandomQuote();
            }
        });
    }

    private void showRandomQuote() {
        Random random = new Random();
        int randomIndex = random.nextInt(quotes.length);
        binding.tvQuote.setText(quotes[randomIndex]);
    }

    private void checkPermissions() {
        int cameraPermissionStatus = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CAMERA);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if ((cameraPermissionStatus == PackageManager.PERMISSION_GRANTED)
                && (storagePermissionStatus == PackageManager.PERMISSION_GRANTED)) {
            isWork = true;
            Toast.makeText(getContext(), "Разрешения есть", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "Запрашиваем разрешения...");
            requestPermissions(new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult вызван, requestCode=" + requestCode);

        if (requestCode == REQUEST_CODE_PERMISSION) {
            // permission granted
            isWork = grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Производится генерирование имени файла на основе текущего времени и создание файла
     * в директории Pictures на ExternalStorage.
     * @return File возвращается объект File.
     * @exception IOException если возвращается ошибка записи в файл
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (storageDirectory != null && !storageDirectory.exists()) {
            storageDirectory.mkdirs();
        }

        return File.createTempFile(imageFileName, ".jpg", storageDirectory);
    }
}