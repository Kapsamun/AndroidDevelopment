package com.mirea.kapsamunelenanikolaevna.yandexdriver;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;

import com.mirea.kapsamunelenanikolaevna.yandexdriver.databinding.ActivityMainBinding;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.directions.driving.VehicleOptions;
import com.yandex.mapkit.geometry.BoundingBox;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        UserLocationObjectListener,
        DrivingSession.DrivingRouteListener {

    private static final int REQUEST_CODE_PERMISSION = 100;
    private boolean isRouteBuilt = false;

    // Координаты маршрута (старт и финиш)
    private final Point ROUTE_START_LOCATION = new Point(55.703742, 37.529178);
    private final Point ROUTE_END_LOCATION = new Point(55.758000, 37.616000);

    private ActivityMainBinding binding;
    private MapView mapView;
    private UserLocationLayer userLocationLayer;
    private MapObjectCollection mapObjects;
    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;

    // Цвета для альтернативных маршрутов
    private final int[] colors = {0xFFFF0000, 0xFF00FF00, 0xFF00BBBB, 0xFF0000FF};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Инициализация библиотек (API ключ уже установлен в классе App)
        MapKitFactory.initialize(this);
        DirectionsFactory.initialize(this);

        // Инфлейтим разметку
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Получаем карту
        mapView = binding.mapview;

        // Отключаем жесты поворота (опционально)
        mapView.getMap().setRotateGesturesEnabled(false);

        // Инициализируем объекты для маршрута
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
        mapObjects = mapView.getMap().getMapObjects().addCollection();

        // *** УБИРАЕМ ЛЮБЫЕ move КАМЕРЫ ЗДЕСЬ! ***

        // Добавляем маркер на конечную точку
        addPlacemark();

        // Запрашиваем разрешения
        checkPermissions();
    }

    private void addPlacemark() {
        PlacemarkMapObject marker = mapView.getMap().getMapObjects().addPlacemark(
                ROUTE_END_LOCATION,
                ImageProvider.fromResource(this, android.R.drawable.ic_menu_info_details)
        );
        marker.addTapListener((mapObject, point) -> {
            Toast.makeText(this, "Точка назначения", Toast.LENGTH_LONG).show();
            return true;
        });
    }

    private void checkPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initUserLayer();
        } else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initUserLayer();
        } else {
            Toast.makeText(this, "Разрешения на геолокацию не получены", Toast.LENGTH_LONG).show();
        }
    }

    private void initUserLayer() {
        userLocationLayer = MapKitFactory.getInstance().createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setObjectListener(this);
        // Запрашиваем маршрут после инициализации слоя
        buildRoute();
    }

    // ==================== UserLocationObjectListener ====================
    @Override
    public void onObjectAdded(@NonNull UserLocationView userLocationView) {
        // Здесь можно настроить внешний вид иконки (стрелка, круг)
        userLocationView.getAccuracyCircle().setFillColor(Color.BLUE & 0x99ffffff);
    }

    @Override
    public void onObjectRemoved(@NonNull UserLocationView userLocationView) { }
    @Override
    public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) { }

    // ==================== ПОСТРОЕНИЕ МАРШРУТА ====================
    private void buildRoute() {
        if (isRouteBuilt) return;

        DrivingOptions drivingOptions = new DrivingOptions();
        drivingOptions.setRoutesCount(3);
        VehicleOptions vehicleOptions = new VehicleOptions();

        ArrayList<RequestPoint> points = new ArrayList<>();
        points.add(new RequestPoint(ROUTE_START_LOCATION, RequestPointType.WAYPOINT, null, null));
        points.add(new RequestPoint(ROUTE_END_LOCATION, RequestPointType.WAYPOINT, null, null));

        drivingSession = drivingRouter.requestRoutes(points, drivingOptions, vehicleOptions, this);
        Toast.makeText(this, "Строим маршрут...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDrivingRoutes(@NonNull List<DrivingRoute> routes) {
        isRouteBuilt = true;
        mapObjects.clear();

        for (int i = 0; i < routes.size(); i++) {
            DrivingRoute route = routes.get(i);
            mapObjects.addPolyline(route.getGeometry()).setStrokeColor(colors[i % colors.length]);
        }

        // Просто центрируем карту на середине между стартом и финишем
        Point center = new Point(
                (ROUTE_START_LOCATION.getLatitude() + ROUTE_END_LOCATION.getLatitude()) / 2,
                (ROUTE_START_LOCATION.getLongitude() + ROUTE_END_LOCATION.getLongitude()) / 2
        );

        mapView.getMap().move(
                new CameraPosition(center, 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 1.0f),
                null
        );

        Toast.makeText(this, "Найдено маршрутов: " + routes.size(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDrivingRoutesError(@NonNull com.yandex.runtime.Error error) {
        String message = "Ошибка построения маршрута";
        if (error instanceof RemoteError) message = "Ошибка соединения с сервером";
        if (error instanceof NetworkError) message = "Нет интернета";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    // ==================== ЖИЗНЕННЫЙ ЦИКЛ ====================
    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }
}