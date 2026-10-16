package com.mirea.kapsamunelenanikolaevna.celebritybase.presantation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.CelebrityRepoImpl;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.MockNetworkApi;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.NetworkApi;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.TmdbNetworkApi;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.storage.room.AppDatabase;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.storage.room.CelebrityDao;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.repo.CelebrityRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public ViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        CelebrityRepository celebrityRepository = new CelebrityRepoImpl(context);
        AppDatabase db = AppDatabase.getInstance(context);
        CelebrityDao celebrityDao = db.celebrityDao();

        // Реальное API, не Mock
        NetworkApi networkApi = new TmdbNetworkApi();

        return (T) new MainViewModel(celebrityRepository, celebrityDao, networkApi);
    }
    /*public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        CelebrityRepository celebrityRepository = new CelebrityRepoImpl(context);

        //Получаем DAO базы данных Room через AppDatabase.getInstance()
        AppDatabase db = AppDatabase.getInstance(context);

        CelebrityDao celebrityDao = db.celebrityDao();
        NetworkApi networkApi = new MockNetworkApi();
        return (T) new MainViewModel(celebrityRepository, celebrityDao, networkApi);
    }*/
}
