package com.mirea.kapsamunelenanikolaevna.fragmentmanagerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class DetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textViewDetails = view.findViewById(R.id.textViewDetails);

        ShareViewModel viewModel =
                new ViewModelProvider(requireActivity()).get(ShareViewModel.class);

        viewModel.getSomeValue().observe(getViewLifecycleOwner(), data -> {
            if (data != null) {
                textViewDetails.setText(data);
            }
        });
    }
}