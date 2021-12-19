package com.example.smartbank;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.smartbank.Adapter.PlaceAdapter;
import com.example.smartbank.Constant.AllConstant;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.smartbank.databinding.ActivityMapsBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;


import com.example.smartbank.Adapter.InfoWindowAdapter;

import com.example.smartbank.Permissions.AppPermissions;


import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private ActivityMapsBinding binding;
    private GoogleMap mGoogleMap;
    private AppPermissions appPermissions;
    private boolean isLocationPermissionOk;
    private boolean isTrafficEnable;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private Marker currentMarker;
    private PlaceAdapter placeAdapter;
    private int radius = 2000;
    private List<PlaceModel> placeModelList;
    private PlaceModel placeModel;
    private InfoWindowAdapter infoWindowAdapter;
    LoginResponse loginResponse;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        if(intent.getExtras()!=null)
        {
            loginResponse = (LoginResponse) intent.getSerializableExtra("data");
            Log.e("TAG","=====>" + loginResponse.getUserid());
        }
        appPermissions = new AppPermissions();
        placeModelList = new ArrayList<PlaceModel>();
        binding.btnMapType.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenuInflater().inflate(R.menu.map_type_menu, popupMenu.getMenu());


            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.btnNormal:
                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;

                    case R.id.btnSatellite:
                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;

                    case R.id.btnTerrain:
                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                }
                return true;
            });

            popupMenu.show();
        });
        binding.enableTraffic.setOnClickListener(view -> {

            if (isTrafficEnable) {
                if (mGoogleMap != null) {
                    mGoogleMap.setTrafficEnabled(false);
                    isTrafficEnable = false;
                }
            } else {
                if (mGoogleMap != null) {
                    mGoogleMap.setTrafficEnabled(true);
                    isTrafficEnable = true;
                }
            }

        });
        binding.currentLocation.setOnClickListener(currentLocation -> getCurrentLocation());
        binding.banks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllBanks();
            }
        });
        binding.nearest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NearestRequest nearestRequest = new NearestRequest();
                nearestRequest.setLongitude(currentLocation.getLongitude());
                nearestRequest.setLatitude(currentLocation.getLatitude());
                nearestRequest.setDistance(1000.0);
                getNearestBanks(nearestRequest);
            }
        });

    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//
//        assert mapFragment != null;
//        mapFragment.getMapAsync(this);
//
//        for (PlaceModel placeModel : AllConstant.placesName) {
//
//            Chip chip = new Chip(this);
//            chip.setText(placeModel.getName());
//            chip.setId(placeModel.getId());
//            chip.setPadding(8, 8, 8, 8);
//            chip.setTextColor(getResources().getColor(R.color.white, null));
//            chip.setChipBackgroundColor(getResources().getColorStateList(R.color.quantum_purple800, null));
//            chip.setChipIcon(ResourcesCompat.getDrawable(getResources(), placeModel.getDrawableId(), null));
//            chip.setCheckable(true);
//            chip.setCheckedIconVisible(false);
//
//            binding.placesGroup.addView(chip);
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mGoogleMap = googleMap;
//
//
//        if (appPermissions.isLocationOk(this)) {
//            isLocationPermissionOk = true;
//            setUpGoogleMap();
//
//        } else if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//            new AlertDialog.Builder(this)
//                    .setTitle("Location Permission")
//                    .setMessage("Near me required location permission to show you near by places")
//                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            requestLocation();
//                        }
//                    })
//                    .create().show();
//        } else {
//            requestLocation();
//        }
//
//    }

    private void setUpLocationUpdate() {

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        Log.d("TAG", "onLocationResult: " + location.getLongitude() + " " + location.getLatitude());
                    }
                }
                super.onLocationResult(locationResult);
            }
        };
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        startLocationUpdates();


    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            isLocationPermissionOk = false;
            return;
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MapsActivity.this, "Location updated started", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        getCurrentLocation();
    }
//
    private void getCurrentLocation() {

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            boolean isLocationPermissionOk = false;
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @SuppressLint("PotentialBehaviorOverride")
            @Override
            public void onSuccess(@NonNull Location location) {
                currentLocation = location;
                infoWindowAdapter = null;
                infoWindowAdapter = new InfoWindowAdapter(currentLocation, MapsActivity.this);
                mGoogleMap.setInfoWindowAdapter(infoWindowAdapter);
                moveCameraToLocation(location);
            }
        });
    }

    private void moveCameraToLocation(Location location) {

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new
                LatLng(location.getLatitude(), location.getLongitude()), 17);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title("Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        if (currentMarker != null) {
            currentMarker.remove();
        }
        currentMarker = mGoogleMap.addMarker(markerOptions);
        currentMarker.setTag(703);
        mGoogleMap.animateCamera(cameraUpdate);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (appPermissions.isLocationOk(this)) {
            isLocationPermissionOk = true;
            setUpGoogleMap();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle("Location Permission")
                    .setMessage("SmartGuide required location permission to show you near by places")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestLocation();
                        }
                    })
                    .create().show();
        } else {
            requestLocation();
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AllConstant.LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isLocationPermissionOk = true;
                setUpGoogleMap();
            } else {
                isLocationPermissionOk = false;
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestLocation() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_BACKGROUND_LOCATION}, AllConstant.LOCATION_REQUEST_CODE);
    }
    @SuppressLint("PotentialBehaviorOverride")
    private void setUpGoogleMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            isLocationPermissionOk = false;
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);
        mGoogleMap.setOnMarkerClickListener(this::onMarkerClick);
        setUpLocationUpdate();
    }
    public boolean onMarkerClick(Marker marker) {

        int markerTag = (int) marker.getTag();
        Log.d("TAG", "onMarkerClick: " + markerTag);
        binding.placesRecyclerView.scrollToPosition(markerTag);
        return false;
    }

    private void stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        Log.d("TAG", "stopLocationUpdate: Location Update stop");
    }

    @Override
    public void onPause() {
        super.onPause();

        if (fusedLocationProviderClient != null)
            stopLocationUpdate();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (fusedLocationProviderClient != null) {

            startLocationUpdates();
            if (currentMarker != null) {
                currentMarker.remove();
            }
        }
    }

    public void getAllBanks() {
        Call<List<PlaceModel>> allResponseCall = LoginApi.getService().getAll();
     //   Toast.makeText(MapsActivity.this,currentLocation.toString(),Toast.LENGTH_SHORT).show();
            if (currentLocation != null) {

                allResponseCall.enqueue(new Callback<List<PlaceModel>>() {
                    @Override
                    public void onResponse(Call<List<PlaceModel>> call, Response<List<PlaceModel>> response) {

                        if(response.isSuccessful()) {

                         //   Toast.makeText(MapsActivity.this, currentLocation.toString(), Toast.LENGTH_SHORT).show();
                         // Gson gson = new Gson();

                            List<PlaceModel> allResponse = response.body();
                           // String res = gson.toJson(allResponse);
                            assert response.body() != null;
                            assert allResponse != null;
                            placeModelList.clear();
                            mGoogleMap.clear();
                            for (int i = 0; i < allResponse.size(); i++) {

                                placeModelList.add(allResponse.get(i));
                                addMarker(allResponse.get(i), i);
                            }
                         //   placeAdapter.setPlaceModels(placeModelList);
                        }
                        else {

                            String message = "Unable to fetch markers. An error occured";
                            Toast.makeText(MapsActivity.this,message,Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<List<PlaceModel>> call, Throwable t) {
                        String message = t.getLocalizedMessage();
                        Toast.makeText(MapsActivity.this,message,Toast.LENGTH_LONG).show();
                    }
                });
            }


    }

    public void getNearestBanks(NearestRequest nearestRequest) {
        Call<List<PlaceModel>> nearestResponseCall = LoginApi.getService().getNearest(nearestRequest);
        if (currentLocation != null) {
            nearestResponseCall.enqueue(new Callback<List<PlaceModel>>() {
                @Override
                public void onResponse(Call<List<PlaceModel>> call, Response<List<PlaceModel>> response) {

                    if(response.isSuccessful()) {
                        List<PlaceModel> nearestResponse = response.body();
                        assert response.body() != null;
                        assert nearestResponse != null;
                        placeModelList.clear();
                        mGoogleMap.clear();
                        for (int i = 0; i < nearestResponse.size(); i++) {

                            placeModelList.add(nearestResponse.get(i));
                            addMarker(nearestResponse.get(i), i);
                        }
                        //   placeAdapter.setPlaceModels(placeModelList);
                    }
                    else {

                        String message = "Unable to fetch markers. An error occured";
                        Toast.makeText(MapsActivity.this,message,Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<List<PlaceModel>> call, Throwable t) {
                    String message = t.getLocalizedMessage();
                    Toast.makeText(MapsActivity.this,message,Toast.LENGTH_LONG).show();
                }
            });
        }


    }


 // @RequiresApi(api = Build.VERSION_CODES.M)
    private void addMarker(PlaceModel placeModel, int position) {

        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(placeModel.getLatitude(),
                        placeModel.getLongitude()))
                .title(placeModel.getBank());

        mGoogleMap.addMarker(markerOptions).setTag(position);
    }
//
  @RequiresApi(api = Build.VERSION_CODES.M)
    private BitmapDescriptor getCustomIcon() {

        Drawable background = ContextCompat.getDrawable(this, R.drawable.ic_location);
       background.setTint(getResources().getColor(R.color.quantum_googred, null));
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    private void setUpRecyclerView() {

        binding.placesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.placesRecyclerView.setHasFixedSize(false);
       // placeAdapter = new PlaceAdapter(MapsActivity.this);
        binding.placesRecyclerView.setAdapter(placeAdapter);

        SnapHelper snapHelper = new PagerSnapHelper();

        snapHelper.attachToRecyclerView(binding.placesRecyclerView);

        binding.placesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                assert linearLayoutManager != null;
                int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (position > -1) {
                    PlaceModel placeModel = placeModelList.get(position);
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(placeModel.getLatitude(),
                            placeModel.getLongitude()), 20));
                }
            }
        });

    }



}