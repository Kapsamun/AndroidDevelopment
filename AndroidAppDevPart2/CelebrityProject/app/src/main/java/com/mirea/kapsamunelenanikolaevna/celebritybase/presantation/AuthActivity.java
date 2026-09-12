package com.mirea.kapsamunelenanikolaevna.celebritybase.presantation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mirea.kapsamunelenanikolaevna.celebritybase.R;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.AuthRepositoryImpl;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.models.User;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.repo.AuthRepository;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.usecase.auth.LoginUseCase;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.usecase.auth.RegisterUseCase;

public class AuthActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private TextView tvStatus;
    private MaterialButton btnLogin, btnRegister;

    private LoginUseCase loginUseCase;
    private RegisterUseCase registerUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        com.google.firebase.FirebaseApp.initializeApp(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvStatus = findViewById(R.id.tvStatus);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        AuthRepository authRepository = new AuthRepositoryImpl();
        loginUseCase = new LoginUseCase(authRepository);
        registerUseCase = new RegisterUseCase(authRepository);

        // Уже авторизован?
        if (authRepository.getCurrentUser() != null) {
            openMain();
            return;
        }

        btnLogin.setOnClickListener(v -> doLogin());
        btnRegister.setOnClickListener(v -> doRegister());
    }

    private void doLogin() {
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        if (email.isEmpty() || password.isEmpty()) {
            showStatus("Заполните все поля");
            return;
        }

        setLoading(true);

        loginUseCase.execute(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                runOnUiThread(() -> {
                    setLoading(false);
                    Toast.makeText(AuthActivity.this, "Добро пожаловать!", Toast.LENGTH_SHORT).show();
                    openMain();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    setLoading(false);
                    showStatus(message);
                });
            }
        });
    }

    private void doRegister() {
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        if (email.isEmpty() || password.isEmpty()) {
            showStatus("Заполните все поля");
            return;
        }

        if (password.length() < 6) {
            showStatus("Пароль должен быть не менее 6 символов");
            return;
        }

        setLoading(true);

        registerUseCase.execute(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                runOnUiThread(() -> {
                    setLoading(false);
                    Toast.makeText(AuthActivity.this, "Аккаунт создан!", Toast.LENGTH_SHORT).show();
                    openMain();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    setLoading(false);
                    showStatus(message);
                });
            }
        });
    }

    private void showStatus(String message) {
        tvStatus.setVisibility(View.VISIBLE);
        tvStatus.setText(message);
    }

    private void setLoading(boolean loading) {
        btnLogin.setEnabled(!loading);
        btnRegister.setEnabled(!loading);
        btnLogin.setText(loading ? "Загрузка..." : "Войти");
    }

    private void openMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}