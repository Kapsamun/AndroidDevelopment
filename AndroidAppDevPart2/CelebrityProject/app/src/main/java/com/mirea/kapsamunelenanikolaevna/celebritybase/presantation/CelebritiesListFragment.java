package com.mirea.kapsamunelenanikolaevna.celebritybase.presantation;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mirea.kapsamunelenanikolaevna.celebritybase.R;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.models.Celebrity;
import com.mirea.kapsamunelenanikolaevna.celebritybase.presantation.practice4.CelebrityAdapter;

public class CelebritiesListFragment extends Fragment {

    private MainViewModel viewModel;
    private CelebrityAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_celebrities, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(requireContext()))
                .get(MainViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCelebrities);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CelebrityAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this::openDetail);

        viewModel.getCelebritiesLiveData().observe(getViewLifecycleOwner(), list -> {
            if (list != null) adapter.setItems(list);
        });

        viewModel.getError().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        // если список ещё не грузили
        viewModel.loadCelebrity();
    }

    private void openDetail(Celebrity celebrity) {
        CelebrityDetailFragment fragment = CelebrityDetailFragment.newInstance(
                celebrity.getId(),
                celebrity.getName(),
                celebrity.getPhotoUrl(),
                celebrity.getBiography()
        );

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack("detail")
                .commit();
    }
}
