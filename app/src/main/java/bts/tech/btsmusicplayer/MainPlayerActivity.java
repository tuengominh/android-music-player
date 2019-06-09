package bts.tech.btsmusicplayer;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;

import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import bts.tech.btsmusicplayer.model.Song;
import bts.tech.btsmusicplayer.service.PlayerService;
import bts.tech.btsmusicplayer.util.Utils;
import bts.tech.btsmusicplayer.view.adapter.SongListAdapter;

public class MainPlayerActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    /** This is the main/host activity
     * handling the media player with control buttons
     * inflating the menu, and
     * handling the list view with all songs */

    //fields to get context & build resource path
    public static String PACKAGE_NAME;
    protected static final String TAG = MainPlayerActivity.class.getSimpleName();

    //fields of UI items
    private Button btnPlay;
    private Button btnPause;
    private Button btnStop;
    private Button btnPrev;
    private Button btnNext;
    private ListView listView;

    //fields to control lists of songs
    private static List<Integer> playList = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();

    //fields to control PlayerService
    private PlayerService playerService;
    private boolean isBound = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
            playerService = binder.getService();
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
        setContentView(R.layout.activity_main_player);

        //get context
        PACKAGE_NAME = getApplicationContext().getPackageName();

        //setup lists of songs
        for (Song song : Utils.getSongList()) {
            songs.add(song);
            playList.add(song.getSongId());
        }

        //setup buttons
        this.btnPlay = findViewById(R.id.btn_play_player);
        this.btnPlay.setOnClickListener(this);

        this.btnPause = findViewById(R.id.btn_pause_player);
        this.btnPause.setOnClickListener(this);

        this.btnStop = findViewById(R.id.btn_stop_player);
        this.btnStop.setOnClickListener(this);

        this.btnPrev = findViewById(R.id.btn_prev_player);
        this.btnPrev.setOnClickListener(this);

        this.btnNext = findViewById(R.id.btn_next_player);
        this.btnNext.setOnClickListener(this);

        this.listView = findViewById(R.id.song_list_view);
        this.listView.setAdapter(new SongListAdapter(this,0, songs));
        this.listView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_play_player:
                Log.d(MainPlayerActivity.TAG,"Played");
                this.playerService.play();
                break;
            case R.id.btn_pause_player:
                Log.d(MainPlayerActivity.TAG,"Paused");
                this.playerService.pause();
                break;
            case R.id.btn_stop_player:
                Log.d(MainPlayerActivity.TAG,"Stopped");
                this.playerService.stop();
                break;
            case R.id.btn_prev_player:
                Log.d(MainPlayerActivity.TAG,"Go Back");
                this.playerService.previous();
                break;
            case R.id.btn_next_player:
                Log.d(MainPlayerActivity.TAG,"Go Next");
                this.playerService.next();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(MainPlayerActivity.TAG,"Item " + position + " Clicked");
        if (isBound) {
            this.playerService.playSelectedSong(position);
        }
    }

    //getters
    public static List<Integer> getPlayList() { return playList; }
    public static List<Song> getSongs() {
        return songs;
    }
}
