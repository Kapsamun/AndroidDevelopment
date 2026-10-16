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

import java.util.HashSet;
import java.util.Set;

public class CelebrityListFragment extends Fragment {

    private FragmentCelebrityListBinding binding;
    private MainViewModel viewModel;
    private CelebrityAdapter adapter;
    private final Set<Integer> favoriteIds = new HashSet<>();

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

        // Клик по элементу → детали
        adapter.setOnItemClickListener(celebrity -> {
            Bundle args = new Bundle();
            args.putInt("celebrityId", celebrity.getId());
            args.putString("celebrityName", celebrity.getName());
            args.putString("celebrityPhoto", celebrity.getPhotoUrl() != null ? celebrity.getPhotoUrl() : "");
            args.putString("celebrityBio", celebrity.getBiography() != null ? celebrity.getBiography() : "");
            Navigation.findNavController(view).navigate(R.id.action_list_to_details, args);
        });

        // Клик по сердечку → добавить/убрать из избранного
        adapter.setOnFavoriteClickListener((celebrity, isCurrentlyFavorite) -> {
            if (isCurrentlyFavorite) {
                viewModel.removeFromFavorites(celebrity);
                favoriteIds.remove(celebrity.getId());
                Toast.makeText(requireContext(), "Удалено из избранного", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.addToFavorites(celebrity);
                favoriteIds.add(celebrity.getId());
                Toast.makeText(requireContext(), "Добавлено в избранное", Toast.LENGTH_SHORT).show();
            }
            adapter.setFavoriteIds(favoriteIds);
        });

        // Объединённые данные (сеть + БД) — контрольное задание предыдущей практики
        viewModel.getMediatorCelebrityData().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                adapter.setItems(list);
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getCelebrityResult().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        // Загружаем избранное, чтобы подсветить сердечки
        viewModel.getFavoritesLiveData().observe(getViewLifecycleOwner(), favorites -> {
            favoriteIds.clear();
            if (favorites != null) {
                for (var c : favorites) {
                    favoriteIds.add(c.getId());
                }
            }
            adapter.setFavoriteIds(favoriteIds);
        });
        viewModel.loadFavorites();

        binding.buttonFetchCombined.setOnClickListener(v -> viewModel.loadCombinedData());

        // Первая загрузка
        viewModel.loadCombinedData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}