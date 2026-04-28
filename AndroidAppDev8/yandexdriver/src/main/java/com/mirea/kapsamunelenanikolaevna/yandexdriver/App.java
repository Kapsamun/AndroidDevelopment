package com.mirea.kapsamunelenanikolaevna.yandexdriver;

import android.app.Application;
import com.yandex.mapkit.MapKitFactory;

public class App extends Application {
    private final String MAPKIT_API_KEY = "268a9f7b-d118-41f2-80ec-c17083c88416";
    @Override
    public void onCreate() {
        super.onCreate();
        // Set the api key before calling initialize on MapKitFactory.
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
    }
}