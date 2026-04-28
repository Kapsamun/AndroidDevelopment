package com.mirea.kapsamunelenanikolaevna.osmmaps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;

import com.mirea.kapsamunelenanikolaevna.osmmaps.databinding.ActivityMainBinding;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.views.MapController;

import android.util.DisplayMetrics;
import android.content.Context;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 100;
    private boolean isLocationPermissionGranted = false;

    private MapView mapView = null;
    private ActivityMainBinding binding;
    private IMapController mapController;
    private MyLocationNewOverlay locationNewOverlay;
    private CompassOverlay compassOverlay;
    private ScaleBarOverlay scaleBarOverlay;

    // Координаты точек маршрута
    private final GeoPoint START_POINT = new GeoPoint(55.703742, 37.529178);  // Воробьевы горы
    private final GeoPoint END_POINT = new GeoPoint(55.758000, 37.616000);     // Красная площадь

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Загрузка конфигурации osmdroid
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mapView = binding.mapView;

        // 1. Настройка кнопок масштабирования и мультитач
        setupMapControls();

        // 2. Получение контроллера карты и установка начальной позиции
        setupMapController();

        // 3. Запрос разрешений на геолокацию
        checkLocationPermissions();

        // 4. Добавление слоя с местоположением пользователя
        setupUserLocationLayer();

        // 5. Добавление компаса
        setupCompass();

        // 6. Добавление метрической шкалы масштаба
        setupScaleBar();

        // 7. Добавление маркеров на карту
        addMarkers();
    }

     //1. Настройка кнопок масштабирования и возможности увеличения двумя пальцами
    private void setupMapControls() {
        mapView.setZoomRounding(true);           // Включение округления масштаба
        mapView.setMultiTouchControls(true);     // Включение управления двумя пальцами
        mapView.setBuiltInZoomControls(true);    // Показывать кнопки +/- на экране
    }

     // Настройка контроллера карты и установка камеры в начальную точку
    private void setupMapController() {
        mapController = mapView.getController();
        mapController.setZoom(15.0);              // Установка масштаба (0-18+)
        mapController.setCenter(START_POINT);     // Центрируем карту на начальной точке
    }

    private void checkLocationPermissions() {
        int locationFinePermissionStatus = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int locationCoarsePermissionStatus = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if ((locationFinePermissionStatus == PackageManager.PERMISSION_GRANTED)
                && (locationCoarsePermissionStatus == PackageManager.PERMISSION_GRANTED)) {
            isLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            isLocationPermissionGranted = grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }

     //Добавление слоя "Мое местоположение" на карту
    private void setupUserLocationLayer() {
        if (isLocationPermissionGranted) {
            locationNewOverlay = new MyLocationNewOverlay(
                    new GpsMyLocationProvider(getApplicationContext()),
                    mapView);
            locationNewOverlay.enableMyLocation();      // Включаем отображение местоположения
            locationNewOverlay.enableFollowLocation();  // Следование за местоположением
            mapView.getOverlays().add(locationNewOverlay);
        }
    }

     // 5. Добавление компаса
    private void setupCompass() {
        compassOverlay = new CompassOverlay(getApplicationContext(),
                new InternalCompassOrientationProvider(getApplicationContext()),
                mapView);
        compassOverlay.enableCompass();  // Включаем компас
        mapView.getOverlays().add(compassOverlay);
    }

     // Добавление метрической шкалы масштаба
    private void setupScaleBar() {
        final Context context = this.getApplicationContext();
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();

        scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setCentred(true);                           // Центрируем шкалу
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);  // Позиция вверху по центру
        mapView.getOverlays().add(scaleBarOverlay);
    }

     //Добавление маркеров на карту с обработкой нажатия
    private void addMarkers() {
        // Маркер начальной точки
        Marker startMarker = new Marker(mapView);
        startMarker.setPosition(START_POINT);
        startMarker.setTitle("Воробьевы горы");
        startMarker.setSubDescription("Смотровая площадка с отличным видом на Москву");
        startMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(getApplicationContext(),
                        "Начальная точка: Воробьевы горы\nВысота: ~80 м над уровнем моря",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        startMarker.setIcon(ResourcesCompat.getDrawable(getResources(),
                org.osmdroid.library.R.drawable.osm_ic_follow_me_on, null));
        mapView.getOverlays().add(startMarker);

        // Маркер конечной точки
        Marker endMarker = new Marker(mapView);
        endMarker.setPosition(END_POINT);
        endMarker.setTitle("Красная площадь");
        endMarker.setSubDescription("Главная площадь Москвы. Здесь находятся:\n" +
                "- Собор Василия Блаженного\n" +
                "- Мавзолей Ленина\n" +
                "- ГУМ\n" +
                "- Исторический музей");
        endMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(getApplicationContext(),
                        "Красная площадь — сердце Москвы!\nОбъект Всемирного наследия ЮНЕСКО",
                        Toast.LENGTH_LONG).show();
                return true;
            }
        });
        mapView.getOverlays().add(endMarker);

        GeoPoint parkPoint = new GeoPoint(55.731438, 37.598490);
        Marker parkMarker = new Marker(mapView);
        parkMarker.setPosition(parkPoint);
        parkMarker.setTitle("Парк Горького");
        parkMarker.setSubDescription("Центральный парк культуры и отдыха\n" +
                "Лучшее место для прогулок в центре Москвы");
        parkMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(getApplicationContext(),
                        "Парк Горького открыт в 1928 году.\nПлощадь парка: 219 гектаров",
                        Toast.LENGTH_LONG).show();
                return true;
            }
        });
        mapView.getOverlays().add(parkMarker);
    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if (mapView != null) {
            mapView.onResume();
        }
        if (locationNewOverlay != null) {
            locationNewOverlay.enableMyLocation();
        }
        if (compassOverlay != null) {
            compassOverlay.enableCompass();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if (mapView != null) {
            mapView.onPause();
        }
        if (locationNewOverlay != null) {
            locationNewOverlay.disableMyLocation();
        }
        if (compassOverlay != null) {
            compassOverlay.disableCompass();
        }
    }
}