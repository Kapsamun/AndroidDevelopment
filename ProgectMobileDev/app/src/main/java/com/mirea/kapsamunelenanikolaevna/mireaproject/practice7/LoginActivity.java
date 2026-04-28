package com.mirea.kapsamunelenanikolaevna.mireaproject.practice7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mirea.kapsamunelenanikolaevna.mireaproject.MainActivity;
import com.mirea.kapsamunelenanikolaevna.mireaproject.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        // Проверяем, не авторизован ли уже пользователь
        if (mAuth.getCurrentUser() != null) {
            navigateToMain();
        }

        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.btnLogin.setOnClickListener(v -> login());
        binding.btnRegister.setOnClickListener(v -> register());
    }

    private void login() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (!validateInput(email, password)) return;

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnLogin.setEnabled(false);

        //ВЫЗОВ FIREBASE API ДЛЯ ВХОДА
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnLogin.setEnabled(true);

                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Вход выполнен успешно!", Toast.LENGTH_SHORT).show();
                        navigateToMain();
                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Ошибка входа: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void register() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (!validateInput(email, password)) return;

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnRegister.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnRegister.setEnabled(true);

                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this,
                                "Регистрация успешна! Теперь войдите.",
                                Toast.LENGTH_LONG).show();

                        // Отправляем письмо для подтверждения
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Ошибка регистрации: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean validateInput(String email, String password) {
        if (email.isEmpty()) {
            binding.etEmail.setError("Введите email");
            return false;
        }
        if (password.isEmpty()) {
            binding.etPassword.setError("Введите пароль");
            return false;
        }
        if (password.length() < 6) {
            binding.etPassword.setError("Пароль должен быть не менее 6 символов");
            return false;
        }
        return true;
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}