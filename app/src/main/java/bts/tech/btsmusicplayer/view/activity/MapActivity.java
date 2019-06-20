package bts.tech.btsmusicplayer.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
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
import java.util.Random;

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
    private List<Song> songs = MainPlayerActivity.songs;
    private int currentSongIndex = 0;

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

        //obtain the SupportMapFragment and get notified when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_map__fr__map);
        mapFragment.getMapAsync(this);

        //setup buttons
        this.btnPlay = findViewById(R.id.activity_map__btn__play);
        this.btnPlay.setOnClickListener(this);

        this.btnGoBack = findViewById(R.id.activity_map__btn__back);
        this.btnGoBack.setOnClickListener(this);
    }

    //when the map is ready to use
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);

        //add markers to locations of songs
        for (Song song : songs) {
            LatLng latLng = MapUtil.getLatLngByTitle(song.getTitle());
            map.addMarker(new MarkerOptions().position(latLng).title(song.getTitle()));
        }

        //move camera to the 1st location (Brazil) when clicking map button at control bar
        //move camera to the location of the long-clicked song in MainPlayerActivity when clicking context menu's map item
        String currentSongTitle = getIntent().getStringExtra("title");

        if (currentSongTitle == null){
            map.moveCamera(CameraUpdateFactory.newLatLng(MapUtil.getLatLngByTitle(songs.get(0).getTitle())));
        } else {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(MapUtil.getLatLngByTitle(currentSongTitle),10));
        }

        map.setOnMarkerClickListener(this);
        map.setOnInfoWindowClickListener(this);

        //bind the service 'PlayerService' in a Thread object
        final Intent serviceIntent = new Intent(this, PlayerService.class);
        Thread thread = new Thread() {
            @Override
            public void run() {
                bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            }
        };
        thread.start();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        //zoom in 10x when clicking marker
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 10));
        currentMarker = marker;

        //inflate custom image views & text views for clicked marker
        final ImageView songIcon = findViewById(R.id.activity_map__flag__icon);
        final TextView songTitle = findViewById(R.id.activity_map__tv__title);
        final TextView songDuration = findViewById(R.id.activity_map__tv__duration);
        final TextView songLocation = findViewById(R.id.activity_map__tv__latlgn);
        final TextView songComment = findViewById(R.id.activity_map__tv__comment);

        for (Song song : songs) {
            if (song.getTitle().equals(currentMarker.getTitle())) {
                songIcon.setImageResource(SongUtil.getFlagResId(song.getCountry()));
                songTitle.setText(song.getTitle());
                songDuration.setText(song.getDuration());
                songLocation.setText(MapUtil.getLatLngByTitle(song.getCountry()).toString());
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

    //control click events on buttons
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_map__btn__play:
                try {
                    playSongInMap();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.activity_map__btn__back:
                playerService.stop();

                //send data of current playing song when switching back to MainPlayerActivity
                Intent mapIntent = new Intent(this, MainPlayerActivity.class);
                mapIntent.putExtra("index", currentSongIndex);
                startActivity(mapIntent);

                break;
            default:
                Log.w(TAG, "Not clickable");
        }
    }

    //play the song related to clicked marker when clicking play button or info windows
    private void playSongInMap() {
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getTitle().equals(currentMarker.getTitle())) {
                if (isBound) {
                    this.playerService.playByIndex(this, i);
                    callNotification(i);
                    currentSongIndex = i;
                }
            }
        }
    }

    //call notification when a song is playing
    public void callNotification(int index) {

        //send data to NotificationActivity
        Intent tapIntent = new Intent(this, NotificationActivity.class);
        tapIntent.putExtra("index", index);
        tapIntent.putExtra("title", songs.get(index).getTitle());
        tapIntent.putExtra("text", songs.get(index).getComment());

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 23, tapIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        //build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channelId")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_noti_foreground)
                .setContentTitle("Now Playing")
                .setContentText(songs.get(index).getTitle())
                .setContentIntent(pendingIntent);

        ((NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE))
                .notify(new Random().nextInt(4), builder.build());
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
}
