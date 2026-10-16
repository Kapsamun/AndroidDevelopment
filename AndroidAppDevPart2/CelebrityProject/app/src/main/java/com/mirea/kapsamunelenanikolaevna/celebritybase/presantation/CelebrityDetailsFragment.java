package com.mirea.kapsamunelenanikolaevna.celebritybase.presantation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
            String name = args.getString("celebrityName", "Без имени");
            String bio = args.getString("celebrityBio", "Нет описания");
            String photo = args.getString("celebrityPhoto", "");
            int id = args.getInt("celebrityId", 0);

            binding.textName.setText(name);
            binding.textBio.setText(bio);
            binding.textExtra.setText("ID: " + id + "\n\nГде снимается / известен: " +
                    (bio.isEmpty() ? "информация отсутствует" : bio));

            if (photo != null && !photo.isEmpty()) {
                Picasso.get()
                        .load(photo)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_report_image)
                        .into(binding.imageCelebrity);
            }
        }

        binding.buttonBack.setOnClickListener(v ->
                Navigation.findNavController(view).navigateUp());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}