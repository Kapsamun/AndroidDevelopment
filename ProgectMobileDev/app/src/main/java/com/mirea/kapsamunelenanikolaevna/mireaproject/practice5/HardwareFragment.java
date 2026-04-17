package com.mirea.kapsamunelenanikolaevna.mireaproject.practice5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.mirea.kapsamunelenanikolaevna.mireaproject.R;
import com.mirea.kapsamunelenanikolaevna.mireaproject.databinding.FragmentHardwareBinding;

public class HardwareFragment extends Fragment {

    private FragmentHardwareBinding binding;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHardwareBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Инициализируем NavController
        navController = Navigation.findNavController(view);


        binding.btnSensor.setOnClickListener(v -> {
            navController.navigate(R.id.action_hardwareFragment_to_sensorFragment);
        });

        binding.btnCamera.setOnClickListener(v -> {
            navController.navigate(R.id.action_hardwareFragment_to_cameraFragment);
        });

        binding.btnMicrophone.setOnClickListener(v -> {
            navController.navigate(R.id.action_hardwareFragment_to_microphoneFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}