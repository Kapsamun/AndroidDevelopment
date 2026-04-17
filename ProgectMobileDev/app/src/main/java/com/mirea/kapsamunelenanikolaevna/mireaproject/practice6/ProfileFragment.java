package com.mirea.kapsamunelenanikolaevna.mireaproject.practice6;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mirea.kapsamunelenanikolaevna.mireaproject.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserProfilePrefs";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Инициализация SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE);

        binding.btnSaveProfile.setOnClickListener(v -> saveProfile());

        binding.btnLoadProfile.setOnClickListener(v -> loadProfile());

        loadProfile();
    }

    private void saveProfile() {
        String userName = binding.etUserName.getText().toString().trim();
        String ageStr = binding.etAge.getText().toString().trim();
        boolean notificationsEnabled = binding.cbNotifications.isChecked();

        // Получаем выбранный жанр
        int selectedGenreId = binding.rgGenre.getCheckedRadioButtonId();
        String selectedGenre = "";
        if (selectedGenreId == binding.rbAction.getId()) {
            selectedGenre = "Боевик";
        } else if (selectedGenreId == binding.rbComedy.getId()) {
            selectedGenre = "Комедия";
        } else if (selectedGenreId == binding.rbDrama.getId()) {
            selectedGenre = "Драма";
        } else if (selectedGenreId == binding.rbSciFi.getId()) {
            selectedGenre = "Фантастика";
        }

        if (userName.isEmpty()) {
            Toast.makeText(getContext(), "Введите имя пользователя", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = 0;
        if (!ageStr.isEmpty()) {
            age = Integer.parseInt(ageStr);
        }

        // Сохраняем в SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USER_NAME", userName);
        editor.putInt("USER_AGE", age);
        editor.putString("FAVORITE_GENRE", selectedGenre);
        editor.putBoolean("NOTIFICATIONS_ENABLED", notificationsEnabled);
        editor.apply();

        Toast.makeText(getContext(), "Профиль сохранён!", Toast.LENGTH_SHORT).show();

        // Обновляем отображение
        displayProfileInfo(userName, age, selectedGenre, notificationsEnabled);
    }

    private void loadProfile() {
        String userName = sharedPreferences.getString("USER_NAME", "");
        int age = sharedPreferences.getInt("USER_AGE", 0);
        String favoriteGenre = sharedPreferences.getString("FAVORITE_GENRE", "");
        boolean notificationsEnabled = sharedPreferences.getBoolean("NOTIFICATIONS_ENABLED", false);

        // Заполняем поля
        binding.etUserName.setText(userName);
        if (age > 0) {
            binding.etAge.setText(String.valueOf(age));
        }
        binding.cbNotifications.setChecked(notificationsEnabled);

        // Устанавливаем RadioButton
        if (favoriteGenre.equals("Боевик")) {
            binding.rbAction.setChecked(true);
        } else if (favoriteGenre.equals("Комедия")) {
            binding.rbComedy.setChecked(true);
        } else if (favoriteGenre.equals("Драма")) {
            binding.rbDrama.setChecked(true);
        } else if (favoriteGenre.equals("Фантастика")) {
            binding.rbSciFi.setChecked(true);
        }

        displayProfileInfo(userName, age, favoriteGenre, notificationsEnabled);

        if (!userName.isEmpty()) {
            Toast.makeText(getContext(), "Профиль загружен", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayProfileInfo(String name, int age, String genre, boolean notifications) {
        String info = "Имя: " + (name.isEmpty() ? "не указано" : name) + "\n" +
                "Возраст: " + (age > 0 ? age + " лет" : "не указан") + "\n" +
                "Любимый жанр: " + (genre.isEmpty() ? "не указан" : genre) + "\n" +
                "Уведомления: " + (notifications ? "Включены" : "Выключены");
        binding.tvProfileInfo.setText(info);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}