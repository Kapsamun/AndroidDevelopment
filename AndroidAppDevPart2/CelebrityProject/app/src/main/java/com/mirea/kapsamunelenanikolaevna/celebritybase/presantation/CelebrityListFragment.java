package com.mirea.kapsamunelenanikolaevna.celebritybase.presantation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mirea.kapsamunelenanikolaevna.celebritybase.R;
import com.mirea.kapsamunelenanikolaevna.celebritybase.databinding.FragmentCelebrityListBinding;
import com.mirea.kapsamunelenanikolaevna.celebritybase.presantation.practice4.CelebrityAdapter;

public class CelebrityListFragment extends Fragment {

    private FragmentCelebrityListBinding binding;
    private MainViewModel viewModel;
    private CelebrityAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCelebrityListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(requireContext()))
                .get(MainViewModel.class);

        adapter = new CelebrityAdapter();
        binding.recyclerViewCelebrities.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewCelebrities.setAdapter(adapter);

        // Переход на детали
        adapter.setOnItemClickListener(celebrity -> {
            Bundle args = new Bundle();
            args.putInt("celebrityId", celebrity.getId());                    // было putString
            args.putString("celebrityName", celebrity.getName());
            args.putString("celebrityPhoto", celebrity.getPhotoUrl());
            args.putString("celebrityBio", celebrity.getBiography());
            Navigation.findNavController(view).navigate(R.id.action_list_to_details, args);
        });

        // LiveData
        viewModel.getCelebritiesLiveData().observe(getViewLifecycleOwner(), list -> {
            if (list != null) adapter.setItems(list);
        });

        viewModel.getMediatorCelebrityData().observe(getViewLifecycleOwner(), list -> {
            if (list != null && !list.isEmpty()) {
                adapter.setItems(list);
                Toast.makeText(requireContext(), "Объединено: " + list.size(), Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show();
            }
        });

        binding.buttonSaveCelebrity.setOnClickListener(v -> {
            String name = binding.editTextCelebrity.getText().toString().trim();
            if (!name.isEmpty()) {
                viewModel.saveCelebrity(name);
                binding.editTextCelebrity.setText("");
            } else {
                Toast.makeText(requireContext(), "Введите имя", Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonGetCelebrity.setOnClickListener(v -> viewModel.loadCelebrity());
        binding.buttonFetchCombined.setOnClickListener(v -> viewModel.loadCombinedData());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}