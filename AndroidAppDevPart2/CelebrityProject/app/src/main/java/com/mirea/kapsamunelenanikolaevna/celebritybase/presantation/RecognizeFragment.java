package com.mirea.kapsamunelenanikolaevna.celebritybase.presantation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mirea.kapsamunelenanikolaevna.celebritybase.databinding.FragmentRecognizeBinding;
import com.mirea.kapsamunelenanikolaevna.celebritybase.presantation.CelebrityClassifier;

import java.io.InputStream;

public class RecognizeFragment extends Fragment {

    private FragmentRecognizeBinding binding;
    private Bitmap selectedBitmap;
    private CelebrityClassifier classifier;

    private final ActivityResultLauncher<String> pickImage =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) loadImage(uri);
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRecognizeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            classifier = new CelebrityClassifier(requireContext());
        } catch (Exception e) {
            binding.textResult.setText("Ошибка загрузки модели: " + e.getMessage()
                    + "\nПроверьте, что в assets лежат mobilenet_v1_1.0_224_quant.tflite и labels.txt");
        }

        binding.buttonPickImage.setOnClickListener(v ->
                pickImage.launch("image/*"));

        binding.buttonRecognize.setOnClickListener(v -> {
            if (selectedBitmap == null || classifier == null) {
                Toast.makeText(requireContext(), "Сначала выберите фото", Toast.LENGTH_SHORT).show();
                return;
            }
            binding.textResult.setText("Распознавание...");
            new Thread(() -> {
                String result = classifier.classify(selectedBitmap);
                requireActivity().runOnUiThread(() ->
                        binding.textResult.setText(result));
            }).start();
        });
    }

    private void loadImage(Uri uri) {
        try {
            InputStream is = requireContext().getContentResolver().openInputStream(uri);
            selectedBitmap = BitmapFactory.decodeStream(is);
            if (is != null) is.close();
            binding.imagePreview.setImageBitmap(selectedBitmap);
            binding.buttonRecognize.setEnabled(true);
            binding.textResult.setText("Фото загружено. Нажмите «Распознать»");
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Не удалось открыть фото", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (classifier != null) {
            classifier.close();
            classifier = null;
        }
        binding = null;
    }
}