package bts.tech.btsmusicplayer;

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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bts.tech.btsmusicplayer.model.Song;
import bts.tech.btsmusicplayer.service.PlayerService;
import bts.tech.btsmusicplayer.util.SongUtil;
import bts.tech.btsmusicplayer.view.activity.MapActivity;
import bts.tech.btsmusicplayer.view.activity.NotificationActivity;
import bts.tech.btsmusicplayer.view.adapter.SongListAdapter;

public class MainPlayerActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    /** This is the main/host activity
     * handling the media player with control buttons,
     * call notifications,
     * inflating the menu, and
     * handling the list view with all songs */

    //fields to get context & build resource path
    public static String PACKAGE_NAME;
    protected static final String TAG = MainPlayerActivity.class.getSimpleName();

    //fields to control lists of songs
    private static List<Integer> playList = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();

    //fields of UI items: list view & buttons
    private Button btnPlay;
    private Button btnPause;
    private Button btnStop;
    private Button btnPrev;
    private Button btnNext;
    private Button btnMap;
    private ListView listView;

    //fields to control the bound service 'PlayerService'
    private PlayerService playerService;
    private boolean isBound = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder serviceInfo) {
            playerService = new PlayerService(serviceInfo);

            //when this activity connect to PlayerService for the 1st time, it will automatically play the 1st song
            //when users navigate to this activity from MapActivity, it will play the current playing song in MapActivity
            int index = getIntent().getIntExtra("index", 0);
            playerService.playByIndex(MainPlayerActivity.this, index);
            callNotification(index);

            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    //onCreate() lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_player);

        //get context to build resource path in other classes
        PACKAGE_NAME = getApplicationContext().getPackageName();

        //setup lists of songs
        for (Song song : SongUtil.getSongList()) {
            songs.add(song);
            playList.add(song.getResId());
        }

        //setup buttons
        this.btnPlay = findViewById(R.id.activity_main_player__btn__play);
        this.btnPlay.setOnClickListener(this);

        this.btnPause = findViewById(R.id.activity_main_player__btn__pause);
        this.btnPause.setOnClickListener(this);

        this.btnStop = findViewById(R.id.activity_main_player__btn__stop);
        this.btnStop.setOnClickListener(this);

        this.btnPrev = findViewById(R.id.activity_main_player__btn__prev);
        this.btnPrev.setOnClickListener(this);

        this.btnNext = findViewById(R.id.activity_main_player__btn__next);
        this.btnNext.setOnClickListener(this);

        this.btnMap = findViewById(R.id.activity_main_player__btn__map);
        this.btnMap.setOnClickListener(this);

        //setup list view and pass data to SongListAdapter
        this.listView = findViewById(R.id.activity_main_player__song__list__view);
        listView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.listview_header, null));
        this.listView.setAdapter(new SongListAdapter(this,R.layout.song_list_adapter, songs));
        this.listView.setOnItemClickListener(this);
    }

    //bind PlayerService within a Thread object
    @Override
    protected void onStart() {
        super.onStart();
        final Intent serviceIntent = new Intent(this, PlayerService.class);
        Thread thread = new Thread() {
            @Override
            public void run() {
                bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            }
        };
        thread.start();
    }

    //control click events on buttons
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_main_player__btn__play:
                this.playerService.play();
                break;
            case R.id.activity_main_player__btn__pause:
                this.playerService.pause();
                break;
            case R.id.activity_main_player__btn__stop:
                this.playerService.stop();
                break;
            case R.id.activity_main_player__btn__prev:
                this.playerService.previous(this);
                callNotification(this.playerService.currentSongIndex);
                break;
            case R.id.activity_main_player__btn__next:
                this.playerService.next(this);
                callNotification(this.playerService.currentSongIndex);
                break;
            case R.id.activity_main_player__btn__map:
                playerService.stop();
                startActivity(new Intent(this, MapActivity.class));
                break;
            default:
                Log.w(TAG, "Not clickable");
        }
    }

    //getters
    public static List<Integer> getPlayList() { return playList; }
    public static List<Song> getSongs() {
        return songs;
    }

    //control click events on list view items
    @Override
    public void onItemClick (AdapterView < ? > parent, View view,int position, long id){
        Log.d(TAG, "Song no." + position + " clicked");
        if (isBound) {
            this.playerService.playByIndex(this, position - 1);
            callNotification(position - 1);
        }
    }

    //call customized notification when a song is playing
    public void callNotification(int index) {

        //send data to NotificationActivity
        Intent tapIntent = new Intent(this, NotificationActivity.class);
        tapIntent.putExtra("index", index);
        tapIntent.putExtra("title", SongUtil.getSongList().get(index).getTitle());
        tapIntent.putExtra("text", SongUtil.getSongList().get(index).getComment());

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 23, tapIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        //build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channelId")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_noti_foreground)
                .setContentTitle("Now Playing")
                .setContentText(SongUtil.getSongList().get(index).getTitle())
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
