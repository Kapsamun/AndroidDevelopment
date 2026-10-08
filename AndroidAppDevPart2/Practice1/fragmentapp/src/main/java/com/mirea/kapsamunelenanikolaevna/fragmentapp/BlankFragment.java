package com.mirea.kapsamunelenanikolaevna.fragmentapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BlankFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(BlankFragment.class.getSimpleName(), "onCreateView");
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Получение номера из Bundle (задание PDF)
        if (getArguments() != null) {
            int numberStudent = requireArguments().getInt("my_number_student", -1);
            Log.d(BlankFragment.class.getSimpleName(), "Номер по списку: " + numberStudent);

            TextView textView = view.findViewById(R.id.textViewInfo);
            textView.setText("Мой номер по списку: " + numberStudent);
        }
    }
}