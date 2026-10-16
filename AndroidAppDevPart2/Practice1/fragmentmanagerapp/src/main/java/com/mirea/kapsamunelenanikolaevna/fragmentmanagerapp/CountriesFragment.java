package com.mirea.kapsamunelenanikolaevna.fragmentmanagerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class CountriesFragment extends Fragment {

    private final String[] countries = {
            "Россия", "Китай", "Индия", "Бразилия", "ЮАР"
    };

    private final String[] details = {
            "Столица: Москва. Население ~146 млн.",
            "Столица: Пекин. Население ~1.4 млрд.",
            "Столица: Нью-Дели. Население ~1.4 млрд.",
            "Столица: Бразилиа. Население ~210 млн.",
            "Столица: Претория. Население ~60 млн."
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_countries, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //передаем requireActivity(), чтобы оба фрагмента получили ОДИН И ТОТ ЖЕ экземпляр ViewModel,
        // привязанный к главному экрану
        ShareViewModel viewModel =
                new ViewModelProvider(requireActivity()).get(ShareViewModel.class);

        ListView listView = view.findViewById(R.id.listViewCountries);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                countries
        );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, v, position, id) -> {
            String info = countries[position] + "\n\n" + details[position];
            viewModel.setSomeValue(info);
        });
    }
}
