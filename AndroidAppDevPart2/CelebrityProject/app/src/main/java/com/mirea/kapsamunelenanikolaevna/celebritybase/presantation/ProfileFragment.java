package com.mirea.kapsamunelenanikolaevna.celebritybase.presantation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mirea.kapsamunelenanikolaevna.celebritybase.R;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.AuthRepositoryImpl;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.models.User;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.usecase.auth.GetCurrentUserUseCase;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.usecase.auth.LogoutUseCase;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView email = view.findViewById(R.id.textViewProfileEmail);
        TextView uid = view.findViewById(R.id.textViewProfileUid);
        TextView status = view.findViewById(R.id.textViewProfileStatus);
        Button logout = view.findViewById(R.id.buttonLogout);

        AuthRepositoryImpl authRepo = new AuthRepositoryImpl();
        User user = new GetCurrentUserUseCase(authRepo).execute();

        if (user != null) {
            email.setText(user.getEmail() != null ? user.getEmail() : "Без email");
            uid.setText("ID: " + user.getUid());
            status.setText("Статус: авторизован");
        } else {
            email.setText("Гость");
            uid.setText("Вы не вошли в аккаунт");
            status.setText("Статус: гость");
        }

        logout.setOnClickListener(v -> {
            new LogoutUseCase(authRepo).execute();
            Intent intent = new Intent(requireContext(), AuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}