package com.mirea.kapsamunelenanikolaevna.mireaproject.practice8;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.mirea.kapsamunelenanikolaevna.mireaproject.R;
import com.mirea.kapsamunelenanikolaevna.mireaproject.practice8.EstablishmentsData;
import com.mirea.kapsamunelenanikolaevna.mireaproject.databinding.FragmentPlacesBinding;
import com.mirea.kapsamunelenanikolaevna.mireaproject.practice8.Establishment;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

public class PlacesFragment extends Fragment {

    private static final String TAG = "PlacesFragment";
    private static final int REQUEST_CODE_PERMISSION = 100;

    private FragmentPlacesBinding binding;
    private MapView mapView;
    private IMapController mapController;
    private MyLocationNewOverlay locationNewOverlay;
    private CompassOverlay compassOverlay;
    private ScaleBarOverlay scaleBarOverlay;

    private List<Marker> placeMarkers = new ArrayList<>();
    private boolean isLocationPermissionGranted = false;

    private final GeoPoint START_POINT = new GeoPoint(55.758000, 37.616000);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: Запуск фрагмента заведений");

        // Загрузка конфигурации osmdroid
        Configuration.getInstance().load(requireContext(),
                PreferenceManager.getDefaultSharedPreferences(requireContext()));

        binding = FragmentPlacesBinding.inflate(inflater, container, false);

        mapView = binding.mapView;


        setupMapControls();


        setupMapController();


        checkLocationPermissions();


        setupUserLocationLayer();


        setupCompass();


        setupScaleBar();


        addEstablishmentMarkers();

        binding.btnClose.setOnClickListener(v -> {
            binding.infoCard.setVisibility(View.GONE);
        });

        return binding.getRoot();
    }

    // Настройка кнопок масштабирования
    private void setupMapControls() {
        mapView.setZoomRounding(true);
        mapView.setMultiTouchControls(true);
        mapView.setBuiltInZoomControls(true);
    }

    // Настройка контроллера карты и установка камеры в начальную точку
    private void setupMapController() {
        mapController = mapView.getController();
        mapController.setZoom(12.0);
        mapController.setCenter(START_POINT);
    }

    private void checkLocationPermissions() {
        int locationFinePermissionStatus = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int locationCoarsePermissionStatus = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if ((locationFinePermissionStatus == PackageManager.PERMISSION_GRANTED)
                && (locationCoarsePermissionStatus == PackageManager.PERMISSION_GRANTED)) {
            isLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{
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
                setupUserLocationLayer();
            }
        }
    }

    // Добавление слоя "Мое местоположение" на карту
    private void setupUserLocationLayer() {
        if (isLocationPermissionGranted) {
            locationNewOverlay = new MyLocationNewOverlay(
                    new GpsMyLocationProvider(requireContext()),
                    mapView);
            locationNewOverlay.enableMyLocation();
            locationNewOverlay.enableFollowLocation();
            mapView.getOverlays().add(locationNewOverlay);
        }
    }

    // Добавление компаса
    private void setupCompass() {
        compassOverlay = new CompassOverlay(requireContext(),
                new InternalCompassOrientationProvider(requireContext()),
                mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);
    }

    // Добавление метрической шкалы масштаба
    private void setupScaleBar() {
        final Context context = requireContext();
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();

        scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        mapView.getOverlays().add(scaleBarOverlay);
    }

    // Добавление маркеров заведений на карту
    private void addEstablishmentMarkers() {
        List<Establishment> establishments = EstablishmentsData.getEstablishments();

        Drawable defaultDrawable = ResourcesCompat.getDrawable(getResources(),
                org.osmdroid.library.R.drawable.osm_ic_follow_me_on, null);

        for (Establishment establishment : establishments) {
            Marker marker = new Marker(mapView);
            marker.setPosition(establishment.getGeoPoint());
            marker.setTitle(establishment.getName());
            marker.setSubDescription(establishment.getAddress());

            if (defaultDrawable != null) {
                marker.setIcon(defaultDrawable);
            }

            final Establishment currentPlace = establishment;
            marker.setOnMarkerClickListener((m, mapView) -> {
                Log.d(TAG, "Нажат маркер: " + currentPlace.getName());
                showEstablishmentInfo(currentPlace);
                return true;
            });

            mapView.getOverlays().add(marker);
            placeMarkers.add(marker);
        }

        Log.d(TAG, "Добавлено маркеров: " + placeMarkers.size());
    }

    // Показ информации о заведении при нажатии на маркер
    private void showEstablishmentInfo(Establishment establishment) {
        binding.tvPlaceName.setText(establishment.getName());
        binding.tvPlaceType.setText(establishment.getType());
        binding.tvPlaceAddress.setText(establishment.getAddress());
        binding.tvPlacePhone.setText(establishment.getPhone());
        binding.tvPlaceHours.setText(establishment.getHours());
        binding.tvPlaceDescription.setText(establishment.getDescription());

        binding.infoCard.setVisibility(View.VISIBLE);

        Toast.makeText(getContext(),
                establishment.getName() + "\n" + establishment.getAddress(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(requireContext(),
                PreferenceManager.getDefaultSharedPreferences(requireContext()));
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
        Configuration.getInstance().save(requireContext(),
                PreferenceManager.getDefaultSharedPreferences(requireContext()));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}