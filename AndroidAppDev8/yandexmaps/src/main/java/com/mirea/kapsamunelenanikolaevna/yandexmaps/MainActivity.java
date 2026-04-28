package com.mirea.kapsamunelenanikolaevna.yandexmaps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.mirea.kapsamunelenanikolaevna.yandexmaps.databinding.ActivityMainBinding;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

public class MainActivity extends AppCompatActivity implements UserLocationObjectListener {

    private static final int REQUEST_CODE_PERMISSION = 100;
    private boolean isLocationPermissionGranted = false;

    private ActivityMainBinding binding;
    private MapView mapView;
    private UserLocationLayer userLocationLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Устанавливаем API ключ
        MapKitFactory.setApiKey("c49d0fb7-4e0d-4890-84e0-41e7c045dbbe");
        MapKitFactory.initialize(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mapView = binding.mapview;

        // Устанавливаем начальную позицию камеры
        mapView.getMap().move(
                new CameraPosition(new Point(55.751574, 37.573856), 14.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 1.0f),
                null);

        checkLocationPermissions();
    }

    private void checkLocationPermissions() {
        int locationFinePermissionStatus = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int locationCoarsePermissionStatus = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if ((locationFinePermissionStatus == PackageManager.PERMISSION_GRANTED)
                && (locationCoarsePermissionStatus == PackageManager.PERMISSION_GRANTED)) {
            isLocationPermissionGranted = true;
            loadUserLocationLayer();
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

            if (isLocationPermissionGranted) {
                loadUserLocationLayer();
            }
        }
    }


     // настройка карты и слоя местоположения пользователя
    private void loadUserLocationLayer() {
        MapKit mapKit = MapKitFactory.getInstance();
        mapKit.resetLocationManagerToDefault();
        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow()); //создаем слой
        userLocationLayer.setVisible(true);           // Показываем слой
        userLocationLayer.setHeadingEnabled(true);    // Включаем направление (компас)
        userLocationLayer.setObjectListener(this);    // Устанавливаем слушатель
    }

    //Вызывается при добавлении объекта местоположения пользователя на карту
    @Override
    public void onObjectAdded(@NonNull UserLocationView userLocationView) {
        // Устанавливаем якорь для иконки (центрируем на карте)
        userLocationLayer.setAnchor(
                new PointF((float) (mapView.getWidth() * 0.5),
                        (float) (mapView.getHeight() * 0.5)),
                new PointF((float) (mapView.getWidth() * 0.5),
                        (float) (mapView.getHeight() * 0.83)));

        // Иконка направления движения (стрелка)
        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                this, android.R.drawable.arrow_up_float));

        // Иконка местоположения (пин)
        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();
        pinIcon.setIcon(
                "pin",
                ImageProvider.fromResource(this, android.R.drawable.star_on),
                new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
                        .setRotationType(RotationType.ROTATE)
                        .setZIndex(1f)
                        .setScale(0.5f)
        );

        // Цвет круга точности (полупрозрачный синий)
        userLocationView.getAccuracyCircle().setFillColor(Color.BLUE & 0x99ffffff);
    }

     //Вызывается при удалении объекта местоположения пользователя с карты
    @Override
    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {
    }

    //Вызывается при обновлении объекта местоположения пользователя
    @Override
    public void onObjectUpdated(@NonNull UserLocationView userLocationView,
                                @NonNull ObjectEvent objectEvent) {
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }
}