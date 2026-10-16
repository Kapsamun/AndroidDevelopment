package com.mirea.kapsamunelenanikolaevna.celebritybase.presantation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mirea.kapsamunelenanikolaevna.celebritybase.databinding.FragmentCelebrityDetailsBinding;
import com.squareup.picasso.Picasso;

public class CelebrityDetailsFragment extends Fragment {

    private FragmentCelebrityDetailsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCelebrityDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            binding.textName.setText(args.getString("celebrityName"));
            binding.textBio.setText(args.getString("celebrityBio"));
            String photo = args.getString("celebrityPhoto");
            if (photo != null && !photo.isEmpty()) {
                Picasso.get().load(photo).into(binding.imageCelebrity);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}