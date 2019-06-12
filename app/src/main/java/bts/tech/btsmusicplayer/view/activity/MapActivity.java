package bts.tech.btsmusicplayer.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import bts.tech.btsmusicplayer.R;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    /** Manipulates the Google Maps fragment */

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_map__fr__map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);

        // Add markers to countries
        //TODO: use Utils add markers to all countries
        LatLng brazil = new LatLng(41, 2);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(brazil, 14));

        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);
        map.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        createMarker(latLng);
        //moveCamera(latLng)

    }

    private void createMarker(LatLng latLng) {
        map.addMarker(new MarkerOptions().position(latLng).title("Marker for " + latLng.toString())); //TODO: bensoundbrazilsamba
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return showToast(marker);
    }

    private boolean showToast(Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Info window clicked", Toast.LENGTH_SHORT).show();
    }
}
