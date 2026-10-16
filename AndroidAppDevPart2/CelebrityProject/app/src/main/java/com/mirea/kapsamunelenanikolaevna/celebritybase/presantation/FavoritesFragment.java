package com.mirea.kapsamunelenanikolaevna.celebritybase.presantation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mirea.kapsamunelenanikolaevna.celebritybase.R;
import com.mirea.kapsamunelenanikolaevna.celebritybase.databinding.FragmentFavoritesBinding;
import com.mirea.kapsamunelenanikolaevna.celebritybase.presantation.practice4.CelebrityAdapter;

import java.util.HashSet;
import java.util.Set;

public class FavoritesFragment extends Fragment {

    private FragmentFavoritesBinding binding;
    private MainViewModel viewModel;
    private CelebrityAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(requireContext()))
                .get(MainViewModel.class);

        adapter = new CelebrityAdapter();
        binding.recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewFavorites.setAdapter(adapter);

        adapter.setOnItemClickListener(celebrity -> {
            Bundle args = new Bundle();
            args.putInt("celebrityId", celebrity.getId());
            args.putString("celebrityName", celebrity.getName());
            args.putString("celebrityPhoto", celebrity.getPhotoUrl() != null ? celebrity.getPhotoUrl() : "");
            args.putString("celebrityBio", celebrity.getBiography() != null ? celebrity.getBiography() : "");
            Navigation.findNavController(view).navigate(R.id.action_favorites_to_details, args);
        });

        adapter.setOnFavoriteClickListener((celebrity, isCurrentlyFavorite) -> {
            viewModel.removeFromFavorites(celebrity);
        });

        viewModel.getFavoritesLiveData().observe(getViewLifecycleOwner(), list -> {
            if (list == null || list.isEmpty()) {
                binding.textEmptyFavorites.setVisibility(View.VISIBLE);
                adapter.setItems(null);
            } else {
                binding.textEmptyFavorites.setVisibility(View.GONE);
                adapter.setItems(list);
                Set<Integer> ids = new HashSet<>();
                for (var c : list) ids.add(c.getId());
                adapter.setFavoriteIds(ids);
            }
        });

        viewModel.loadFavorites();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) viewModel.loadFavorites();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}