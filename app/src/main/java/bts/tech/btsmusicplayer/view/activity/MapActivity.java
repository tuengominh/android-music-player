package bts.tech.btsmusicplayer.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import bts.tech.btsmusicplayer.MainPlayerActivity;
import bts.tech.btsmusicplayer.R;
import bts.tech.btsmusicplayer.model.Song;
import bts.tech.btsmusicplayer.service.PlayerService;
import bts.tech.btsmusicplayer.util.MapUtil;
import bts.tech.btsmusicplayer.util.SongUtil;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, View.OnClickListener, GoogleMap.OnInfoWindowClickListener {

    /** Manipulates the Google Maps activity */

    //get context
    protected static final String TAG = MapActivity.class.getSimpleName();

    //fields to handle map & UI items
    private GoogleMap map;
    private Button btnGoBack;
    private Button btnPlay;
    private Marker currentMarker;

    //fields to control the bound service 'PlayerService'
    private PlayerService playerService;
    private boolean isBound = false;
    private List<Song> songs = MainPlayerActivity.getSongs();

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder serviceInfo) {
            playerService = new PlayerService(serviceInfo);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_map__fr__map);
        mapFragment.getMapAsync(this);

        this.btnPlay = findViewById(R.id.activity_map__btn__play);
        this.btnPlay.setOnClickListener(this);

        this.btnGoBack = findViewById(R.id.activity_map__btn__back);
        this.btnGoBack.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);

        //add markers to countries and move camera the the first country (Brazil)
        for (Song song : songs) {
            LatLng latLng = MapUtil.getLatLng(song.getCountry());
            map.addMarker(new MarkerOptions().position(latLng).title(song.getComment()));
        }
        map.moveCamera(CameraUpdateFactory.newLatLng(MapUtil.getLatLng(songs.get(0).getCountry())));
        map.setOnMarkerClickListener(this);

        //bind the service 'PlayerService' in a Thread object
        final Intent serviceIntent = new Intent(this, PlayerService.class);
        Thread thread = new Thread() {
            @Override
            public void run() {
                bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            }
        };
        thread.start();

        //add info windows
        map.setOnInfoWindowClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        //zoom in 10x selected marker
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 10));
        currentMarker = marker;

        //inflate custom image views & text views for current marker
        final ImageView songIcon = findViewById(R.id.activity_map__flag__icon);
        final TextView songTitle = findViewById(R.id.activity_map__tv__title);
        final TextView songDuration = findViewById(R.id.activity_map__tv__duration);
        final TextView songLocation = findViewById(R.id.activity_map__tv__latlgn);
        final TextView songComment = findViewById(R.id.activity_map__tv__comment);

        for (Song song : songs) {
            if (song.getComment().equals(currentMarker.getTitle())) {
                songIcon.setImageResource(SongUtil.getFlagResId(song.getCountry()));
                songTitle.setText(song.getTitle());
                songDuration.setText(song.getDuration());
                songLocation.setText(MapUtil.getLatLng(song.getCountry()).toString());
                songComment.setText(song.getComment());
            }
        }

        //show info window
        marker.showInfoWindow();
        return true;
    }

    //response to click events on info window
    @Override
    public void onInfoWindowClick(Marker marker) {
        currentMarker = marker;
        playSongInMap();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_map__btn__play:
                try {
                    Log.d(TAG,"Prepare to play " + currentMarker.getTitle());
                    playSongInMap();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.activity_map__btn__back:
                Log.d(TAG,"Go to Media Player");
                startActivity(new Intent(this, MainPlayerActivity.class));
                break;
            default:
                Log.w(TAG, "Not clickable");
        }
    }

    //stop PlayerService if isFinishing()
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            unbindService(serviceConnection);
            isBound = false;
            playerService.stopSelf();
        }
    }

    private void playSongInMap() {
        for (int i = 0; i < songs.size() - 1; i++) {
            if (songs.get(i).getComment().equals(currentMarker.getTitle())) {
                if (isBound) {
                    Log.d(TAG, "Playing song with index " + i);
                    this.playerService.playByIndex(i);
                }
            }
        }
    }
}
