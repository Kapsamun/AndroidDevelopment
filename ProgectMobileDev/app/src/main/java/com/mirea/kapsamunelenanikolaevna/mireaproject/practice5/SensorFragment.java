package com.mirea.kapsamunelenanikolaevna.mireaproject.practice5;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mirea.kapsamunelenanikolaevna.mireaproject.databinding.FragmentSensorBinding;

public class SensorFragment extends Fragment implements SensorEventListener {

    private FragmentSensorBinding binding;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    private float currentAltitude = 0;
    private float targetAltitude = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSensorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL); //delay_normal - стандартная частота обновлений
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            binding.tvXValue.setText(String.format("%.2f м/с²", x));
            binding.tvYValue.setText(String.format("%.2f м/с²", y));
            binding.tvZValue.setText(String.format("%.2f м/с²", z));

            calculateAltitude(x, y);
            giveAdvice();
        }
    }

    private void calculateAltitude(float x, float y) {
        // Вычисляем угол наклона устройства
        double tiltAngle = Math.sqrt(x * x + y * y);

        // Переводим угол в высоту
        targetAltitude = (float) (tiltAngle / 20.0 * 8000);

        // Плавное изменение высоты
        currentAltitude = currentAltitude * 0.9f + targetAltitude * 0.1f;

        if (currentAltitude < 500) {
            binding.tvAltitude.setText(String.format("Высота: %.0f м (Низкогорье)", currentAltitude));
        } else if (currentAltitude < 2500) {
            binding.tvAltitude.setText(String.format("Высота: %.0f м (Среднегорье)", currentAltitude));
        } else if (currentAltitude < 4500) {
            binding.tvAltitude.setText(String.format("Высота: %.0f м (Высокогорье)", currentAltitude));
        } else {
            binding.tvAltitude.setText(String.format("Высота: %.0f м (Экстремальная высота)", currentAltitude));
        }
    }

    private void giveAdvice() {
        String advice;

        if (currentAltitude < 500) {
            advice = "Низкая высота:\n" +
                    "Кислорода достаточно\n" +
                    "Физическая нагрузка переносится легко\n";
        } else if (currentAltitude < 1500) {
            advice = "Средняя высота (500-1500м):\n" +
                    "Небольшое снижение кислорода\n" +
                    "Организм адаптируется естественно\n" +
                    "Рекомендуется пить больше воды\n";
        } else if (currentAltitude < 2500) {
            advice = "Высокогорье (1500-2500м):\n" +
                    "Учащенное сердцебиение\n";
        } else if (currentAltitude < 3500) {
            advice = "Зона высокой высоты (2500-3500м):\n" +
                    "Головная боль возможна\n" +
                    "Тошнота и головокружение\n" +
                    "Снижение аппетита\n";
        } else if (currentAltitude < 4500) {
            advice = "ЭКСТРЕМАЛЬНАЯ ВЫСОТА (3500-4500м):\n" +
                    "СЕРЬЕЗНЫЙ РИСК ДЛЯ ЗДОРОВЬЯ!\n" +
                    "Кислорода ~60% от нормы\n" +
                    "Сильная одышка в покое\n" +
                    "Высокий риск отека легких/мозга\n";
        } else {
            advice = "Опасная зона (>4500м):\n" +
                    "Кислорода менее 50%\n" +
                    "Требуется дополнительный кислород\n";
        }

        binding.tvAdvice.setText(advice);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}